package com.thecuong064.pointcounter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAlertDialog extends Dialog {

    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private View mExtraView;
    private OnClickListener mPositiveButtonListener;
    private OnClickListener mNegativeButtonListener;

    private TextView mTitleTv;
    private TextView mMessageTv;
    private FrameLayout mContainer;
    private View mMidLine;
    private TextView mPositiveButton;
    private TextView mNegativeButton;

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    public void setMessage(CharSequence message) {
        this.mMessage = message;
        if (mMessageTv != null) {
            mMessageTv.setText(message);
            if (TextUtils.isEmpty(message)) {
                mMessageTv.setVisibility(View.GONE);
            } else {
                mMessageTv.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setExtraView(View extraView) {
        mExtraView = extraView;
        if (mContainer != null) {
            mContainer.removeAllViews();
            if (mExtraView != null) {
                mContainer.addView(mExtraView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mContainer.setVisibility(View.VISIBLE);
            } else {
                mContainer.setVisibility(View.GONE);
            }
        }
    }

    public void setPositiveButton(CharSequence text, final OnClickListener listener) {
        mPositiveButtonText = text;
        mPositiveButtonListener = listener;
        if (mPositiveButton != null) {
            mPositiveButton.setText(mPositiveButtonText);
            mPositiveButton.setOnClickListener(v -> {
                dismiss();
                if (mPositiveButtonListener != null) {
                    mPositiveButtonListener.onClick(CustomAlertDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
            });
        }
    }

    public void setNegativeButton(CharSequence text, final OnClickListener listener) {
        mNegativeButtonText = text;
        mNegativeButtonListener = listener;
        if (mNegativeButton != null) {
            mNegativeButton.setText(mNegativeButtonText);
            mNegativeButton.setOnClickListener(v -> {
                dismiss();
                if (mNegativeButtonListener != null) {
                    mNegativeButtonListener.onClick(CustomAlertDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
            });
        }
    }

    public CustomAlertDialog(@NonNull Context context) {
        //super(context, R.style.DialogTranslucentNoTitleWithDim);
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        bindView();
        Window dialogWindow = getWindow();
        if (dialogWindow == null) {
            return;
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void bindView() {
        mTitleTv = findViewById(R.id.tv_title);
        if (mTitle != null && mTitle.length() > 0 ) {
            mTitleTv.setVisibility(View.VISIBLE);
            setTitle(mTitle);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }
        mMessageTv = findViewById(R.id.tv_message);
        setMessage(mMessage);
        mPositiveButton = findViewById(R.id.btn_positive);
        setPositiveButton(mPositiveButtonText, mPositiveButtonListener);
        mNegativeButton = findViewById(R.id.btn_negative);
        setNegativeButton(mNegativeButtonText, mNegativeButtonListener);
        mContainer = findViewById(R.id.view_container);
        setExtraView(mExtraView);
        mMidLine = findViewById(R.id.line_btn_mid);
        fixButton();
    }

    private void fixButton() {
        if (TextUtils.isEmpty(mPositiveButtonText)) {
            mPositiveButton.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mNegativeButtonText)) {
            mNegativeButton.setVisibility(View.GONE);
        }
        if (mNegativeButton.getVisibility() == View.VISIBLE
                && mPositiveButton.getVisibility() == View.VISIBLE) {
            mMidLine.setVisibility(View.VISIBLE);
        } else {
            mMidLine.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        Context mContext;
        CharSequence mTitle;
        CharSequence mMessage;
        View mExtraView;
        CharSequence mPositiveButtonText;
        CharSequence mNegativeButtonText;
        OnClickListener mPositiveButtonListener;
        OnClickListener mNegativeButtonListener;
        OnCancelListener mOnCancelListener;
        OnDismissListener mOnDismissListener;
        boolean mCancelable = true;
        boolean mCanceledOnTouchOutside = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder setView(View extraView) {
            this.mExtraView = extraView;
            return this;
        }

        public Builder setView(int resId) {
            this.mExtraView = LayoutInflater.from(mContext).inflate(resId, null, false);
            return this;
        }


        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            this.mNegativeButtonText = text;
            this.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener cancelListener) {
            mOnCancelListener = cancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        public CustomAlertDialog create() {
            CustomAlertDialog alertDialog = new CustomAlertDialog(mContext);
            alertDialog.setTitle(mTitle);
            alertDialog.setMessage(mMessage);
            alertDialog.setExtraView(mExtraView);
            alertDialog.setPositiveButton(mPositiveButtonText, mPositiveButtonListener);
            alertDialog.setNegativeButton(mNegativeButtonText, mNegativeButtonListener);
            alertDialog.setCancelable(mCancelable);
            alertDialog.setOnCancelListener(mOnCancelListener);
            alertDialog.setOnDismissListener(mOnDismissListener);
            alertDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            alertDialog.setCancelable(mCancelable);
            return alertDialog;
        }

        public void show() {
            create().show();
        }
    }
}