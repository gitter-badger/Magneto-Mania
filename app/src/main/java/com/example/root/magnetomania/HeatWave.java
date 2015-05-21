package com.example.root.magnetomania;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;



public class HeatWave {
    protected Point heatCenter = new Point(0,0);

    protected int heatWaveVelocity;
    protected int heatWaveRadius;
    protected Paint heatWavePaint = new Paint();


    public HeatWave()
    {
        this.heatCenter.x = GameActivity.mScreenSize.x + 80;
        this.heatCenter.y = GameActivity.mScreenSize.y + 80;
        this.heatWaveVelocity = 12;
        this.heatWaveRadius = 0;
        this.heatWavePaint.setColor(Color.YELLOW);
    }

    public void initHeatWave(MonsterBall monsterBall)
    {
        heatCenter.x = monsterBall.monsterPosition.x;
        heatCenter.y = monsterBall.monsterPosition.y;
        heatWaveRadius = 0;
    }

    public RectF setHeatWaveSize(Point center)
    {
        RectF heatRect = new RectF();

        heatRect.left   = center.x - heatWaveRadius;
        heatRect.top    = center.y - heatWaveRadius;
        heatRect.right  = center.x + heatWaveRadius;
        heatRect.bottom = center.y + heatWaveRadius;

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

    public boolean didHeatWaveBurnTheFinger (Point fingerPosition, int waveType)
    {
        int distance = Geometry.distance(fingerPosition, heatCenter);
        
        double slope = (double)(fingerPosition.y - heatCenter.y) / (double)(fingerPosition.x - heatCenter.x);
        double tan30 = 0.57735;
        double tan60 = 1.73205;

        if (distance <= heatWaveRadius + 3 && distance >= heatWaveRadius - 9)
        {
            if(waveType == 1)
            {
                if(fingerPosition.x > heatCenter.x && fingerPosition.y > heatCenter.y)
                {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(fingerPosition.x < heatCenter.x && fingerPosition.y > heatCenter.y)
                {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
                else if(fingerPosition.x < heatCenter.x && fingerPosition.y < heatCenter.y)
                {
                    if(slope > tan30 && slope < tan60)
                        return true;
                }
                else if(fingerPosition.x > heatCenter.x && fingerPosition.y < heatCenter.y)
                {
                    if(slope < -tan60)
                        return true;
                    if(slope > -tan30 && slope < 0)
                        return true;
                }
            }
            else
            {
                if(fingerPosition.x > heatCenter.x && fingerPosition.y > heatCenter.y)
                {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(fingerPosition.x < heatCenter.x && fingerPosition.y > heatCenter.y)
                {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
                else if(fingerPosition.x < heatCenter.x && fingerPosition.y < heatCenter.y)
                {
                    if(slope > 0 && slope < tan30)
                        return true;
                    if(slope > tan60)
                        return true;
                }
                else if(fingerPosition.x > heatCenter.x && fingerPosition.y < heatCenter.y)
                {
                    if(slope < -tan30 && slope > -tan60)
                        return true;
                }
            }
        }
        return false;
    }

}
