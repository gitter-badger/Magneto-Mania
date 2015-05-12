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
        this.monsterVelocity = random.nextInt(10) + 10;

        if(this.monsterX == 1)
            this.monsterX = GameActivity.mScreenSize.x;


        monsterPaint.setColor(Color.parseColor("#FFFFFF"));
    }


    public void attackFingerPosition(int attackAtX, int attackAtY, int initialX, int initialY, int moveStyle) {
        int distance = (int) Math.sqrt((attackAtX - initialX) * (attackAtX - initialX) + (attackAtY - initialY) * (attackAtY - initialY));
        int monsterVelocityX = monsterVelocity * Math.abs(attackAtX - initialX) / distance;
        int monsterVelocityY = monsterVelocity * Math.abs(attackAtY - initialY) / distance;

        if (moveStyle == 1) {
            monsterX += monsterVelocityX;
            monsterY += monsterVelocityY;
        } else if (moveStyle == 2) {
            monsterX += monsterVelocityX;
            monsterY -= monsterVelocityY;
        } else if (moveStyle == 3) {
            monsterX -= monsterVelocityX;
            monsterY -= monsterVelocityY;
        } else if (moveStyle == 4) {
            monsterX -= monsterVelocityX;
            monsterY += monsterVelocityY;
        }
    }
}
