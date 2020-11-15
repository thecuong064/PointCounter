package com.thecuong064.pointcounter.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thecuong064.pointcounter.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    protected ViewGroup mMainView;

    @BindView(R.id.view_new_title)
    View mTitleBarView;
    @BindView(R.id.btn_back)
    View mBackBtn;
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    protected abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mMainView = findViewById(R.id.ll_main);
        if (getContentViewId() != 0) {
            View childView = LayoutInflater.from(this).inflate(getContentViewId(), null, false);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mMainView.addView(childView, lp);
        }
        ButterKnife.bind(this);
        mBackBtn.setOnClickListener(v -> finish());
        afterCreate();
    }

    protected void afterCreate() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*-----------------------------    TitleBar Set            -------------------------------*/

    public void setTitleBarVisible(boolean visible) {
        mTitleBarView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(int title) {
        setTitle(getString(title));
    }

    public void setTitle(String title) {
        mTitleBarView.setVisibility(View.VISIBLE);
        //mTitleTv.setText(StringUtils.isEmpty(title) ? "" : title);
    }

    public void setTitle(int title, int rightTitle, View.OnClickListener rl) {
        setTitle(getString(title), getString(rightTitle), rl);
    }

    public void setTitle(String title, String rightTitle, View.OnClickListener rl) {
        mTitleBarView.setVisibility(View.VISIBLE);
        //mTitleTv.setText(StringUtils.isEmpty(title) ? "" : title);
    }
}
