package com.yhy.all.of.tv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-11 17:44
 * version: 1.0.0
 * desc   : View工具类
 */
public abstract class ViewUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * 初始化
     *
     * @param ctx 上下文对象
     */
    public static void init(Context ctx) {
        context = ctx;
    }

    /**
     * 移除view的父控件
     *
     * @param view view
     */
    public static void removeParent(View view) {
        if (null != view && null != view.getParent() && view.getParent() instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view.getParent();
            vg.removeView(view);
        }
    }

    /**
     * 计算两点间的距离
     *
     * @param p1 点1
     * @param p2 点2
     * @return 距离
     */
    public static float distanceOf(PointF p1, PointF p2) {
        double distance = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        return Float.parseFloat(distance + "");
    }

    /**
     * dp转px
     *
     * @param dpVal dp值
     * @return px值
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context
                .getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param spVal sp值
     * @return px值
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context
                .getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param pxVal px值
     * @return dp值
     */
    public static float px2dp(float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal px值
     * @return sp值
     */
    public static float px2sp(float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 将view转成图片文件字节流
     *
     * @param view    需要进行截图的控件
     * @param quality 图片的质量 0-100
     * @return 该控件截图的byte数组对象
     */
    public static byte[] printView(View view, int quality) {
        Bitmap bitmap = printView(view);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return baos.toByteArray();
    }

    /**
     * 将view转换成bitmap图片
     * <p>
     * 适用于已经正常显示在界面上的view
     *
     * @param view 需要进行截图的控件
     * @return 该控件截图的Bitmap对象
     */
    public static Bitmap printView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        // DrawingCache得到的位图在禁用后会被回收
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 将view转换成bitmap图片
     * <p>
     * 适用于尚未显示在界面上的view
     *
     * @param view 需要进行截图的控件
     * @return 该控件截图的Bitmap对象
     */
    public static Bitmap printViewAdvance(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return bitmap;
    }
}
