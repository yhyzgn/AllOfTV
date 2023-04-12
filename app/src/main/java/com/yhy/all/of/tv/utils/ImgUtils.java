package com.yhy.all.of.tv.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created on 2022-09-20 16:33
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ImgUtils {

    private static ImgLoader mLoader;

    public static void init(ImgLoader loader) {
        mLoader = loader;
    }

    public static <T> void load(Context ctx, ImageView iv, T model) {
        if (null != mLoader) {
            mLoader.load(ctx, iv, model);
        }
    }

    /**
     * 图片加载器
     */
    public interface ImgLoader {
        <T> void load(Context ctx, ImageView iv, T model);
    }
}
