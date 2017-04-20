package com.mathscribe.app.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.mathscribe.app.R;
import com.mathscribe.app.models.Paths;
import com.mathscribe.app.presenters.interfaces.DrawingPresenter;
import com.mathscribe.app.presenters.DrawingPresenterImpl;
import com.mathscribe.app.utils.ObservableFactory;
import com.mathscribe.app.views.interfaces.DrawingController;
import com.mathscribe.app.views.view_models.DrawingViewState;

import io.reactivex.Observable;

public class DrawActivity extends Activity implements DrawingController {
    // Presenter and subview
    private DrawingPresenter mPresenter;
    private DrawingViewImpl mDrawingView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private FloatingActionButton mDrawableToggleButton;
    private FloatingActionButton mNewPageButton;

    // ViewModel maintains view state
    private DrawingViewState mViewModel;
    private Observable<View> mDrawableClickSource;
    private Observable<View> mNewPageClickSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);

        // Track view state
        mViewModel = new DrawingViewState();

        // Bind subviews
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawableToggleButton = (FloatingActionButton) findViewById(R.id.drawable_button);
        mNewPageButton = (FloatingActionButton) findViewById(R.id.add_page_button);

        setUpDrawingView();
        setUpDrawableToggle();
        setUpNewPageButton();

        // Presenter
        mPresenter = new DrawingPresenterImpl();
        mPresenter.attachView(this);
    }

    private void setUpDrawingView() {
        mDrawingView = (DrawingViewImpl) findViewById(R.id.drawing_view_impl);
        mDrawingView.setIsDrawable(mViewModel.isDrawable());
        mDrawingView.setPaint(mViewModel.getDrawPaint());
    }

    private void setUpDrawableToggle() {
        mDrawableClickSource = ObservableFactory.createOnClickObservable(mDrawableToggleButton);
        //mDrawableToggleButton.setChecked(mViewModel.isDrawable());
    }

    private void setUpNewPageButton() {
        mNewPageClickSource = ObservableFactory.createOnClickObservable(mNewPageButton);
    }

    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            if (mProgressBar == null) {
                LayoutInflater inflater = getLayoutInflater();
                mProgressBar = (ProgressBar) inflater.inflate(R.layout.progress_bar, null);
            }
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void setPaths(Paths paths) { mDrawingView.setPaths(paths); };

    public void setIsDrawable() {
        mViewModel.setIsDrawable(!mViewModel.isDrawable());
        toggleDrawable();
    }

    public void redraw() { mDrawingView.postInvalidate(); }

    public Observable<MotionEvent> getMotionEventSource() { return mDrawingView.getMotionEventSource(); }
    public Observable<View> getDrawableClickSource() { return mDrawableClickSource; }
    public Observable<View> getNewPageClickSource() { return mNewPageClickSource; }

    private void toggleDrawable() {
        boolean isDrawable = mViewModel.isDrawable();

        // Set focusable on drawing subView
        mDrawingView.setIsDrawable(isDrawable);

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

    @Override
    public void createNewPage() {
        // TODO: Add the page to page model
        Intent drawActivityIntent = new Intent(this, DrawActivity.class);
        startActivity(drawActivityIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
