package com.mathscribe.app;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by pavela on 2016-12-07.
 */

public class MotionListenerObservable {
    Observable<MotionEvent> source;
    Subscriber<MotionEvent> sink;

    public MotionListenerObservable(View view) {
        source = createObservable(view);
        sink = createSubscriber();
        source.subscribe(sink);
    }

    private Observable<MotionEvent> createObservable(View view) {
        return Observable.create(new Observable.OnSubscribe<MotionEvent>() {
            @Override
            public void call(Subscriber <? super MotionEvent> observer) {
                View.OnTouchListener listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        try {
                            if (observer.isUnsubscribed()) {
                                view.setOnTouchListener(null);
                                return false;
                            } else {
                                observer.onNext(event);
                                view.postInvalidate();
                                return true;
                            }
                        }
                        catch (Exception e) {
                            observer.onError(e);
                            return false;
                        }
                    }
                };
                view.setOnTouchListener(listener);
            }
        });
    }

    private Subscriber<MotionEvent> createSubscriber() {
        return new Subscriber<MotionEvent>() {
            @Override
            public void onNext(MotionEvent event) {
                float posx = event.getX();
                float posy = event.getY();
                Log.d("OnNext X", String.valueOf(posx));
                Log.d("OnNext Y", String.valueOf(posy));
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

    public void usubscribe() {
        sink.unsubscribe();
    }
}
