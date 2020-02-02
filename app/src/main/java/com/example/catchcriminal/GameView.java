package com.example.catchcriminal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Vector;

public class GameView extends View {

    public static final int STATE_READY=0;
    public static final int STATE_GAME=1;
    public static final int STATE_END=2;
    public static final int STATE_WAIT=3;
    public static final int Window_WidthCount=2;
    public static final int Window_HeightCount=4;
    public static final int Window_LeftStartPoint=100;
    public static final int Window_TopStartPoint=300;

    Typeface m_font;
    private int m_state=STATE_READY;
    private int HeartCount=5;
    private int CatchedCount=0;
    int CanvasWidth;
    int CanvasHeight;
    int startcountdown=0;
    Bitmap m_BackGroundImage;
    Bitmap m_WindowImage;
    Bitmap m_Criminal1Image;
    Bitmap m_Criminal2Image;
    Bitmap m_Criminal3Image;
    Bitmap m_HostageImage;
    Bitmap m_StartButtonImage;
    Bitmap m_GuideImage;
    Bitmap m_HeartImage;
    Bitmap m_GameOverImage;
    Bitmap m_BulletHoleImage;

    MediaPlayer gun_effect;
    MediaPlayer hostage_effect;

    GameThread _thread=new GameThread(this);

    Window m_Window[][];
    public  GameView(Context context)
    {
        super(context);
        m_BackGroundImage= BitmapFactory.decodeResource(getResources(), R.drawable.background);
        m_WindowImage=BitmapFactory.decodeResource(getResources(),R.drawable.empty_window);
        m_Criminal1Image=BitmapFactory.decodeResource(getResources(),R.drawable.criminal1_window);
        m_Criminal2Image=BitmapFactory.decodeResource(getResources(),R.drawable.criminal2_window);
        m_Criminal3Image=BitmapFactory.decodeResource(getResources(),R.drawable.criminal3_window);
        m_HostageImage=BitmapFactory.decodeResource(getResources(),R.drawable.hostage_window);
        m_StartButtonImage=BitmapFactory.decodeResource(getResources(),R.drawable.startbutton);
        m_GuideImage=BitmapFactory.decodeResource(getResources(),R.drawable.guide);
        m_HeartImage=BitmapFactory.decodeResource(getResources(),R.drawable.heartimg);
        m_GameOverImage=BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
        m_BulletHoleImage=BitmapFactory.decodeResource(getResources(),R.drawable.bullethole);

        gun_effect=MediaPlayer.create(context,R.raw.guneffect);
        hostage_effect=MediaPlayer.create(context,R.raw.hostagesound);

        m_font=Typeface.createFromAsset(getResources().getAssets(),"font1.TTF");

        m_Window=new Window[Window_WidthCount][Window_HeightCount];
        for(int i=0;i<Window_HeightCount;i++)
        {
            for(int j=0;j<Window_WidthCount;j++)
            {
                m_Window[j][i]=new Window(Window.HIDE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.w("KeyCode : ",Integer.toString(keyCode));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap resize_bitmap = Bitmap.createScaledBitmap(m_BackGroundImage,canvas.getWidth(),canvas.getHeight(),true);
        canvas.drawBitmap(resize_bitmap,0,0,null);
        CanvasWidth=canvas.getWidth();
        CanvasHeight=canvas.getHeight();
        if(m_state==STATE_READY)
        {
            if(startcountdown==0)
            {
                canvas.drawBitmap(m_GuideImage, canvas.getWidth()/2-m_GuideImage.getWidth()/2,(canvas.getHeight()/2-m_GuideImage.getHeight()/2)-100, null);
                canvas.drawBitmap(m_StartButtonImage, canvas.getWidth()/2-m_StartButtonImage.getWidth()/2,(canvas.getHeight()/2-m_StartButtonImage.getHeight()/2)+450, null);
            }
            else
            {
                Paint p = new Paint();
                p.setTypeface(m_font);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(150);
                p.setColor(Color.BLACK);
                canvas.drawText(Integer.toString(startcountdown),canvas.getWidth()/2,canvas.getHeight()/2,p);
            }
        }
        else if(m_state==STATE_GAME)
        {
            Paint p = new Paint();
            p.setTypeface(m_font);
            p.setTextAlign(Paint.Align.LEFT);
            p.setTextSize(70);
            p.setColor(Color.BLACK);
            canvas.drawText("CATCH : "+Integer.toString(CatchedCount),50,220,p);

            for(int n=0; n<HeartCount;n++)
            {
                canvas.drawBitmap(m_HeartImage, 640+(n*m_HeartImage.getWidth())+10, 160, null);
            }
            for (int i = 0; i < Window_HeightCount; i++) {
                for (int j = 0; j < Window_WidthCount; j++) {
                    if (m_Window[j][i].m_state == Window.SHOW) {
                        switch (m_Window[j][i].m_target) {
                            case Window.CRIMINAL1:
                                canvas.drawBitmap(m_Criminal1Image, Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310, null);
                                break;
                            case Window.CRIMINAL2:
                                canvas.drawBitmap(m_Criminal2Image, Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310, null);
                                break;
                            case Window.CRIMINAL3:
                                canvas.drawBitmap(m_Criminal3Image, Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310, null);
                                break;
                            case Window.HOTSAGE:
                                canvas.drawBitmap(m_HostageImage, Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310, null);
                                break;
                        }
                    } else {
                        canvas.drawBitmap(m_WindowImage, Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310, null);
                    }
                }
            }
        }
        else
        {
            canvas.drawBitmap(m_GameOverImage, canvas.getWidth()/2-m_GameOverImage.getWidth()/2,canvas.getHeight()/2-m_GameOverImage.getHeight()/2, null);
            canvas.drawBitmap(m_BulletHoleImage, 100,500, null);
            canvas.drawBitmap(m_BulletHoleImage, 410,1050, null);
            canvas.drawBitmap(m_BulletHoleImage, 610,360, null);Paint p = new Paint();
            p.setTypeface(m_font);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(70);
            p.setColor(Color.BLACK);
            canvas.drawText("YOU CATCHED : "+Integer.toString(CatchedCount),canvas.getWidth()/2,canvas.getHeight()/2+200,p);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(m_state==STATE_READY)
        {
            int px = (int) event.getX();
            int py = (int) event.getY();
            Rect box_window=new Rect(CanvasWidth/2-m_StartButtonImage.getWidth()/2,(CanvasHeight/2-m_StartButtonImage.getHeight()/2)+500,CanvasWidth/2+m_StartButtonImage.getWidth()/2,(CanvasHeight/2+m_StartButtonImage.getHeight()/2)+500);
            if(box_window.contains(px,py))
            {
                StartGame();
            }
        }
        else if(m_state==STATE_GAME&&HeartCount>0)
        {
            int px = (int) event.getX();
            int py = (int) event.getY();

            for(int i=0;i<Window_HeightCount;i++) {
                for (int j = 0; j < Window_WidthCount; j++) {
                    if (m_Window[j][i].m_state == Window.SHOW) {
                        Rect box_window=new Rect(Window_LeftStartPoint + j * 500, Window_TopStartPoint + i * 310,Window_LeftStartPoint + j * 500+m_WindowImage.getWidth(), Window_TopStartPoint + i * 310+m_WindowImage.getHeight());
                        if(box_window.contains(px,py))
                        {
                            if(m_Window[j][i].m_state==Window.SHOW)
                            {
                                if(m_Window[j][i].m_target==Window.HOTSAGE)
                                {
                                    hostage_effect.start();
                                    HeartCount--;
                                    m_Window[j][i].Hide();

                                }
                                else
                                {
                                    gun_effect.start();
                                    CatchedCount++;
                                    m_Window[j][i].Hide();
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(m_state==STATE_END)
        {
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {

            }
            CatchedCount=0;
            HeartCount=5;
            m_state=STATE_READY;
            _thread.interrupt();
        }

        invalidate();
        return true;
    }

    public void StartGame()
    {
        startcountdown=3;
        _thread.Showing_Time=1000;
        if(!_thread.playing)
            _thread.start();
    }
    public void ShowTarget()
    {
        if(HeartCount==0)
        {
            //_thread.playing=false;
            m_state=STATE_WAIT;
            try{
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {

            }
            m_state=STATE_END;
        }

        if(startcountdown>0) {
            startcountdown--;
            if (startcountdown == 0)
                m_state = STATE_GAME;
        }
        while(m_state==STATE_GAME) {
            Random ran = new Random();
            int x = ran.nextInt(Window_WidthCount);
            int y = ran.nextInt(Window_HeightCount);
            int targetNum = ran.nextInt(4) + 1; // 1~3 : 범인 , 4 : 인질

            if (m_Window[x][y].m_state == Window.HIDE) {
                m_Window[x][y].m_state = Window.SHOW;
                m_Window[x][y].m_target = targetNum;
                WindowsThread w_Thread = new WindowsThread(m_Window[x][y], (ran.nextInt(3) + 1)*1000);
                w_Thread.start();
                break;
            }
        }
        postInvalidate();

    }
}
