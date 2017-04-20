package com.mathscribe.app.presenters;

import android.app.Activity;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mathscribe.app.ScribeApplication;
import com.mathscribe.app.db.models.EPage;
import com.mathscribe.app.db.models.EPath;
import com.mathscribe.app.models.Paths;
import com.mathscribe.app.presenters.interfaces.DrawingPresenter;
import com.mathscribe.app.views.interfaces.DrawingController;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import io.reactivex.observers.ResourceObserver;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;

public class DrawingPresenterImpl implements DrawingPresenter<DrawingController> {
    private DrawingController mView;
    private ResourceObserver<MotionEvent> mMotionEventSink;
    private ResourceObserver<View> mToggleDrawableSink;
    private ResourceObserver<View> mNewPageSink;

    // Should be in a separate model
    private Paths mPaths;

    // TODO: Test
    private EPage mPage;
    private ReactiveEntityStore<Persistable> data;

    public DrawingPresenterImpl() {
        loadPaths();
    }

    @Override
    public void attachView(DrawingController drawingView) {
        mView = drawingView;
        mView.setPaths(mPaths);
        subscribeToView();
    }

    private void subscribeToView() {
        if (mView != null) {
            mMotionEventSink = createMotionEventObserver();
            mView.getMotionEventSource()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mMotionEventSink);

            mToggleDrawableSink = createDrawableClickObserver();
            mView.getDrawableClickSource()
                    .subscribe(mToggleDrawableSink);

            mNewPageSink = createNewPageClickObserver();
            mView.getNewPageClickSource()
                    .subscribe(mNewPageSink);
        }
    }

    private void loadPaths() {
        if (mView != null) {
            setViewStartLoading();
        }
        mPaths = new Paths();
        // Tries to load paths
        // else...
        if (mView != null) {
            setViewFinishLoading();
        }
    }

    public void detachView() {
        mMotionEventSink.dispose();
        mMotionEventSink = null;
        mToggleDrawableSink.dispose();
        mToggleDrawableSink = null;
        mNewPageSink.dispose();
        mNewPageSink = null;
        mView = null;
    }

    private void setViewStartLoading() {
        mView.showProgress();
    }

    private void setViewFinishLoading() {
        mView.hideProgress();
    }

    private ResourceObserver<MotionEvent> createMotionEventObserver() {
        return new ResourceObserver<MotionEvent>() {
            @Override
            public void onNext(MotionEvent event) {
                float posX = event.getX();
                float posY = event.getY();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPaths.moveTo(posX, posY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPaths.lineTo(posX, posY);
                        break;
                    case MotionEvent.ACTION_UP:
                        mPaths.addCurrentPathToPrevious();
                        Log.d("SCRIBE", Integer.toString(mPaths.getCurrentPoints().size()));
                        Log.d("SCRIBE", Integer.toString(mPaths.getPreviousPoints().size()));
                        savePointsLocal();
                        break;
                    default:
                        //no-op
                }
                mView.setPaths(mPaths);
                mView.redraw();
            }

            @Override
            public void onError(Throwable t) { Log.d("error!", t.toString()); }

            @Override
            public void onComplete() {}
        };
    }

    private ResourceObserver<View> createDrawableClickObserver() {
        return new ResourceObserver<View>() {
            @Override
            public void onNext(View view) { mView.setIsDrawable(); }

            @Override
            public void onError(Throwable e) { Log.d("SCRIBE", e.toString()); }

            @Override
            public void onComplete() {}
        };
    }

    private ResourceObserver<View> createNewPageClickObserver() {
        return new ResourceObserver<View>() {
            @Override
            public void onNext(View view) { mView.createNewPage(); }

            @Override
            public void onError(Throwable e) { Log.d("SCRIBE", e.toString()); }

            @Override
            public void onComplete() {}
        };
    }

    // TODO: Should live on the model
    private String convertPointListToString(List<PointF> points) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        try {
            for (PointF p : points) {
                sb.append(p.x);
                sb.append(",");
                sb.append(p.y);
                sb.append(",");
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.d("SCRIBE", e.toString());
        }
        return result;
    }

    private void savePointsLocal() {
        // Local save should be fast enough to avoid race conditions
        // Write to Sqlite DB
        Log.d("SCRIBE", "savePointsLocal");
        data = ((ScribeApplication) ((Activity)mView).getApplication()).getDataStore();
        List<List<PointF>> allPoints = mPaths.getPreviousPoints();
        List<PointF> points = allPoints.get(0);
        Log.d("SCRIBE", Integer.toString(allPoints.size()));
        Log.d("SCRIBE", "got here");
        String result = convertPointListToString(points);
        EPath ePath = new EPath();
        ePath.setPoints(result);
        EPage ePage = new EPage();
        ePage.setIdx(1);
        ePage.getPaths().add(ePath);
        data.insert(ePage).toObservable().subscribe(new ResourceObserver<EPage>() {
            @Override
            public void onNext(EPage epg) {
                Log.d("SCRIBE", "onNext " + epg.toString());
            }
            @Override
            public void onComplete() {
                Log.d("SCRIBE", "onComplete");
                getData();
            }
            @Override
            public void onError(Throwable t) { Log.d("SCRIBE", t.toString()); }
        });
        Log.d("SCRIBE", "finished");
    }

    public void getData() {
        Log.d("SCRIBE", "getData");
        data = ((ScribeApplication) ((Activity)mView).getApplication()).getDataStore();
        Observable<ReactiveResult<EPage>> result1 = data.select(EPage.class).where(EPage.PAGE_ID.eq(1)).get().observableResult();
        result1.subscribe(new Observer<ReactiveResult<EPage>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ReactiveResult<EPage> ePage) {
               Log.d("SCRIBE", "onNext " + ePage.first().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("SCRIBE", "onError" + e);
            }

            @Override
            public void onComplete() {
                Log.d("SCRIBE", "onComplete");
            }
        });

        Result<EPage> query = data.select(EPage.class).get();
        Log.d("SCRIBE query", query.first().toString());
    }

    public void onDestroy() { detachView(); }
}
