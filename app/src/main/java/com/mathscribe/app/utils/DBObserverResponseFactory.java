package com.mathscribe.app.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.requery.reactivex.ReactiveResult;

public class DBObserverResponseFactory<T> {

    public Observer<ReactiveResult<T>> getObserverReponse() {
        return new Observer<ReactiveResult<T>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ReactiveResult<T> ts) { Log.d("SCRIBE onNext", ts.toString()); }

            @Override
            public void onError(Throwable e) { Log.d("SCRIBE onError", e.toString()); }

            @Override
            public void onComplete() { Log.d("SCRIBE", "onComplete"); }
        };
    }
}
