package com.example.root.magnetomania;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView {

    /******************************************** CLASS MEMBERS ********************************************/
    private SurfaceHolder   mHolder;
    private GameThread      mThread          = null;
    private Context         mContext;

    private Point           mScreenDimension = new Point(0,0);

    public boolean          is_game_started;
    public boolean          is_game_over;

    private MonsterBall     mBall            = new MonsterBall();
    private MagnetRocket    mRocket          = new MagnetRocket();
    private BulletFan[]     mFan             = new BulletFan[3];
    private HeatWave[]      mWave            = new HeatWave[5];
    private RectF[]         heatRect         = new RectF[5];

    private Point           fingerPosition   = new Point(0,0);
    private Point           destinationPoint = new Point(0,0);
    private Point           initialPoint     = new Point(0,0);

    private int             monsterSleepCount;
    private int             rocketXhaustCount;
    private int             bulletFansTimeGap;
    private int             heatWaveTimeGap;

    private boolean         monster_trick_time;
    private boolean         time_to_shoot_bullets;
    private boolean         bullets_on_screen;
    private boolean         time_for_some_heat;
    private boolean         heat_waves_on_screen;

    private Random          random          = new Random();

    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public GameView(Context context) {
        super(context);

        this.mHolder                = this.getHolder();
        this.mThread                = new GameThread(this);
        this.mContext               = getContext();

        this.mScreenDimension.x     = GameActivity.mScreenSize.x;
        this.mScreenDimension.y     = GameActivity.mScreenSize.y;

        this.is_game_started        = false;
        this.is_game_over           = false;

        for(int i=0; i<3; i++) {
            this.mFan[i]     = new BulletFan();
        }

        for(int i=0; i<5; i++) {
            this.heatRect[i] = new RectF();
            this.mWave[i]    = new HeatWave();
        }

        this.monsterSleepCount      = 1;
        this.rocketXhaustCount      = 1;
        this.bulletFansTimeGap      = 1;
        this.heatWaveTimeGap        = 1;

        this.monster_trick_time     = false;
        this.time_to_shoot_bullets  = false;
        this.bullets_on_screen      = false;
        this.time_for_some_heat     = false;
        this.heat_waves_on_screen   = false;

        this.mHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder mHolder) {
                mThread.setRunning(true);
                mThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder mHolder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder mHolder) {
                mThread.setRunning(false);
                boolean retry = true;

                while(retry) {
                    try {
                        mThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }
        });
    }
    /**---------------------------------------------------------------------------------------------------**/


    public void update() {

        if(is_game_started) {
            /*** Condition of game over when finger touches the monster ball. ***/
            /***/   is_game_over = mBall.didMonsterGetTheFinger(fingerPosition);
            /***/   if(is_game_over)
            /***/   tryGameOver();

            if(monsterSleepCount <= mBall.monsterSleepTime) {
                monsterSleepCount++;

                if(monsterSleepCount == mBall.monsterSleepTime) {
                    if(monster_trick_time) {
                        randomizeTrajectory();
                        monster_trick_time = false;
                    }
                    else {
                        mBall.monsterAttackTrick = 0;
                        monster_trick_time = true;
                    }
                    monsterSleepCount++;
                    mBall.monsterSleepTime = 0;
                }
            }
            else if(mBall.monsterAttackTrick == 3) {
                monsterSleepCount = 1;

                /*** Condition p game over when finger touches the rocket. ***/
                /***/   is_game_over = mRocket.didRocketGetTheFinger(fingerPosition);
                /***/   if(is_game_over)
                /***/   tryGameOver();

                if(rocketXhaustCount <= mRocket.rocketXhaustTime) {
                    mRocket.rocketTrackFinger(fingerPosition);
                    rocketXhaustCount++;
                }
                else {
                    rocketXhaustCount = 1;
                    mRocket.rocketPosition.x = mScreenDimension.x + 80;
                    mRocket.rocketPosition.y = mScreenDimension.y + 80;
                    mRocket.rocketXhaustTime = 0;

                    mBall.monsterAttackTrick = 0;
                    mBall.monsterVelocity = random.nextInt(20) + 15;
                    mBall.monsterSleepTime = random.nextInt(10) + 5;
                    destinationPoint.x = fingerPosition.x;
                    destinationPoint.y = fingerPosition.y;
                }
            }
            else if(mBall.monsterAttackTrick == 2) {

                monsterSleepCount = 1;

                for(int i=0; i<3; i++) {
                    /*** Conditon of game over when finger touches the bullet. ***/
                    /***/is_game_over = mFan[i].didBulletGetTheFinger(fingerPosition);
                    /***/if (is_game_over)
                    /***/ tryGameOver();
                }

                if(time_to_shoot_bullets) {
                    time_to_shoot_bullets = false;
                    bullets_on_screen = true;
                    bulletFansTimeGap = 1;

                    destinationPoint.x = fingerPosition.x;
                    destinationPoint.y = fingerPosition.y;
                    initialPoint.x = mBall.monsterPosition.x;
                    initialPoint.y = mBall.monsterPosition.y;

                    mFan[0].initBullets(mBall, destinationPoint.x, destinationPoint.y);
                    mFan[1].initBullets(mBall, destinationPoint.x, destinationPoint.y);
                    mFan[2].initBullets(mBall, destinationPoint.x, destinationPoint.y);
                }

                if(bullets_on_screen) {

                    bulletFansTimeGap++;
                    mFan[0].setDirectionAndShoot();

                    if(bulletFansTimeGap>5)
                    mFan[1].setDirectionAndShoot();

                    if(bulletFansTimeGap>10)
                    mFan[2].setDirectionAndShoot();

                    int howManyBulletsOnScreen = 0;

                    for(int i=0; i<3; i++) {
                        for(int j=0; j<7; j++) {
                            if ((mFan[i].bulletPosition[j].x >= mScreenDimension.x-10 || mFan[i].bulletPosition[j].x <= 10) &&
                                (mFan[i].bulletPosition[j].y >= mScreenDimension.y-10 || mFan[i].bulletPosition[j].y <= 10))
                                howManyBulletsOnScreen++;
                        }
                    }

                    if(howManyBulletsOnScreen >= 16)
                        bullets_on_screen = false;
                }
                else {
                    mBall.monsterAttackTrick = 0;
                    mBall.monsterVelocity = random.nextInt(20) + 15;
                    mBall.monsterSleepTime = random.nextInt(1) + 5;
                    destinationPoint.x = fingerPosition.x;
                    destinationPoint.y = fingerPosition.y;
                }
            }
            else if(mBall.monsterAttackTrick == 4) {

                monsterSleepCount = 1;

                if(time_for_some_heat) {
                    time_for_some_heat = false;
                    heat_waves_on_screen = true;
                    heatWaveTimeGap = 1;

                    for(int i=0; i<5; i++) {
                        mWave[i].initHeatWave(mBall);
                    }
                }

                if(heat_waves_on_screen) {
                    heatWaveTimeGap++;
                    heatRect[0] = mWave[0].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 12)
                    heatRect[1] = mWave[1].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 24)
                    heatRect[2] = mWave[2].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 36)
                    heatRect[3] = mWave[3].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 48)
                    heatRect[4] = mWave[4].setHeatWaveSize(mBall.monsterPosition);

                    for(int i=0; i<5; i++) {
                        /*** Condition of game over when finger touches the heat wave. ***/
                        /***/is_game_over = mWave[i].didHeatWaveBurnTheFinger(fingerPosition, (i+1)%2);
                        /***/if (is_game_over)
                        /***/ tryGameOver();
                    }

                    if(mWave[4].heatWaveRadius > 3*mScreenDimension.y/2)
                        heat_waves_on_screen = false;
                }
                else {
                    mBall.monsterAttackTrick = 0;
                    mBall.monsterVelocity = random.nextInt(20) + 15;
                    mBall.monsterSleepTime = random.nextInt(10) + 5;
                    destinationPoint.x = fingerPosition.x;
                    destinationPoint.y = fingerPosition.y;
                }
            }
            else {
                monsterSleepCount = 1;

                if (mBall.monsterPosition.x >= mScreenDimension.x || mBall.monsterPosition.x <= 0 ||
                    mBall.monsterPosition.y >= mScreenDimension.y || mBall.monsterPosition.y <= 0) {

                    // For preventing glitchy movement at the boundary.
                    if (mBall.monsterPosition.x > mScreenDimension.x) {
                        mBall.monsterPosition.x = mScreenDimension.x;
                    }
                    else if (mBall.monsterPosition.x < 0) {
                        mBall.monsterPosition.x = 0;
                    }
                    else if (mBall.monsterPosition.y > mScreenDimension.y) {
                        mBall.monsterPosition.y = mScreenDimension.y;
                    }
                    else if (mBall.monsterPosition.y < 0) {
                        mBall.monsterPosition.y = 0;
                    }

                    destinationPoint.x = fingerPosition.x;
                    destinationPoint.y = fingerPosition.y;
                    initialPoint.x = mBall.monsterPosition.x;
                    initialPoint.y = mBall.monsterPosition.y;

                    mBall.monsterVelocity = random.nextInt(20) + 15;
                    mBall.monsterSleepTime = random.nextInt(10) + 5;
                }
                mBall.attackFingerPosition(destinationPoint, initialPoint);
            }
        }
    }

    public void draw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        if(mFan != null && mBall.monsterAttackTrick == 2) {
            for(int i=0; i<7; i++) {
                for (int j = 0; j < 3; j++) {
                    canvas.drawCircle((float) mFan[j].bulletPosition[i].x, (float) mFan[j].bulletPosition[i].y,
                                      (float) mFan[j].bulletsRadius, mFan[j].bulletsPaint);
                }
            }
        }

        if(mRocket != null && mBall.monsterAttackTrick == 3) {
            canvas.drawCircle((float) mRocket.rocketPosition.x, (float) mRocket.rocketPosition.y,
                              (float) mRocket.rocketRadius, mRocket.rocketPaint);
        }

        if(mWave != null && mBall.monsterAttackTrick == 4) {

            mWave[0].drawHeatWave(canvas, heatRect[0], 30);

            if(heatWaveTimeGap > 12)
            mWave[1].drawHeatWave(canvas, heatRect[1], 0);

            if(heatWaveTimeGap > 24)
            mWave[2].drawHeatWave(canvas, heatRect[2], 30);

            if(heatWaveTimeGap > 36)
            mWave[3].drawHeatWave(canvas, heatRect[3], 0);

            if(heatWaveTimeGap > 48)
            mWave[4].drawHeatWave(canvas, heatRect[4], 30);
        }

        canvas.drawCircle((float)mBall.monsterPosition.x, (float)mBall.monsterPosition.y,
                          (float)mBall.monsterRadius, mBall.monsterPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: is_game_started = true;
                                          break;

            case MotionEvent.ACTION_UP:   is_game_over = true;
                                          tryGameOver();
                                          break;

            case MotionEvent.ACTION_MOVE: fingerPosition.x = (int)event.getX();
                                          fingerPosition.y = (int)event.getY();
                                          break;
            }
        return true;
    }

    public void randomizeTrajectory() {
        mBall.monsterAttackTrick = random.nextInt(4) + 1;

        if(mBall.monsterAttackTrick == 2) {
            time_to_shoot_bullets = true;
        }
        else if(mBall.monsterAttackTrick == 3) {
            mRocket.initRocket(mBall);
        }
        else if(mBall.monsterAttackTrick == 4) {
            time_for_some_heat = true;
        }
    }

    public void tryGameOver() {
        if(is_game_over) {
            try {
                gameOver();
                is_game_over = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void gameOver() throws InterruptedException {
        mThread.setRunning(false);
        Intent intent = new Intent(mContext, GameOverActivity.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}
