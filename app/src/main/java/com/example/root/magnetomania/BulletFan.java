package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class BulletFan {

    /******************************************** CLASS MEMBERS ********************************************/
    protected int bulletX;
    protected int bulletY;
    protected int bulletVelocity;

    protected Paint bulletPaint = new Paint();
    protected final int bulletRadius = 20;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BulletFan()
    {
        this.bulletX = GameActivity.mScreenSize.x + 80;
        this.bulletY = GameActivity.mScreenSize.y + 80;
        this.bulletVelocity = 0;

        bulletPaint.setColor(Color.parseColor("#22EE44"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD WHICH POSITIONS THE ROCKET AT MONSTER BALL POSITION *********************/
    public void initBullet(MonsterBall monsterBall) {
        Random random = new Random();

        this.bulletX = monsterBall.monsterX;
        this.bulletY = monsterBall.monsterY;

        /*Velocity and exhaust time is randomized for each attack.*/
        this.bulletVelocity = random.nextInt(15) + 25;
    }
    /**--------------------------------------------------------------------------------------------------**/



    public void setDirectionAndShoot(BulletFan bulletFan)
    {

    }
}
