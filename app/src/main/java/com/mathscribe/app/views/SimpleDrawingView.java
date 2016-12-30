package com.mathscribe.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavela on 2016-12-04.
 */

public class SimpleDrawingView extends View {
    private final int paintColor = Color.BLACK;
    private Paint drawPaint;
    private Path currentPath = new Path();
    private List<Path> paths = new ArrayList<>();

    public SimpleDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
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

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(currentPath, drawPaint);
        for (Path path : paths) {
            canvas.drawPath(path, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float posx = event.getX();
        float posy = event.getY();
        Log.d("X", String.valueOf(posx));
        Log.d("Y", String.valueOf(posy));

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(posx, posy);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(posx, posy);
                break;
            case MotionEvent.ACTION_UP:
                paths.add(currentPath);
                currentPath = new Path();
                for (Path path : paths) {
                    Log.d("path", path.toString());
                }
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }
}
