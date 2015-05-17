package com.example.root.magnetomania;

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

}
