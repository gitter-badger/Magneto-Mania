package com.example.root.magnetomania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;


public class SpriteAnimation {

    /******************************************** CLASS MEMBERS ********************************************/
    private GameView mGameView;

    private Bitmap mBallCoreBody;
    private Bitmap mBallLeftRing;
    private Bitmap mBallRiteRing;

    private final int M_BALL_BMP_ROWS = 3;
    private final int M_BALL_BMP_COLS = 5;

    private Point spriteUnitCoreBody = new Point(0,0);
    private Point spriteUnitLeftRing = new Point(0,0);
    private Point spriteUnitRiteRing = new Point(0,0);
    private static int spriteSheetIterator= 0;
    /**--------------------------------------------------------------------------------------------------**/


    public SpriteAnimation (GameView gameView) {
        this.mGameView = gameView;

        mBallCoreBody = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballcore);
        mBallLeftRing = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballringleft);
        mBallRiteRing = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.mballringright);

        this.spriteUnitCoreBody.x = mBallCoreBody.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitCoreBody.y = mBallCoreBody.getHeight() / M_BALL_BMP_ROWS;

        this.spriteUnitLeftRing.x = mBallLeftRing.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitLeftRing.y = mBallLeftRing.getHeight() / M_BALL_BMP_ROWS;

        this.spriteUnitRiteRing.x = mBallRiteRing.getWidth() / M_BALL_BMP_COLS;
        this.spriteUnitRiteRing.y = mBallRiteRing.getHeight() / M_BALL_BMP_ROWS;
    }


    public static void iteratorIncrement() {
        spriteSheetIterator = ++spriteSheetIterator;

        if(spriteSheetIterator > 15)
            spriteSheetIterator = 0;
    }


    public void drawMonsterBall (MonsterBall monsterBall, Canvas canvas) {

        int srcX = (spriteSheetIterator % M_BALL_BMP_COLS)* spriteUnitCoreBody.x;
        int srcY = (spriteSheetIterator / M_BALL_BMP_COLS)* spriteUnitCoreBody.y;

        Rect src = new Rect(srcX, srcY, srcX + spriteUnitCoreBody.x, srcY +spriteUnitCoreBody.y);
        Rect dst = new Rect(monsterBall.monsterPosition.x - monsterBall.monsterRadius,
                            monsterBall.monsterPosition.y - monsterBall.monsterRadius,
                            monsterBall.monsterPosition.x + monsterBall.monsterRadius,
                            monsterBall.monsterPosition.y + monsterBall.monsterRadius);

        canvas.drawBitmap(mBallCoreBody, src, dst, null);

        srcX = (spriteSheetIterator % M_BALL_BMP_COLS)* spriteUnitLeftRing.x;
        srcY = (spriteSheetIterator / M_BALL_BMP_COLS)* spriteUnitLeftRing.y;

        src = new Rect(srcX, srcY, srcX + spriteUnitLeftRing.x, srcY +spriteUnitLeftRing.y);
       
        canvas.drawBitmap(mBallLeftRing, src, dst, null);

        srcX = (spriteSheetIterator % M_BALL_BMP_COLS)* spriteUnitRiteRing.x;
        srcY = (spriteSheetIterator / M_BALL_BMP_COLS)* spriteUnitRiteRing.y;

        src = new Rect(srcX, srcY, srcX + spriteUnitRiteRing.x, srcY +spriteUnitRiteRing.y);

        canvas.drawBitmap(mBallRiteRing, src, dst, null);
    }
}
