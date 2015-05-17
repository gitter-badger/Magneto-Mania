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
    protected final int bulletsRadius = 20;
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
            this.bulletPosition[i].x = monsterBall.monsterX;
            this.bulletPosition[i].y = monsterBall.monsterY;
        }

        /*Velocity and exhaust time is randomized for each attack.*/
        this.bulletsVelocity = random.nextInt(15) + 25;

        /*Setting slopeOfPath of central bullet and the extreme bullet. */
        slopeOfPathCentre = ((double)(fingerY - monsterBall.monsterY))/((double)(fingerX - monsterBall.monsterX));
        slopeOfPathCorner = (slopeOfPathCentre + 1.73205) / (1.0 - 1.73205*slopeOfPathCentre);

        /*The bullet exactly in middle will shoot towards the finger.*/
        bulletDestination[2].x = fingerX;
        bulletDestination[2].y = fingerY;

        /* The first bullet, making angle 60 degrees with central bullet. */
        bulletDestination[0].x = (int)(((double)(fingerY - monsterBall.monsterY) + ((double)fingerX / slopeOfPathCentre) + (double)monsterBall.monsterX * slopeOfPathCorner)/(slopeOfPathCorner + 1.00/slopeOfPathCentre));
        bulletDestination[0].y = (int)((double)fingerY + ((double)fingerX/slopeOfPathCentre) - ((double)bulletDestination[0].x/slopeOfPathCentre));

        /* The last Bullet on opposite of first bullet. */
        bulletDestination[4].x = 2*fingerX - bulletDestination[0].x;
        bulletDestination[4].y = 2*fingerY - bulletDestination[0].y;

        /* The second bullet between first and central. */
        bulletDestination[1].x = (bulletDestination[0].x + fingerX)/2;
        bulletDestination[1].y = (bulletDestination[0].y + fingerY)/2;

        /* The fourth bullet between central and last. */
        bulletDestination[3].x = (bulletDestination[4].x + fingerX)/2;
        bulletDestination[3].y = (bulletDestination[4].y + fingerY)/2;

        /* Two more bullets on immediate left and right of central bullet. */
        bulletDestination[5].x = (bulletDestination[1].x + fingerX)/2;
        bulletDestination[5].y = (bulletDestination[1].y + fingerY)/2;

        bulletDestination[6].x = (bulletDestination[3].x + fingerX)/2;
        bulletDestination[6].y = (bulletDestination[3].y + fingerY)/2;

        /* Fixing distance between the attack point of bullets and destination of bullets, to decide velocity components. */
        for(int i=0; i<7; i++)
        {
            this.bulletDistance[i] = Math.sqrt((bulletDestination[i].x - monsterBall.monsterX) * (bulletDestination[i].x - monsterBall.monsterX)
                                             + (bulletDestination[i].y - monsterBall.monsterY) * (bulletDestination[i].y - monsterBall.monsterY));

            /* X and Y components of velocity of each bullet, calculate like those of MonsterBall. */
            this.bulletVelocity[i].x = (int)(bulletsVelocity * (double)(bulletDestination[i].x - monsterBall.monsterX) / bulletDistance[i]);
            this.bulletVelocity[i].y = (int)(bulletsVelocity * (double)(bulletDestination[i].y - monsterBall.monsterY) / bulletDistance[i]);
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
}
