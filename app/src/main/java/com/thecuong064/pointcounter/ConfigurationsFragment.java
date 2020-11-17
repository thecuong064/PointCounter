package com.thecuong064.pointcounter;

import android.content.DialogInterface;
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
    @BindView(R.id.btn_save) TextView saveButton;

    private int totalTimeMinute;
    private int totalTimeSecond;
    private int afterReboundingTimeInSecond;
    private int offenseTimeInSecond;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_configurations;
    }

    @Override
    protected void initViewsAndEvents(View rootView) {
        initViewOnClick();
        initData();
    }

    private void initViewOnClick() {
        totalTimeItem.setOnClickListener(v ->
                showMinuteAndSecondPickerDialog("Total time", 60, 0, totalTimeMinute,
                        59, 0, totalTimeSecond)
        );

        offenseTimeItem.setOnClickListener(v ->
                showSecondPickerDialog(MainActivity.OFFENSE_TIME_SAVE_KEY,
                        "Offense time", 59, 1, offenseTimeInSecond)
        );

        afterReboundingTimeItem.setOnClickListener(v ->
                showSecondPickerDialog(MainActivity.AFTER_REBOUNDING_TIME_SAVE_KEY,
                        "After-offensive-rebounding time", 59, 1, afterReboundingTimeInSecond)
        );

        saveButton.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showMinuteAndSecondPickerDialog(String title,
                                                 int minuteMax, int minuteMin, int currentMin,
                                                 int secondMax, int secondMin, int currentSec) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);

        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_minute_second_picker, null);
        builder.setView(customLayout);

        CustomNumberPicker minutePicker = customLayout.findViewById(R.id.picker_minute);
        minutePicker.setMaxValue(minuteMax);
        minutePicker.setMinValue(minuteMin);
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutePicker.setFormatter(i -> String.format("%02d", i));
        minutePicker.setValue(currentMin);

        CustomNumberPicker secondPicker = customLayout.findViewById(R.id.picker_second);
        secondPicker.setMaxValue(secondMax);
        secondPicker.setMinValue(secondMin);
        secondPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        secondPicker.setFormatter(i -> String.format("%02d", i));
        secondPicker.setValue(currentSec);

        AlertDialog dialog = builder.create();

        TextView cancelBtn = customLayout.findViewById(R.id.btn_left);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        TextView acceptBtn = customLayout.findViewById(R.id.btn_right);
        acceptBtn.setOnClickListener(v -> {
            if (minutePicker.getValue() != 0 || secondPicker.getValue() != 0) {
                totalTimeMinute = minutePicker.getValue();
                totalTimeSecond = secondPicker.getValue();
                showData();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Invalid time", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showSecondPickerDialog(String type, String title,
                                        int maxValue, int minValue, int currentValue) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);

        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_second_picker, null);
        builder.setView(customLayout);

        CustomNumberPicker secondPicker = customLayout.findViewById(R.id.picker_second);
        secondPicker.setMaxValue(maxValue);
        secondPicker.setMinValue(minValue);
        secondPicker.setValue(currentValue);
        secondPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        secondPicker.setFormatter(i -> String.format("%02d", i));

        AlertDialog dialog = builder.create();

        TextView cancelBtn = customLayout.findViewById(R.id.btn_left);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        TextView acceptBtn = customLayout.findViewById(R.id.btn_right);
        acceptBtn.setOnClickListener(v -> {
            if (secondPicker.getValue() != 0) {
                if (type == MainActivity.OFFENSE_TIME_SAVE_KEY) {
                    offenseTimeInSecond = secondPicker.getValue();
                } else {
                    afterReboundingTimeInSecond = secondPicker.getValue();
                }
                showData();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Invalid time", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromMinute(totalTimeMinute),
                    MainActivity.TOTAl_TIME_MINUTE_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(totalTimeSecond),
                    MainActivity.TOTAl_TIME_SECOND_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(offenseTimeInSecond),
                    MainActivity.OFFENSE_TIME_SAVE_KEY);

            ((MainActivity) getActivity()).setConfiguration(MainActivity.millisFromSecond(afterReboundingTimeInSecond),
                    MainActivity.AFTER_REBOUNDING_TIME_SAVE_KEY);
        }
    }

    public void initData() {
        totalTimeMinute = MainActivity.totalTimeMinute;
        totalTimeSecond = MainActivity.totalTimeSecond;
        offenseTimeInSecond = MainActivity.offenseTimeInSecond;
        afterReboundingTimeInSecond = MainActivity.afterReboundingTimeInSecond;
        showData();
    }

    private void showData() {
        totalTimeItem.setContent(totalTimeMinute + " mins " + totalTimeSecond + " secs");
        offenseTimeItem.setContent(offenseTimeInSecond + " secs");
        afterReboundingTimeItem.setContent(afterReboundingTimeInSecond + " secs");
    }

}