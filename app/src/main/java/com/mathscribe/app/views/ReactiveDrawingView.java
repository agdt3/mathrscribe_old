package com.mathscribe.app.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.mathscribe.app.MotionListenerObservable;

/**
 * Created by pavela on 2016-12-06.
 */

public class ReactiveDrawingView extends View {
    private Paint drawPaint;
    private int paintColor = Color.BLACK;
    private MotionListenerObservable motionListenerObservable;


    public ReactiveDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

        motionListenerObservable = new MotionListenerObservable(this);
    }

    public void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
}
