package com.sdsmdg.kd.magnetomania;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;



public class ScoreBubble {
    /******************************************** CLASS MEMBERS ********************************************/
    protected final Point   center       = new Point(GameActivity.mScreenSize.x/2, GameActivity.mScreenSize.y/2);

    protected int           bubbleStartTheta;
    protected int           orbitRadius;
    protected Point         bubbleCenter = new Point(0,0);
    protected int           bubbleValue;
    protected int           bubbleRadius;

    protected int           bubbleTheta;
    protected int           bubbleOmega;
    protected Paint         bubblePaint  = new Paint();
    protected Paint         valuePaint   = new Paint();
    protected Random        random       = new Random();
    protected final int[]   scoreValues  = {50, 100, 250, 500};
    protected boolean       is_bubble_taken;
    protected boolean       clockwise;
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public ScoreBubble() {
        this.bubblePaint.setColor(Color.TRANSPARENT);
        this.valuePaint.setColor(Color.TRANSPARENT);
        this.is_bubble_taken = false;
        this.clockwise       = random.nextBoolean();
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void initBubble () {
        bubbleStartTheta                = random.nextInt(360) - 180;
        orbitRadius                     = random.nextInt(3*GameActivity.mScreenSize.x/8) + GameActivity.mScreenSize.x/8;
        bubbleCenter                    = Geometry.setPolarCoordinates(center, orbitRadius, bubbleStartTheta);
        bubbleValue                     = scoreValues[random.nextInt(4)];

        bubbleRadius                    = (int)(Math.sqrt(Geometry.area(GameActivity.mScreenSize) / (150 * Math.PI))*bubbleValue/100);
        bubbleTheta                     = bubbleStartTheta;
        bubbleOmega                     = 1;

        switch(bubbleValue) {
            case 50:
                bubblePaint.setColor(Color.parseColor("#FFEB3B"));
                break;
            case 100:
                bubblePaint.setColor(Color.parseColor("#FF9800"));
                break;
            case 250:
                bubblePaint.setColor(Color.parseColor("#E65100"));
                break;
            case 500:
                bubblePaint.setColor(Color.parseColor("#DD2C00"));
                break;
        }

        bubblePaint.setAlpha(0);
        valuePaint.setColor(Color.WHITE);
        valuePaint.setTextSize(10);
        is_bubble_taken                 = false;
        clockwise                       = random.nextBoolean();
    }


    public void bubbleClockwiseFadeIn () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() + 3);
    }

    public void bubbleClockwiseDisplacement () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
    }

    public void bubbleClockwiseFadeOut () {
        bubbleTheta          += bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() - 3);
    }

    public void bubbleAntiClockwiseFadeIn () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() + 3);
    }

    public void bubbleAntiClockwiseDisplacement () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
    }

    public void bubbleAntiClockwiseFadeOut () {
        bubbleTheta          -= bubbleOmega;
        bubbleCenter          = Geometry.setPolarCoordinates(center, orbitRadius, bubbleTheta);
        bubblePaint.setAlpha(bubblePaint.getAlpha() - 3);
    }


    public void drawBubble (Canvas canvas) {
        canvas.drawCircle(bubbleCenter.x, bubbleCenter.y, bubbleRadius, bubblePaint);
        canvas.drawText(Integer.toString(bubbleValue), bubbleCenter.x - bubbleRadius/2, bubbleCenter.y - bubbleRadius/2, valuePaint);
    }
}
