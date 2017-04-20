package com.mathscribe.app.utils;

import android.view.MotionEvent;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class ObservableFactory {
    public static Observable<View> createOnClickObservable(final View view) {
        return Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(final ObservableEmitter<View> emitter) throws Exception {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (emitter.isDisposed()) {
                                view.setOnClickListener(null);
                            } else {
                                emitter.onNext(view);
                            }
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<MotionEvent> createMotionEventObservable(final View view) {
        return Observable.create(new ObservableOnSubscribe<MotionEvent>() {
            @Override
            public void subscribe(final ObservableEmitter<MotionEvent> emitter) throws Exception {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        try {
                            if (emitter.isDisposed()) {
                                view.setOnClickListener(null);
                                return false;
                            } else {
                                emitter.onNext(event);
                                return true;
                            }
                        } catch (Exception e) {
                            emitter.onError(e);
                            return false;
                        }
                    }
                });
            }
        });
    }
}
