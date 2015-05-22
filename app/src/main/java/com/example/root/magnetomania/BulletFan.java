package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class BulletFan {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point     bulletPosition[]    = new Point[7];
    protected Point     bulletDestination[] = new Point[7];
    protected Point     bulletVelocity[]    = new Point[7];

    protected double    bulletDistance[]    = new double[7];
    protected double    slopeOfPathCentre;
    protected double    slopeOfPathCorner;

    protected double    bulletsVelocity;

    protected Paint     bulletsPaint        = new Paint();
    protected final int bulletsRadius = 20;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BulletFan() {
        for(int i=0; i<7; i++) {
            this.bulletPosition[i]    = new Point(0,0);
            this.bulletDestination[i] = new Point(0,0);
            this.bulletVelocity[i]    = new Point(0,0);
        }

        for(int i=0; i<7; i++) {
            this.bulletPosition[i].x  = GameActivity.mScreenSize.x + 80;
            this.bulletPosition[i].y  = GameActivity.mScreenSize.y + 80;
            this.bulletDistance[i]    = 0;
        }

        this.slopeOfPathCentre = 0;
        this.slopeOfPathCorner = 0;
        this.bulletsVelocity   = 0;
        bulletsPaint.setColor(Color.parseColor("#11FF22"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initBullets(MonsterBall monsterBall, Point fingerPosition) {

        for(int i=0; i<7; i++) {
            bulletPosition[i] = Geometry.setCoordinates(monsterBall.monsterPosition);
        }
        bulletsVelocity = 25;

        /*Setting slopeOfPath of central bullet and the extreme bullet. ------------*/
        slopeOfPathCentre = ((double)(fingerPosition.y - monsterBall.monsterPosition.y))/((double)(fingerPosition.y - monsterBall.monsterPosition.x));
        slopeOfPathCorner = (slopeOfPathCentre + 1.73205) / (1.0 - 1.73205*slopeOfPathCentre);


        /* First Bullet: Making 60 degrees lagging angle with the central bullet. --*/
        bulletDestination[0].x = (int)(((double)(fingerPosition.y - monsterBall.monsterPosition.y) + ((double)fingerPosition.x / slopeOfPathCentre) + (double)monsterBall.monsterPosition.x * slopeOfPathCorner)/(slopeOfPathCorner + 1.00/slopeOfPathCentre));
        bulletDestination[0].y = (int)((double)fingerPosition.y + ((double)fingerPosition.x/slopeOfPathCentre) - ((double)bulletDestination[0].x/slopeOfPathCentre));

        /* Second Bullet: Between the central and first bullet. --------------------*/
        bulletDestination[1].x = (bulletDestination[0].x + fingerPosition.x)/2;
        bulletDestination[1].y = (bulletDestination[0].y + fingerPosition.y)/2;

        /* Third Bullet: Immediately near central bullet, with a lagging angle. ----*/
        bulletDestination[2].x = (bulletDestination[1].x + fingerPosition.x)/2;
        bulletDestination[2].y = (bulletDestination[1].y + fingerPosition.y)/2;

        /* Fourth Bullet: The Central Bullet. --------------------------------------*/
        bulletDestination[3].x = fingerPosition.x;
        bulletDestination[3].y = fingerPosition.y;

        /* Fifth Bullet: Immediately near central bullet, with a leading angle. ----*/
        bulletDestination[4].x = (bulletDestination[3].x + fingerPosition.x)/2;
        bulletDestination[4].y = (bulletDestination[3].y + fingerPosition.y)/2;

        /* Seventh Bullet: Making 60 degrees leading angle with the central bullet. */
        bulletDestination[6].x = 2*fingerPosition.x - bulletDestination[0].x;
        bulletDestination[6].y = 2*fingerPosition.y - bulletDestination[0].y;

        /* Sixth Bullet: Between the central and seventh bullet. -------------------*/
        bulletDestination[5].x = (bulletDestination[6].x + fingerPosition.x)/2;
        bulletDestination[5].y = (bulletDestination[6].y + fingerPosition.y)/2;

        /* Setting distance between the attack point of bullets and destination of bullets, to decide velocity components. */
        for(int i=0; i<7; i++) {
            bulletVelocity[i] = Geometry.calcVelocityComponents(bulletDestination[i], monsterBall.monsterPosition, (int)bulletsVelocity);
        }
    }


    public void setDirectionAndShoot() {

        for(int i=0; i<7; i++) {
            bulletPosition[i].x += bulletVelocity[i].x;
            bulletPosition[i].y += bulletVelocity[i].y;
        }
    }


    public boolean didBulletGetTheFinger (Point fingerPosition) {
        int distance;

        for (int i=0; i<7; i++) {
            distance = Geometry.distance(bulletPosition[i], fingerPosition);

            if (distance < bulletsRadius)
            return true;
        }
        return false;
    }

}
