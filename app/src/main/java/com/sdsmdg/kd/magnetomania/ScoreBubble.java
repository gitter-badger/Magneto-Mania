package com.sdsmdg.kd.magnetomania;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;



public class ScoreBubble {
    /******************************************** CLASS MEMBERS ********************************************/
    protected final Point   center       = new Point(GameActivity.mScreenSize.x/2, GameActivity.mScreenSize.y/2);

    private   GameView      gameView;
    protected int           bubbleStartTheta;
    protected int           orbitRadius;
    protected Point         bubbleCenter = new Point(0,0);
    protected int           bubbleValue;
    protected int           bubbleRadius;

    protected int           bubbleTheta;
    protected int           bubbleOmega;
    protected Bitmap        bubbleBitmap;
    protected Paint         bubblePaint  = new Paint();
    protected Random        random       = new Random();
    protected final int[]   scoreValues  = {50, 100, 250, 500};
    protected boolean       is_bubble_taken;
    protected boolean       is_time_out;
    protected boolean       is_to_fade_out;
    protected boolean       clockwise;

    protected Rect          fromSheet = new Rect();
    protected Rect          toDisplay = new Rect();
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public ScoreBubble(GameView gameView) {
        this.gameView        = gameView;
        this.bubblePaint.setColor(Color.TRANSPARENT);
        this.is_bubble_taken = false;
        this.is_time_out     = false;
        this.is_to_fade_out  = false;
        this.clockwise       = random.nextBoolean();
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initBubble () {
        bubbleStartTheta                = random.nextInt(360) - 180;
        orbitRadius                     = random.nextInt(3*GameActivity.mScreenSize.x/8) + GameActivity.mScreenSize.x/8;
        bubbleCenter                    = Geometry.setPolarCoordinates(center, orbitRadius, bubbleStartTheta);
        bubbleValue                     = scoreValues[random.nextInt(4)];

        bubbleRadius                    = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (100 * Math.PI)));
        bubbleTheta                     = bubbleStartTheta;
        bubbleOmega                     = 2;

        switch(bubbleValue) {
            case 50:
                bubbleBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.mipmap.sabercenter);
                break;
            case 100:
                bubbleBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.mipmap.sabercenter);
                break;
            case 250:
                bubbleBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.mipmap.sabercenter);
                break;
            case 500:
                bubbleBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.mipmap.sabercenter);
                break;
        }

        bubblePaint.setAlpha(0);
        is_bubble_taken                 = false;
        is_time_out                     = false;
        clockwise                       = random.nextBoolean();
        fromSheet.set(0, 0, bubbleBitmap.getWidth(), bubbleBitmap.getHeight());
    }


    public void bubbleClockwiseFadeIn () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() + 3);
    }

    public void bubbleClockwiseDisplacement () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);

        if(bubbleTheta > bubbleStartTheta + 360) {
            is_to_fade_out = true;
        }
    }

    public void bubbleClockwiseFadeOut () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() - 3);

        if(bubblePaint.getAlpha() < 5) {
            is_time_out = true;
        }
    }

    public void bubbleAntiClockwiseFadeIn () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() + 3);
    }

    public void bubbleAntiClockwiseDisplacement () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);

        if(bubbleTheta < bubbleStartTheta + 180) {
            is_to_fade_out = true;
        }
    }

    public void bubbleAntiClockwiseFadeOut () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() - 3);

        if(bubblePaint.getAlpha() < 5) {
            is_time_out = true;
        }
    }


    public void drawBubble (Canvas canvas) {
        toDisplay.set(bubbleCenter.x - bubbleRadius, bubbleCenter.y - bubbleRadius,
                      bubbleCenter.x + bubbleRadius, bubbleCenter.y + bubbleRadius);
        canvas.drawBitmap(bubbleBitmap, fromSheet, toDisplay, bubblePaint);
    }
}
