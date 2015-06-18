package com.sdsmdg.kd.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class BulletFan {

    /******************************************** CLASS MEMBERS ********************************************/
    protected final int SIZE                     = 7;
    protected Point     bulletPathCenter         = new Point(0,0);
    protected Point     bulletPosition[]         = new Point[SIZE];
    protected Point     bulletDestination        = new Point(0,0);
    protected int       destinationPositionAngle;
    protected int[]     bulletAngleOfPropagation = new int[SIZE];
    protected int       bulletPathRadius;

    protected int       bulletsVelocity;

    protected Paint     bulletsPaint             = new Paint();
    protected final int bulletsRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (300 * Math.PI)));
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public BulletFan() {
        for(int i = 0; i < SIZE; i++) {
            this.bulletPosition[i]              = new Point(GameActivity.mScreenSize.x + 80, GameActivity.mScreenSize.y + 80);
            this.bulletAngleOfPropagation[i]    = 0;
        }

        this.destinationPositionAngle           = 0;
        this.bulletPathRadius                   = 0;
        this.bulletsVelocity                    = 0;
        bulletsPaint.setColor(Color.parseColor("#11FF22"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initBullets(MonsterBall monsterBall) {
        bulletPathCenter         = Geometry.setCoordinates(monsterBall.monsterPosition);
        for(int i = 0; i < SIZE; i++) {
            bulletPosition[i]    = Geometry.setCoordinates(bulletPathCenter);
        }
        bulletDestination        = Geometry.setCoordinates(GameView.destinationPoint);
        bulletsVelocity = 20;

        destinationPositionAngle = (int)(Math.atan2(bulletDestination.y - bulletPosition[3].y,
                                                    bulletDestination.x - bulletPosition[3].x)*180/Math.PI);

        for(int i = 0; i < SIZE; i++) {
            bulletAngleOfPropagation[i] = destinationPositionAngle + 15*(i- SIZE/2);
        }
        bulletPathRadius         = 0;
    }


    public void setDirectionAndShoot() {
        bulletPathRadius += bulletsVelocity;
        for(int i = 0; i < SIZE; i++) {
            bulletPosition[i]    = Geometry.setPolarCoordinates(bulletPathCenter, bulletPathRadius, bulletAngleOfPropagation[i]);
        }
    }


    public boolean didBulletGetTheFinger() {
        int distance;

        for (int i = 0; i < SIZE; i++) {
            distance = Geometry.distance(bulletPosition[i], GameView.fingerPosition);

            if (distance < bulletsRadius)
                return true;
        }
        return false;
    }

}
