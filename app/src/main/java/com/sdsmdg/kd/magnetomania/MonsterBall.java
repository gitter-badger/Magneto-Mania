package com.sdsmdg.kd.magnetomania;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;



public class MonsterBall {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point monsterPosition = new Point(0,0);
    protected double monsterVelocity;

    protected Paint monsterPaint = new Paint();
    protected final int monsterRadius = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (12 * Math.PI)));

    protected int monsterSleepTime;
    protected int monsterAttackTrick;
    protected int monsterTrickSetDecider;

    protected Random random = new Random();
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public MonsterBall() {

        this.monsterPosition.y = random.nextInt(GameActivity.mScreenSize.y + 1);
        this.monsterPosition.x = random.nextInt(2);

        if(this.monsterPosition.x == 1)
        this.monsterPosition.x = GameActivity.mScreenSize.x;

        this.monsterVelocity = random.nextInt(20) + 15 + (int)(GameView.Score / 500);
        this.monsterSleepTime = random.nextInt(15) + 15;

        this.monsterAttackTrick = 0;
        this.monsterTrickSetDecider = 0;
        monsterPaint.setColor(Color.parseColor("#FFFFFF"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void attackFingerPosition() {
        Point mVelocityComponent = Geometry.calcVelocityComponents(GameView.destinationPoint, GameView.initialPoint, (int)monsterVelocity);

            monsterVelocity   -= 0.05;
            monsterPosition.x += mVelocityComponent.x;
            monsterPosition.y += mVelocityComponent.y;

        SpriteAnimation.iteratorIncrement();
    }


    public void prepareForSleepAndAttack() {
        if (monsterPosition.x >= GameActivity.mScreenSize.x-5 || monsterPosition.x <= 5 ||
            monsterPosition.y >= GameActivity.mScreenSize.y-5 || monsterPosition.y <= 5) {

            // For preventing glitchy movement at the boundary.
            if (monsterPosition.x > GameActivity.mScreenSize.x) {
                monsterPosition.x = GameActivity.mScreenSize.x;
            }
            else if (monsterPosition.x < 0) {
                monsterPosition.x = 0;
            }
            else if (monsterPosition.y > GameActivity.mScreenSize.y) {
                monsterPosition.y = GameActivity.mScreenSize.y;
            }
            else if (monsterPosition.y < 0) {
                monsterPosition.y = 0;
            }

            GameView.destinationPoint = Geometry.setCoordinates(GameView.fingerPosition);
            GameView.initialPoint     = Geometry.setCoordinates(monsterPosition);

            monsterAttackTrick = 0;
            monsterVelocity  = random.nextInt(20) + 10 + (int)(GameView.Score / 100);
            monsterSleepTime = random.nextInt(15) + 15;
        }
    }


    public boolean didMonsterGetTheFinger() {
        int distance = Geometry.distance(GameView.fingerPosition, monsterPosition);

        return distance < this.monsterRadius;
    }

}
