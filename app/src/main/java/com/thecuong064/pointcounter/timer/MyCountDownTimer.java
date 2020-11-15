package com.thecuong064.pointcounter.timer;

import android.os.CountDownTimer;

import com.thecuong064.pointcounter.listener.TimerListener;

public class MyCountDownTimer {

    private TimerListener timerListener;
    private CountDownTimer timer;
    private long timeRemaining;

    public MyCountDownTimer(long milliseconds) {
        timeRemaining = milliseconds;
        timer = initNewTimer(milliseconds);
    }

    public void play() {
        if (timer != null){
            timer.start();
        }
    }

    public void pause() {
        if (timer != null){
            timer.cancel();
            timer = initNewTimer(timeRemaining);
        }
    }

    public void stop() {
        if (timer != null){
            timer.cancel();
        }
        timer = null;
    }

    public void setOnTickListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    private CountDownTimer initNewTimer(long milliseconds) {
        if (timer != null){
            timer.cancel();
        }
        return new CountDownTimer(milliseconds, 100) {

            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                if (timerListener != null) {
                    timerListener.onTick(millisUntilFinished);
                }
            }

            public void onFinish() {
                timeRemaining = 0;
                if (timerListener != null) {
                    timerListener.onFinish();
                }
            }
        };
    }
}
