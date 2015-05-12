package com.example.root.magnetomania;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class GameView extends SurfaceView {

    private SurfaceHolder mHolder;
    private GameThread mThread = null;

    private int mScreenWidth;
    private int mScreenHeight;

    public boolean is_game_started;
    public boolean is_game_over;
    public boolean is_game_paused;
    private MonsterBall mBall = new MonsterBall();

    private int fingerX;
    private int fingerY;
    private int attackAtX;
    private int attackAtY;
    private int initialX;
    private int initialY;
    private int moveStyle;

    private Random random = new Random();

    public GameView(Context context){
        super(context);

        this.mScreenWidth = GameActivity.mScreenSize.x;
        this.mScreenHeight= GameActivity.mScreenSize.y;

        this.mThread = new GameThread(this);
        this.mHolder = this.getHolder();
        this.is_game_paused = false;
        this.is_game_over = false;


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

    public void update()
    {

        if(is_game_started) {
            if (mBall.monsterX >= mScreenWidth || mBall.monsterY >= mScreenHeight || mBall.monsterX <= 0 || mBall.monsterY <= 0) {
                this.attackAtX = this.fingerX;
                this.attackAtY = this.fingerY;
                this.initialX = this.mBall.monsterX;
                this.initialY = this.mBall.monsterY;

                if(mBall.monsterX < attackAtX && mBall.monsterY < attackAtY)
                    moveStyle = 1;
                else if(mBall.monsterX < attackAtX && mBall.monsterY > attackAtY)
                    moveStyle = 2;
                else if(mBall.monsterX > attackAtX && mBall.monsterY > attackAtY)
                    moveStyle = 3;
                else if(mBall.monsterX > attackAtX && mBall.monsterY < attackAtY)
                    moveStyle = 4;

                this.mBall.monsterVelocity = random.nextInt(10) + 10;

            }

            mBall.attackFingerPosition(attackAtX, attackAtY, initialX, initialY, moveStyle);
        }

        if(is_game_over)
        {
            is_game_over = false;
        }

    }

    public void draw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle((float)mBall.monsterX, (float)mBall.monsterY, (float)mBall.monsterRadius, mBall.monsterPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!is_game_paused)
        {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //Do Something here.
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
}
