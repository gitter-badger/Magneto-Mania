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

    public static Point calcVelocityComponents(Point a, Point b, int velocity)
    {
        int distance = Geometry.distance(a, b);

        Point mVelocityComponent = new Point(0,0);
        mVelocityComponent.x = velocity * (a.x - b.x) / distance;  // velocity times cos(theta)
        mVelocityComponent.y = velocity * (a.y - b.y) / distance;  // velocity times sin(theta)

        return mVelocityComponent;
    }
}
