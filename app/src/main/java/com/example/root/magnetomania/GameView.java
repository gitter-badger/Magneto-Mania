package com.example.root.magnetomania;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView {

    /******************************************** CLASS MEMBERS ********************************************/
    private SurfaceHolder   mHolder;
    private GameThread      mThread          = null;
    private Context         mContext;

    public boolean          is_game_started;
    public boolean          is_game_over;

    private MonsterBall     mBall            = new MonsterBall();
    private MagnetRocket    mRocket          = new MagnetRocket();
    private BulletFan[]     mFan             = new BulletFan[3];
    private HeatWave[]      mWave            = new HeatWave[5];
    private RectF[]         heatRect         = new RectF[5];
    private LaserBeam[]     mBeam            = new LaserBeam[8];
    private TimeBomb[]      mBomb            = new TimeBomb[3];

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

    private boolean         monster_trick_time;
    private boolean         time_to_shoot_bullets;
    private boolean         bullets_on_screen;
    private boolean         time_for_some_heat;
    private boolean         heat_waves_on_screen;
    private boolean         time_to_fire_laser;
    private boolean         laser_beam_on_screen;
    private boolean         time_to_plant_bombs;
    private boolean         bomb_residue_on_screen;

    private SpriteAnimation animation       = new SpriteAnimation(this);
    private Random          random          = new Random();

    private Point           pFingerPosition = new Point(0,0);
    public static double    Score;
    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public GameView(Context context) {
        super(context);

        this.mHolder                = this.getHolder();
        this.mThread                = new GameThread(this);
        this.mContext               = getContext();
        
        this.is_game_started        = false;
        this.is_game_over           = false;

        for(int i=0; i<3; i++) {
            this.mFan[i]     = new BulletFan();
        }

        for(int i=0; i<5; i++) {
            this.heatRect[i] = new RectF();
            this.mWave[i]    = new HeatWave();
        }

        for(int i=0; i<8; i++) {
            this.mBeam[i] = new LaserBeam();
        }

        for(int i=0; i<3; i++) {
            this.mBomb[i] = new TimeBomb();
        }

        this.monsterSleepCount      = 1;
        this.rocketXhaustCount      = 1;
        this.bulletFansTimeGap      = 1;
        this.heatWaveTimeGap        = 1;
        this.laserBeamMoveCount     = 1;
        this.laserAlphaCount        = 1;
        this.bombPlantCount         = 1;

        this.monster_trick_time     = false;
        this.time_to_shoot_bullets  = false;
        this.bullets_on_screen      = false;
        this.time_for_some_heat     = false;
        this.heat_waves_on_screen   = false;
        this.time_to_fire_laser     = false;
        this.laser_beam_on_screen   = false;
        this.time_to_plant_bombs    = false;
        this.bomb_residue_on_screen = false;

        this.Score                  = 0.0;

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

                for(int i=0; i<5; i++) {
                    /*** Condition of game over when finger touches the heat wave. ***/
                    /***/is_game_over = mWave[i].didHeatWaveBurnTheFinger((i+1)%2);
                    /***/if (is_game_over)
                    /***/ tryGameOver();
                }

                if(heat_waves_on_screen) {
                    heatWaveTimeGap++;
                    heatRect[0] = mWave[0].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 20)
                        heatRect[1] = mWave[1].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 40)
                        heatRect[2] = mWave[2].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 60)
                        heatRect[3] = mWave[3].setHeatWaveSize(mBall.monsterPosition);

                    if(heatWaveTimeGap > 80)
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
                    /***/if (is_game_over)
                    /***/ tryGameOver();
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

                    if(bulletFansTimeGap>5)
                        mFan[1].setDirectionAndShoot();

                    if(bulletFansTimeGap>10)
                        mFan[2].setDirectionAndShoot();

                    int howManyBulletsOnScreen = 0;

                    for(int i=0; i<3; i++) {
                        for(int j=0; j<7; j++) {
                            if ((mFan[i].bulletPosition[j].x >= GameActivity.mScreenSize.x-10 || mFan[i].bulletPosition[j].x <= 10) &&
                                (mFan[i].bulletPosition[j].y >= GameActivity.mScreenSize.y-10 || mFan[i].bulletPosition[j].y <= 10))
                                howManyBulletsOnScreen++;
                        }
                    }

                    if(howManyBulletsOnScreen >= 16)
                        bullets_on_screen = false;
                }
                else {
                    mBall.prepareForSleepAndAttack();
                    mBall.attackFingerPosition();
                }
            }
            else if (mBall.monsterTrickSetDecider == 1 && mBall.monsterAttackTrick == 1) {
                monsterSleepCount = 1;
                laserAlphaCount = 10;

                if(time_to_fire_laser) {
                    initialPoint = Geometry.setCoordinates(mBall.monsterPosition);
                    laserAlphaCount+=40;

                    for(int i=0; i<8; i++) {
                        mBeam[i].initLaserBeam(i);
                        mBeam[i].laserBeamPaint.setAlpha(laserAlphaCount);
                    }

                    Geometry.moveMonsterToCenter(mBall);

                    if(mBall.monsterPosition.x == Geometry.center.x && mBall.monsterPosition.y == Geometry.center.y) {
                        time_to_fire_laser   = false;
                        laser_beam_on_screen = true;
                        laserBeamMoveCount   = 1;

                        for(int i=0; i<8; i++) {
                            mBeam[i].laserBeamPaint.setAlpha(255);
                        }
                    }
                }
                else if(laser_beam_on_screen) {
                    laserBeamMoveCount++;

                    if(laserBeamMoveCount < 200) {
                        if(laserBeamMoveCount % 25 == 0) {
                            for(int i=0; i<8; i++) {
                                mBeam[i].initLaserBeam(i);
                            }
                        }

                        for(int i=0; i<8; i++) {
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
                    mBall.prepareForSleepAndAttack();
                }
            }
            else if(mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 1) {
                monsterSleepCount = 1;

                for(int i=0; i<3; i++) {
                    /*** Conditon of game over when finger touches the bomb. ***/
                    /***/is_game_over = mBomb[i].didFingerBecameVictimOfBombBlast();
                    /***/if (is_game_over)
                    /***/ tryGameOver();
                }

                if(time_to_plant_bombs) {
                    bombPlantCount++;
                    mBall.attackFingerPosition();

                    for(int i=0; i<3; i++) {
                        if(bombPlantCount > 10*(i+1) && !mBomb[i].is_bomb_planted) {
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

                        if (mBomb[0].is_bomb_planted && mBomb[1].is_bomb_planted && mBomb[2].is_bomb_planted) {
                            time_to_plant_bombs = false;
                            bomb_residue_on_screen = true;

                            for(int i=0; i<3; i++) {
                                mBomb[i].is_bomb_planted = false;
                            }
                        }
                    }
                }
                else if(bomb_residue_on_screen) {
                    for(int i=0; i<3; i++) {
                        if(mBomb[i].timeBombCounter > 0) {
                            mBomb[i].tickTockCountDown();
                        }
                        else {
                            mBomb[i].increaseBombExplosion();
                            if(mBomb[2].bombCurrentRadius == mBomb[2].bombExplosionRadius)
                                bomb_residue_on_screen = false;
                        }
                    }
                }
                else {
                    for(int i=0; i<3; i++) {
                        mBomb[i].bombPosition.set(GameActivity.mScreenSize.x + 100, GameActivity.mScreenSize.y + 100);
                        mBomb[i].bombCurrentRadius = mBomb[i].bombInitialRadius;
                    }
                    bombPlantCount = 1;

                    mBall.prepareForSleepAndAttack();
                    mBall.attackFingerPosition();
                }
            }
            else if(mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 2) {
                monsterSleepCount = 1;

                /*** Condition of game over when finger touches the rocket. ***/
                /***/   is_game_over = mRocket.didRocketGetTheFinger();
                /***/   if(is_game_over)
                /***/   tryGameOver();

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
                    mBall.attackFingerPosition();
                }
            }
            else {
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

            if(heatWaveTimeGap > 20)
                mWave[1].drawHeatWave(canvas, heatRect[1], 0);

            if(heatWaveTimeGap > 40)
                mWave[2].drawHeatWave(canvas, heatRect[2], 30);

            if(heatWaveTimeGap > 60)
                mWave[3].drawHeatWave(canvas, heatRect[3], 0);

            if(heatWaveTimeGap > 80)
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
                for(int i=0; i<8; i++) {
                    canvas.drawLine((float) mBeam[i].center.x, (float) mBeam[i].center.y, (float) mBeam[i].laserDestinationX, (float) mBeam[i].laserDestinationY, mBeam[i].laserBeamPaint);
                }
            }
            else if (laserBeamMoveCount%2==1 && laser_beam_on_screen) {
                for(int i=0; i<8; i++) {
                    canvas.drawLine((float) mBeam[i].center.x, (float) mBeam[i].center.y, (float) mBeam[i].laserDestinationX, (float) mBeam[i].laserDestinationY, mBeam[i].laserBeamPaint);
                }
            }
        }

        if(mBomb != null && mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 1) {
            for(int i=0; i<3; i++) {
                canvas.drawCircle(mBomb[i].bombPosition.x, mBomb[i].bombPosition.y, mBomb[i].bombCurrentRadius, mBomb[i].bombPaint);
            }
        }

        if(mRocket != null && mBall.monsterTrickSetDecider == 2 && mBall.monsterAttackTrick == 2) {
            canvas.drawCircle((float)mRocket.rocketPosition.x, (float)mRocket.rocketPosition.y, (float)mRocket.rocketRadius, mRocket.rocketPaint);
        }

        canvas.drawCircle((float) mBall.monsterPosition.x, (float) mBall.monsterPosition.y, (float) mBall.monsterRadius, mBall.monsterPaint);
        canvas.drawText(Integer.toString((int) Score), 5, 5, mBall.monsterPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                is_game_started = true;
                break;

            case MotionEvent.ACTION_UP:
                is_game_over = true;
                tryGameOver();
                break;

            case MotionEvent.ACTION_MOVE:
                pFingerPosition = Geometry.setCoordinates(fingerPosition);
                fingerPosition.x = (int)event.getX();
                fingerPosition.y = (int)event.getY();

                if(fingerPosition.x < 15) {
                    fingerPosition.x = 15;
                }
                if(fingerPosition.y < 15) {
                    fingerPosition.y = 15;
                }
                if(fingerPosition.x > GameActivity.mScreenSize.x -15) {
                    fingerPosition.x = GameActivity.mScreenSize.x - 15;
                }
                if(fingerPosition.y > GameActivity.mScreenSize.y -15) {
                    fingerPosition.y = GameActivity.mScreenSize.y - 15;
                }

                for(int i=0; i<5; i++) {
                    /*** Condition of game over when finger touches the heat wave. ***/
                    /***/is_game_over = mWave[i].didHeatWaveBurnTheFinger((i+1)%2);
                    /***/if (is_game_over)
                    /***/ tryGameOver();
                }

                Score+=Geometry.distanceForScore(fingerPosition, pFingerPosition)/10.0;
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
                mThread.setFPS(50);
            }
        }
        else if(mBall.monsterTrickSetDecider == 2) {
            if(mBall.monsterAttackTrick == 1) {
                time_to_plant_bombs = true;
                initialPoint        = Geometry.setCoordinates(mBall.monsterPosition);
                destinationPoint    = Geometry.setCoordinates(fingerPosition);
            }
            else if(mBall.monsterAttackTrick == 2) {
                mRocket.initRocket(mBall);
            }
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
        intent.putExtra("Your Score:", Score);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}
