package com.example.root.magnetomania;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView {

    /******************************************** CLASS MEMBERS ********************************************/
    private SurfaceHolder mHolder;
    private GameThread mThread = null;
    private Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    public boolean is_game_started;
    public boolean is_game_paused;
    public boolean is_game_over;

    private MonsterBall mBall = new MonsterBall();
    private MagnetRocket mRocket = new MagnetRocket();

    private BulletFan mFan1 = new BulletFan();
    private BulletFan mFan2 = new BulletFan();
    private BulletFan mFan3 = new BulletFan();

    private HeatWave mWave1 = new HeatWave();
    private HeatWave mWave2 = new HeatWave();
    private HeatWave mWave3 = new HeatWave();
    private HeatWave mWave4 = new HeatWave();
    private HeatWave mWave5 = new HeatWave();

    private int fingerX;
    private int fingerY;
    private int attackAtX;
    private int attackAtY;
    private int attackFromX;
    private int attackFromY;

    private int moveStyle;
    private int monsterSleepCount;
    private int rocketXhaustCount;
    private int bulletFansTimeGap;
    private int heatWaveTimeGap;

    private RectF heatRect1 = new RectF();
    private RectF heatRect2 = new RectF();
    private RectF heatRect3 = new RectF();
    private RectF heatRect4 = new RectF();
    private RectF heatRect5 = new RectF();

    private boolean time_to_shoot_bullets;
    private boolean bullets_on_screen;
    private boolean time_for_some_heat;
    private boolean heat_waves_on_screen;

    private Random random = new Random();

    /**---------------------------------------------------------------------------------------------------**/


    /********************************************* CONSTRUCTOR *********************************************/
    public GameView(Context context){
        super(context);

        this.mContext = getContext();
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
                    bullets_on_screen = true;
                    bulletFansTimeGap = 1;

                    this.attackAtX = this.fingerX;
                    this.attackAtY = this.fingerY;
                    this.attackFromX = this.mBall.monsterX;
                    this.attackFromY = this.mBall.monsterY;

                    this.mFan1.initBullets(mBall, this.attackAtX, this.attackAtY);
                    this.mFan2.initBullets(mBall, this.attackAtX, this.attackAtY);
                    this.mFan3.initBullets(mBall, this.attackAtX, this.attackAtY);
                }

                if(bullets_on_screen)
                {
                    mFan1.setDirectionAndShoot();
                    bulletFansTimeGap++;

                    if(bulletFansTimeGap>5)
                        mFan2.setDirectionAndShoot();

                    if(bulletFansTimeGap>10)
                        mFan3.setDirectionAndShoot();

                    int howManyBulletsOnScreen = 0;

                    for(int i=0; i<7; i++)
                    {
                        if((this.mFan1.bulletPosition[i].x >= mScreenWidth-10 || this.mFan1.bulletPosition[i].x <= 10) && (this.mFan1.bulletPosition[i].y >= mScreenHeight-10 || this.mFan1.bulletPosition[i].y <= 10))
                            howManyBulletsOnScreen++;
                        if((this.mFan2.bulletPosition[i].x >= mScreenWidth-10 || this.mFan2.bulletPosition[i].x <= 10) && (this.mFan2.bulletPosition[i].y >= mScreenHeight-10 || this.mFan2.bulletPosition[i].y <= 10))
                            howManyBulletsOnScreen++;
                        if((this.mFan3.bulletPosition[i].x >= mScreenWidth-10 || this.mFan3.bulletPosition[i].x <= 10) && (this.mFan3.bulletPosition[i].y >= mScreenHeight-10 || this.mFan3.bulletPosition[i].y <= 10))
                            howManyBulletsOnScreen++;
                    }

                    if(howManyBulletsOnScreen >= 18)
                        bullets_on_screen = false;
                }
                else
                {
                    this.mBall.monsterAttackTrick = 0;
                    this.mBall.monsterVelocity = random.nextInt(20) + 10;
                    this.mBall.monsterSleepTime = random.nextInt(10) + 5;
                }
            }
            else if(this.mBall.monsterAttackTrick == 4)
            {
                monsterSleepCount = 1;

                if(time_for_some_heat)
                {
                    time_for_some_heat = false;
                    heat_waves_on_screen = true;
                    heatWaveTimeGap = 1;

                    this.mWave1.initHeatWave(mBall);
                    this.mWave2.initHeatWave(mBall);
                    this.mWave3.initHeatWave(mBall);
                    this.mWave4.initHeatWave(mBall);
                    this.mWave5.initHeatWave(mBall);
                }

                if(heat_waves_on_screen)
                {
                    heatWaveTimeGap++;
                    heatRect1 = this.mWave1.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(heatWaveTimeGap>10)
                    heatRect2 = this.mWave2.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(heatWaveTimeGap>20)
                    heatRect3 = this.mWave3.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(heatWaveTimeGap>30)
                    heatRect4 = this.mWave4.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(heatWaveTimeGap>40)
                    heatRect5 = this.mWave5.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(this.mWave5.heatWaveRadius > this.mScreenHeight)
                        heat_waves_on_screen = false;
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

                    // For preventing glitchy movement at the boundary.
                    if (mBall.monsterX >= mScreenWidth)
                    mBall.monsterX = mScreenWidth;
                    else if (mBall.monsterX <= 0)
                    mBall.monsterX = 0;
                    else if (mBall.monsterY >=mScreenHeight)
                    mBall.monsterY = mScreenHeight;
                    else if (mBall.monsterY <= 0)
                    mBall.monsterY = 0;


                    this.attackAtX = this.fingerX;
                    this.attackAtY = this.fingerY;
                    this.attackFromX = this.mBall.monsterX;
                    this.attackFromY = this.mBall.monsterY;

                    this.mBall.monsterVelocity = random.nextInt(20) + 10;
                    this.mBall.monsterSleepTime = random.nextInt(10) + 5;


                }

                mBall.attackFingerPosition(attackAtX, attackAtY, attackFromX, attackFromY);
            }
        }

        if(is_game_over)
        {
            try {
                this.gameOver();
                is_game_over = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);

        if(this.mFan1 != null && this.mBall.monsterAttackTrick == 2)
        {
            for(int i=0; i<7; i++)
            {
                canvas.drawCircle((float)mFan1.bulletPosition[i].x , (float)mFan1.bulletPosition[i].y , (float)mFan1.bulletsRadius , mFan1.bulletsPaint);
                canvas.drawCircle((float)mFan2.bulletPosition[i].x , (float)mFan2.bulletPosition[i].y , (float)mFan2.bulletsRadius , mFan2.bulletsPaint);
                canvas.drawCircle((float)mFan3.bulletPosition[i].x , (float)mFan3.bulletPosition[i].y , (float)mFan3.bulletsRadius , mFan3.bulletsPaint);
            }
        }

        if(this.mRocket != null && this.mBall.monsterAttackTrick == 3)
        canvas.drawCircle((float)mRocket.rocketX, (float)mRocket.rocketY, (float)mRocket.rocketRadius, mRocket.rocketPaint);

        if(this.mWave1 != null && this.mBall.monsterAttackTrick == 4)
        {
                canvas.drawArc(heatRect1,   0, 30, false, this.mWave1.heatWavePaint);
                canvas.drawArc(heatRect1,  60, 30, false, this.mWave1.heatWavePaint);
                canvas.drawArc(heatRect1, 120, 30, false, this.mWave1.heatWavePaint);
                canvas.drawArc(heatRect1, 180, 30, false, this.mWave1.heatWavePaint);
                canvas.drawArc(heatRect1, 240, 30, false, this.mWave1.heatWavePaint);
                canvas.drawArc(heatRect1, 300, 30, false, this.mWave1.heatWavePaint);

            if(heatWaveTimeGap>10)
            {
                canvas.drawArc(heatRect2,  10, 30, false, this.mWave2.heatWavePaint);
                canvas.drawArc(heatRect2,  70, 30, false, this.mWave2.heatWavePaint);
                canvas.drawArc(heatRect2, 130, 30, false, this.mWave2.heatWavePaint);
                canvas.drawArc(heatRect2, 190, 30, false, this.mWave2.heatWavePaint);
                canvas.drawArc(heatRect2, 250, 30, false, this.mWave2.heatWavePaint);
                canvas.drawArc(heatRect2, 310, 30, false, this.mWave2.heatWavePaint);
            }

            if(heatWaveTimeGap>20)
            {
                canvas.drawArc(heatRect3,  20, 30, false, this.mWave3.heatWavePaint);
                canvas.drawArc(heatRect3,  80, 30, false, this.mWave3.heatWavePaint);
                canvas.drawArc(heatRect3, 140, 30, false, this.mWave3.heatWavePaint);
                canvas.drawArc(heatRect3, 200, 30, false, this.mWave3.heatWavePaint);
                canvas.drawArc(heatRect3, 260, 30, false, this.mWave3.heatWavePaint);
                canvas.drawArc(heatRect3, 320, 30, false, this.mWave3.heatWavePaint);
            }

            if(heatWaveTimeGap>30)
            {
                canvas.drawArc(heatRect4,  30, 30, false, this.mWave4.heatWavePaint);
                canvas.drawArc(heatRect4,  90, 30, false, this.mWave4.heatWavePaint);
                canvas.drawArc(heatRect4, 150, 30, false, this.mWave4.heatWavePaint);
                canvas.drawArc(heatRect4, 210, 30, false, this.mWave4.heatWavePaint);
                canvas.drawArc(heatRect4, 270, 30, false, this.mWave4.heatWavePaint);
                canvas.drawArc(heatRect4, 330, 30, false, this.mWave4.heatWavePaint);
            }

            if(heatWaveTimeGap>40)
            {
                canvas.drawArc(heatRect5,  40, 30, false, this.mWave5.heatWavePaint);
                canvas.drawArc(heatRect5, 100, 30, false, this.mWave5.heatWavePaint);
                canvas.drawArc(heatRect5, 160, 30, false, this.mWave5.heatWavePaint);
                canvas.drawArc(heatRect5, 220, 30, false, this.mWave5.heatWavePaint);
                canvas.drawArc(heatRect5, 280, 30, false, this.mWave5.heatWavePaint);
                canvas.drawArc(heatRect5, 340, 30, false, this.mWave5.heatWavePaint);
            }
        }

        canvas.drawCircle((float)mBall.monsterX, (float)mBall.monsterY, (float)mBall.monsterRadius, mBall.monsterPaint);

        if(is_game_over)
        {
            try {
                this.gameOver();
                is_game_over = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
        if(this.mBall.monsterAttackTrick == 2)
        {
            time_to_shoot_bullets = true;
        }
        else if(this.mBall.monsterAttackTrick == 3)
        {
            this.mRocket.initRocket(mBall);
        }
        else if(this.mBall.monsterAttackTrick == 4)
        {
            time_for_some_heat = true;
        }
    }

    void gameOver() throws InterruptedException {
        mThread.setRunning(false);
        Intent intent = new Intent(mContext, GameOverActivity.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}
