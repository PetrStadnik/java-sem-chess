package com.company.classes;

import com.company.gui.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StopWatch implements Runnable
{
    private long offset, currentStart;
    private boolean isStopped;
    public long timeLimit;
    public boolean outOfTime;
    Thread th;
    public JLabel textArea;

    public GameWindow g;
    public int c;

    public StopWatch(long timeLimit, JLabel textArea, GameWindow g, int c) {
        this.textArea = textArea;
        this.timeLimit = 0L + timeLimit;
        //System.out.println(this.timeLimit);
        outOfTime = false;
        offset = 0L;
        currentStart = System.currentTimeMillis();
        isStopped = true;

        this.g = g;
        this.c = c;
    }

    public void start()
    {
        if(isStopped)
        {
            th = new Thread(this);
            th.start();
            currentStart = System.currentTimeMillis() - offset;
        }

        isStopped = false;
    }

    public void stop()
    {
        if(!isStopped)
        {
            th = null;
            offset = System.currentTimeMillis() - currentStart;
        }
        isStopped = true;
    }

    public String getTime()
    {
        long mstime;
        if(!isStopped)
            mstime = timeLimit - System.currentTimeMillis() + currentStart;
        else
            mstime = timeLimit - offset;

        if (mstime<= 0){
            outOfTime = true;
            stop();
            g.timeEnded(c);
            return "00:00:00";
        }
        //System.out.print(mstime);

        DateFormat simple = new SimpleDateFormat("HH:mm:ss");
        Date result = new Date(mstime);
        result.setTime(result.getTime()-3600*1000);
        return simple.format(result);
    }
    public boolean outOfTime(){
        return outOfTime;
    }

    @Override
    public void run() {
        while(!isStopped)
        {
            //System.out.print("\rTime: "+ getTime());

            try {
                Thread.sleep(10);
                textArea.setText(getTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
