package com.sdsmdg.kd.magnetomania;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView {

    /******************************************** CLASS MEMBERS ********************************************/
    private SurfaceHolder       mHolder;
    public static GameThread   mThread          = null;
    public static Context      mContext;

    public static boolean   is_game_started;
    public static boolean   is_game_over;

    private MonsterBall     mBall            = new MonsterBall();
    private MagnetRocket    mRocket          = new MagnetRocket();
    private BulletFan[]     mFan             = new BulletFan[3];
    private HeatWave[]      mWave            = new HeatWave[5];
    private RectF[]         heatRect         = new RectF[5];
    private LaserBeam[]     mBeam            = new LaserBeam[4];
    private TimeBomb[]      mBomb            = new TimeBomb[2];
    private BoomerangTwister[] mTwister      = new BoomerangTwister[5];

    public static Point     fingerPosition   = new Point(0,0);
    public static Point     destinationPoint = new Point(0,0);
    public static Point     initialPoint     = new Point(0,0);

    private int             monsterSleepCount;
    private int             rocketXhaustCount;
    private int             bulletFansTimeGap;
    private int             heatWaveTimeGap;
    private int             laserBeamMoveCount;
    private int             laserAlphaCount;
    private int             bombPlantCount;
    private int             twisterTimeGap;

    private boolean         monster_trick_time;
    private boolean         time_to_shoot_bullets;
    private boolean         bullets_on_screen;
    private boolean         time_for_some_heat;
    private boolean         heat_waves_on_screen;
    private boolean         time_to_fire_laser;
    private boolean         laser_beam_on_screen;
    private boolean         time_to_plant_bombs;
    private boolean         bomb_residue_on_screen;
    private boolean         time_for_some_twisters;
    private boolean         twisters_on_screen;

    private SpriteAnimation animation       = new SpriteAnimation(this);
    private Random          random          = new Random();

    public static double    Score;
    private Paint           scorePaint      = new Paint();

    //These variables are for multi-touch disabling.
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    float x, y, mPosX, mPosY, mLastTouchX, mLastTouchY;

    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public GameView(Context context) {
        super(context);

        mHolder                = this.getHolder();
        mThread                = new GameThread(this);
        mContext               = getContext();
        
        is_game_started        = false;
        is_game_over           = false;

        for(int i=0; i<3; i++) {
            this.mFan[i]     = new BulletFan();
        }

        for(int i=0; i<5; i++) {
            this.heatRect[i] = new RectF();
            this.mWave[i]    = new HeatWave();
            this.mTwister[i] = new BoomerangTwister();
        }

        for(int i=0; i<4; i++) {
            this.mBeam[i] = new LaserBeam();
        }

        for(int i=0; i<2; i++) {
            this.mBomb[i] = new TimeBomb();
        }

        this.monsterSleepCount      = 1;
        this.rocketXhaustCount      = 1;
        this.bulletFansTimeGap      = 1;
        this.heatWaveTimeGap        = 1;
        this.laserBeamMoveCount     = 1;
        this.laserAlphaCount        = 1;
        this.bombPlantCount         = 1;
        this.twisterTimeGap         = 1;

        this.monster_trick_time     = false;
        this.time_to_shoot_bullets  = false;
        this.bullets_on_screen      = false;
        this.time_for_some_heat     = false;
        this.heat_waves_on_screen   = false;
        this.time_to_fire_laser     = false;
        this.laser_beam_on_screen   = false;
        this.time_to_plant_bombs    = false;
        this.bomb_residue_on_screen = false;
        this.time_for_some_twisters = false;
        this.twisters_on_screen     = false;

        Score                       = 0.0;
        this.scorePaint.setColor(Color.LTGRAY);
        this.scorePaint.setTextSize(20);

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
            /***/   is_game_over = mBall.didMonsterGetTheFinger();
            /***/   if(is_game_over) {
                /***/       tryGameOver();
                /***/       System.exit(0);
            }
                Score += 0.3 + Score / 2500;

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
            else if(mBall.monsterTrickSetDecider == 0 && mBall.monsterAttackTrick == 1) {
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

                    if(heatWaveTimeGap > 25)
                        heatRect[1] = mWave[1].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 50)
                        heatRect[2] = mWave[2].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 75)
                        heatRect[3] = mWave[3].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 100)
                        heatRect[4] = mWave[4].setHeatWaveSize(mBall.monsterPosition);

                    if(mWave[4].heatWaveRadius > 3*GameActivity.mScreenSize.y/2)
                        heat_waves_on_screen = false;
                }
                else {
                    mBall.prepareForSleepAndAttack();
                    mBall.attackFingerPosition();
                }
            }
            else if(mBall.monsterTrickSetDecider == 0 && mBall.monsterAttackTrick == 2) {
                monsterSleepCount = 1;

                for(int i=0; i<3; i++) {
                    /*** Conditon of game over when finger touches the bullet. ***/
                    /***/is_game_over = mFan[i].didBulletGetTheFinger();
                    /***/if (is_game_over) {
                        /***/    tryGameOver();
                        /***/    System.exit(0);
                    }
                }

                if(time_to_shoot_bullets) {
                    time_to_shoot_bullets = false;
                    bullets_on_screen = true;
                    bulletFansTimeGap = 1;

                    destinationPoint = Geometry.setCoordinates(fingerPosition);
                    initialPoint     = Geometry.setCoordinates(mBall.monsterPosition);

                    mFan[0].initBullets(mBall);
                    mFan[1].initBullets(mBall);
                    mFan[2].initBullets(mBall);
                }

                if(bullets_on_screen) {
                    bulletFansTimeGap++;
                    mFan[0].setDirectionAndShoot();

                    if(bulletFansTimeGap>7)
                        mFan[1].setDirectionAndShoot();

                    if(bulletFansTimeGap>14)
                        mFan[2].setDirectionAndShoot();

                    int howManyBulletsOnScreen = 0;

                    for(int i=0; i<3; i++) {
                        for (int j = 0; j < 7; j++) {
                            if (mFan[i].bulletPosition[j].x >= GameActivity.mScreenSize.x+25 || mFan[i].bulletPosition[j].x <= -25 ||
                                    mFan[i].bulletPosition[j].y >= GameActivity.mScreenSize.y+25 || mFan[i].bulletPosition[j].y <= -25) {
                                howManyBulletsOnScreen++;
                            }
                        }
                    }

                    if(howManyBulletsOnScreen == 21) {
                        bullets_on_screen = false;
                    }
                }
                else {
                    mBall.prepareForSleepAndAttack();
                    mBall.monsterSleepTime = random.nextInt(15) + 15;

                }
            }
            else if (mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 1) {
                monsterSleepCount = 1;

                if(time_to_fire_laser) {
                    initialPoint = Geometry.setCoordinates(mBall.monsterPosition);
                    laserAlphaCount+=10;

                    for(int i=0; i<4; i++) {
                        mBeam[i].initLaserBeam(i);
                        mBeam[i].laserBeamPaint.setAlpha(laserAlphaCount);
                    }

                    Geometry.moveMonsterToCenter(mBall);

                    if(mBall.monsterPosition.x == Geometry.center.x && mBall.monsterPosition.y == Geometry.center.y) {
                        time_to_fire_laser   = false;
                        laser_beam_on_screen = true;
                        laserBeamMoveCount   = 1;

                        for(int i=0; i<4; i++) {
                            mBeam[i].laserBeamPaint.setAlpha(255);
                        }
                    }
                }
                else if(laser_beam_on_screen) {
                    laserBeamMoveCount++;

                    if(laserBeamMoveCount < 180) {
                        if(laserBeamMoveCount % 30 == 0) {
                            for(int i=0; i<4; i++) {
                                mBeam[i].initLaserBeam(i);
                            }
                        }

                        for(int i=0; i<4; i++) {
                            mBeam[i].rotateBeam(i);
                        }
                    }
                    else {
                        laser_beam_on_screen = false;
                    }
                }
                else {
                    if (mBall.monsterPosition.x <= GameActivity.mScreenSize.x || mBall.monsterPosition.x <= 0 ||
                            mBall.monsterPosition.y <= GameActivity.mScreenSize.y || mBall.monsterPosition.y <= 0) {
                        mBall.attackFingerPosition();
                    }
                    laserAlphaCount = 1;
                    mBall.prepareForSleepAndAttack();
                }
            }
            else if(mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 2) {
                monsterSleepCount = 1;

                if(time_to_plant_bombs) {
                    bombPlantCount++;
                    mBall.attackFingerPosition();

                    for(int i = 0; i < 2; i++) {
                        if(bombPlantCount > 7*(i+1) && !mBomb[i].is_bomb_planted) {
                            mBomb[i].initTimeBomb(mBall);
                        }
                        else if(mBomb[i].timeBombCounter <= 0) {
                            mBomb[i].increaseBombExplosion();
                        }
                        else if(mBomb[i].is_bomb_planted) {
                            mBomb[i].tickTockCountDown();
                        }
                    }

                    if (mBall.monsterPosition.x >= GameActivity.mScreenSize.x || mBall.monsterPosition.x <= 0 ||
                            mBall.monsterPosition.y >= GameActivity.mScreenSize.y || mBall.monsterPosition.y <= 0) {

                        // For preventing glitchy movement at the boundary.
                        if (mBall.monsterPosition.x > GameActivity.mScreenSize.x) {
                            mBall.monsterPosition.x = GameActivity.mScreenSize.x;
                        } else if (mBall.monsterPosition.x < 0) {
                            mBall.monsterPosition.x = 0;
                        } else if (mBall.monsterPosition.y > GameActivity.mScreenSize.y) {
                            mBall.monsterPosition.y = GameActivity.mScreenSize.y;
                        } else if (mBall.monsterPosition.y < 0) {
                            mBall.monsterPosition.y = 0;
                        }

                        if (mBomb[0].is_bomb_planted && mBomb[1].is_bomb_planted) {
                            time_to_plant_bombs = false;
                            bomb_residue_on_screen = true;

                            for(int i = 0; i < 2; i++) {
                                mBomb[i].is_bomb_planted = false;
                            }
                        }
                    }
                }
                else if(bomb_residue_on_screen) {
                    for(int i = 0; i < 2; i++) {
                        if(mBomb[i].timeBombCounter > 0) {
                            mBomb[i].tickTockCountDown();
                        }
                        else {
                            mBomb[i].increaseBombExplosion();
                            if(mBomb[1].bombCurrentRadius == mBomb[1].bombExplosionRadius)
                                bomb_residue_on_screen = false;
                        }
                    }
                }
                else {
                    for(int i = 0; i < 2; i++) {
                        mBomb[i].bombPosition.set(GameActivity.mScreenSize.x + 100, GameActivity.mScreenSize.y + 100);
                        mBomb[i].bombCurrentRadius = mBomb[i].bombInitialRadius;
                    }
                    bombPlantCount = 1;

                    mBall.prepareForSleepAndAttack();
                    mBall.attackFingerPosition();
                }
            }
            else if(mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 1) {
                monsterSleepCount = 1;

                if(time_for_some_twisters) {
                    twisterTimeGap++;

                    for(int i = 0; i < 5; i++) {
                        if(twisterTimeGap > 10*(i+1) && !mTwister[i].is_twister_thrown) {
                            mTwister[i].initTwister(mBall);
                            mTwister[i].is_twister_thrown = true;
                        }
                        else if(mTwister[i].is_twister_thrown) {
                            mTwister[i].attackTowardsFinger();
                        }
                    }

                    if(mTwister[4].is_twister_thrown) {
                        time_for_some_twisters = false;
                        twisters_on_screen = true;
                    }
                }
                else if(twisters_on_screen) {
                    twisterTimeGap++;

                    for(int i = 0; i < 5; i++) {
                        if (twisterTimeGap > 10*(i+1)) {
                            mTwister[i].attackTowardsFinger();
                        }
                    }

                    if ((mTwister[4].twisterPosition.x >= GameActivity.mScreenSize.x + 25 || mTwister[4].twisterPosition.x <= -25 ||
                         mTwister[4].twisterPosition.y >= GameActivity.mScreenSize.y + 25 || mTwister[4].twisterPosition.y <= -25) &&
                         mTwister[4].twisterVelocity < -(mTwister[4].twisterVelocityMaxMagnitude)) {
                        twisters_on_screen = false;
                    }
                }
                else {
                    twisterTimeGap = 1;

                    for(int i = 0; i < 5; i++) {
                        mTwister[i].twisterPosition.x = GameActivity.mScreenSize.x + 180;
                        mTwister[i].twisterPosition.y = GameActivity.mScreenSize.y + 180;
                        mTwister[i].is_twister_thrown = false;
                    }

                    mBall.prepareForSleepAndAttack();
                    mBall.monsterSleepTime = random.nextInt(15) + 15;
                    mBall.attackFingerPosition();
                }
            }
            else if(mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 2) {
                monsterSleepCount = 1;

                /*** Condition of game over when finger touches the rocket. ***/
                /***/is_game_over = mRocket.didRocketGetTheFinger();
                /***/if(is_game_over) {
                    /***/    tryGameOver();
                    /***/    System.exit(0);
                }

                if(rocketXhaustCount <= mRocket.rocketXhaustTime) {
                    mRocket.rocketTrackFinger();
                    rocketXhaustCount++;
                }
                else {
                    rocketXhaustCount = 1;
                    mRocket.rocketPosition.x = GameActivity.mScreenSize.x + 80;
                    mRocket.rocketPosition.y = GameActivity.mScreenSize.y + 80;
                    mRocket.rocketXhaustTime = 0;

                    mBall.prepareForSleepAndAttack();
                    mBall.monsterSleepTime = random.nextInt(15) + 15;
                    mBall.attackFingerPosition();
                }
            }
            else {
                mThread.setFPS(50);
                monsterSleepCount = 1;
                mBall.prepareForSleepAndAttack();
                mBall.attackFingerPosition();
            }
        }
    }

    public void draw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        if(mWave != null && mBall.monsterTrickSetDecider == 0 && mBall.monsterAttackTrick == 1) {

            mWave[0].drawHeatWave(canvas, heatRect[0], 30);

            if(heatWaveTimeGap > 25)
                mWave[1].drawHeatWave(canvas, heatRect[1], 0);

            if(heatWaveTimeGap > 50)
                mWave[2].drawHeatWave(canvas, heatRect[2], 30);

            if(heatWaveTimeGap > 75)
                mWave[3].drawHeatWave(canvas, heatRect[3], 0);

            if(heatWaveTimeGap > 100)
                mWave[4].drawHeatWave(canvas, heatRect[4], 30);
        }

        if(mFan != null && mBall.monsterTrickSetDecider == 0 && mBall.monsterAttackTrick == 2) {
            for(int i=0; i<7; i++) {
                for (int j = 0; j < 3; j++) {
                    canvas.drawCircle((float) mFan[j].bulletPosition[i].x, (float) mFan[j].bulletPosition[i].y,
                            (float) mFan[j].bulletsRadius, mFan[j].bulletsPaint);
                }
            }
        }

        if(mBeam != null && mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 1) {
            if(time_to_fire_laser) {
                for(int i=0; i<4; i++) {
                    canvas.drawLine((float) mBeam[i].center.x, (float) mBeam[i].center.y, (float) mBeam[i].laserDestinationX, (float) mBeam[i].laserDestinationY, mBeam[i].laserBeamPaint);
                }
            }
            else if (laser_beam_on_screen) {
                for(int i=0; i<4; i++) {
                    canvas.drawLine((float) mBeam[i].center.x, (float) mBeam[i].center.y, (float) mBeam[i].laserDestinationX, (float) mBeam[i].laserDestinationY, mBeam[i].laserBeamPaint);
                }
            }
        }

        if(mBomb != null && mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 2) {
            for(int i=0; i<2; i++) {
                canvas.drawCircle(mBomb[i].bombPosition.x, mBomb[i].bombPosition.y, mBomb[i].bombCurrentRadius, mBomb[i].bombPaint);
            }
        }

        if(mTwister != null && mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 1) {
            for(int i=0; i<5; i++) {
//                canvas.drawCircle((float) mTwister[i].twisterPosition.x, (float) mTwister[i].twisterPosition.y,
//                        (float) mTwister[i].twisterRadius, mTwister[i].twisterPaint);
                  animation.drawBoomerangTwister(mTwister[i], canvas);
            }
        }

        if(mRocket != null && mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 2) {
            canvas.drawCircle((float)mRocket.rocketPosition.x, (float)mRocket.rocketPosition.y, (float)mRocket.rocketRadius, mRocket.rocketPaint);
        }

        canvas.drawCircle((float) mBall.monsterPosition.x, (float) mBall.monsterPosition.y, (float) mBall.monsterRadius, mBall.monsterPaint);
        canvas.drawText(Integer.toString((int) Score), 40, 40, scorePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();

                mPosX = x;
                mPosY = y;

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                is_game_started = true;
                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                is_game_over = true;
                tryGameOver();
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerIndex = ev.findPointerIndex(mActivePointerId);
                x = ev.getX(pointerIndex);
                y = ev.getY(pointerIndex);

                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                mLastTouchX = x;
                mLastTouchY = y;

                invalidate();

                fingerPosition.x = (int) ev.getX();
                fingerPosition.y = (int) ev.getY();

                if (fingerPosition.x < 15) {
                    fingerPosition.x = 15;
                }
                if (fingerPosition.y < 15) {
                    fingerPosition.y = 15;
                }
                if (fingerPosition.x > GameActivity.mScreenSize.x - 15) {
                    fingerPosition.x = GameActivity.mScreenSize.x - 15;
                }
                if (fingerPosition.y > GameActivity.mScreenSize.y - 15) {
                    fingerPosition.y = GameActivity.mScreenSize.y - 15;
                }

                if (mWave != null && mBall.monsterTrickSetDecider == 0 && mBall.monsterAttackTrick == 1) {
                    for (int i = 0; i < 5; i++) {
                        /*** Condition of game over when finger touches the heat wave. ***/
                        /***/is_game_over = mWave[i].didHeatWaveBurnTheFinger((i + 1) % 2);
                        /***/if (is_game_over) {
                            /***/tryGameOver();
                            /***/System.exit(0);
                        }
                    }
                }

                if (mBeam != null && mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 1 && laser_beam_on_screen) {
                    for (int i = 0; i < 4; i++) {
                        /*** Condition of game over when finger touches the laser beam. ***/
                        /***/is_game_over = mBeam[i].didLaserBeamPenetrateTheFinger();
                        /***/if (is_game_over) {
                            /***/tryGameOver();
                            /***/System.exit(0);
                        }
                    }
                }

                if (mBomb != null && mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 2) {
                    for (int i = 0; i < 2; i++) {
                        /*** Conditon of game over when finger touches the bomb. ***/
                        /***/is_game_over = mBomb[i].didFingerBecameVictimOfBombBlast();
                        /***/if (is_game_over) {
                            /***/tryGameOver();
                            /***/System.exit(0);
                        }
                    }
                }

                if (mTwister != null && mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 1) {
                    for (int i = 0; i < 5; i++) {
                        /*** Conditon of game over when finger touches the boomerang. ***/
                        /***/is_game_over = mTwister[i].didTwisterCaptureTheFinger();
                        /***/if (is_game_over) {
                            /***/tryGameOver();
                            /***/System.exit(0);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP:

                pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
        }
        return true;
    }


    public void randomizeTrajectory() {
        mBall.monsterTrickSetDecider = ++mBall.monsterTrickSetDecider % 3;
        mBall.monsterAttackTrick     = random.nextInt(2) + 1;

        if(mBall.monsterTrickSetDecider == 0) {
            if(mBall.monsterAttackTrick == 1) {
                time_for_some_heat = true;
                mThread.setFPS(100);
            }
            else if(mBall.monsterAttackTrick == 2) {
                time_to_shoot_bullets = true;
            }
        }
        else if(mBall.monsterTrickSetDecider == 1) {
            if(mBall.monsterAttackTrick == 1) {
                time_to_fire_laser = true;
            }
            else if(mBall.monsterAttackTrick == 2) {
                time_to_plant_bombs = true;
                initialPoint        = Geometry.setCoordinates(mBall.monsterPosition);
                destinationPoint    = Geometry.setCoordinates(fingerPosition);
            }
        }
        else if(mBall.monsterTrickSetDecider == 2) {
            if(mBall.monsterAttackTrick == 1) {
                time_for_some_twisters = true;
            }
            else if(mBall.monsterAttackTrick == 2) {
                mRocket.initRocket(mBall);
            }
        }
    }


    public static void tryGameOver() {
        if(is_game_over) {
            try {
                gameOver();
                is_game_over = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static void gameOver() throws InterruptedException {
        mThread.setRunning(false);
        Intent intent = new Intent(mContext, GameOverActivity.class);
        intent.putExtra("Your Score:", Score);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}
