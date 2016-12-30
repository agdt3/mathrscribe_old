package com.mathscribe.app.models;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavela on 2016-12-25.
 */

public class Paths {
    public Path mCurrent;
    public List<Path> mPrevious;

    public Paths() {
        mCurrent = new Path();
        mPrevious = new ArrayList<>();
    }

    public void addCurrentToPrevious() {
        mPrevious.add(mCurrent);
        mCurrent = new Path();
    }
}
