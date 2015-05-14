package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class BulletFan {

    /******************************************** CLASS MEMBERS ********************************************/
    protected int bulletCenterX;
    protected int bulletCenterY;

    protected int bulletAboveX;
    protected int bulletAboveY;

    protected int bulletBelowX;
    protected int bulletBelowY;

    protected int bulletTopMostX;
    protected int bulletTopMostY;

    protected int bulletBottomX;
    protected int bulletBottomY;

    protected int bulletsVelocity;

    protected Paint bulletsPaint = new Paint();
    protected final int bulletsRadius = 20;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BulletFan()
    {
        this.bulletCenterX = GameActivity.mScreenSize.x + 80;
        this.bulletCenterY = GameActivity.mScreenSize.y + 80;

        this.bulletAboveX = GameActivity.mScreenSize.x + 80;
        this.bulletAboveY = GameActivity.mScreenSize.y + 80;

        this.bulletBelowX = GameActivity.mScreenSize.x + 80;
        this.bulletBelowY = GameActivity.mScreenSize.y + 80;

        this.bulletTopMostX = GameActivity.mScreenSize.x + 80;
        this.bulletTopMostY = GameActivity.mScreenSize.y + 80;

        this.bulletBottomX = GameActivity.mScreenSize.x + 80;
        this.bulletBottomY = GameActivity.mScreenSize.y + 80;

        this.bulletsVelocity = 0;

        bulletsPaint.setColor(Color.parseColor("#22EE44"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD WHICH POSITIONS THE ROCKET AT MONSTER BALL POSITION *********************/
    public void initBullets(MonsterBall monsterBall) {
        Random random = new Random();

        this.bulletCenterX = monsterBall.monsterX;
        this.bulletCenterY = monsterBall.monsterY;

        this.bulletAboveX = monsterBall.monsterX;
        this.bulletAboveY = monsterBall.monsterY;

        this.bulletBelowX = monsterBall.monsterX;
        this.bulletBelowY = monsterBall.monsterY;

        this.bulletTopMostX = monsterBall.monsterX;
        this.bulletTopMostY = monsterBall.monsterY;

        this.bulletBottomX = monsterBall.monsterX;
        this.bulletBottomY = monsterBall.monsterY;

        /*Velocity and exhaust time is randomized for each attack.*/
        this.bulletsVelocity = random.nextInt(15) + 25;
    }
    /**--------------------------------------------------------------------------------------------------**/



    public void setDirectionAndShoot(int attackAtX, int attackAtY, int attackFromX, int attackFromY, int moveStyle) {

        /*distance is the length of he line of contact between monster and finger at the moment,
         * when the monster is resting on the wall, and just about to shoot the bullets. */
        int distance = (int) Math.sqrt((attackAtX - attackFromX) * (attackAtX - attackFromX) + (attackAtY - attackFromY) * (attackAtY - attackFromY));


        /*These are the x and y components of the velocity of the bullets, so that the bullets
         *travel with constant velocity during one shoot towards the finger.*/
        int bulletCenterVelocityX = bulletsVelocity * Math.abs(attackAtX - attackFromX) / distance;
        int bulletCenterVelocityY = bulletsVelocity * Math.abs(attackAtY - attackFromY) / distance;

        int bulletAboveVelocityX = bulletsVelocity * Math.abs(attackAtX - attackFromX + 20) / distance;
        int bulletAboveVelocityY = bulletsVelocity * Math.abs(attackAtY - attackFromY + 20) / distance;

        int bulletBelowVelocityX = bulletsVelocity * Math.abs(attackAtX - attackFromX - 20) / distance;
        int bulletBelowVelocityY = bulletsVelocity * Math.abs(attackAtY - attackFromY - 20) / distance;

        int bulletTopMostVelocityX = bulletsVelocity * Math.abs(attackAtX - attackFromX + 40) / distance;
        int bulletTopMostVelocityY = bulletsVelocity * Math.abs(attackAtY - attackFromY + 40) / distance;

        int bulletBottomVelocityX = bulletsVelocity * Math.abs(attackAtX - attackFromX - 40) / distance;
        int bulletBottomVelocityY = bulletsVelocity * Math.abs(attackAtY - attackFromY - 40) / distance;

        /*moveStyle decides the direction of movement of the monster, its vaue is decided during
         *update() in the GameView. */
        if (moveStyle == 1)
        {
            bulletCenterX  += bulletCenterVelocityX;
            bulletAboveX   += bulletAboveVelocityX;
            bulletBelowX   += bulletBelowVelocityX;
            bulletTopMostX += bulletTopMostVelocityX;
            bulletBottomX  += bulletBottomVelocityX;

            bulletCenterY  += bulletCenterVelocityY;
            bulletAboveY   += bulletAboveVelocityY;
            bulletBelowY   += bulletBelowVelocityY;
            bulletTopMostY += bulletTopMostVelocityY;
            bulletBottomY  += bulletBottomVelocityY;
        }
        else if (moveStyle == 2)
        {
            bulletCenterX  += bulletCenterVelocityX;
            bulletAboveX   += bulletAboveVelocityX;
            bulletBelowX   += bulletBelowVelocityX;
            bulletTopMostX += bulletTopMostVelocityX;
            bulletBottomX  += bulletBottomVelocityX;

            bulletCenterY  -= bulletCenterVelocityY;
            bulletAboveY   -= bulletAboveVelocityY;
            bulletBelowY   -= bulletBelowVelocityY;
            bulletTopMostY -= bulletTopMostVelocityY;
            bulletBottomY  -= bulletBottomVelocityY;
        }
        else if (moveStyle == 3)
        {
            bulletCenterX  -= bulletCenterVelocityX;
            bulletAboveX   -= bulletAboveVelocityX;
            bulletBelowX   -= bulletBelowVelocityX;
            bulletTopMostX -= bulletTopMostVelocityX;
            bulletBottomX  -= bulletBottomVelocityX;

            bulletCenterY  -= bulletCenterVelocityY;
            bulletAboveY   -= bulletAboveVelocityY;
            bulletBelowY   -= bulletBelowVelocityY;
            bulletTopMostY -= bulletTopMostVelocityY;
            bulletBottomY  -= bulletBottomVelocityY;
        }
        else if (moveStyle == 4)
        {
            bulletCenterX  -= bulletCenterVelocityX;
            bulletAboveX   -= bulletAboveVelocityX;
            bulletBelowX   -= bulletBelowVelocityX;
            bulletTopMostX -= bulletTopMostVelocityX;
            bulletBottomX  -= bulletBottomVelocityX;

            bulletCenterY  += bulletCenterVelocityY;
            bulletAboveY   += bulletAboveVelocityY;
            bulletBelowY   += bulletBelowVelocityY;
            bulletTopMostY += bulletTopMostVelocityY;
            bulletBottomY  += bulletBottomVelocityY;
        }
    }
    /**--------------------------------------------------------------------------------------------------**/
}
