package com.thecuong064.pointcounter.listener;

public interface TimerListener {
    void onTick(long millisUntilFinished);
    void onFinish();
}
