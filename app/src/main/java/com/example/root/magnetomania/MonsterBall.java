package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;


public class MonsterBall {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point monsterPosition = new Point(0,0);
    protected int monsterVelocity;

    protected Paint monsterPaint = new Paint();
    protected final int monsterRadius = 100;

    /*This is the time for which the monster sticks to the boundary, between two successive attacks.*/
    protected int monsterSleepTime;

    /*monsterAttackTrick is to randomize the way in which the monster attacks the finger.
     *Different values correspond to different calls of methods.*/
    protected int monsterAttackTrick;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public MonsterBall()
    {
        Random random = new Random();

        this.monsterPosition.y = random.nextInt(GameActivity.mScreenSize.y + 1);
        this.monsterPosition.x = random.nextInt(2);

        if(this.monsterPosition.x == 1)
            this.monsterPosition.x = GameActivity.mScreenSize.x;


        /*Velocity and sleeptime is randomized for each attack.*/
        this.monsterVelocity = random.nextInt(10) + 15;
        this.monsterSleepTime = random.nextInt(10) + 10;

        this.monsterAttackTrick = 0;
        monsterPaint.setColor(Color.parseColor("#FFFFFF"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /***************** METHOD BY WHICH MONSTER RUSHES AT THE FINGER ALONG LINE OF CONTACT *****************/
    public void attackFingerPosition(Point destinationPoint, Point initialPoint) {

        Point mVelocityComponent = Geometry.calcVelocityComponents(destinationPoint, initialPoint, monsterVelocity);

            monsterPosition.x += mVelocityComponent.x;
            monsterPosition.y += mVelocityComponent.y;
    }

    public boolean didMonsterGetTheFinger (Point fingerPosition)
    {
        int distance = Geometry.distance(fingerPosition, monsterPosition);

        if (distance < this.monsterRadius)
            return true;
        else
            return false;
    }
    /**--------------------------------------------------------------------------------------------------**/
}
