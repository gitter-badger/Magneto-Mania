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

        this.saberCenterVelocity  = 15;

        for (int i = 0; i < 4; i++) {
            this.lightSaberTip[i] = new Point(0,0);
        }

        this.lightSaberOmega      = 5;

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
            lightSaberTip[i].set(lightSaberCenter.x + (int)(saberTipRadius * Math.cos(Math.PI*i/2)),
                    lightSaberCenter.y - (int)(saberTipRadius * Math.sin(Math.PI*i/2)));
        }
    }


    public void swirlTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(GameView.destinationPoint, GameView.initialPoint, (int)saberCenterVelocity);

        lightSaberCenter.x += mVelocityComponent.x;
        lightSaberCenter.y += mVelocityComponent.y;

        for(int i = 0; i < 4; i++) {
            lightSaberTip[i].x += mVelocityComponent.x;
            lightSaberTip[i].y += mVelocityComponent.y;
        }

        for(int i = 0; i < 4; i++) {
            lightSaberTip[i] = Geometry.circularPathDisplacement(lightSaberTip[i], lightSaberCenter, saberTipRadius, lightSaberOmega);
        }
    }


    public void drawLightSaberBlade(Canvas canvas) {
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, lightSaberTip[0].x, lightSaberTip[0].y, saberClassOnePaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, lightSaberTip[1].x, lightSaberTip[1].y, saberClassTwoPaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, lightSaberTip[2].x, lightSaberTip[2].y, saberClassOnePaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, lightSaberTip[3].x, lightSaberTip[3].y, saberClassTwoPaint);
        canvas.drawCircle(lightSaberCenter.x, lightSaberCenter.y, saberCentralRadius, saberCentralPaint);
    }
}
