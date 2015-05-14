package com.example.root.magnetomania;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView {

    /******************************************** CLASS MEMBERS ********************************************/
    private SurfaceHolder mHolder;
    private GameThread mThread = null;

    private int mScreenWidth;
    private int mScreenHeight;

    public boolean is_game_started;
    public boolean is_game_paused;
    public boolean is_game_over;

    private MonsterBall mBall = new MonsterBall();
    private MagnetRocket mRocket = new MagnetRocket();
    private BulletFan mFan = new BulletFan();

    private int fingerX;
    private int fingerY;
    private int attackAtX;
    private int attackAtY;
    private int attackFromX;
    private int attackFromY;
    private int moveStyle;

    private int monsterSleepCount;
    private int rocketXhaustCount;
    private boolean time_to_shoot_bullets;
    private boolean bullets_on_screen;

    private Random random = new Random();

    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public GameView(Context context){
        super(context);

        this.mScreenWidth = GameActivity.mScreenSize.x;
        this.mScreenHeight= GameActivity.mScreenSize.y;

        this.mThread = new GameThread(this);
        this.mHolder = this.getHolder();
        this.is_game_paused = false;
        this.is_game_over = false;

        this.monsterSleepCount = 1;
        this.rocketXhaustCount = 1;

        this.mHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder mHolder)
            {
                mThread.setRunning(true);
                mThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder mHolder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder mHolder)
            {
                mThread.setRunning(false);
                boolean retry = true;

                while(retry)
                {
                    try {
                        mThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }
        });
    }
    /**---------------------------------------------------------------------------------------------------**/


    public void update()
    {

        if(is_game_started) {
            if(monsterSleepCount <= this.mBall.monsterSleepTime)
            {
                monsterSleepCount++;

                if(monsterSleepCount == this.mBall.monsterSleepTime)
                {
                    randomizeTrajectory();
                    monsterSleepCount++;
                    this.mBall.monsterSleepTime = 0;
                }
            }
            else if(this.mBall.monsterAttackTrick == 3)
            {
                monsterSleepCount = 1;

                if(rocketXhaustCount <= this.mRocket.rocketXhaustTime)
                {
                    this.mRocket.rocketTrackFinger(this.fingerX, this.fingerY);
                    rocketXhaustCount++;
                }
                else
                {
                    rocketXhaustCount = 1;
                    this.mRocket.rocketX = this.mScreenWidth + 80;
                    this.mRocket.rocketY = this.mScreenHeight + 80;
                    this.mRocket.rocketXhaustTime = 0;

                    this.mBall.monsterAttackTrick = 0;
                    this.mBall.monsterVelocity = random.nextInt(20) + 10;
                    this.mBall.monsterSleepTime = random.nextInt(10) + 5;
                }
            }
            else if(this.mBall.monsterAttackTrick == 2)
            {
                monsterSleepCount = 1;

                if(time_to_shoot_bullets)
                {
                    time_to_shoot_bullets = false;
                    if (mBall.monsterX < attackAtX && mBall.monsterY < attackAtY)
                        moveStyle = 1;
                    else if (mBall.monsterX < attackAtX && mBall.monsterY > attackAtY)
                        moveStyle = 2;
                    else if (mBall.monsterX > attackAtX && mBall.monsterY > attackAtY)
                        moveStyle = 3;
                    else if (mBall.monsterX > attackAtX && mBall.monsterY < attackAtY)
                        moveStyle = 4;

                    bullets_on_screen = true;
                    this.attackAtX = this.fingerX;
                    this.attackAtY = this.fingerY;
                    this.attackFromX = this.mBall.monsterX;
                    this.attackFromY = this.mBall.monsterY;
                }

                if(bullets_on_screen)
                {
                    mFan.setDirectionAndShoot(attackAtX, attackAtY, attackFromX, attackFromY, moveStyle);

                    if((mFan.bulletCenterX >= mScreenWidth || mFan.bulletCenterX <= 0) && (mFan.bulletCenterY >= mScreenHeight || mFan.bulletCenterY <= 0) &&
                       (mFan.bulletAboveX  >= mScreenWidth || mFan.bulletAboveX  <= 0) && (mFan.bulletAboveY  >= mScreenHeight || mFan.bulletAboveY  <= 0) &&
                       (mFan.bulletBelowX  >= mScreenWidth || mFan.bulletBelowX  <= 0) && (mFan.bulletBelowY  >= mScreenHeight || mFan.bulletBelowY  <= 0) &&
                       (mFan.bulletTopMostX>= mScreenWidth || mFan.bulletTopMostX<= 0) && (mFan.bulletTopMostY>= mScreenHeight || mFan.bulletTopMostY<= 0) &&
                       (mFan.bulletBottomX >= mScreenWidth || mFan.bulletBottomX <= 0) && (mFan.bulletBottomY >= mScreenHeight || mFan.bulletBottomY <= 0))
                        bullets_on_screen = false;
                }
                else
                {
                    this.mBall.monsterAttackTrick = 0;
                    this.mBall.monsterVelocity = random.nextInt(20) + 10;
                    this.mBall.monsterSleepTime = random.nextInt(10) + 5;
                }
            }
            else
            {
                monsterSleepCount = 1;

                if (mBall.monsterX >= mScreenWidth || mBall.monsterY >= mScreenHeight || mBall.monsterX <= 0 || mBall.monsterY <= 0) {
                    this.attackAtX = this.fingerX;
                    this.attackAtY = this.fingerY;
                    this.attackFromX = this.mBall.monsterX;
                    this.attackFromY = this.mBall.monsterY;

                    if (mBall.monsterX < attackAtX && mBall.monsterY < attackAtY)
                        moveStyle = 1;
                    else if (mBall.monsterX < attackAtX && mBall.monsterY > attackAtY)
                        moveStyle = 2;
                    else if (mBall.monsterX > attackAtX && mBall.monsterY > attackAtY)
                        moveStyle = 3;
                    else if (mBall.monsterX > attackAtX && mBall.monsterY < attackAtY)
                        moveStyle = 4;

                    this.mBall.monsterVelocity = random.nextInt(20) + 10;
                    this.mBall.monsterSleepTime = random.nextInt(10) + 5;


                }

                mBall.attackFingerPosition(attackAtX, attackAtY, attackFromX, attackFromY, moveStyle);
            }
        }

        if(is_game_over)
        {
            is_game_over = false;
        }

    }

    public void draw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);

        if(this.mFan != null && this.mBall.monsterAttackTrick == 2)
        {
            canvas.drawCircle((float)mFan.bulletCenterX , (float)mFan.bulletCenterY , (float)mFan.bulletsRadius , mFan.bulletsPaint);
            canvas.drawCircle((float)mFan.bulletAboveX  , (float)mFan.bulletAboveY  , (float)mFan.bulletsRadius , mFan.bulletsPaint);
            canvas.drawCircle((float)mFan.bulletBelowX  , (float)mFan.bulletBelowY  , (float)mFan.bulletsRadius , mFan.bulletsPaint);
            canvas.drawCircle((float)mFan.bulletTopMostX, (float)mFan.bulletTopMostY, (float)mFan.bulletsRadius , mFan.bulletsPaint);
            canvas.drawCircle((float)mFan.bulletBottomX , (float)mFan.bulletBottomY , (float)mFan.bulletsRadius , mFan.bulletsPaint);

        }

        if(this.mRocket != null && this.mBall.monsterAttackTrick == 3)
        canvas.drawCircle((float)mRocket.rocketX, (float)mRocket.rocketY, (float)mRocket.rocketRadius, mRocket.rocketPaint);

        canvas.drawCircle((float)mBall.monsterX, (float)mBall.monsterY, (float)mBall.monsterRadius, mBall.monsterPaint);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!is_game_paused)
        {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    is_game_started = true;
                    break;

                case MotionEvent.ACTION_UP:
                    //Do Something here.
                    is_game_over = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    this.fingerX = (int)event.getX();
                    this.fingerY = (int)event.getY();
                    break;
            }
        }
        return true;
    }

    public void randomizeTrajectory()
    {
        this.mBall.monsterAttackTrick = random.nextInt(5);
        if(this.mBall.monsterAttackTrick == 3)
        {
            this.mRocket.initRocket(mBall);
        }
        else if(this.mBall.monsterAttackTrick == 2)
        {
            this.mFan.initBullets(mBall);
            time_to_shoot_bullets = true;
        }
    }
}
