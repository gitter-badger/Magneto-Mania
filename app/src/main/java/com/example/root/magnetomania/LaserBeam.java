package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class LaserBeam {

    protected final Point center = new Point(GameActivity.mScreenSize.x/2, GameActivity.mScreenSize.y/2);
    protected double laserDestinationX;
    protected double laserDestinationY;
    protected Paint laserBeamPaint   = new Paint();


    public LaserBeam() {
        this.laserDestinationX = center.x;
        this.laserDestinationY = center.y;
        this.laserBeamPaint.setColor(Color.CYAN);
        this.laserBeamPaint.setAlpha(0);
        this.laserBeamPaint.setStrokeWidth(30);
        this.laserBeamPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.laserBeamPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    public void initLaserBeam(int orientation) {
        switch(orientation) {
            case 0: this.laserDestinationX = 2*center.x;
                this.laserDestinationY = 0;
                break;
            case 1: this.laserDestinationX = center.x;
                this.laserDestinationY = 0;
                break;
            case 2: this.laserDestinationX = 0;
                this.laserDestinationY = 0;
                break;
            case 3: this.laserDestinationX = 0;
                this.laserDestinationY = center.y;
                break;
            case 4: this.laserDestinationX = 0;
                this.laserDestinationY = 2*center.y;
                break;
            case 5: this.laserDestinationX = center.x;
                this.laserDestinationY = 2*center.y;
                break;
            case 6: this.laserDestinationX = 2*center.x;
                this.laserDestinationY = 2*center.y;
                break;
            case 7: this.laserDestinationX = 2*center.x;
                this.laserDestinationY = center.y;
                break;
        }
    }


    public void rotateBeam(int orientation) {

        switch(orientation) {
            case 0:
            case 1: laserDestinationX -= (double)GameActivity.mScreenSize.x/50.0;
                    break;
            case 2:
            case 3: laserDestinationY += (double)GameActivity.mScreenSize.y/50.0;
                    break;
            case 4:
            case 5: laserDestinationX += (double)GameActivity.mScreenSize.x/50.0;
                    break;
            case 6:
            case 7: laserDestinationY -= (double)GameActivity.mScreenSize.y/50.0;
                    break;
        }
    }


    public boolean didLaserBeamPenetrateTheFinger() {
        int destinationToCenterAngle = (int)(Math.atan2(laserDestinationY - center.y, laserDestinationX - center.x)*180/Math.PI);
        int fingerToCenterAngle = (int)(Math.atan2(GameView.fingerPosition.y - center.y, GameView.fingerPosition.x - center.x)*180/Math.PI);

        return (fingerToCenterAngle < destinationToCenterAngle + 3 && fingerToCenterAngle > destinationToCenterAngle - 3);
    }
}