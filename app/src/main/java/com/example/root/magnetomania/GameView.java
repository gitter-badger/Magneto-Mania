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
    private BulletFan mFan = new BulletFan();
    private HeatWave mWave = new HeatWave();

    private int fingerX;
    private int fingerY;
    private int attackAtX;
    private int attackAtY;
    private int attackFromX;
    private int attackFromY;
    private int moveStyle;

    private int monsterSleepCount;
    private int rocketXhaustCount;
    private RectF heatRect = new RectF();

    private boolean time_to_shoot_bullets;
    private boolean bullets_on_screen;
    private boolean time_for_some_heat;

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
                    bullets_on_screen = true;

                    this.attackAtX = this.fingerX;
                    this.attackAtY = this.fingerY;
                    this.attackFromX = this.mBall.monsterX;
                    this.attackFromY = this.mBall.monsterY;

                    this.mFan.initBullets(mBall, this.attackAtX, this.attackAtY);
                }

                if(bullets_on_screen)
                {
                    mFan.setDirectionAndShoot();
                    int count = 0;

                    for(int i=0; i<7; i++)
                    {
                        if((this.mFan.bulletPosition[i].x >= mScreenWidth-10 || this.mFan.bulletPosition[i].x <= 10) && (this.mFan.bulletPosition[i].y >= mScreenHeight-10 || this.mFan.bulletPosition[i].y <= 10))
                            count++;
                    }
                    if(count == 7)
                        bullets_on_screen = false;
                    count = 0;

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
                    heatRect = this.mWave.setHeatWaveSize(this.mBall.monsterX, this.mBall.monsterY);

                    if(this.mWave.heatWaveRadius > this.mScreenHeight)
                        time_for_some_heat = false;

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

        if(this.mFan != null && this.mBall.monsterAttackTrick == 2)
        {
            for(int i=0; i<7; i++)
            {
                canvas.drawCircle((float)mFan.bulletPosition[i].x , (float)mFan.bulletPosition[i].y , (float)mFan.bulletsRadius , mFan.bulletsPaint);
            }
        }

        if(this.mRocket != null && this.mBall.monsterAttackTrick == 3)
        canvas.drawCircle((float)mRocket.rocketX, (float)mRocket.rocketY, (float)mRocket.rocketRadius, mRocket.rocketPaint);

        if(this.mWave != null && this.heatRect != null && this.mBall.monsterAttackTrick == 4)
        {
            canvas.drawArc(heatRect, 0, 45, false, this.mWave.heatWavePaint);
            canvas.drawArc(heatRect, 60, 45, false, this.mWave.heatWavePaint);
            canvas.drawArc(heatRect, 120, 45, false, this.mWave.heatWavePaint);
            canvas.drawArc(heatRect, 180, 45, false, this.mWave.heatWavePaint);
            canvas.drawArc(heatRect, 240, 45, false, this.mWave.heatWavePaint);
            canvas.drawArc(heatRect, 300, 45, false, this.mWave.heatWavePaint);
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
            this.mWave.initHeatWave(mBall);
            this.heatRect = null;
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
