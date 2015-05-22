package com.example.root.magnetomania;

import android.graphics.Point;


public class Geometry extends Point {


    public static Point setCoordinates(Point src) {
        Point point = new Point(0,0);
        point.x = src.x;
        point.y = src.y;

        return point;
    }


    public static int distance(Point a, Point b) {
        return (int)Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }


    public static Point calcVelocityComponents(Point a, Point b, int velocity) {
        int distance = Geometry.distance(a, b);

        Point mVelocityComponent = new Point(0,0);
        mVelocityComponent.x = velocity * (a.x - b.x) / distance;  // velocity times cos(theta)
        mVelocityComponent.y = velocity * (a.y - b.y) / distance;  // velocity times sin(theta)

        return mVelocityComponent;
    }
}
