package com.mathscribe.app.views.view_models;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by pavela on 2016-12-25.
 */

public class DrawingViewState {
    private Paint mDrawPaint;
    private int mPaintColor = Color.BLACK;
    private int mSTROKE_WIDTH= 5;
    private boolean mAntiAlias = true;
    private boolean mViewIsDrawable = false;

    // Button alpha - no reason to make these private
    public int mFULL_ALPHA = 255;
    public int mQUARTER_ALPHA = 64;

    public DrawingViewState() {
        setupPaint();
    }

    private void setupPaint() {
        mDrawPaint = new Paint();
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(mAntiAlias);
        mDrawPaint.setStrokeWidth(mSTROKE_WIDTH);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Paint getDrawPaint() { return mDrawPaint; }

    public void setIsDrawable(boolean isDrawable) { mViewIsDrawable = isDrawable; }
    public boolean isDrawable() { return mViewIsDrawable; }
    public void setStrokeWidth(int strokeWidth) { mSTROKE_WIDTH = strokeWidth; }
    public void setPaintColor(int color) { mPaintColor = color; }
    public void setAntiAlias(boolean antiAlias) { mAntiAlias = antiAlias; }
}
