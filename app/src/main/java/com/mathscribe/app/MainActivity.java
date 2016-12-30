package com.mathscribe.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mathscribe.app.presenters.DrawingPresenterImpl;
import com.mathscribe.app.views.DrawingViewImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawingViewImpl drawingViewImpl = new DrawingViewImpl(this);
        DrawingPresenterImpl presenter = new DrawingPresenterImpl();
        presenter.attachView(drawingViewImpl);
        setContentView(drawingViewImpl);
    }
}
