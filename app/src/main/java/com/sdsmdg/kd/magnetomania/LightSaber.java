package com.sdsmdg.kd.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


public class LightSaber {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point     lightSaberCenter   = new Point(0,0);
    protected Point[]   lightSaberTip      = new Point[4];

    protected double saberCenterVelocity;
    protected double lightSaberOmega;

    protected Paint saberCentralPaint  = new Paint();
    protected Paint saberClassOnePaint = new Paint();
    protected Paint saberClassTwoPaint = new Paint();

    protected final int saberCentralRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (300 * Math.PI)));
    protected final int saberTipRadius     = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (5 * Math.PI)));
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public LightSaber() {

        this.lightSaberCenter.x   = GameActivity.mScreenSize.x + 300;
        this.lightSaberCenter.y   = GameActivity.mScreenSize.y + 300;

        this.saberCenterVelocity  = 20;

        for (int i = 0; i < 4; i++) {
            this.lightSaberTip[i] = new Point(0,0);
        }

        this.lightSaberOmega      = 10;

        this.saberCentralPaint.setColor(Color.GRAY);

        this.saberClassOnePaint.setColor(Color.LTGRAY);
        this.saberClassTwoPaint.setColor(Color.MAGENTA);

        this.saberClassOnePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.saberClassTwoPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.saberClassOnePaint.setStrokeWidth(25);
        this.saberClassTwoPaint.setStrokeWidth(25);
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initLightSaberBlade(MonsterBall monsterBall) {
        lightSaberCenter.x = monsterBall.monsterPosition.x;
        lightSaberCenter.y = monsterBall.monsterPosition.y;

        for (int i = 0; i < 4; i++) {
            lightSaberTip[i].set(lightSaberCenter.x + (int)(saberTipRadius * Math.cos(90*i)),
                    lightSaberCenter.y + (int)(saberTipRadius * Math.sin(90*i)));
        }
    }


    public void swirlTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(GameView.destinationPoint, GameView.initialPoint, (int)saberCenterVelocity);

        lightSaberCenter.x += mVelocityComponent.x;
        lightSaberCenter.y += mVelocityComponent.y;
    }


    public void drawLightSaberBlade(Canvas canvas) {
        canvas.drawCircle(lightSaberCenter.x, lightSaberCenter.y, saberCentralRadius, saberCentralPaint);
    }
}
