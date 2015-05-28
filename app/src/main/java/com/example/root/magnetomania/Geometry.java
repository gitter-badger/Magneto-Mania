package com.example.root.magnetomania;

import android.graphics.Point;


public class Geometry extends Point {

    public static final Point center = new Point(GameActivity.mScreenSize.x/2, GameActivity.mScreenSize.y/2);
    public static Point setCoordinates(Point src) {
        Point point = new Point(0,0);
        point.x = src.x;
        point.y = src.y;

        return point;
    }


    public static int distance(Point a, Point b) {
        return (int)Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }

    public static double distanceForScore(Point a, Point b) {
        return (int)Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }

    public static double area(Point dimensions) {
        return (double)dimensions.x*dimensions.y;
    }


    public static Point calcVelocityComponents(Point a, Point b, int velocity) {
        int distance = Geometry.distance(a, b);

        Point mVelocityComponent = new Point(0,0);
        mVelocityComponent.x = velocity * (a.x - b.x) / distance;  // velocity times cos(theta)
        mVelocityComponent.y = velocity * (a.y - b.y) / distance;  // velocity times sin(theta)

        return mVelocityComponent;
    }


    public static void moveMonsterToCenter(MonsterBall monsterBall) {
        int distanceFromCenter = Geometry.distance(center, monsterBall.monsterPosition);
        monsterBall.monsterVelocity = 15;

        if(distanceFromCenter > 15) {
            GameView.destinationPoint = Geometry.setCoordinates(center);
            monsterBall.attackFingerPosition();
        }
        else {
            monsterBall.monsterPosition = Geometry.setCoordinates(center);
        }
    }
}
