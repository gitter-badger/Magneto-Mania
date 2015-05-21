package com.example.root.magnetomania;

import android.graphics.Point;

/**
 * Created by root on 21/5/15.
 */
public class Geometry {

    public static int distance(Point a, Point b)
    {
        return (int)Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }
}
