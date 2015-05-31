package com.sdsmdg.kd.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;



public class MagnetRocket {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point rocketPosition = new Point(0,0);
    protected double rocketVelocity;

    protected Paint rocketPaint = new Paint();
    protected final int rocketRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize)/ (32*Math.PI)));

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
        rocketVelocity   = random.nextInt(10) + 10 + (int)(GameView.Score / 750);
        rocketXhaustTime = random.nextInt(50) + 150;

        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }


    public void rocketTrackFinger() {
        Point rVelocityComponent = Geometry.calcVelocityComponents(GameView.fingerPosition, rocketPosition, (int)rocketVelocity);

            rocketVelocity   += 0.05;
            rocketPosition.x += rVelocityComponent.x;
            rocketPosition.y += rVelocityComponent.y;

        SpriteAnimation.iteratorIncrement();
    }


    public boolean didRocketGetTheFinger () {
        int distance = Geometry.distance(GameView.fingerPosition, rocketPosition);

        return distance < this.rocketRadius;
    }

}