package com.mathscribe.app.views.interfaces;

import android.graphics.Paint;
import android.view.MotionEvent;

import com.mathscribe.app.models.Paths;

import io.reactivex.Observable;

/**
 * Created by pavela on 2016-12-28.
 */

public interface DrawingView {
    void setPaths(Paths paths);
    void setPaint(Paint paint);
    void setIsDrawable(boolean isDrawable);
    Observable<MotionEvent> getMotionEventSource();
}
