package com.mathscribe.app.presenters.interfaces;

/**
 * Created by pavela on 2016-12-25.
 */

public interface DrawingPresenter<V> {
    void attachView(V view);
    void detachView();
    void onDestroy();
}
