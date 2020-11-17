package com.thecuong064.pointcounter;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.thecuong064.pointcounter.base.BaseFragment;
import com.thecuong064.pointcounter.listener.DoubleClickListener;
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
    @BindView(R.id.btn_stop) TextView stopButton;
    @BindView(R.id.shot_clock_short) TextView shortShotClockButton;
    @BindView(R.id.shot_clock_long) TextView longShotClockButton;

    private final String HOME_NAME = "HOME_NAME";
    private final String AWAY_NAME = "AWAY_NAME";
    private final String PLAY_STATE = "PLAY";
    private final String PAUSE_STATE = "PAUSE";
    private String SHOT_CLOCK_STATE = PAUSE_STATE;

    List<View> homeFoulsViews = new ArrayList<>();
    List<View> awayFoulsViews = new ArrayList<>();

    int homePointValue, awayPointValue;
    int homeFoulCount, awayFoulCount;
    long totalMillis, shotClockMillis;

    MyCountDownTimer totalCountDownTimer, shotClockCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_scoreboard;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        homePointValue = 0;
        awayPointValue = 0;
        homeFoulCount = 0;
        awayFoulCount = 0;

        initTimersView();
        initView();

        initViewOnClick();
    }

    public void initTimersView() {
        shortShotClockButton.setText(MainActivity.afterReboundingTimeInSecond + "");
        longShotClockButton.setText(MainActivity.offenseTimeInSecond + "");
        initTotalTimer();
        initShotClockTimer(MainActivity.offenseTimeInSecond);
        playPauseButton.setText(PLAY_STATE);
    }

    private void initShotClockTimer(int timeInSeconds) {
        SHOT_CLOCK_STATE = PAUSE_STATE;
        if (shotClockCountDownTimer != null) {
            shotClockCountDownTimer.stop();
        }
        shotClockMillis = MainActivity.millisFromSecond(timeInSeconds);
        shotClockTimeTextView.setText(getShotClockTimeStringFromMillis(shotClockMillis));
        shotClockCountDownTimer = new MyCountDownTimer(shotClockMillis);
        shotClockCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getShotClockTimeStringFromMillis(millisUntilFinished);
                shotClockTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                shotClockTimeTextView.setText("0.0");
                pauseTotalTimer();
                shotClockCountDownTimer.stop();
                initShotClockTimer(MainActivity.offenseTimeInSecond);
            }
        });
    }

    private void initTotalTimer() {
        if (totalCountDownTimer != null) {
            totalCountDownTimer.stop();
        }
        totalMillis = MainActivity.millisFromMinute(MainActivity.totalTimeMinute)
                    + MainActivity.millisFromSecond(MainActivity.totalTimeSecond);
        totalTimeTextView.setText(getTotalTimeStringFromMillis(totalMillis));
        totalCountDownTimer = new MyCountDownTimer(totalMillis);
        totalCountDownTimer.setOnTickListener(new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = getTotalTimeStringFromMillis(millisUntilFinished);
                totalTimeTextView.setText(currentTime);
            }

            @Override
            public void onFinish() {
                totalTimeTextView.setText("00 : 00");
                totalCountDownTimer.stop();
            }
        });
    }

    private String getShotClockTimeStringFromMillis(long millisUntilFinished) {
        long timeInSeconds = millisUntilFinished/1000;
        long seconds = timeInSeconds%60;
        if (seconds > 5) {
            return getStringWithTwoNumber(seconds);
        }
        long tick = (millisUntilFinished - seconds*1000)/100;
        return getStringWithTwoNumber(seconds) + "." + tick;
    }

    private String getTotalTimeStringFromMillis(long millisUntilFinished) {
        long timeInSeconds = millisUntilFinished/1000;
        long seconds = timeInSeconds%60;
        long minutes = timeInSeconds/60;
        String secondStr = getStringWithTwoNumber(seconds);
        String minuteStr = getStringWithTwoNumber(minutes);
        if (minutes == 0) {
            long tick = (millisUntilFinished - seconds*1000)/100;
            return getStringWithTwoNumber(seconds) + "." + tick;
        }
        return minuteStr + " : " + secondStr;
    }

    private String getStringWithTwoNumber(long num) {
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

        homePointDecButton.setOnClickListener(v -> pointButtonOnClick(v));

        homePointIncButton.setOnClickListener(v -> pointButtonOnClick(v));

        awayPointDecButton.setOnClickListener(v -> pointButtonOnClick(v));

        awayPointIncButton.setOnClickListener(v -> pointButtonOnClick(v));

        homeNameTextView.setOnClickListener(v -> openDialogEditTeamName(HOME_NAME));

        awayNameTextView.setOnClickListener(v -> openDialogEditTeamName(AWAY_NAME));

        homeFoulsView.setOnClickListener(v -> homeFoulCountChanged());

        awayFoulsView.setOnClickListener(v -> awayFoulCountChanged());

        playPauseButton.setOnClickListener(v -> playPauseTotalTimer());

        totalTimeTextView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                //initTotalTimer();
            }
        });

        //shotClockTimeTextView.setOnClickListener(v -> playPauseShotClockTimer());

        stopButton.setOnClickListener(v -> stopTimer());

        shortShotClockButton.setOnClickListener(v -> resetShotClock(MainActivity.afterReboundingTimeInSecond));
        longShotClockButton.setOnClickListener(v -> resetShotClock(MainActivity.offenseTimeInSecond));
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
            pauseTotalTimer();
            pauseShotClock();
        } else {
            playTotalTimer();
            playShotClock();
        }
    }

    private void playShotClock() {
        shotClockCountDownTimer.play();
        SHOT_CLOCK_STATE = PLAY_STATE;
    }

    private void pauseShotClock() {
        shotClockCountDownTimer.pause();
        SHOT_CLOCK_STATE = PAUSE_STATE;
    }

    private void playPauseTotalTimer() {
        // TODO: change the state to enum
        if (playPauseButton.getText().toString().equals(PLAY_STATE)) {
            playTotalTimer();
            playShotClock();
        } else {
            pauseTotalTimer();
            pauseShotClock();
        }
    }

    private void playTotalTimer() {
        totalCountDownTimer.play();
        playPauseButton.setText(PAUSE_STATE);
    }

    private void pauseTotalTimer() {
        totalCountDownTimer.pause();
        playPauseButton.setText(PLAY_STATE);
    }

    private void stopTimer() {
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

        // --------------------AWAY--------------
        awayPointTextView.setText(awayPointValue + "");
    }

    private void openDialogEditTeamName(final String team) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Team name");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.input_dialog, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.editText);
                changeTeamName(team, editText.getText().toString());
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
        pauseTotalTimer();
        pauseShotClock();
    }

}