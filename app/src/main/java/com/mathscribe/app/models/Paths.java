package com.mathscribe.app.models;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

// Should be able to reconstruct paths from points
public class Paths {
    private Path mCurrentPath;
    private List<Path> mPreviousPaths;
    private List<PointF> mCurrentPoints;
    private List<List<PointF>> mPreviousPoints;

    public Paths() {
        mCurrentPath = new Path();
        mPreviousPaths = new ArrayList<>();
        mCurrentPoints = new ArrayList<>();
        mPreviousPoints = new ArrayList<>();
    }

    public Paths(List<List<PointF>> points) {
        mCurrentPath = new Path();
        mPreviousPaths = new ArrayList<>();
        mCurrentPoints = new ArrayList<>();
        mPreviousPoints = points;
        convertPointsToPaths();
    }

    public void startNewPath() {
        mCurrentPath = new Path();
        mCurrentPoints = new ArrayList<>();
    }

    public void moveTo(float x, float y) {
        mCurrentPath.moveTo(x, y);
        mCurrentPoints.add(new PointF(x, y));
    }

    public void lineTo(float x, float y) {
        mCurrentPath.lineTo(x, y);
        mCurrentPoints.add(new PointF(x, y));
    }

    public void addCurrentPathToPrevious() {
        mPreviousPaths.add(mCurrentPath);
        mCurrentPath = new Path();
        mPreviousPoints.add(mCurrentPoints);
        mCurrentPoints = new ArrayList<>();
    }

    public void loadPoints(List<List<PointF>> points) {
        mPreviousPoints = points;
        convertPointsToPaths();
    }

    public Path getCurrentPath() { return mCurrentPath; }
    public List<Path> getPreviousPaths() { return mPreviousPaths; }
    public List<PointF> getCurrentPoints() { return mCurrentPoints; }
    public List<List<PointF>> getPreviousPoints() { return mPreviousPoints; }

    private void convertPointsToPaths() {
        for (List<PointF> points : mPreviousPoints) {
            Path path = new Path();
            for (PointF point : points) {
                if (path.isEmpty()) {
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            }
            mPreviousPaths.add(path);
        }
    }
}
