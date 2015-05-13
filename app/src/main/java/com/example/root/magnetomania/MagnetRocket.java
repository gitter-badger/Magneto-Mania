package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import java.util.Random;


public class MagnetRocket {

    /******************************************** CLASS MEMBERS ********************************************/
    protected int rocketX;
    protected int rocketY;
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
        this.rocketX = GameActivity.mScreenSize.x + 80;
        this.rocketY = GameActivity.mScreenSize.y + 80;
        this.rocketVelocity = 0;

        this.rocketXhaustTime = 0;
        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD WHICH POSITIONS THE ROCKET AT MONSTER BALL POSITION *********************/
    public void initRocket(MonsterBall monsterBall) {
        Random random = new Random();

        this.rocketX = monsterBall.monsterX;
        this.rocketY = monsterBall.monsterY;

        /*Velocity and exhaust time is randomized for each attack.*/
        this.rocketVelocity = random.nextInt(15) + 15;
        this.rocketXhaustTime = random.nextInt(50) + 100;

        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD THAT ENABLES ROCKET TO CONSTANTLY FOLLOW THE FINGER *********************/
    public void rocketTrackFinger(int fingerX, int fingerY) {
        /*Meanings of the variables here are same as that in MonsterBall method.*/
        int distance = (int) Math.sqrt((fingerX - rocketX)*(fingerX - rocketX) + (fingerY - rocketY)*(fingerY - rocketY));
        int rocketVelocityX = rocketVelocity * (fingerX - rocketX) / distance;
        int rocketVelocityY = rocketVelocity * (fingerY - rocketY) / distance;

        /*No moveStyle variable needed as it always has to travel in direction of finger
         * at every instant. */
            rocketX += rocketVelocityX;
            rocketY += rocketVelocityY;
    }
    /**--------------------------------------------------------------------------------------------------**/
}