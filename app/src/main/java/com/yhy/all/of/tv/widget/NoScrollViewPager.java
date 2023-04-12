package com.yhy.all.of.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 不可滚动的 ViewPager
 * <p>
 * Created on 2023-04-12 21:42
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(@NonNull Context context) {
        this(context, null);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 禁止viewpager里面内容导致页面切换
     *
     * @param event 参数
     * @return result
     */
    @Override
    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    // 修正首页触屏左右滑动会移位
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
