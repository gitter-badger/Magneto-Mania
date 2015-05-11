package com.example.root.magnetomania;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView {

    private SurfaceHolder mHolder;
    private GameThread mThread = null;

    private int mScreenWidth;
    private int mScreenHeight;

    public GameView(Context context){
        super(context);

        this.mScreenWidth = GameActivity.mScreenSize.x;
        this.mScreenHeight= GameActivity.mScreenSize.y;

        this.mThread = new GameThread(this);
        this.mHolder = this.getHolder();

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
        //My Code goes here.
    }

    public void draw(Canvas canvas)
    {
        //My Code goes here.
    }

}
