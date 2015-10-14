package com.sdsmdg.kd.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;


public class HeatWave {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point  heatCenter = new Point(0,0);

    protected int    heatWaveVelocity;
    protected int    heatWaveRadius;
    protected int    heatWavePrevRadius;
    protected int    heatWaveDrawRadius;
    protected Paint  heatWavePaint = new Paint();
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public HeatWave() {
        this.heatCenter.x     = GameActivity.mScreenSize.x + 80;
        this.heatCenter.y     = GameActivity.mScreenSize.y + 80;
        this.heatWaveVelocity = 10;
        this.heatWaveRadius   = 0;
        this.heatWavePrevRadius = 0;
        this.heatWaveDrawRadius = 0;
        this.heatWavePaint.setAlpha(10);
        this.heatWavePaint.setColor(Color.YELLOW);
        this.heatWavePaint.setStrokeWidth(20);
        this.heatWavePaint.setStyle(Paint.Style.STROKE);
        this.heatWavePaint.setStrokeCap(Paint.Cap.BUTT);
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initHeatWave(MonsterBall monsterBall) {
        heatCenter          = Geometry.setCoordinates(monsterBall.monsterPosition);
        heatWaveRadius      = 0;
        heatWavePrevRadius = 0;
        heatWaveDrawRadius = 0;

        heatWaveVelocity    = 10;
        heatWavePaint.setAlpha(10);
    }


    public RectF setHeatWaveSize() {
        RectF heatRect     = new RectF();

        int alpha = heatWavePaint.getAlpha();
        if(alpha < 255) {
            alpha += 5;
            if(alpha > 255) {
                alpha = 255;
            }
        }
        heatWavePaint.setAlpha(alpha);
        heatWavePrevRadius = heatWaveRadius;
        heatWaveRadius += heatWaveVelocity;
        return heatRect;
    }


    public void drawHeatWave(Canvas canvas, RectF heatRect, int startAngle, float interpolation) {
        for(int i=0; i<6; i++) {
            heatWaveDrawRadius = (int)((heatWaveRadius - heatWavePrevRadius)*interpolation) + heatWavePrevRadius;

            heatRect.left      = heatCenter.x - heatWaveDrawRadius;
            heatRect.top       = heatCenter.y - heatWaveDrawRadius;
            heatRect.right     = heatCenter.x + heatWaveDrawRadius;
            heatRect.bottom    = heatCenter.y + heatWaveDrawRadius;
            canvas.drawArc(heatRect, startAngle + 60*i, 30, false, heatWavePaint);
        }
    }


    public boolean didHeatWaveBurnTheFinger (int waveType) {
        int distance = Geometry.distance(GameView.fingerPosition, heatCenter);
        int angle = (int)(Math.atan2((double)(GameView.fingerPosition.y - heatCenter.y), (double)(GameView.fingerPosition.x - heatCenter.x))*180/Math.PI);

        if (distance <= heatWaveRadius+10 && distance >= heatWaveRadius-30) {
            if(waveType == 1) {
                return ((angle > 30 && angle < 60)  || (angle > 90  && angle < 120) || (angle > 150  && angle < 180) ||
                        (angle < 0  && angle > -30) || (angle < -60 && angle > -90) || (angle < -120 && angle > -150));
            }
            else {
                return (angle > 0   && angle < 30)  || (angle > 60  && angle < 90)   || (angle > 120 && angle < 150) ||
                       (angle < -30 && angle > -60) || (angle < -90 && angle > -120) || (angle < -150 && angle > -180);
            }
        }
        return false;
    }
}
