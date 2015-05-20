package com.example.root.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

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
        this.heatWaveVelocity = 15;
        this.heatWaveRadius = 0;
        this.heatWavePaint.setColor(Color.YELLOW);
    }

    public void initHeatWave(MonsterBall monsterBall)
    {
        this.heatOriginX = monsterBall.monsterX;
        this.heatOriginY = monsterBall.monsterY;
        this.heatWaveRadius = 0;
    }

    public RectF setHeatWaveSize(int centerX, int centerY)
    {
        RectF heatRect = new RectF();

        heatRect.left   = centerX - this.heatWaveRadius;
        heatRect.top    = centerY - this.heatWaveRadius;
        heatRect.right  = centerX + this.heatWaveRadius;
        heatRect.bottom = centerY + this.heatWaveRadius;

        this.heatWaveRadius += this.heatWaveVelocity;
        return heatRect;
    }

    public void drawHeatWave(Canvas canvas, RectF heatRect, int startAngle)
    {
        for(int i=0; i<6; i++)
        {
            canvas.drawArc(heatRect, startAngle + 60*i, 30, false, this.heatWavePaint);
        }
    }

    public boolean didHeatWaveBurnTheFinger (int fingerX, int fingerY, int waveType)
    {
        int distance = (int) Math.sqrt((heatOriginX - fingerX)*(heatOriginX - fingerX) + (heatOriginY - fingerY)*(heatOriginY - fingerY));
        double slope = (double)(fingerY - heatOriginY) / (double)(fingerX - heatOriginX);
        double tan30 = 0.57735;
        double tan60 = 1.73205;

        if (distance <= heatWaveRadius && distance >= heatWaveRadius - 5 )
        {
            if(waveType == 1)
            {
                if((fingerY > heatOriginY && slope < tan30) || (slope > tan60 && fingerX > heatOriginX) || (slope > -tan60 && slope < -tan30) ||
                   (fingerY < heatOriginY && slope < tan30) || (slope > tan60 && fingerX < heatOriginX))
                    return true;
            }
            else
            {
                if((slope > tan30 && slope < tan60) || (fingerX < heatOriginX && slope < -tan60) || (slope > -tan30 && fingerY > heatOriginY) ||
                   (fingerX > heatOriginX && slope < -tan60) || (slope > -tan30 && fingerY < heatOriginY))

                    return true;
            }
        }
        return false;
    }

}
