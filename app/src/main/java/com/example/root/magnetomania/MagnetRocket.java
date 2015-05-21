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

    /*rocketXhaustTime is the measure of the capacity of the rocket to follow the finger.*/
    /*There is a member in GameView named rocketXhaustCount, which increases on every update.
     * When it becomes equal to this, the rocket dies out and stops following the finger.*/
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



    /********************* METHOD WHICH POSITIONS THE ROCKET AT MONSTER BALL POSITION *********************/
    public void initRocket(MonsterBall monsterBall) {
        Random random = new Random();

        rocketPosition.x = monsterBall.monsterPosition.x;
        rocketPosition.y = monsterBall.monsterPosition.y;

        /*Velocity and exhaust time is randomized for each attack.*/
        rocketVelocity = random.nextInt(15) + 15;
        rocketXhaustTime = random.nextInt(50) + 100;

        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD THAT ENABLES ROCKET TO CONSTANTLY FOLLOW THE FINGER *********************/
    public void rocketTrackFinger(Point fingerPosition) {
        Point rVelocityComponent = Geometry.calcVelocityComponents(fingerPosition, rocketPosition, rocketVelocity);

            rocketPosition.x += rVelocityComponent.x;
            rocketPosition.y += rVelocityComponent.y;
    }
    /**--------------------------------------------------------------------------------------------------**/


    public boolean didRocketGetTheFinger (Point fingerPosition)
    {
        int distance = Geometry.distance(fingerPosition, rocketPosition);

        if (distance < this.rocketRadius)
            return true;
        else
            return false;
    }
}