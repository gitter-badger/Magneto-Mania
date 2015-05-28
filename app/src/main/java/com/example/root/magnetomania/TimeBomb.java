package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class TimeBomb {
    /******************************************** CLASS MEMBERS ********************************************/
    protected Point bombPosition = new Point(0,0);

    protected Paint bombPaint = new Paint();

    protected final int bombInitialRadius     = 15;
    protected final int bombExplosionRadius   = 150;
    protected final int explosionIncreaseRate = 15;

    protected int bombCurrentRadius;
    protected int timeBombCounter;
    protected boolean is_bomb_planted;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public TimeBomb() {
        this.bombPosition.x = GameActivity.mScreenSize.x + 30;
        this.bombPosition.y = GameActivity.mScreenSize.y + 30;

        this.is_bomb_planted = false;
        this.bombCurrentRadius = 0;
        bombPaint.setColor(Color.parseColor("#1100DD"));
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initTimeBomb(MonsterBall monsterBall) {
        bombPosition      = Geometry.setCoordinates(monsterBall.monsterPosition);
        bombCurrentRadius = bombInitialRadius;
        timeBombCounter   = 15;
    }


    public void tickTockCountDown() {
        timeBombCounter--;
    }


    public void increaseBombExplosion () {
        bombCurrentRadius += explosionIncreaseRate;
    }


    public boolean didFingerBecameVictimOfBombBlast(Point fingerPosition) {
        int distance = Geometry.distance(fingerPosition, bombPosition);

        if (distance < bombCurrentRadius)
            return true;
        else
            return false;
    }
}
