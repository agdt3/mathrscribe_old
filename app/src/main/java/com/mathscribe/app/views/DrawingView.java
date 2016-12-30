package com.mathscribe.app.views;

import android.view.MotionEvent;
import android.view.View;

import com.mathscribe.app.models.Paths;

import rx.Observable;

/**
 * Created by pavela on 2016-12-28.
 */

public interface DrawingView {
    void showProgress();
    void hideProgress();
    void setModel(Paths paths);
    void setIsDrawable(boolean isDrawable);
    Observable<MotionEvent> getMotionEventSource();
    Observable<View> getDrawableClickSource();
    Observable<View> getNewPageClickSource();
}
