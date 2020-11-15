package com.thecuong064.pointcounter;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.thecuong064.pointcounter.base.BaseFragment;

import butterknife.BindView;

public class ConfigurationsFragment extends BaseFragment {

    @BindView(R.id.et_total_time) TextInputEditText totalTimeEditText;
    @BindView(R.id.et_short_shot_clock_time) TextInputEditText shortShotClockTimeEditText;
    @BindView(R.id.et_long_shot_clock_time) TextInputEditText longShotClockTimeEditText;
    @BindView(R.id.btn_save) TextView saveButton;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_configurations;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        initViewOnClick();
        initTextWatcher();
        initData();
    }

    private void initViewOnClick() {
        saveButton.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String message = "Wanna save and reset the current scoreboard?";
        builder.setMessage(message)
                .setTitle("Warning");

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveConfigurations();
                changeToScoreboardTab();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeToScoreboardTab() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).changeTab(MainActivity.SCOREBOARD_TAB_INDEX);
        }
    }

    private void saveConfigurations() {
        if (getActivity() instanceof MainActivity) {
            long totalTime = Long.valueOf(totalTimeEditText.getText().toString());
            long shortTime = Long.valueOf(shortShotClockTimeEditText.getText().toString());
            long longTime = Long.valueOf(longShotClockTimeEditText.getText().toString());
            ((MainActivity) getActivity()).setConfiguration(totalTime, MainActivity.TOTAl_TIME_SAVE_KEY);
            ((MainActivity) getActivity()).setConfiguration(shortTime, MainActivity.SHORT_SHOT_CLOCK_TIME_SAVE_KEY);
            ((MainActivity) getActivity()).setConfiguration(longTime, MainActivity.LONG_SHOT_CLOCK_TIME_SAVE_KEY);
        }
    }

    private void initTextWatcher() {
        totalTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    long cur = Long.parseLong(s.toString());
                    if (cur > MainActivity.DEFAULT_TOTAL_TIME) {
                        totalTimeEditText.setText(String.valueOf(MainActivity.DEFAULT_TOTAL_TIME));
                        return;
                    }
                    if (cur < 1) {
                        totalTimeEditText.setText(String.valueOf(1));
                        return;
                    }
                }
            }
        });

        shortShotClockTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    long cur = Long.parseLong(s.toString());
                    if (cur > MainActivity.DEFAULT_SHORT_SHOT_CLOCK_TIME) {
                        shortShotClockTimeEditText.setText(String.valueOf(MainActivity.DEFAULT_SHORT_SHOT_CLOCK_TIME));
                        return;
                    }
                    if (cur < 1) {
                        shortShotClockTimeEditText.setText(String.valueOf(1));
                        return;
                    }
                }
            }
        });

        longShotClockTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    long cur = Long.parseLong(s.toString());
                    if (cur > MainActivity.DEFAULT_LONG_SHOT_CLOCK_TIME) {
                        longShotClockTimeEditText.setText(String.valueOf(MainActivity.DEFAULT_LONG_SHOT_CLOCK_TIME));
                        return;
                    }
                    if (cur < 1) {
                        longShotClockTimeEditText.setText(String.valueOf(1));
                        return;
                    }
                }
            }
        });
    }

    public void initData() {
        totalTimeEditText.setText(MainActivity.totalTimeInMinute + "");
        shortShotClockTimeEditText.setText(MainActivity.shortShotClockTimeInSecond + "");
        longShotClockTimeEditText.setText(MainActivity.longShotClockTimeInSecond + "");
    }

}