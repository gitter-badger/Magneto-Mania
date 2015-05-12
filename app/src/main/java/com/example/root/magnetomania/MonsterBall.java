package com.example.root.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


public class MonsterBall {

    protected int monsterX;
    protected int monsterY;
    protected int monsterVelocity;

    protected Paint monsterPaint = new Paint();
    protected final int monsterRadius = 100;

    public MonsterBall()
    {
        Random random = new Random();

        this.monsterY = random.nextInt(GameActivity.mScreenSize.y + 1);
        this.monsterX = random.nextInt(2);
        this.monsterVelocity = 10;

        if(this.monsterX == 1)
            this.monsterX = GameActivity.mScreenSize.x;

        monsterPaint.setColor(Color.parseColor("#FFFFFF"));
    }

    public void followFinger(int fingerX, int fingerY)
    {
            monsterX -= (monsterX - fingerX) * monsterVelocity / 100;
            monsterY -= (monsterY - fingerY) * monsterVelocity / 100;
    }
}
