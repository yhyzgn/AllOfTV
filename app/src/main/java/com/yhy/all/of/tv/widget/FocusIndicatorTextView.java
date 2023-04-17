package com.yhy.all.of.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yhy.all.of.tv.R;

/**
 * Created on 2023-04-17 12:40
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class FocusIndicatorTextView extends LinearLayout implements View.OnFocusChangeListener {

    private TextView tvText;
    private View vIndicator;

    public FocusIndicatorTextView(Context context) {
        this(context, null);
    }

    public FocusIndicatorTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusIndicatorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FocusIndicatorTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.focus_indicator_text_view, this);
        tvText = view.findViewById(R.id.tvText);
        vIndicator = view.findViewById(R.id.vIndicator);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FocusIndicatorTextView);
        String text = ta.getString(R.styleable.FocusIndicatorTextView_android_text);

        ta.recycle();

        setFocusable(true);
        setFocusableInTouchMode(true);

        setText(text);
        setIndicatorVisible(hasFocus());

        setOnFocusChangeListener(this);
    }

    public void setText(CharSequence text) {
        tvText.setText(text);
    }

    public CharSequence getText() {
        return tvText.getText();
    }

    public void setIndicatorVisible(boolean visible) {
        vIndicator.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setText(hasFocus ? "有" : "无");
        setIndicatorVisible(hasFocus);
    }
}
