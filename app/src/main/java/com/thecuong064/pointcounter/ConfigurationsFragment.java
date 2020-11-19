package com.thecuong064.pointcounter;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.thecuong064.pointcounter.base.BaseFragment;
import com.thecuong064.pointcounter.widget.ConfigurationItemView;
import com.thecuong064.pointcounter.widget.CustomNumberPicker;

import butterknife.BindView;

public class ConfigurationsFragment extends BaseFragment {

    @BindView(R.id.item_total_time) ConfigurationItemView totalTimeItem;
    @BindView(R.id.item_offense_time) ConfigurationItemView offenseTimeItem;
    @BindView(R.id.item_after_rebounding_time) ConfigurationItemView afterReboundingTimeItem;
    @BindView(R.id.item_time_out) ConfigurationItemView timeOutItem;

    @BindView(R.id.btn_save) TextView saveButton;

    public static String TOTAl_TIME = "total_time";
    public static String OFFENSE_TIME = "offense_time";
    public static String AFTER_REBOUNDING_TIME = "after_rebounding_time";
    public static String TIME_OUT = "time_out";

    private int totalTimeMinute;
    private int totalTimeSecond;
    private int afterReboundingTimeInSecond;
    private int offenseTimeInSecond;
    private int timeOutMinute;
    private int timeOutSecond;

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

        afterReboundingTimeItem.setOnClickListener(v ->
                showSecondPickerDialog(AFTER_REBOUNDING_TIME,
                        "After-offensive-rebounding time", 59, 1, afterReboundingTimeInSecond)
        );

        timeOutItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Time out", TIME_OUT,
                        20, 0, timeOutMinute,
                        59, 0, timeOutSecond)
        );

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
                        if (type == TOTAl_TIME) {
                            totalTimeMinute = minutePicker.getValue();
                            totalTimeSecond = secondPicker.getValue();
                        } else {
                            timeOutMinute = minutePicker.getValue();
                            timeOutSecond = secondPicker.getValue();
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
                            afterReboundingTimeInSecond = secondPicker.getValue();
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

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(afterReboundingTimeInSecond),
                    MainActivity.AFTER_REBOUNDING_TIME_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(timeOutMinute),
                    MainActivity.TIME_OUT_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(timeOutSecond),
                    MainActivity.TIME_OUT_SECOND_SAVE_KEY);
        }
    }

    public void initAndShowData() {
        saveButton.setEnabled(false);
        totalTimeMinute = MainActivity.totalTimeMinute;
        totalTimeSecond = MainActivity.totalTimeSecond;
        offenseTimeInSecond = MainActivity.offenseTimeInSecond;
        afterReboundingTimeInSecond = MainActivity.afterReboundingTimeInSecond;
        timeOutMinute = MainActivity.timeOutMinute;
        timeOutSecond = MainActivity.timeOutSecond;
        showData();
    }

    private void enableSaveButtonIfValuesChanged() {
        if (totalTimeMinute != MainActivity.totalTimeMinute ||
                totalTimeSecond != MainActivity.totalTimeSecond ||
                offenseTimeInSecond != MainActivity.offenseTimeInSecond ||
                afterReboundingTimeInSecond != MainActivity.afterReboundingTimeInSecond ||
                timeOutMinute != MainActivity.timeOutMinute ||
                timeOutSecond != MainActivity.timeOutSecond) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private void showData() {
        totalTimeItem.setContent(totalTimeMinute + " mins " + totalTimeSecond + " secs");
        offenseTimeItem.setContent(offenseTimeInSecond + " secs");
        afterReboundingTimeItem.setContent(afterReboundingTimeInSecond + " secs");
        timeOutItem.setContent(timeOutMinute + " mins " + timeOutSecond + " secs");
    }

}