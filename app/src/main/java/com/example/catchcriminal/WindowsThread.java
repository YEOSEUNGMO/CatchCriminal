package com.example.catchcriminal;

public class WindowsThread extends Thread {
    Window m_Windows;
    int m_ShowingTime;
    public WindowsThread( Window _window,int _ShowingTime)
    {
        m_Windows=_window;
        m_ShowingTime=_ShowingTime;
    }

    public void run()
    {
        try {
            Thread.sleep(m_ShowingTime);
        }
        catch (InterruptedException e) {
        }
        m_Windows.Hide();

    }
}
