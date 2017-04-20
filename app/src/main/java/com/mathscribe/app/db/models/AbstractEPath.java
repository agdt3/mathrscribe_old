package com.mathscribe.app.db.models;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Transient;


@Entity
abstract class AbstractEPath {
    @Key @Generated
    int path_id;

    String points;

    @ManyToOne
    EPage page;

    public String toString() {
        return "EPath id " + path_id + " points " + points + " page id " + page.getPage_id();
    }

    @Transient
    Path path = new Path();

    @Transient
    List<PointF> pointsList;

    public String convertPointListToString(List<PointF> points) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        try {
            for (PointF p : points) {
                sb.append(p.x);
                sb.append(",");
                sb.append(p.y);
                sb.append(",");
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.d("SCRIBE", e.toString());
        }
        return result;
    }

    public void convertStringToPath() {
        List<String> pts = Arrays.asList(points.split(",[ ]*"));
        int size = pts.size();
        for (int i = 0; i < size; i += 2) {
            float x = Float.valueOf(pts.get(i));
            float y = Float.valueOf(pts.get(i+1));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
    }
}
