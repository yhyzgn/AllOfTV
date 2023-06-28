package com.yhy.all.of.tv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.appcompat.app.AppCompatActivity;

import com.yhy.player.utils.CutoutUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

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
     * 获取屏幕大小
     *
     * @return 屏幕大小
     */
    public static Size getScreenSize(AppCompatActivity activity) {
        // 获取屏幕宽高
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // 如果有刘海屏，需要加上刘海屏的宽高
        Size cutoutSize = CutoutUtils.size(activity);
        width += cutoutSize.getWidth();
        height += cutoutSize.getHeight();
        return new Size(width, height);
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

    /**
     * 让 activity transition 动画过程中可以正常渲染页面
     */
    public static void setDrawDuringWindowsAnimating(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M
            || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 1 android n以上  & android 4.1以下不存在此问题，无须处理
            return;
        }
        // 4.2不存在setDrawDuringWindowsAnimating，需要特殊处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            handleDispatchDoneAnimating(view);
            return;
        }
        try {
            // 4.3及以上，反射setDrawDuringWindowsAnimating来实现动画过程中渲染
            ViewParent rootParent = view.getRootView().getParent();
            Method method = rootParent.getClass().getDeclaredMethod("setDrawDuringWindowsAnimating", boolean.class);
            method.setAccessible(true);
            method.invoke(rootParent, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * android4.2可以反射handleDispatchDoneAnimating来解决
     */
    public static void handleDispatchDoneAnimating(View paramView) {
        try {
            ViewParent localViewParent = paramView.getRootView().getParent();
            Class<?> localClass = localViewParent.getClass();
            Method localMethod = localClass.getDeclaredMethod("handleDispatchDoneAnimating");
            localMethod.setAccessible(true);
            localMethod.invoke(localViewParent);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
