package com.sdsmdg.kd.magnetomania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;


public class SpriteAnimation {

    /******************************************** CLASS MEMBERS ********************************************/
    private GameView mGameView;

    private Bitmap mBallCoreBodyBmp;
    private Bitmap mBallLeftRingBmp;
    private Bitmap mBallRiteRingBmp;

    private Bitmap mRocketBmp;

    private final int M_BALL_BMP_ROWS = 3;
    private final int M_BALL_BMP_COLS = 5;
    private final int ROCKET_BMP_ROWS = 2;
    private final int ROCKET_BMP_COLS = 4;

    private Point spriteUnitCoreBody = new Point(0,0);
    private Point spriteUnitLeftRing = new Point(0,0);
    private Point spriteUnitRiteRing = new Point(0,0);
    private Point spriteUnitRocket   = new Point(0,0);

    private final int mBallUnitsOnSheet   = 15;
    private final int mRocketUnitsonSheet = 8;

    private static int spriteSheetIterator= 0;

    private Rect fromSheet = new Rect();
    private Rect toDisplay = new Rect();
    /**--------------------------------------------------------------------------------------------------**/



    public SpriteAnimation (GameView gameView) {
        this.mGameView = gameView;

        this.mBallCoreBodyBmp = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballcore);
        this.mBallLeftRingBmp = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballringleft);
        this.mBallRiteRingBmp = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballringright);
        this.mRocketBmp       = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.rocket);

        this.spriteUnitCoreBody.x = mBallCoreBodyBmp.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitCoreBody.y = mBallCoreBodyBmp.getHeight() / M_BALL_BMP_ROWS;

        this.spriteUnitLeftRing.x = mBallLeftRingBmp.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitLeftRing.y = mBallLeftRingBmp.getHeight() / M_BALL_BMP_ROWS;

        this.spriteUnitRiteRing.x = mBallRiteRingBmp.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitRiteRing.y = mBallRiteRingBmp.getHeight() / M_BALL_BMP_ROWS;

        this.spriteUnitRocket.x   = mRocketBmp.getWidth() / ROCKET_BMP_COLS;
        this.spriteUnitRocket.y   = mRocketBmp.getHeight() / ROCKET_BMP_ROWS;
    }


    public static void iteratorIncrement() {
        spriteSheetIterator = ++spriteSheetIterator;

        if(spriteSheetIterator > 120)
            spriteSheetIterator = 0;
    }


    public void drawMonsterBall (MonsterBall monsterBall, Canvas canvas) {

        int mBallIterator = spriteSheetIterator % mBallUnitsOnSheet;

        int srcX = (mBallIterator % M_BALL_BMP_COLS)* spriteUnitCoreBody.x;
        int srcY = (mBallIterator  / M_BALL_BMP_COLS)* spriteUnitCoreBody.y;

        fromSheet.set(srcX, srcY, srcX + spriteUnitCoreBody.x, srcY + spriteUnitCoreBody.y);
        toDisplay.set(monsterBall.monsterPosition.x - monsterBall.monsterRadius,
                monsterBall.monsterPosition.y - monsterBall.monsterRadius,
                monsterBall.monsterPosition.x + monsterBall.monsterRadius,
                monsterBall.monsterPosition.y + monsterBall.monsterRadius);

        canvas.drawBitmap(mBallCoreBodyBmp, fromSheet, toDisplay, null);

        srcX = (mBallIterator % M_BALL_BMP_COLS)* spriteUnitLeftRing.x;
        srcY = (mBallIterator / M_BALL_BMP_COLS)* spriteUnitLeftRing.y;

        fromSheet.set(srcX, srcY, srcX + spriteUnitLeftRing.x, srcY + spriteUnitLeftRing.y);
       
        canvas.drawBitmap(mBallLeftRingBmp, fromSheet, toDisplay, null);

        srcX = (mBallIterator % M_BALL_BMP_COLS)* spriteUnitRiteRing.x;
        srcY = (mBallIterator / M_BALL_BMP_COLS)* spriteUnitRiteRing.y;

        fromSheet.set(srcX, srcY, srcX + spriteUnitRiteRing.x, srcY + spriteUnitRiteRing.y);

        canvas.drawBitmap(mBallRiteRingBmp, fromSheet, toDisplay, null);
    }


    public void drawMagnetRocket (MagnetRocket magnetRocket, Canvas canvas) {

        int mRocketIterator = (spriteSheetIterator / 4) % mRocketUnitsonSheet;

        int srcX = (mRocketIterator % ROCKET_BMP_COLS)* spriteUnitRocket.x;
        int srcY = (mRocketIterator / ROCKET_BMP_COLS)* spriteUnitRocket.y;

        fromSheet.set(srcX, srcY, srcX + spriteUnitRocket.x, srcY + spriteUnitRocket.y);
        toDisplay.set(magnetRocket.rocketPosition.x - magnetRocket.rocketRadius,
                magnetRocket.rocketPosition.y - magnetRocket.rocketRadius - 10,
                magnetRocket.rocketPosition.x + magnetRocket.rocketRadius,
                magnetRocket.rocketPosition.y + magnetRocket.rocketRadius + 10);

        float angleFromSpriteToFinger = (float)Math.atan2((double)GameView.fingerPosition.y - magnetRocket.rocketPosition.y, (double)GameView.fingerPosition.x - magnetRocket.rocketPosition.x);

        canvas.save();
        canvas.rotate(angleFromSpriteToFinger*180/(float)Math.PI + 90, (float) magnetRocket.rocketPosition.x, (float) magnetRocket.rocketPosition.y);

        canvas.drawBitmap(mRocketBmp, fromSheet, toDisplay, null);
        canvas.restore();
    }
}
