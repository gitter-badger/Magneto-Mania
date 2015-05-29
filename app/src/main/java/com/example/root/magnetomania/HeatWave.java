package com.example.root.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;



public class HeatWave {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point heatCenter = new Point(0,0);

    protected int heatWaveVelocity;
    protected int heatWaveRadius;
    protected Paint heatWavePaint = new Paint();
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public HeatWave() {
        this.heatCenter.x = GameActivity.mScreenSize.x + 80;
        this.heatCenter.y = GameActivity.mScreenSize.y + 80;
        this.heatWaveVelocity = 6;
        this.heatWaveRadius = 0;
        this.heatWavePaint.setColor(Color.YELLOW);
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initHeatWave(MonsterBall monsterBall) {
        heatCenter = Geometry.setCoordinates(monsterBall.monsterPosition);
        heatWaveRadius = 0;
        heatWaveVelocity = 6 + (int)(GameView.Score / 500);
    }


    public RectF setHeatWaveSize(Point center) {
        RectF heatRect = new RectF();

        heatRect.left   = center.x - heatWaveRadius;
        heatRect.top    = center.y - heatWaveRadius;
        heatRect.right  = center.x + heatWaveRadius;
        heatRect.bottom = center.y + heatWaveRadius;

        heatWaveRadius += heatWaveVelocity;
        return heatRect;
    }


    public void drawHeatWave(Canvas canvas, RectF heatRect, int startAngle) {
        for(int i=0; i<6; i++) {
            canvas.drawArc(heatRect, startAngle + 60*i, 30, false, heatWavePaint);
        }
    }


    public boolean didHeatWaveBurnTheFinger (int waveType) {
        int distance = Geometry.distance(GameView.fingerPosition, heatCenter);
        
        double slope = (double)(GameView.fingerPosition.y - heatCenter.y) / (double)(GameView.fingerPosition.x - heatCenter.x);
        double tan30 = 0.57735;
        double tan60 = 1.73205;

        if (distance <= heatWaveRadius && distance >= heatWaveRadius-3) {
            if(waveType == 1) {
                if(GameView.fingerPosition.x > heatCenter.x && GameView.fingerPosition.y > heatCenter.y) {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(GameView.fingerPosition.x < heatCenter.x && GameView.fingerPosition.y > heatCenter.y) {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
                else if(GameView.fingerPosition.x < heatCenter.x && GameView.fingerPosition.y < heatCenter.y) {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(GameView.fingerPosition.x > heatCenter.x && GameView.fingerPosition.y < heatCenter.y) {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
            }
            else {
                if(GameView.fingerPosition.x > heatCenter.x && GameView.fingerPosition.y > heatCenter.y) {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(GameView.fingerPosition.x < heatCenter.x && GameView.fingerPosition.y > heatCenter.y) {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
                else if(GameView.fingerPosition.x < heatCenter.x && GameView.fingerPosition.y < heatCenter.y) {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(GameView.fingerPosition.x > heatCenter.x && GameView.fingerPosition.y < heatCenter.y) {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
            }
        }
        return false;
    }

}
