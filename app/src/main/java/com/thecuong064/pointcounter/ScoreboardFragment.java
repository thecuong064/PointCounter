package com.thecuong064.pointcounter;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thecuong064.pointcounter.base.BaseFragment;
import com.thecuong064.pointcounter.dialog.CustomAlertDialog;
import com.thecuong064.pointcounter.listener.OnClickListenerWithSound;
import com.thecuong064.pointcounter.listener.TimerListener;
import com.thecuong064.pointcounter.timer.MyCountDownTimer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScoreboardFragment extends BaseFragment {

    @BindView(R.id.tv_home_name) TextView homeNameTextView;
    @BindView(R.id.tv_away_name) TextView awayNameTextView;

    @BindView(R.id.tv_home_point) TextView homePointTextView;
    @BindView(R.id.tv_away_point) TextView awayPointTextView;

    @BindView(R.id.btn_home_dec) TextView homePointDecButton;
    @BindView(R.id.btn_home_inc) TextView homePointIncButton;
    @BindView(R.id.btn_away_dec) TextView awayPointDecButton;
    @BindView(R.id.btn_away_inc) TextView awayPointIncButton;

    @BindView(R.id.view_home_fouls) View homeFoulsView;
    @BindView(R.id.view_home_fouls_1) View homeFouls1;
    @BindView(R.id.view_home_fouls_2) View homeFouls2;
    @BindView(R.id.view_home_fouls_3) View homeFouls3;
    @BindView(R.id.view_home_fouls_4) View homeFouls4;
    @BindView(R.id.view_home_fouls_5) View homeFouls5;

    @BindView(R.id.view_away_fouls) View awayFoulsView;
    @BindView(R.id.view_away_fouls_1) View awayFouls1;
    @BindView(R.id.view_away_fouls_2) View awayFouls2;
    @BindView(R.id.view_away_fouls_3) View awayFouls3;
    @BindView(R.id.view_away_fouls_4) View awayFouls4;
    @BindView(R.id.view_away_fouls_5) View awayFouls5;

    @BindView(R.id.tv_total_time) TextView totalTimeTextView;
    @BindView(R.id.tv_shot_clock_time) TextView shotClockTimeTextView;

    @BindView(R.id.btn_play_pause) TextView playPauseButton;
    @BindView(R.id.btn_reset_timers) TextView resetTimersButton;
    @BindView(R.id.shot_clock_short) TextView shortShotClockButton;
    @BindView(R.id.shot_clock_long) TextView longShotClockButton;
    @BindView(R.id.btn_time_out) TextView timeOutButton;
    @BindView(R.id.btn_reset_points) TextView resetPointsButton;
    @BindView(R.id.btn_short_break) TextView shortBreakButton;
    @BindView(R.id.btn_long_break) TextView longBreakButton;

    private final String HOME_NAME = "HOME";
    private final String AWAY_NAME = "AWAY";
    private final String PLAY_STATE = "PLAY";
    private final String PAUSE_STATE = "PAUSE";
    private String SHOT_CLOCK_STATE = PAUSE_STATE;
    private String GAME_CLOCK_STATE = PAUSE_STATE;
    private String TIME_OUT_STATE = PAUSE_STATE;
    private String SHORT_BREAK_STATE = PAUSE_STATE;
    private String LONG_BREAK_STATE = PAUSE_STATE;

    List<View> homeFoulsViews = new ArrayList<>();
    List<View> awayFoulsViews = new ArrayList<>();

    int homePointValue, awayPointValue;
    int homeFoulCount, awayFoulCount;
    long totalMillis, shotClockMillis, timeOutMillis, shortBreakMillis, longBreakMillis;

    MyCountDownTimer totalCountDownTimer, shotClockCountDownTimer, timeOutCountDownTimer;
    MyCountDownTimer shortBreakCountDownTimer, longBreakCountDownTimer;

    MediaPlayer shotClockViolationSound;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_scoreboard_smaller;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        homePointValue = 0;
        awayPointValue = 0;
        homeFoulCount = 0;
        awayFoulCount = 0;

        shotClockViolationSound = MediaPlayer.create(getContext(),R.raw.shot_clock_violation_buzzer);

        initTimersView();
        initView();

        initViewOnClick();
    }

    public void initTimersView() {
        shortShotClockButton.setText(MainActivity.afterReboundTimeInSecond + "");
        longShotClockButton.setText(MainActivity.offenseTimeInSecond + "");
        initTotalTimer();
        initShotClockTimer(MainActivity.offenseTimeInSecond);
        initTimeOutTimer();
        initShortBreakTimer();
        initLongBreakTimer();
        playPauseButton.setText(PLAY_STATE);
    }

    private void initShotClockTimer(int timeInSeconds) {
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
                if (MainActivity.isGameClockStoppedWhenShotClockExpires) {
                    pauseGameClock();
                }
                initShotClockTimer(MainActivity.offenseTimeInSecond);
            }
        });
    }

    private void initTotalTimer() {
        stopTotalTimer();
        totalMillis = MainActivity.millisFromMinute(MainActivity.totalTimeMinute)
                    + MainActivity.millisFromSecond(MainActivity.totalTimeSecond);
        totalTimeTextView.setText(getMinuteSecondTimeStringFromMillis(totalMillis, 59));
        totalCountDownTimer = new MyCountDownTimer(totalMillis);
        totalCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getMinuteSecondTimeStringFromMillis(millisUntilFinished, 59);
                totalTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                totalTimeTextView.setText("00 : 00");
            }
        });
    }

    private void initTimeOutTimer() {
        stopTimeOutTimer();
        TIME_OUT_STATE = PAUSE_STATE;
        timeOutMillis = MainActivity.millisFromMinute(MainActivity.timeOutMinute)
                + MainActivity.millisFromSecond(MainActivity.timeOutSecond);
        timeOutCountDownTimer = new MyCountDownTimer(timeOutMillis);
        timeOutCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getMinuteSecondTimeStringFromMillis(millisUntilFinished, 15);
                shotClockTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                shotClockTimeTextView.setText("00 : 00");
                playStopTimeOutTimer();
            }
        });
    }

    private void initShortBreakTimer() {
        stopShortBreakTimer();
        SHORT_BREAK_STATE = PAUSE_STATE;
        shortBreakMillis = MainActivity.millisFromMinute(MainActivity.shortBreakMinute)
                + MainActivity.millisFromSecond(MainActivity.shortBreakSecond);
        shortBreakCountDownTimer = new MyCountDownTimer(shortBreakMillis);
        shortBreakCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getMinuteSecondTimeStringFromMillis(millisUntilFinished, 15);
                shotClockTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                shotClockTimeTextView.setText("00 : 00");
                playStopShortBreakTimer();
            }
        });
    }
    
    private void initLongBreakTimer() {
        stopLongBreakTimer();
        LONG_BREAK_STATE = PAUSE_STATE;
        longBreakMillis = MainActivity.millisFromMinute(MainActivity.longBreakMinute)
                + MainActivity.millisFromSecond(MainActivity.longBreakSecond);
        longBreakCountDownTimer = new MyCountDownTimer(longBreakMillis);
        longBreakCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getMinuteSecondTimeStringFromMillis(millisUntilFinished, 15);
                shotClockTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                shotClockTimeTextView.setText("00 : 00");
                playStopLongBreakTimer();
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

    private void initView() {
        homeFoulsViews.add(homeFouls1);
        homeFoulsViews.add(homeFouls2);
        homeFoulsViews.add(homeFouls3);
        homeFoulsViews.add(homeFouls4);
        homeFoulsViews.add(homeFouls5);

        awayFoulsViews.add(awayFouls1);
        awayFoulsViews.add(awayFouls2);
        awayFoulsViews.add(awayFouls3);
        awayFoulsViews.add(awayFouls4);
        awayFoulsViews.add(awayFouls5);
    }

    private void initViewOnClick() {

        homePointDecButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                pointButtonOnClick(v);
            }
        });

        homePointIncButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                pointButtonOnClick(v);
            }
        });

        awayPointDecButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                pointButtonOnClick(v);
            }
        });

        awayPointIncButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                pointButtonOnClick(v);
            }
        });

        homeFoulsView.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                homeFoulCountChanged();
            }
        });

        awayFoulsView.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                awayFoulCountChanged();
            }
        });

        playPauseButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playPauseAllTimers();
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

        timeOutButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playStopTimeOutTimer();
            }
        });

        shortBreakButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playStopShortBreakTimer();
            }
        });

        longBreakButton.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playStopLongBreakTimer();
            }
        });

        shotClockTimeTextView.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playPauseShotClockTimer();
            }
        });

        totalTimeTextView.setOnClickListener(new OnClickListenerWithSound() {
            @Override
            public void onClickWithSound(View v) {
                playPauseGameClockTimer();
            }
        });

        homeNameTextView.setOnClickListener(v -> openDialogEditTeamName(HOME_NAME));
        awayNameTextView.setOnClickListener(v -> openDialogEditTeamName(AWAY_NAME));
        resetTimersButton.setOnClickListener(v -> showTimerResetConfirmationDialog());
        resetPointsButton.setOnClickListener(v -> showPointsResetConfirmationDialog());
    }

    private void showTimerResetConfirmationDialog() {
        new CustomAlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to reset all timers?")
                .setPositiveButton("OK", (dialog, which) -> {
                    stopAndResetTimers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPointsResetConfirmationDialog() {
        new CustomAlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to reset all the points to 0?")
                .setPositiveButton("OK", (dialog, which) -> {
                    resetPoints();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetPoints() {
        homePointValue = 0;
        awayPointValue = 0;
        updatePoints();
    }

    private void resetShotClock(int timeInSeconds) {
        initShotClockTimer(timeInSeconds);
        // TODO: change the state to enum
        if (playPauseButton.getText().toString().equals(PAUSE_STATE)) {
            shotClockCountDownTimer.play();
        }
    }

    private void playPauseShotClockTimer() {
        if (SHOT_CLOCK_STATE.equals(PLAY_STATE)) {
            pauseShotClock();
        } else {
            playGameClock();
            playShotClock();
        }
    }

    private void playShotClock() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.play();
            SHOT_CLOCK_STATE = PLAY_STATE;
        }
    }

    private void pauseShotClock() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.pause();
            SHOT_CLOCK_STATE = PAUSE_STATE;
        }
    }

    private void playPauseAllTimers() {
        // TODO: change the state to enum
        if (playPauseButton.getText().toString().equals(PLAY_STATE)) {
            playGameClock();
            playShotClock();
            playPauseButton.setText(PAUSE_STATE);
        } else {
            pauseGameClock();
            pauseShotClock();
            playPauseButton.setText(PLAY_STATE);
        }
    }

    private void playPauseGameClockTimer() {
        if (GAME_CLOCK_STATE.equals(PLAY_STATE)) {
            pauseGameClock();
        } else {
            playGameClock();
        }
    }

    private void playGameClock() {
        if (totalCountDownTimer != null) {
            totalCountDownTimer.play();
            GAME_CLOCK_STATE = PLAY_STATE;
        }
    }

    private void pauseGameClock() {
        if (totalCountDownTimer != null) {
            totalCountDownTimer.pause();
            GAME_CLOCK_STATE = PAUSE_STATE;
        }
    }
    
    private void playStopTimeOutTimer() {
        // TODO: change the state to enum
        if (TIME_OUT_STATE.equals(PAUSE_STATE)) {
            pauseGameClock();
            stopShotClockTimer();
            playTimeOutTimer();
            disableScoreboardLayout();
            timeOutButton.setActivated(true);
        } else {
            timeOutButton.setActivated(false);
            stopTimeOutTimer();
            initTimeOutTimer();
            initShotClockTimer(MainActivity.offenseTimeInSecond);
            enableScoreboardLayout();
        }
    }

    private void playTimeOutTimer() {
        if (timeOutCountDownTimer != null) {
            timeOutCountDownTimer.play();
            TIME_OUT_STATE = PLAY_STATE;
        }
    }

    private void stopTimeOutTimer() {
        if (timeOutCountDownTimer != null) {
            timeOutCountDownTimer.stop();
            TIME_OUT_STATE = PAUSE_STATE;
        }
    }

    private void stopTotalTimer() {
        if (totalCountDownTimer != null) {
            totalCountDownTimer.stop();
        }
    }

    private void stopShotClockTimer() {
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.stop();
        }
    }

    private void playShortBreakTimer() {
        if (shortBreakCountDownTimer != null) {
            shortBreakCountDownTimer.play();
            SHORT_BREAK_STATE = PLAY_STATE;
        }
    }

    private void stopShortBreakTimer() {
        if (shortBreakCountDownTimer != null) {
            shortBreakCountDownTimer.stop();
            SHORT_BREAK_STATE = PAUSE_STATE;
        }
    }

    private void playStopShortBreakTimer() {
        // TODO: change the state to enum
        if (SHORT_BREAK_STATE.equals(PAUSE_STATE)) {
            pauseGameClock();
            stopShotClockTimer();
            playShortBreakTimer();
            disableScoreboardLayout();
            shortBreakButton.setActivated(true);
        } else {
            shortBreakButton.setActivated(false);
            stopShortBreakTimer();
            initShortBreakTimer();
            initShotClockTimer(MainActivity.offenseTimeInSecond);
            enableScoreboardLayout();
        }
    }

    private void playLongBreakTimer() {
        if (longBreakCountDownTimer != null) {
            longBreakCountDownTimer.play();
            LONG_BREAK_STATE = PLAY_STATE;
        }
    }

    private void stopLongBreakTimer() {
        if (longBreakCountDownTimer != null) {
            longBreakCountDownTimer.stop();
            LONG_BREAK_STATE = PAUSE_STATE;
        }
    }

    private void playStopLongBreakTimer() {
        // TODO: change the state to enum
        if (LONG_BREAK_STATE.equals(PAUSE_STATE)) {
            pauseGameClock();
            stopShotClockTimer();
            playLongBreakTimer();
            disableScoreboardLayout();
            longBreakButton.setActivated(true);
        } else {
            longBreakButton.setActivated(false);
            stopLongBreakTimer();
            initLongBreakTimer();
            initShotClockTimer(MainActivity.offenseTimeInSecond);
            enableScoreboardLayout();
        }
    }

    private void disableScoreboardLayout() {
        shotClockTimeTextView.setActivated(true);
        shotClockTimeTextView.setEnabled(false);
        totalTimeTextView.setEnabled(false);
        homePointIncButton.setEnabled(false);
        homePointDecButton.setEnabled(false);
        awayPointIncButton.setEnabled(false);
        awayPointDecButton.setEnabled(false);
        homeFoulsView.setEnabled(false);
        awayFoulsView.setEnabled(false);
        playPauseButton.setEnabled(false);
        resetTimersButton.setEnabled(false);
        shortShotClockButton.setEnabled(false);
        longShotClockButton.setEnabled(false);
        resetPointsButton.setEnabled(false);
        timeOutButton.setEnabled(TIME_OUT_STATE.equals(PLAY_STATE));
        shortBreakButton.setEnabled(SHORT_BREAK_STATE.equals(PLAY_STATE));
        longBreakButton.setEnabled(LONG_BREAK_STATE.equals(PLAY_STATE));
    }

    private void enableScoreboardLayout() {
        shotClockTimeTextView.setActivated(false);
        shotClockTimeTextView.setEnabled(true);
        totalTimeTextView.setEnabled(true);
        homePointIncButton.setEnabled(true);
        homePointDecButton.setEnabled(true);
        awayPointIncButton.setEnabled(true);
        awayPointDecButton.setEnabled(true);
        homeFoulsView.setEnabled(true);
        awayFoulsView.setEnabled(true);
        playPauseButton.setEnabled(true);
        resetTimersButton.setEnabled(true);
        shortShotClockButton.setEnabled(true);
        longShotClockButton.setEnabled(true);
        resetPointsButton.setEnabled(true);
        timeOutButton.setEnabled(true);
        shortBreakButton.setEnabled(true);
        longBreakButton.setEnabled(true);
    }

    public void stopAndResetTimers() {
        stopTotalTimer();
        stopShotClockTimer();
        initTimersView();
    }

    private void pointButtonOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home_inc:
                homePointValue++;
                break;
            case R.id.btn_home_dec:
                homePointValue--;
                break;
            case R.id.btn_away_inc:
                awayPointValue++;
                break;
            case R.id.btn_away_dec:
                awayPointValue--;
                break;
        }

        if (homePointValue < 0) homePointValue = 0;
        if (awayPointValue < 0) awayPointValue = 0;

        updatePoints();
    }

    private void updatePoints() {
        // --------------------HOME--------------
        homePointTextView.setText(homePointValue + "");
        homePointDecButton.setEnabled(homePointValue > 0);

        // --------------------AWAY--------------
        awayPointTextView.setText(awayPointValue + "");
        awayPointDecButton.setEnabled(awayPointValue > 0);
    }

    private void openDialogEditTeamName(final String team) {
        final View customLayout = getLayoutInflater().inflate(R.layout.input_dialog, null);
        new CustomAlertDialog.Builder(getContext())
                .setMessage(team)
                .setView(customLayout)
                .setPositiveButton("OK", (dialog, which) -> {
                    EditText editText = customLayout.findViewById(R.id.editText);
                    changeTeamName(team, editText.getText().toString());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changeTeamName(String team, String newName) {
        if (newName.isEmpty()) return;
        if (team.equals(HOME_NAME)) {
            homeNameTextView.setText(newName.toUpperCase());
        } else {
            awayNameTextView.setText(newName.toUpperCase());
        }
    }

    private void homeFoulCountChanged() {
        homeFoulCount++;
        if (homeFoulCount > 5) homeFoulCount = 0;
        updateHomeFouls();
    }

    private void updateHomeFouls() {
        for (int index = 0; index < 5; index ++) {
            if (index <= homeFoulCount - 1) {
                homeFoulsViews.get(index).setVisibility(View.VISIBLE);
            } else {
                homeFoulsViews.get(index).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void awayFoulCountChanged() {
        awayFoulCount++;
        if (awayFoulCount > 5) awayFoulCount = 0;
        updateAwayFouls();
    }

    private void updateAwayFouls() {
        for (int index = 0; index < 5; index ++) {
            if (index <= awayFoulCount - 1) {
                awayFoulsViews.get(index).setVisibility(View.VISIBLE);
            } else {
                awayFoulsViews.get(index).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void pauseAllTimers() {
        pauseGameClock();
        pauseShotClock();
    }
}