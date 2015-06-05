package com.sdsmdg.kd.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


public class LightSaber {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point     lightSaberCenter   = new Point(0,0);
    protected Point     saberDestination   = new Point(0,0);

    protected double[]   lightSaberTipX = new double[4];
    protected double[]   lightSaberTipY = new double[4];
    protected double[]   lightSaberTip  = {0,0};

    protected double saberCenterVelocity;
    protected double lightSaberOmega;

    protected Paint saberCentralPaint  = new Paint();
    protected Paint saberClassOnePaint = new Paint();
    protected Paint saberClassTwoPaint = new Paint();

    protected final int saberCentralRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (300 * Math.PI)));
    protected final int saberTipRadius     = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (5 * Math.PI)));

    protected boolean is_saber_thrown;
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public LightSaber() {

        this.lightSaberCenter.x   = GameActivity.mScreenSize.x + 300;
        this.lightSaberCenter.y   = GameActivity.mScreenSize.y + 300;

        this.saberCenterVelocity  = 5;

        for (int i = 0; i < 4; i++) {
            this.lightSaberTipX[i] = 0;
            this.lightSaberTipY[i] = 0;
        }

        this.lightSaberOmega      = 3;

        this.saberCentralPaint.setColor(Color.GRAY);

        this.saberClassOnePaint.setColor(Color.LTGRAY);
        this.saberClassTwoPaint.setColor(Color.MAGENTA);

        this.saberClassOnePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.saberClassTwoPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.saberClassOnePaint.setAlpha(3);
        this.saberClassTwoPaint.setAlpha(3);

        this.saberClassOnePaint.setStrokeWidth(25);
        this.saberClassTwoPaint.setStrokeWidth(25);

        this.is_saber_thrown      = false;
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initLightSaberBlade(MonsterBall monsterBall) {
        lightSaberCenter = Geometry.setCoordinates(monsterBall.monsterPosition);
        saberDestination = Geometry.setCoordinates(GameView.fingerPosition);

        for (int i = 0; i < 4; i++) {
            lightSaberTipX[i] = lightSaberCenter.x + (saberTipRadius * Math.cos(Math.PI*i/2));
            lightSaberTipY[i] = lightSaberCenter.y - (saberTipRadius * Math.sin(Math.PI*i/2));
        }
    }


    public void swirlTowardsFinger() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(saberDestination, GameView.initialPoint, (int)saberCenterVelocity);

        lightSaberCenter.x += mVelocityComponent.x;
        lightSaberCenter.y += mVelocityComponent.y;

        for(int i = 0; i < 4; i++) {
            lightSaberTipX[i] += mVelocityComponent.x;
            lightSaberTipY[i] += mVelocityComponent.y;
        }

        int alpha = saberClassOnePaint.getAlpha();
        if(alpha < 255) {
            alpha = (alpha + 3) % 256;
            saberClassOnePaint.setAlpha(alpha);
            saberClassTwoPaint.setAlpha(alpha);
        }
        for(int i = 0; i < 4; i++) {
            lightSaberTip[0] = lightSaberTipX[i];
            lightSaberTip[1] = lightSaberTipY[i];
            lightSaberTip = Geometry.circularPathDisplacement(lightSaberTip, lightSaberCenter, saberTipRadius, lightSaberOmega);
            lightSaberTipX[i] = lightSaberTip[0];
            lightSaberTipY[i] = lightSaberTip[1];
        }
    }


    public void drawLightSaberBlade(Canvas canvas) {
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, (float)lightSaberTipX[0], (float)lightSaberTipY[0], saberClassOnePaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, (float)lightSaberTipX[1], (float)lightSaberTipY[1], saberClassTwoPaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, (float)lightSaberTipX[2], (float)lightSaberTipY[2], saberClassOnePaint);
        canvas.drawLine(lightSaberCenter.x, lightSaberCenter.y, (float)lightSaberTipX[3], (float)lightSaberTipY[3], saberClassTwoPaint);
        canvas.drawCircle(lightSaberCenter.x, lightSaberCenter.y, saberCentralRadius, saberCentralPaint);
    }
}
