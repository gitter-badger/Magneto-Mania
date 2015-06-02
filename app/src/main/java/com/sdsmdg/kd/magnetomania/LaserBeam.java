package com.sdsmdg.kd.magnetomania;

import android.graphics.Canvas;
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
        this.laserBeamPaint.setColor(Color.parseColor("#9C27B0"));
        this.laserBeamPaint.setAlpha(0);
        this.laserBeamPaint.setStrokeWidth(30);
        this.laserBeamPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.laserBeamPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    public void initLaserBeam(int orientation) {
        switch (orientation) {
            case 0:
                this.laserDestinationX = 2 * center.x;
                this.laserDestinationY = 0;
                break;
            case 1:
                this.laserDestinationX = 0;
                this.laserDestinationY = 0;
                break;
            case 2:
                this.laserDestinationX = 0;
                this.laserDestinationY = 2 * center.y;
                break;
            case 3:
                this.laserDestinationX = 2 * center.x;
                this.laserDestinationY = 2 * center.y;
                break;
        }
        laserBeamPaint.setStrokeWidth(30);
        laserBeamPaint.setColor(Color.parseColor("#9C27B0"));
    }


    public void rotateBeam(int orientation) {

        switch (orientation) {
            case 0:
                laserDestinationX -= (double) GameActivity.mScreenSize.x / 25.0;
                break;
            case 1:
                laserDestinationY += (double) GameActivity.mScreenSize.y / 25.0;
                break;
            case 2:
                laserDestinationX += (double) GameActivity.mScreenSize.x / 25.0;
                break;
            case 3:
                laserDestinationY -= (double) GameActivity.mScreenSize.y / 25.0;
                break;
        }
    }


    public boolean didLaserBeamPenetrateTheFinger() {
        int destinationToCenterAngle = (int)(Math.atan2(laserDestinationY - center.y, laserDestinationX - center.x)*180/Math.PI);
        int fingerToCenterAngle = (int)(Math.atan2(GameView.fingerPosition.y - center.y, GameView.fingerPosition.x - center.x)*180/Math.PI);

        return (fingerToCenterAngle < destinationToCenterAngle + 3 && fingerToCenterAngle > destinationToCenterAngle - 3);
    }


    public void setLaserBeamPaint(int strokeWidth, String color, Canvas canvas) {
        laserBeamPaint.setStrokeWidth(strokeWidth);
        laserBeamPaint.setColor(Color.parseColor(color));
        canvas.drawLine((float) center.x, (float) center.y, (float) laserDestinationX, (float) laserDestinationY, laserBeamPaint);
        canvas.drawCircle((float)laserDestinationX, (float)laserDestinationY, strokeWidth/4, laserBeamPaint);
    }

    public void drawLaserBeam(Canvas canvas) {
        setLaserBeamPaint(45, "#9C27B0", canvas);
        setLaserBeamPaint(43, "#AB47BC", canvas);
        setLaserBeamPaint(39, "#B868C8", canvas);
        setLaserBeamPaint(33, "#CE93D8", canvas);
        setLaserBeamPaint(25, "#E1BEE7", canvas);
        setLaserBeamPaint(20, "#F3E5F5", canvas);
    }
}