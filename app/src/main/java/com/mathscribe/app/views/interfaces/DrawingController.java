package com.mathscribe.app.views.interfaces;

import android.view.MotionEvent;
import android.view.View;

import com.mathscribe.app.models.Paths;

import io.reactivex.Observable;

/**
 * Created by pavela on 2016-12-28.
 */

public interface DrawingController {
    void showProgress();
    void hideProgress();
    void setPaths(Paths paths);
    void redraw();
    void setIsDrawable();
    void createNewPage();
    Observable<MotionEvent> getMotionEventSource();
    Observable<View> getDrawableClickSource();
    Observable<View> getNewPageClickSource();
}
