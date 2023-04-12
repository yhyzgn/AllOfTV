package com.yhy.all.of.tv.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 自定义滚动速度的 Scroller
 * <p>
 * Created on 2023-04-12 21:44
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 0;

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, mDuration);
    }
}