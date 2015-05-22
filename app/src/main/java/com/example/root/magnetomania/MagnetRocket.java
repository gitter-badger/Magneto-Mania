package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;



public class MagnetRocket {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point rocketPosition = new Point(0,0);
    protected int rocketVelocity;

    protected Paint rocketPaint = new Paint();
    protected final int rocketRadius = 50;

    protected int rocketXhaustTime;
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public MagnetRocket() {
        this.rocketPosition.x = GameActivity.mScreenSize.x + 80;
        this.rocketPosition.y = GameActivity.mScreenSize.y + 80;
        this.rocketVelocity = 0;

        this.rocketXhaustTime = 0;
        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initRocket(MonsterBall monsterBall) {
        Random random = new Random();

        rocketPosition   = Geometry.setCoordinates(monsterBall.monsterPosition);
        rocketVelocity   = random.nextInt(15) + 15;
        rocketXhaustTime = random.nextInt(50) + 100;

        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }


    public void rocketTrackFinger(Point fingerPosition) {
        Point rVelocityComponent = Geometry.calcVelocityComponents(fingerPosition, rocketPosition, rocketVelocity);

            rocketPosition.x += rVelocityComponent.x;
            rocketPosition.y += rVelocityComponent.y;
    }


    public boolean didRocketGetTheFinger (Point fingerPosition) {
        int distance = Geometry.distance(fingerPosition, rocketPosition);

        if (distance < this.rocketRadius)
            return true;
        else
            return false;
    }

}