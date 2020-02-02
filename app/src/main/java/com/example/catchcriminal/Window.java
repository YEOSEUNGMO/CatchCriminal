package com.example.catchcriminal;

import android.util.Log;

public class Window {
    public  static final int EMPTY=0;        // 빈 창문
    public  static final int CRIMINAL1=1;   // 범인
    public  static final int CRIMINAL2=2;   // 범인
    public  static final int CRIMINAL3=3;   // 범인
    public  static final int HOTSAGE=4;     // 인질

    public  static final int HIDE=0;     //
    public  static final int SHOW=1;     //

    public int m_state;
    public int m_target;

    public  Window(int _State)
    {
        m_state=_State;
        m_target=EMPTY;
    }

    public void Hide()
    {
        m_target=EMPTY;
        m_state=HIDE;
    }
}
