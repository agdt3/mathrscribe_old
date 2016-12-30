package com.mathscribe.app.presenters;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.mathscribe.app.models.Paths;
import com.mathscribe.app.views.DrawingViewImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pavela on 2016-12-11.
 */

public class DrawingPresenterImpl implements DrawingPresenter<DrawingViewImpl> {
    private DrawingViewImpl mView;
    private Subscriber<MotionEvent> mMotionEventSink;
    private Subscriber<View> mToggleDrawableSink;
    private Subscriber<View> mNewPageSink;

    // Should be in a separate model
    private Paths mPaths;

    public DrawingPresenterImpl() {
        loadPaths();
    }

    @Override
    public void attachView(DrawingViewImpl drawingViewImpl) {
        mView = drawingViewImpl;
        mView.setModel(mPaths);
        subscribeToView();
    }

    private void subscribeToView() {
        mMotionEventSink = createSubscriber();
        mView.getMotionEventSource()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMotionEventSink);

        mToggleDrawableSink = createDrawableClickSubscriber();
        mView.getDrawableClickSource()
                .subscribe(mToggleDrawableSink);
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

    private Func1<MotionEvent, Boolean> everyOther() {
        return new Func1<MotionEvent, Boolean>() {
            Boolean counter = false;

            @Override
            public Boolean call(MotionEvent motionEvent) {
                counter = !counter;
                return counter;
            }
        };
    }

    public void detachView() {
        mMotionEventSink.unsubscribe();
        mMotionEventSink = null;
        mToggleDrawableSink.unsubscribe();
        mToggleDrawableSink = null;
        mView = null;
    }

    public void setViewDrawState(Boolean inDrawState) {
        mView.setIsDrawable(inDrawState);
    }

    private void setViewStartLoading() {
        mView.showProgress();
    }

    private void setViewFinishLoading() {
        mView.hideProgress();
    }

    private Subscriber<MotionEvent> createSubscriber() {
        return new Subscriber<MotionEvent>() {
            @Override
            public void onNext(MotionEvent event) {
                float posx = event.getX();
                float posy = event.getY();

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPaths.mCurrent.moveTo(posx, posy);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPaths.mCurrent.lineTo(posx, posy);
                        break;
                    case MotionEvent.ACTION_UP:
                        mPaths.addCurrentToPrevious();
                        break;
                    default:
                        //no-op
                }
                mView.setModel(mPaths);
                mView.postInvalidate();
            }

            @Override
            public void onError(Throwable t) {
                Log.d("error!", t.toString());
            }

            @Override
            public void onCompleted() {
                Log.d("onCompleted", "completed");
            }
        };
    }

    private Subscriber<View> createDrawableClickSubscriber() {
        return new Subscriber<View>() {
            @Override
            public void onNext(View v) {
                setViewDrawState(((ToggleButton) v).isChecked());
            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable t) {
                Log.d("SCRIBE", t.toString());
            }
        };
    }
}
