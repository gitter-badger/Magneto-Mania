package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by root on 13/5/15.
 */
public class MagnetRocket {
    protected int rocketX;
    protected int rocketY;
    protected int rocketVelocity;

    protected Paint rocketPaint = new Paint();
    protected final int rocketRadius = 50;
    
    protected int rocketXhaustTime;


    public MagnetRocket() {
        this.rocketX = GameActivity.mScreenSize.x + 80;
        this.rocketY = GameActivity.mScreenSize.y + 80;
        this.rocketVelocity = 0;

        this.rocketXhaustTime = 0;
        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }


    public void initRocket(MonsterBall monsterBall) {
        Random random = new Random();

        this.rocketX = monsterBall.monsterX;
        this.rocketY = monsterBall.monsterY;
        this.rocketVelocity = random.nextInt(15) + 15;

        this.rocketXhaustTime = random.nextInt(50) + 100;
        rocketPaint.setColor(Color.parseColor("#CC1100"));
    }

    public void rocketTrackFinger(int fingerX, int fingerY) {
        int distance = (int) Math.sqrt((fingerX - rocketX)*(fingerX - rocketX) + (fingerY - rocketY)*(fingerY - rocketY));
        int rocketVelocityX = rocketVelocity * (fingerX - rocketX) / distance;
        int rocketVelocityY = rocketVelocity * (fingerY - rocketY) / distance;

            rocketX += rocketVelocityX;
            rocketY += rocketVelocityY;
    }
}