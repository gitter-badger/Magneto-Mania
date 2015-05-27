package com.example.root.magnetomania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
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

        Rect src = new Rect(srcX, srcY, srcX + spriteUnitCoreBody.x, srcY +spriteUnitCoreBody.y);
        Rect dst = new Rect(monsterBall.monsterPosition.x - monsterBall.monsterRadius,
                            monsterBall.monsterPosition.y - monsterBall.monsterRadius,
                            monsterBall.monsterPosition.x + monsterBall.monsterRadius,
                            monsterBall.monsterPosition.y + monsterBall.monsterRadius);

        canvas.drawBitmap(mBallCoreBodyBmp, src, dst, null);

        srcX = (mBallIterator % M_BALL_BMP_COLS)* spriteUnitLeftRing.x;
        srcY = (mBallIterator / M_BALL_BMP_COLS)* spriteUnitLeftRing.y;

        src = new Rect(srcX, srcY, srcX + spriteUnitLeftRing.x, srcY +spriteUnitLeftRing.y);
       
        canvas.drawBitmap(mBallLeftRingBmp, src, dst, null);

        srcX = (mBallIterator % M_BALL_BMP_COLS)* spriteUnitRiteRing.x;
        srcY = (mBallIterator / M_BALL_BMP_COLS)* spriteUnitRiteRing.y;

        src = new Rect(srcX, srcY, srcX + spriteUnitRiteRing.x, srcY +spriteUnitRiteRing.y);

        canvas.drawBitmap(mBallRiteRingBmp, src, dst, null);
    }


    public void drawMagnetRocket (MagnetRocket magnetRocket, Canvas canvas, Point fingerPosition) {

        int mRocketIterator = (spriteSheetIterator / 4) % mRocketUnitsonSheet;

        int srcX = (mRocketIterator % ROCKET_BMP_COLS)* spriteUnitRocket.x;
        int srcY = (mRocketIterator / ROCKET_BMP_COLS)* spriteUnitRocket.y;

        Rect src = new Rect(srcX, srcY, srcX + spriteUnitRocket.x, srcY +spriteUnitRocket.y);
        Rect dst = new Rect(magnetRocket.rocketPosition.x - magnetRocket.rocketRadius,
                magnetRocket.rocketPosition.y - magnetRocket.rocketRadius - 10,
                magnetRocket.rocketPosition.x + magnetRocket.rocketRadius,
                magnetRocket.rocketPosition.y + magnetRocket.rocketRadius + 10);

        canvas.save();
        canvas.rotate((float)Math.atan((double)fingerPosition.y - magnetRocket.rocketPosition.y/(double)fingerPosition.x - magnetRocket.rocketPosition.x), (float)magnetRocket.rocketPosition.x - Geometry.center.x, (float)magnetRocket.rocketPosition.y - Geometry.center.y);

        canvas.drawBitmap(mRocketBmp, src, dst, null);
        canvas.restore();

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mRocketBmp, src, dst, null);
    }
}
