package com.mathscribe.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.mathscribe.app.R;
import com.mathscribe.app.models.Paths;
import com.mathscribe.app.utils.ObservableFactory;
import com.mathscribe.app.views.interfaces.DrawingView;

import io.reactivex.Observable;


public class DrawingViewImpl extends RelativeLayout implements DrawingView {
    // ViewModel maintains view state
    private Observable<MotionEvent> mMotionEventSource;

    // Relevant Models
    private Paths mPaths;
    private Paint mPaint;

    public DrawingViewImpl(Context context) {
        super(context);
        init();
    }

    public DrawingViewImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        // Inflate manually from drawing view
        inflate(getContext(), R.layout.drawing_view, this);

        // ViewGroups don't redraw by default
        setWillNotDraw(false);

        // Create observable motion event source
        mMotionEventSource = ObservableFactory.createMotionEventObservable(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaths != null) {
            canvas.drawPath(mPaths.getCurrentPath(), mPaint);
        }

        if (mPaths != null) {
            for (Path path : mPaths.getPreviousPaths()) {
                canvas.drawPath(path, mPaint);
            }
        }
    }

    public void setPaths(Paths paths) { mPaths = paths; }
    public void setPaint(Paint paint) { mPaint = paint; }

    public void setIsDrawable(boolean isDrawable) {
        setEnabled(isDrawable);
        setFocusable(isDrawable);
        setFocusableInTouchMode(isDrawable);
    }

    public Observable<MotionEvent> getMotionEventSource() { return mMotionEventSource; }
}
