package com.sdsmdg.kd.magnetomania;


import android.graphics.Canvas;

public class GameThread extends Thread {
    private long FPS = 50;
    private boolean running=false;

    private Canvas mGameCanvas;
    private GameView mGameView;

    public GameThread(GameView mGameView)
    {
        this.mGameView = mGameView;
        this.mGameCanvas = null;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    @Override
    public void run()
    {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while(running)
        {
            mGameCanvas = null;
            startTime = System.currentTimeMillis();

            try
            {
                mGameCanvas = mGameView.getHolder().lockCanvas();
                synchronized (mGameView.getHolder())
                {
                    mGameView.update();
                    if(mGameCanvas != null)
                        mGameView.draw(mGameCanvas);
                }
            }
            finally
            {
                if (mGameCanvas != null)
                {
                    mGameView.getHolder().unlockCanvasAndPost(mGameCanvas);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try
            {
                if(sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}