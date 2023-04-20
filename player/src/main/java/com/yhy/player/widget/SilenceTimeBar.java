package com.yhy.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.DefaultTimeBar;

/**
 * Created on 2023-02-06 01:40
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class SilenceTimeBar extends DefaultTimeBar {

    public SilenceTimeBar(Context context) {
        super(context);
    }

    public SilenceTimeBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SilenceTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SilenceTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs) {
        super(context, attrs, defStyleAttr, timebarAttrs);
    }

    public SilenceTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs, int defStyleRes) {
        super(context, attrs, defStyleAttr, timebarAttrs, defStyleRes);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }
}
