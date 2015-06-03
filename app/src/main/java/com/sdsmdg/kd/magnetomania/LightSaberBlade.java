package com.sdsmdg.kd.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


public class LightSaberBlade {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point bladePosition       = new Point(0,0);
    protected Point[] saberPosition     = new Point[4];
    protected double bladeVelocity;
    protected double firstTheta;
    protected double bladeOmega;

    protected Paint bladeCentralPaint;
    protected Paint bladeSaberPaint1;
    protected Paint bladeSaberPaint2;

    protected final int bladeCenterRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (300 * Math.PI)));
    protected final int bladeSaberRadius  = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (5 * Math.PI)));
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public LightSaberBlade() {

        this.bladePosition.x = GameActivity.mScreenSize.x - 300;
        this.bladePosition.y = GameActivity.mScreenSize.y - 300;

        this.bladeVelocity   = 20;
        this.firstTheta      = 0;

        for (int i = 0; i < 4; i++) {
            this.saberPosition[i] = new Point(0,0);
        }

        this.bladeOmega      = 10;

        this.bladeCentralPaint.setColor(Color.GRAY);

        this.bladeSaberPaint1.setColor(Color.LTGRAY);
        this.bladeSaberPaint2.setColor(Color.MAGENTA);

        this.bladeSaberPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        this.bladeSaberPaint2.setStyle(Paint.Style.FILL_AND_STROKE);

        this.bladeSaberPaint1.setStrokeWidth(25);
        this.bladeSaberPaint2.setStrokeWidth(25);
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initLightSaberBlade(MonsterBall monsterBall) {
        bladePosition.x = monsterBall.monsterPosition.x;
        bladePosition.y = monsterBall.monsterPosition.y;

        for (int i = 0; i < 4; i++) {
            saberPosition[i] = new Point(bladePosition.x + (int)(bladeSaberRadius * Math.cos(90*i)),
                    bladePosition.y + (int)(bladeSaberRadius * Math.sin(90*i)));
        }
    }


    public void swirlTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(GameView.destinationPoint, GameView.initialPoint, (int)bladeVelocity);

        bladePosition.x += mVelocityComponent.x;
        bladePosition.y += mVelocityComponent.y;

        for (int i = 0; i < 4; i++) {
            saberPosition[i] = Geometry.circularPathDisplacement(saberPosition[i], bladePosition, bladeSaberRadius, bladeOmega);
        }
    }
}
