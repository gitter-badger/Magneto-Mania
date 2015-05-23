package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class LaserBeam {

    protected final Point center = new Point(GameActivity.mScreenSize.x/2, GameActivity.mScreenSize.y/2);
    protected Point laserDestination = new Point(0,0);
    protected Paint laserBeamPaint   = new Paint();


    public LaserBeam(int orientation) {

        switch(orientation) {
            case 0: this.laserDestination.set(2*center.x, 0);
                    break;
            case 1: this.laserDestination.set(center.x, 0);
                    break;
            case 2: this.laserDestination.set(0,0);
                    break;
            case 3: this.laserDestination.set(0, center.y);
                    break;
            case 4: this.laserDestination.set(0, 2*center.y);
                    break;
            case 5: this.laserDestination.set(center.x, 2*center.y);
                    break;
            case 6: this.laserDestination.set(2*center.x, 2*center.y);
                    break;
            case 7: this.laserDestination.set(2*center.x, center.y);
                    break;
        }
        this.laserBeamPaint.setColor(Color.CYAN);
    }


    public void moveMonsterToCenter(MonsterBall monsterBall, Point initialPoint) {

        int distanceFromCenter = Geometry.distance(center, monsterBall.monsterPosition);

        if(distanceFromCenter > 15)
        monsterBall.attackFingerPosition(center, initialPoint);
        else
        monsterBall.monsterPosition = Geometry.setCoordinates(center);
    }


    public void rotateBeam(int orientation) {

        switch(orientation) {
            case 0:
            case 1: laserDestination.x -= GameActivity.mScreenSize.x/100;
                    break;
            case 2:
            case 3: laserDestination.y += GameActivity.mScreenSize.y/100;
                    break;
            case 4:
            case 5: laserDestination.x += GameActivity.mScreenSize.x/100;
                    break;
            case 6:
            case 7: laserDestination.y -= GameActivity.mScreenSize.y/100;
                    break;
        }
    }
}