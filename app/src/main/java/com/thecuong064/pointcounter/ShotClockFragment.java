package com.thecuong064.pointcounter;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;

import com.thecuong064.pointcounter.base.BaseFragment;
import com.thecuong064.pointcounter.listener.OnClickListenerWithSound;
import com.thecuong064.pointcounter.listener.TimerListener;
import com.thecuong064.pointcounter.timer.MyCountDownTimer;

import butterknife.BindView;

public class ShotClockFragment extends BaseFragment {

    @BindView(R.id.tv_shot_clock_time) TextView shotClockTimeTextView;

    @BindView(R.id.btn_play_pause) TextView playPauseButton;
    @BindView(R.id.shot_clock_short) TextView shortShotClockButton;
    @BindView(R.id.shot_clock_long) TextView longShotClockButton;

    private final String PLAY_STATE = "PLAY";
    private final String PAUSE_STATE = "PAUSE";
    private String SHOT_CLOCK_STATE = PAUSE_STATE;

    long shotClockMillis;

    MyCountDownTimer shotClockCountDownTimer;

    MediaPlayer shotClockViolationSound;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_shot_clock;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        shotClockViolationSound = MediaPlayer.create(getContext(),R.raw.shot_clock_violation_buzzer);

        initShotClockTimer(MainActivity.offenseTimeInSecond);
        playPauseButton.setText(PLAY_STATE);

        initViewOnClick();
    }

    private void initShotClockTimer(int timeInSeconds) {
        shortShotClockButton.setText(MainActivity.afterReboundTimeInSecond + "");
        longShotClockButton.setText(MainActivity.offenseTimeInSecond + "");
        stopShotClockTimer();
        SHOT_CLOCK_STATE = PAUSE_STATE;
        shotClockMillis = MainActivity.millisFromSecond(timeInSeconds);
        shotClockTimeTextView.setText(getSecondTimeStringFromMillis(shotClockMillis, 10));
        shotClockCountDownTimer = new MyCountDownTimer(shotClockMillis);
        shotClockCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getSecondTimeStringFromMillis(millisUntilFinished, 10);
                shotClockTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                shotClockTimeTextView.setText("0.0");
                shotClockViolationSound.start();
                initShotClockTimer(MainActivity.offenseTimeInSecond);
            }
        });
    }

    private String getSecondTimeStringFromMillis(long millisUntilFinished, int secondToDisplayDecimalPlaces) {
        long timeInSeconds = millisUntilFinished/1000;
        long seconds = timeInSeconds%60 + (timeInSeconds/60)*60;
        long tick = (millisUntilFinished - seconds*1000)/100;
        return seconds <= secondToDisplayDecimalPlaces
                ? getNumberStringWithTwoDigit(seconds) + "." + tick
                : getNumberStringWithTwoDigit(seconds);
    }

    private String getMinuteSecondTimeStringFromMillis(long millisUntilFinished, int secondToDisplayDecimalPlaces) {
        long timeInSeconds = millisUntilFinished/1000;
        long seconds = timeInSeconds%60;
        long minutes = timeInSeconds/60;
        String secondStr = getNumberStringWithTwoDigit(seconds);
        String minuteStr = getNumberStringWithTwoDigit(minutes);
        if (minutes == 0) {
            long tick = (millisUntilFinished - seconds*1000)/100;
            return seconds <= secondToDisplayDecimalPlaces
                    ? getNumberStringWithTwoDigit(seconds) + "." + tick
                    : getNumberStringWithTwoDigit(seconds);
        }
        return minuteStr + " : " + secondStr;
    }

    private String getNumberStringWithTwoDigit(long num) {
        return (num > 9) ? "" + num : "0" + num;
    }

    private void initViewOnClick() {

        playPauseButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playPauseShotClockTimer();
            }
        });

        shortShotClockButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                resetShotClock(MainActivity.afterReboundTimeInSecond);
            }
        });

        longShotClockButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                resetShotClock(MainActivity.offenseTimeInSecond);
            }
        });

        shotClockTimeTextView.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playPauseShotClockTimer();
            }
        });
    }

    private void resetShotClock(int timeInSeconds) {
        initShotClockTimer(timeInSeconds);
        // TODO: change the state to enum
        if (playPauseButton.getText().toString().equals(PAUSE_STATE)) {
            playShotClock();
        }
    }

    private void playPauseShotClockTimer() {
        playPauseButton.setText(SHOT_CLOCK_STATE);
        if (SHOT_CLOCK_STATE.equals(PLAY_STATE)) {
            pauseShotClock();
        } else {
            playShotClock();
        }
    }

    private void playShotClock() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.play();
            shotClockTimeTextView.setActivated(true);
            SHOT_CLOCK_STATE = PLAY_STATE;
        }
    }

    private void pauseShotClock() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.pause();
            shotClockTimeTextView.setActivated(false);
            SHOT_CLOCK_STATE = PAUSE_STATE;
        }
    }


    private void stopShotClockTimer() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.stop();
        }
    }

    public void stopAndResetTimers() {
        initShotClockTimer(MainActivity.offenseTimeInSecond);
    }
}