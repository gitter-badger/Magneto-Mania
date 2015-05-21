package com.example.root.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by root on 16/5/15.
 */
public class HeatWave {
    protected int heatOriginX;
    protected int heatOriginY;

    protected int heatWaveVelocity;
    protected int heatWaveRadius;
    protected Paint heatWavePaint = new Paint();


    public HeatWave()
    {
        this.heatOriginX = GameActivity.mScreenSize.x + 80;
        this.heatOriginY = GameActivity.mScreenSize.y + 80;
        this.heatWaveVelocity = 12;
        this.heatWaveRadius = 0;
        this.heatWavePaint.setColor(Color.YELLOW);
    }

    public void initHeatWave(MonsterBall monsterBall)
    {
        heatOriginX = monsterBall.monsterPosition.x;
        heatOriginY = monsterBall.monsterPosition.y;
        heatWaveRadius = 0;
    }

    public RectF setHeatWaveSize(int centerX, int centerY)
    {
        RectF heatRect = new RectF();

        heatRect.left   = centerX - heatWaveRadius;
        heatRect.top    = centerY - heatWaveRadius;
        heatRect.right  = centerX + heatWaveRadius;
        heatRect.bottom = centerY + heatWaveRadius;

        heatWaveRadius += heatWaveVelocity;
        return heatRect;
    }

    public void drawHeatWave(Canvas canvas, RectF heatRect, int startAngle)
    {
        for(int i=0; i<6; i++)
        {
            canvas.drawArc(heatRect, startAngle + 60*i, 30, false, heatWavePaint);
        }
    }

    public boolean didHeatWaveBurnTheFinger (int fingerX, int fingerY, int waveType)
    {
        int distance = (int) Math.sqrt((heatOriginX - fingerX)*(heatOriginX - fingerX) + (heatOriginY - fingerY)*(heatOriginY - fingerY));
        double slope = (double)(fingerY - heatOriginY) / (double)(fingerX - heatOriginX);
        double tan30 = 0.57735;
        double tan60 = 1.73205;

        if (distance <= heatWaveRadius + 3 && distance >= heatWaveRadius - 9)
        {
            if(waveType == 1)
            {
                if(fingerX > heatOriginX && fingerY > heatOriginY)
                {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(fingerX < heatOriginX && fingerY > heatOriginY)
                {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
                else if(fingerX < heatOriginX && fingerY < heatOriginY)
                {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(fingerX > heatOriginX && fingerY < heatOriginY)
                {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
            }
            else
            {
                if(fingerX > heatOriginX && fingerY > heatOriginY)
                {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(fingerX < heatOriginX && fingerY > heatOriginY)
                {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
                else if(fingerX < heatOriginX && fingerY < heatOriginY)
                {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(fingerX > heatOriginX && fingerY < heatOriginY)
                {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
            }
        }
        return false;
    }

}
