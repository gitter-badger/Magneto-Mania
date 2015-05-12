package com.example.root.magnetomania;


import android.graphics.Canvas;

public class GameThread extends Thread {
    static final long FPS = 30;
    private boolean running=false;

    public Canvas mGameCanvas;
    private GameView mGameView;

    public GameThread(GameView mGameView)
    {
        this.mGameView = mGameView;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
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