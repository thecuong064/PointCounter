package com.thecuong064.pointcounter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.thecuong064.pointcounter.base.BaseFragment;
import com.thecuong064.pointcounter.dialog.CustomAlertDialog;
import com.thecuong064.pointcounter.dialog.NumberPickerDialog;
import com.thecuong064.pointcounter.widget.ConfigurationItemView;
import com.thecuong064.pointcounter.widget.CustomNumberPicker;

import butterknife.BindView;

public class ConfigurationsFragment extends BaseFragment {

    @BindView(R.id.item_total_time) ConfigurationItemView totalTimeItem;
    @BindView(R.id.item_offense_time) ConfigurationItemView offenseTimeItem;
    @BindView(R.id.item_after_rebound_time) ConfigurationItemView afterReboundTimeItem;
    @BindView(R.id.item_time_out) ConfigurationItemView timeOutItem;
    @BindView(R.id.item_short_break) ConfigurationItemView shortBreakItem;
    @BindView(R.id.item_long_break) ConfigurationItemView longBreakItem;
    @BindView(R.id.item_shot_clock_violation) ConfigurationItemView shotClockViolationItem;

    @BindView(R.id.btn_save) TextView saveButton;

    public static final String TOTAl_TIME = "total_time";
    public static final String OFFENSE_TIME = "offense_time";
    public static final String AFTER_REBOUNDING_TIME = "after_rebound_time";
    public static final String TIME_OUT = "time_out";
    public static final String SHORT_BREAK = "short_break";
    public static final String LONG_BREAK = "long_break";

    private int totalTimeMinute;
    private int totalTimeSecond;

    private int afterReboundTimeInSecond;
    private int offenseTimeInSecond;

    private int timeOutMinute;
    private int timeOutSecond;

    public int shortBreakMinute;
    public int shortBreakSecond;
    public int longBreakMinute;
    public int longBreakSecond;

    public boolean isGameClockStoppedWhenShotClockExpires;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_configurations;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        initViewOnClick();
        initAndShowData();
    }

    private void initViewOnClick() {
        totalTimeItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Total time", TOTAl_TIME,
                        60, 0, totalTimeMinute,
                        59, 0, totalTimeSecond)
        );

        offenseTimeItem.setOnClickListener(v ->
                showSecondPickerDialog(OFFENSE_TIME,
                        "Offense time", 59, 1, offenseTimeInSecond)
        );

        afterReboundTimeItem.setOnClickListener(v ->
                showSecondPickerDialog(AFTER_REBOUNDING_TIME,
                        "After-offensive-rebound time", 59, 1, afterReboundTimeInSecond)
        );

        timeOutItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Time out", TIME_OUT,
                        20, 0, timeOutMinute,
                        59, 0, timeOutSecond)
        );

        shortBreakItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Short break", SHORT_BREAK,
                        20, 0, shortBreakMinute,
                        59, 0, shortBreakSecond)
        );

        longBreakItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Long break", LONG_BREAK,
                        20, 0, longBreakMinute,
                        59, 0, longBreakSecond)
        );

        shotClockViolationItem.setSwitchOnCheckedChangeListener((buttonView, isChecked) -> {
            isGameClockStoppedWhenShotClockExpires = isChecked;
            enableSaveButtonIfValuesChanged();
        });

        saveButton.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showMinuteAndSecondPickerDialog(String title, String type,
                                                 int minuteMax, int minuteMin, int currentMin,
                                                 int secondMax, int secondMin, int currentSec) {
        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.view_picker_minute_second, null);

        CustomNumberPicker minutePicker = getMinutePicker(customLayout, minuteMax, minuteMin, currentMin);
        CustomNumberPicker secondPicker = getSecondPicker(customLayout, secondMax, secondMin, currentSec);

        new NumberPickerDialog.Builder(getContext())
                .setView(customLayout)
                .setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (minutePicker.getValue() != 0 || secondPicker.getValue() != 0) {
                        switch (type) {
                            case ConfigurationsFragment.TOTAl_TIME:
                                totalTimeMinute = minutePicker.getValue();
                                totalTimeSecond = secondPicker.getValue();
                                break;
                            case TIME_OUT:
                                timeOutMinute = minutePicker.getValue();
                                timeOutSecond = secondPicker.getValue();
                                break;
                            case SHORT_BREAK:
                                shortBreakMinute = minutePicker.getValue();
                                shortBreakSecond = secondPicker.getValue();
                                break;
                            case LONG_BREAK:
                                longBreakMinute = minutePicker.getValue();
                                longBreakSecond = secondPicker.getValue();
                                break;
                            default:
                                break;
                        }
                        showData();
                        enableSaveButtonIfValuesChanged();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Invalid time", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSecondPickerDialog(String type, String title,
                                        int maxValue, int minValue, int currentValue) {
        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.view_picker_second, null);

        CustomNumberPicker secondPicker = getSecondPicker(customLayout, maxValue, minValue, currentValue);

        new NumberPickerDialog.Builder(getContext())
                .setView(customLayout)
                .setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (secondPicker.getValue() != 0) {
                        if (type == OFFENSE_TIME) {
                            offenseTimeInSecond = secondPicker.getValue();
                        } else {
                            afterReboundTimeInSecond = secondPicker.getValue();
                        }
                        showData();
                        enableSaveButtonIfValuesChanged();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Invalid time", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private CustomNumberPicker getMinutePicker(View pickerLayout, int minuteMax, int minuteMin, int currentMin) {
        CustomNumberPicker minutePicker = pickerLayout.findViewById(R.id.picker_minute);
        minutePicker.setMaxValue(minuteMax);
        minutePicker.setMinValue(minuteMin);
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutePicker.setFormatter(i -> String.format("%02d", i));
        minutePicker.setValue(currentMin);

        return minutePicker;
    }

    private CustomNumberPicker getSecondPicker(View pickerLayout, int secondMax, int secondMin, int currentSecond) {
        CustomNumberPicker secondPicker = pickerLayout.findViewById(R.id.picker_second);
        secondPicker.setMaxValue(secondMax);
        secondPicker.setMinValue(secondMin);
        secondPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        secondPicker.setFormatter(i -> String.format("%02d", i));
        secondPicker.setValue(currentSecond);

        return secondPicker;
    }


    private void showConfirmationDialog() {
        new CustomAlertDialog.Builder(getContext())
                .setMessage("Wanna save and reset the current scoreboard?")
                .setPositiveButton("OK", (dialog, which) -> {
                    saveConfigurations();
                    changeToScoreboardTab();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changeToScoreboardTab() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).changeTab(MainActivity.SCOREBOARD_TAB_INDEX);
        }
    }

    private void saveConfigurations() {
        if (getActivity() instanceof MainActivity) {

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(totalTimeMinute),
                    MainActivity.TOTAl_TIME_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(totalTimeSecond),
                    MainActivity.TOTAl_TIME_SECOND_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(offenseTimeInSecond),
                    MainActivity.OFFENSE_TIME_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(afterReboundTimeInSecond),
                    MainActivity.AFTER_REBOUNDING_TIME_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(timeOutMinute),
                    MainActivity.TIME_OUT_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(timeOutSecond),
                    MainActivity.TIME_OUT_SECOND_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(shortBreakMinute),
                    MainActivity.SHORT_BREAK_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(shortBreakSecond),
                    MainActivity.SHORT_BREAK_SEC_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(longBreakMinute),
                    MainActivity.LONG_BREAK_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(longBreakSecond),
                    MainActivity.LONG_BREAK_SEC_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(isGameClockStoppedWhenShotClockExpires,
                    MainActivity.SHOT_CLOCK_VIOLATION_KEY);
        }
    }

    public void initAndShowData() {
        saveButton.setEnabled(false);

        totalTimeMinute = MainActivity.totalTimeMinute;
        totalTimeSecond = MainActivity.totalTimeSecond;

        offenseTimeInSecond = MainActivity.offenseTimeInSecond;
        afterReboundTimeInSecond = MainActivity.afterReboundTimeInSecond;

        timeOutMinute = MainActivity.timeOutMinute;
        timeOutSecond = MainActivity.timeOutSecond;

        shortBreakMinute = MainActivity.shortBreakMinute;
        shortBreakSecond = MainActivity.shortBreakSecond;
        longBreakMinute = MainActivity.longBreakMinute;
        longBreakSecond = MainActivity.longBreakSecond;

        isGameClockStoppedWhenShotClockExpires = MainActivity.isGameClockStoppedWhenShotClockExpires;

        showData();
    }

    private void enableSaveButtonIfValuesChanged() {
        saveButton.setEnabled(totalTimeMinute != MainActivity.totalTimeMinute
                || totalTimeSecond != MainActivity.totalTimeSecond
                || offenseTimeInSecond != MainActivity.offenseTimeInSecond
                || afterReboundTimeInSecond != MainActivity.afterReboundTimeInSecond
                || timeOutMinute != MainActivity.timeOutMinute
                || timeOutSecond != MainActivity.timeOutSecond
                || shortBreakMinute != MainActivity.shortBreakMinute
                || shortBreakSecond != MainActivity.shortBreakSecond
                || isGameClockStoppedWhenShotClockExpires != MainActivity.isGameClockStoppedWhenShotClockExpires);
    }

    private void showData() {
        totalTimeItem.setContent(totalTimeMinute + " mins " + totalTimeSecond + " secs");
        offenseTimeItem.setContent(offenseTimeInSecond + " secs");
        afterReboundTimeItem.setContent(afterReboundTimeInSecond + " secs");
        timeOutItem.setContent(timeOutMinute + " mins " + timeOutSecond + " secs");
        shortBreakItem.setContent(shortBreakMinute + " mins " + shortBreakSecond + " secs");
        shotClockViolationItem.setSwitchState(isGameClockStoppedWhenShotClockExpires);
    }

}