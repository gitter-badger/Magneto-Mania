package com.sdsmdg.kd.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BoomerangTwister {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point twisterPosition = new Point(0,0);
    protected double twisterVelocity;
    protected double twisterVelocityMaxMagnitude;

    protected Paint twisterPaint = new Paint();
    protected final int twisterRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize)/ (50*Math.PI)));
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BoomerangTwister() {
        this.twisterPosition.x = GameActivity.mScreenSize.x + 120;
        this.twisterPosition.y = GameActivity.mScreenSize.y + 120;
        this.twisterVelocity = 20;
        this.twisterVelocityMaxMagnitude = 20;
        this.twisterPaint.setColor(Color.parseColor("#FFAA00"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initTwister(MonsterBall monsterBall) {
        Random random = new Random();

        twisterPosition               = Geometry.setCoordinates(monsterBall.monsterPosition);
        twisterVelocity               = random.nextInt(10) + 15 + (int)(GameView.Score / 1000);
        twisterVelocityMaxMagnitude   = random.nextInt(10) + 15 + (int)(GameView.Score / 1000);
    }


    public void attackTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(GameView.destinationPoint, GameView.initialPoint, (int)twisterVelocity);

        twisterVelocity   -= 0.5;
        twisterPosition.x += mVelocityComponent.x;
        twisterPosition.y += mVelocityComponent.y;
    }


    public boolean didTwisterCaptureTheFinger () {
        int distance = Geometry.distance(GameView.fingerPosition, twisterPosition);

        return distance < this.twisterRadius;
    }
}
