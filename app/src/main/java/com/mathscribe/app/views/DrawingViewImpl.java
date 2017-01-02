package com.mathscribe.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.mathscribe.app.R;
import com.mathscribe.app.models.Paths;
import com.mathscribe.app.utils.ObservableFactory;
import com.mathscribe.app.views.view_models.DrawingViewState;

import rx.Observable;

/**
 * Created by pavela on 2016-12-11.
 */

public class DrawingViewImpl extends RelativeLayout implements DrawingView {
    // ViewModel maintains view state
    private DrawingViewState mViewModel;
    private Observable<MotionEvent> mMotionEventSource;
    private Observable<View> mDrawableClickSource;
    private Observable<View> mNewPageClickSource;

    // Model
    private Paths mPaths;

    // Subviews
    private ProgressBar mProgressBar;
    private ToggleButton mDrawableToggleButton;
    private Toolbar mToolbar;
    private ImageButton mNewPageButton;

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

        // Create view model
        mViewModel = new DrawingViewState();

        // Bind subviews
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawableToggleButton = (ToggleButton) findViewById(R.id.drawable_button);
        mNewPageButton = (ImageButton) findViewById(R.id.add_page_button);

        // Setup stuff
        mMotionEventSource = ObservableFactory.createMotionEventObservable(this);
        setUpDrawableToggle();
        setUpNewPageButton();
        toggleDrawable();
    }

    private void setUpDrawableToggle() {
        mDrawableClickSource = ObservableFactory.createOnClickObservable(mDrawableToggleButton);
        mDrawableToggleButton.setChecked(mViewModel.isDrawable());
    }

    private void setUpNewPageButton() {
        mNewPageClickSource = ObservableFactory.createOnClickObservable(mNewPageButton);
    }

    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            if (mProgressBar == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                mProgressBar = (ProgressBar) inflater.inflate(R.layout.progress_bar, this);
            }
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = mViewModel.getDrawPaint();
        if (mPaths.mCurrent != null) {
            canvas.drawPath(mPaths.mCurrent, paint);
        }

        if (mPaths != null) {
            for (Path path : mPaths.mPrevious) {
                canvas.drawPath(path, paint);
            }
        }
    }

    public void setModel(Paths paths) { mPaths = paths; }
    public void setIsDrawable(boolean isDrawable) {
        mViewModel.setIsDrawable(isDrawable);
        toggleDrawable();
    }

    public Observable<MotionEvent> getMotionEventSource() { return mMotionEventSource; }
    public Observable<View> getDrawableClickSource() { return mDrawableClickSource; }
    public Observable<View> getNewPageClickSource() { return mNewPageClickSource; }

    private void toggleDrawable() {
        boolean isDrawable = mViewModel.isDrawable();

        setEnabled(isDrawable);
        setFocusable(isDrawable);
        setFocusableInTouchMode(isDrawable);

        if (isDrawable) {
            mToolbar.setVisibility(View.GONE);
            mDrawableToggleButton.getBackground().setAlpha(mViewModel.mQUARTER_ALPHA);
            mNewPageButton.setVisibility(View.GONE);
        }
        else {
            mToolbar.setVisibility(View.VISIBLE);
            mDrawableToggleButton.getBackground().setAlpha(mViewModel.mFULL_ALPHA);
            mNewPageButton.setVisibility(View.VISIBLE);
        }
    }
}
