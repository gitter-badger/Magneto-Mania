package com.sdsmdg.kd.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BoomerangTwister {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point twisterPosition = new Point(0,0);
    protected double twisterVelocity;

    protected Paint twisterPaint = new Paint();
    protected final int twisterRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize)/ (40*Math.PI)));
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BoomerangTwister() {
        this.twisterPosition.x = GameActivity.mScreenSize.x + 120;
        this.twisterPosition.y = GameActivity.mScreenSize.y + 120;
        this.twisterVelocity = 0;

        this.twisterPaint.setColor(Color.parseColor("#FFAA00"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initTwister(MonsterBall monsterBall) {
        Random random = new Random();

        twisterPosition   = Geometry.setCoordinates(monsterBall.monsterPosition);
        twisterVelocity   = random.nextInt(10) + 20 + (int)(GameView.Score / 750);
    }


    public void attackTowardsFinger(MonsterBall monsterBall) {
        Point rVelocityComponent = Geometry.calcVelocityComponents(GameView.fingerPosition, twisterPosition, (int)twisterVelocity);
        twisterVelocity   -= 0.1;
        twisterPosition.x += rVelocityComponent.x;
        twisterPosition.y += rVelocityComponent.y;
    }


    public boolean didTwisterCaptureTheFinger () {
        int distance = Geometry.distance(GameView.fingerPosition, twisterPosition);

        return distance < this.twisterRadius;
    }
}
