package com.sdsmdg.kd.magnetomania;


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
    protected final int bulletsRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (300 * Math.PI)));
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


    public void initBullets(MonsterBall monsterBall) {

        for(int i=0; i<7; i++) {
            bulletPosition[i] = Geometry.setCoordinates(monsterBall.monsterPosition);
        }
        bulletsVelocity = 20;

        /*Setting slopeOfPath of central bullet and the extreme bullet. ------------*/
        slopeOfPathCentre = ((double)(GameView.destinationPoint.y - monsterBall.monsterPosition.y))/((double)(GameView.destinationPoint.y - monsterBall.monsterPosition.x));
        slopeOfPathCorner = (slopeOfPathCentre + 1.73205) / (1.0 - 1.73205*slopeOfPathCentre);


        /* First(index 0) Bullet: Making 60 degrees lagging angle with the central bullet. --*/
        bulletDestination[0].x = (int)(((double)(GameView.destinationPoint.y - monsterBall.monsterPosition.y) + ((double)GameView.destinationPoint.x / slopeOfPathCentre) + (double)monsterBall.monsterPosition.x * slopeOfPathCorner)/(slopeOfPathCorner + 1.00/slopeOfPathCentre));
        bulletDestination[0].y = (int)((double)GameView.destinationPoint.y + ((double)GameView.destinationPoint.x/slopeOfPathCentre) - ((double)bulletDestination[0].x/slopeOfPathCentre));

        /* Fourth(index 3) Bullet: The Central Bullet. --------------------------------------*/
        bulletDestination[3].x = GameView.destinationPoint.x;
        bulletDestination[3].y = GameView.destinationPoint.y;

        /* Seventh(index 6) Bullet: Making 60 degrees leading angle with the central bullet. */
        bulletDestination[6].x = 2*GameView.destinationPoint.x - bulletDestination[0].x;
        bulletDestination[6].y = 2*GameView.destinationPoint.y - bulletDestination[0].y;

        /* Second(index 1) Bullet: Between the central and first bullet. --------------------*/
        bulletDestination[1].x = (bulletDestination[0].x + bulletDestination[3].x)/3;
        bulletDestination[1].y = (bulletDestination[0].y + bulletDestination[3].y)/3;

        /* Sixth(index 5) Bullet: Between the central and seventh bullet. -------------------*/
        bulletDestination[5].x = (bulletDestination[6].x + bulletDestination[3].x)/3;
        bulletDestination[5].y = (bulletDestination[6].y + bulletDestination[3].y)/3;

        int distance01 = Geometry.distance(bulletDestination[0], bulletDestination[1]);
        int distance31 = Geometry.distance(bulletDestination[3], bulletDestination[1]);

        /* Third(index 2) Bullet: Immediately near central bullet, with a lagging angle. ----*/
        if(distance01 > distance31) {
            bulletDestination[2].x = (bulletDestination[0].x + bulletDestination[1].x)/2;
            bulletDestination[2].y = (bulletDestination[0].y + bulletDestination[1].y)/2;
        }
        else {
            bulletDestination[2].x = (bulletDestination[3].x + bulletDestination[1].x)/2;
            bulletDestination[2].y = (bulletDestination[3].y + bulletDestination[1].y)/2;
        }

        int distance65 = Geometry.distance(bulletDestination[6], bulletDestination[5]);
        int distance35 = Geometry.distance(bulletDestination[3], bulletDestination[5]);

        /* Fifth(index 4) Bullet: Immediately near central bullet, with a leading angle. ----*/
        if(distance65 > distance35) {
            bulletDestination[4].x = (bulletDestination[6].x + bulletDestination[5].x)/2;
            bulletDestination[4].y = (bulletDestination[6].y + bulletDestination[5].y)/2;
        }
        else {
            bulletDestination[4].x = (bulletDestination[3].x + bulletDestination[5].x)/2;
            bulletDestination[4].y = (bulletDestination[3].y + bulletDestination[5].y)/2;
        }


        /* Setting distance between the attack point of bullets and destination of bullets, to decide velocity components. */
        for(int i = 0; i < 7; i++) {
            bulletVelocity[i] = Geometry.calcVelocityComponents(bulletDestination[i], monsterBall.monsterPosition, (int)bulletsVelocity);
        }
    }


    public void setDirectionAndShoot() {

        for(int i=0; i<7; i++) {
            bulletPosition[i].x += bulletVelocity[i].x;
            bulletPosition[i].y += bulletVelocity[i].y;
        }
    }


    public boolean didBulletGetTheFinger() {
        int distance;

        for (int i=0; i<7; i++) {
            distance = Geometry.distance(bulletPosition[i], GameView.fingerPosition);

            if (distance < bulletsRadius)
            return true;
        }
        return false;
    }

}
