package com.yhy.all.of.tv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.widget.focus.MyFocusHighlightHelper;

/**
 * Created on 2023-01-23 22:00
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ScaleConstraintLayout extends ConstraintLayout implements View.OnFocusChangeListener {

    private MyFocusHighlightHelper.BrowseItemFocusHighlight mBrowseItemFocusHighlight;

    public ScaleConstraintLayout(Context context) {
        this(context, null);
    }

    public ScaleConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setClickable(true);
        setFocusableInTouchMode(true);
        setClipChildren(false);
        setClipToPadding(false);
        setOnFocusChangeListener(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleConstraintLayout);
        int zoomIndex = typedArray.getInteger(R.styleable.ScaleConstraintLayout_scale_mode, MyFocusHighlightHelper.ZOOM_FACTOR_XXXSMALL);
        typedArray.recycle();
        if (mBrowseItemFocusHighlight == null) {
            mBrowseItemFocusHighlight =
                    new MyFocusHighlightHelper
                            .BrowseItemFocusHighlight(zoomIndex, false);
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mBrowseItemFocusHighlight != null) {
            mBrowseItemFocusHighlight.onItemFocused(v, hasFocus);
        }
    }
}