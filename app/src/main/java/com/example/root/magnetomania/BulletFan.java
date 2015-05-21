package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BulletFan {

    /******************************************** CLASS MEMBERS ********************************************/
    /*These two Point arrays each store the coordinates of bullets,
     * velocity components along the axes of the bullets. */
    protected Point bulletPosition[] = new Point[7];
    protected Point bulletDestination[] = new Point[7];
    protected Point bulletVelocity[] = new Point[7];

    protected double bulletDistance[] = new double[7];
    protected double slopeOfPathCentre;
    protected double slopeOfPathCorner;

    protected double bulletsVelocity;

    protected Paint bulletsPaint = new Paint();
    protected final int bulletsRadius = 18;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BulletFan()
    {
        for(int i=0; i<7; i++)
        {
            this.bulletPosition[i]    = new Point(0,0);
            this.bulletDestination[i] = new Point(0,0);
            this.bulletVelocity[i]    = new Point(0,0);
        }

        for(int i=0; i<7; i++) {
            this.bulletPosition[i].x = GameActivity.mScreenSize.x + 80;
            this.bulletPosition[i].y = GameActivity.mScreenSize.y + 80;
            this.bulletDistance[i] = 0;
        }

        this.slopeOfPathCentre = 0;
        this.slopeOfPathCorner = 0;
        this.bulletsVelocity = 0;

        bulletsPaint.setColor(Color.parseColor("#11FF22"));
    }
    /**--------------------------------------------------------------------------------------------------**/



    /********************* METHOD WHICH FIXES DIRECTIONS AND VELOCITIES OF BULLETS ************************/
    public void initBullets(MonsterBall monsterBall, int fingerX, int fingerY) {
        Random random = new Random();

        for(int i=0; i<7; i++) {
            bulletPosition[i].x = monsterBall.monsterPosition.x;
            bulletPosition[i].y = monsterBall.monsterPosition.y;
        }

        /*Velocity and exhaust time is randomized for each attack. -----------------*/
        bulletsVelocity = 25;

        /*Setting slopeOfPath of central bullet and the extreme bullet. ------------*/
        slopeOfPathCentre = ((double)(fingerY - monsterBall.monsterPosition.y))/((double)(fingerX - monsterBall.monsterPosition.x));
        slopeOfPathCorner = (slopeOfPathCentre + 1.73205) / (1.0 - 1.73205*slopeOfPathCentre);


        /* First Bullet: Making 60 degrees lagging angle with the central bullet. --*/
        bulletDestination[0].x = (int)(((double)(fingerY - monsterBall.monsterPosition.y) + ((double)fingerX / slopeOfPathCentre) + (double)monsterBall.monsterPosition.x * slopeOfPathCorner)/(slopeOfPathCorner + 1.00/slopeOfPathCentre));
        bulletDestination[0].y = (int)((double)fingerY + ((double)fingerX/slopeOfPathCentre) - ((double)bulletDestination[0].x/slopeOfPathCentre));

        /* Second Bullet: Between the central and first bullet. --------------------*/
        bulletDestination[1].x = (bulletDestination[0].x + fingerX)/2;
        bulletDestination[1].y = (bulletDestination[0].y + fingerY)/2;

        /* Third Bullet: Immediately near central bullet, with a lagging angle. ----*/
        bulletDestination[2].x = (bulletDestination[1].x + fingerX)/2;
        bulletDestination[2].y = (bulletDestination[1].y + fingerY)/2;

        /* Fourth Bullet: The Central Bullet. --------------------------------------*/
        bulletDestination[3].x = fingerX;
        bulletDestination[3].y = fingerY;

        /* Fifth Bullet: Immediately near central bullet, with a leading angle. ----*/
        bulletDestination[4].x = (bulletDestination[3].x + fingerX)/2;
        bulletDestination[4].y = (bulletDestination[3].y + fingerY)/2;

        /* Seventh Bullet: Making 60 degrees leading angle with the central bullet. */
        bulletDestination[6].x = 2*fingerX - bulletDestination[0].x;
        bulletDestination[6].y = 2*fingerY - bulletDestination[0].y;

        /* Sixth Bullet: Between the central and seventh bullet. -------------------*/
        bulletDestination[5].x = (bulletDestination[6].x + fingerX)/2;
        bulletDestination[5].y = (bulletDestination[6].y + fingerY)/2;


        /* Setting distance between the attack point of bullets and destination of bullets, to decide velocity components. */
        for(int i=0; i<7; i++)
        {
            bulletDistance[i] = Geometry.distance(bulletDestination[i], monsterBall.monsterPosition);

            /* X and Y components of velocity of each bullet, calculate like those of MonsterBall. */
            bulletVelocity[i].x = (int)(bulletsVelocity * (double)(bulletDestination[i].x - monsterBall.monsterPosition.x) / bulletDistance[i]);
            bulletVelocity[i].y = (int)(bulletsVelocity * (double)(bulletDestination[i].y - monsterBall.monsterPosition.y) / bulletDistance[i]);
        }
    }
    /**--------------------------------------------------------------------------------------------------**/



    /****************************** METHOD WHICH MAKES THE BULLETS MOVE  **********************************/
    public void setDirectionAndShoot() {

        for(int i=0; i<7; i++)
        {
            bulletPosition[i].x += bulletVelocity[i].x;
            bulletPosition[i].y += bulletVelocity[i].y;
        }
    }
    /**--------------------------------------------------------------------------------------------------**/

    public boolean didBulletGetTheFinger (Point fingerPosition)
    {
        int distance = 0;

        for (int i = 0; i < 7; i++)
        {
            distance = Geometry.distance(bulletPosition[i], fingerPosition);

            if (distance < bulletsRadius)
                return true;
        }

        return false;
    }
}
