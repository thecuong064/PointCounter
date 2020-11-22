package com.thecuong064.pointcounter.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.thecuong064.pointcounter.R;

public class ConfigurationItemView extends RelativeLayout {

    private TextView mTitleTv, mContentTv;
    private View mLine;
    private SwitchCompat mContentSwitch;

    public ConfigurationItemView(@NonNull Context context) {
        this(context, null);
    }

    public ConfigurationItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConfigurationItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        inflate(getContext(), R.layout.configuration_item_view, this);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);
        mLine = findViewById(R.id.line);
        mContentSwitch = findViewById(R.id.sw_content);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ConfigurationItemView);
        mTitleTv.setText(ta.getString(R.styleable.ConfigurationItemView_itemTitle));
        int resourceId = ta.getResourceId(R.styleable.ConfigurationItemView_itemTitleIcon, 0);
        if (resourceId != 0) {
            mTitleTv.setCompoundDrawablesRelativeWithIntrinsicBounds(resourceId, 0, 0, 0);
        }
        mContentTv.setText(ta.getString(R.styleable.ConfigurationItemView_itemContent));

        ColorStateList titleColor = ta.getColorStateList(R.styleable.ConfigurationItemView_itemTitleTextColor);
        if (titleColor != null) {
            mTitleTv.setTextColor(titleColor);
        }

        mLine.setVisibility(ta.getBoolean(R.styleable.ConfigurationItemView_itemLineShow, false) ? VISIBLE : GONE);

        mContentSwitch.setVisibility(ta.getBoolean(R.styleable.ConfigurationItemView_itemIsSwitchShowed, false) ? VISIBLE : GONE);
        mContentTv.setVisibility(ta.getBoolean(R.styleable.ConfigurationItemView_itemIsSwitchShowed, false) ? GONE : VISIBLE);

        ta.recycle();
    }

    public void setTitle(CharSequence title) {
        mTitleTv.setText(title);
    }

    public void setContent(CharSequence content) {
        mContentTv.setText(content);
    }

    public CharSequence getContent() {
        return mContentTv.getText();
    }

    public void setSwitchState(boolean state) {
        mContentSwitch.setChecked(state);
    }

    public void setSwitchOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        if (mContentSwitch != null) {
            mContentSwitch.setOnCheckedChangeListener(listener);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTitleTv.setEnabled(enabled);
    }
}
