package com.sdsmdg.kd.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BoomerangTwister {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point twisterPosition = new Point(0,0);
    protected Point twisterDestination = new Point(0,0);
    protected double twisterVelocity;
    protected double twisterVelocityMaxMagnitude;

    protected Paint twisterPaint = new Paint();
    protected final int twisterRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize)/ (50*Math.PI)));

    protected boolean is_twister_thrown;
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BoomerangTwister() {
        this.twisterPosition.x = GameActivity.mScreenSize.x + 120;
        this.twisterPosition.y = GameActivity.mScreenSize.y + 120;
        this.is_twister_thrown = false;

        this.twisterVelocity = 20;
        this.twisterVelocityMaxMagnitude = 20;
        this.twisterPaint.setColor(Color.parseColor("#FFAA00"));
        this.twisterPaint.setAlpha(150);
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initTwister(MonsterBall monsterBall) {
        Random random = new Random();

        twisterPosition               = Geometry.setCoordinates(monsterBall.monsterPosition);
        twisterVelocity               = random.nextInt(5) + 20 + (int)(GameView.Score / 1000);
        twisterVelocityMaxMagnitude   = random.nextInt(5) + 20 + (int)(GameView.Score / 1000);
        twisterDestination            = Geometry.setCoordinates(GameView.fingerPosition);
        is_twister_thrown             = true;
    }


    public void attackTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(twisterDestination, GameView.initialPoint, (int)twisterVelocity);

        twisterVelocity   -= 0.25;
        twisterPosition.x += mVelocityComponent.x;
        twisterPosition.y += mVelocityComponent.y;
    }


    public boolean didTwisterCaptureTheFinger () {
        int distance = Geometry.distance(GameView.fingerPosition, twisterPosition);

        return distance < this.twisterRadius;
    }
}
