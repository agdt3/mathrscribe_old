package com.mathscribe.app.utils;

import android.view.MotionEvent;
import android.view.View;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by pavela on 2016-12-26.
 */

public class ObservableFactory {
    public static Observable<View> createOnClickObservable(View view) {
        return Observable.create(new Observable.OnSubscribe<View>() {
            @Override
            public void call(final Subscriber<? super View> subscriber) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (subscriber.isUnsubscribed()) {
                                view.setOnClickListener(null);
                            } else {
                                subscriber.onNext(v);
                            }
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<MotionEvent> createMotionEventObservable(View view) {
        return Observable.create(new Observable.OnSubscribe<MotionEvent>() {
            @Override
            public void call(Subscriber<? super MotionEvent> observer) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        try {
                            if (observer.isUnsubscribed()) {
                                view.setOnTouchListener(null);
                                return false;
                            } else {
                                observer.onNext(event);
                                return true;
                            }
                        }
                        catch (Exception e) {
                            observer.onError(e);
                            return false;
                        }
                    }
                });
            }
        });
    }
}
