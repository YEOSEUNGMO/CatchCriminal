package com.example.catchcriminal;

import android.util.Log;

public class GameThread extends Thread {
    GameView m_View;
    public int Showing_Time=1000;
    public boolean playing;

    public GameThread(GameView _view)
    {
        m_View=_view;
        playing=false;
    }

    public void run()
    {
        playing=true;
        while(playing) {
            try {
                Thread.sleep(Showing_Time);
            }
            catch (InterruptedException e) {
            }
            m_View.ShowTarget();
            if(Showing_Time>300)
                Showing_Time-=20;
        }
    }

}
