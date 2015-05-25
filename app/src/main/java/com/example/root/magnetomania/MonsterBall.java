package com.example.root.magnetomania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;



public class MonsterBall {

    /******************************************** CLASS MEMBERS ********************************************/
    protected Point monsterPosition = new Point(0,0);
    protected int monsterVelocity;

    protected Paint monsterPaint = new Paint();
    protected final int monsterRadius = 100;

    protected int monsterSleepTime;
    protected int monsterAttackTrick;

    private GameView mGameView;

    private Bitmap monsterBallLeftBand;
    private Bitmap monsterBallRiteBand;

    private final int BMP_ROWS = 3;
    private final int BMP_COLS = 5;

    private Point spriteUnitLeftBand = new Point(0,0);
    private Point spriteUnitRiteBand = new Point(0,0);
    private int spriteSheetIterator = 0;
    /**--------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public MonsterBall(GameView gameView) {
        Random random = new Random();

        this.monsterPosition.y = random.nextInt(GameActivity.mScreenSize.y + 1);
        this.monsterPosition.x = random.nextInt(2);

        if(this.monsterPosition.x == 1)
        this.monsterPosition.x = GameActivity.mScreenSize.x;

        this.monsterVelocity = random.nextInt(10) + 15;
        this.monsterSleepTime = random.nextInt(10) + 10;

        this.monsterAttackTrick = 0;
        monsterPaint.setColor(Color.parseColor("#FFFFFF"));

        this.mGameView = gameView;
        this.monsterBallLeftBand = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.monsterballleftband);
        this.monsterBallRiteBand = BitmapFactory.decodeResource(mGameView.getResources(), R.mipmap.monsterballrightband);

        this.spriteUnitLeftBand.x = monsterBallLeftBand.getWidth() / BMP_COLS;
        this.spriteUnitLeftBand.y = monsterBallLeftBand.getHeight() / BMP_ROWS;

        this.spriteUnitRiteBand.x = monsterBallRiteBand.getWidth() / BMP_COLS;
        this.spriteUnitRiteBand.y = monsterBallRiteBand.getHeight() / BMP_ROWS;
    }
    /**--------------------------------------------------------------------------------------------------**/


    public void attackFingerPosition(Point destinationPoint, Point initialPoint) {
        Point mVelocityComponent = Geometry.calcVelocityComponents(destinationPoint, initialPoint, monsterVelocity);

            monsterPosition.x += mVelocityComponent.x;
            monsterPosition.y += mVelocityComponent.y;
            spriteSheetIterator = ++spriteSheetIterator;

        if(spriteSheetIterator > 15)
            spriteSheetIterator = 0;
    }


    public boolean didMonsterGetTheFinger (Point fingerPosition) {
        int distance = Geometry.distance(fingerPosition, monsterPosition);

        if (distance < this.monsterRadius)
            return true;
        else
            return false;
    }


    public void drawBandOnMonsterBall (Canvas canvas) {

        int srcX = (spriteSheetIterator % BMP_COLS)* spriteUnitLeftBand.x;
        int srcY = (spriteSheetIterator / BMP_COLS)* spriteUnitLeftBand.y;

        Rect src = new Rect(srcX, srcY, srcX + spriteUnitLeftBand.x, srcY +spriteUnitLeftBand.y);
        Rect dst = new Rect(monsterPosition.x - monsterRadius, monsterPosition.y - monsterRadius,
                            monsterPosition.x + monsterRadius, monsterPosition.y + monsterRadius);

        canvas.drawBitmap(monsterBallLeftBand, src, dst, null);

        srcX = (spriteSheetIterator % BMP_COLS)* spriteUnitRiteBand.x;
        srcY = (spriteSheetIterator / BMP_COLS)* spriteUnitRiteBand.y;

        src = new Rect(srcX, srcY, srcX + spriteUnitRiteBand.x, srcY +spriteUnitRiteBand.y);
        dst = new Rect(monsterPosition.x - monsterRadius, monsterPosition.y - monsterRadius,
                monsterPosition.x + monsterRadius, monsterPosition.y + monsterRadius);

        canvas.drawBitmap(monsterBallRiteBand, src, dst, null);
    }
}
