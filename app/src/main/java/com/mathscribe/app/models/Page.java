package com.mathscribe.app.models;


/**
 * Created by pavela on 2016-12-25.
 */

public class Page {
    private int index;
    public Paths paths;

    public Page(int i) {
        index = i;
    }

    public int getIndex() { return index; }
}
