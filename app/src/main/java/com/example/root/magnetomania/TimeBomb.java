package com.example.root.magnetomania;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



public class TimeBomb {
    /******************************************** CLASS MEMBERS ********************************************/
    protected Point bombPosition = new Point(0,0);

    protected Paint bombPaint = new Paint();

    protected final int bombInitialRadius     = 15;
    protected final int bombExplosionRadius   = 85;
    protected final int explosionIncreaseRate = 10;

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
        timeBombCounter   = 20;
        is_bomb_planted   = true;
    }


    public void tickTockCountDown() {
        timeBombCounter--;
    }


    public void increaseBombExplosion () {
        if(bombCurrentRadius < bombExplosionRadius) {
            bombCurrentRadius += explosionIncreaseRate;
        }
    }


    public boolean didFingerBecameVictimOfBombBlast() {
        int distance = Geometry.distance(GameView.fingerPosition, bombPosition);
        return distance < bombCurrentRadius;
    }
}
