package com.yhy.all.of.tv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-01-15 14:17
 * version: 1.0.0
 * desc   : 文件工具类
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class FileUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    /**
     * 初始化，在Application入口调用
     *
     * @param ctx 上下文对象
     */
    public static void init(Context ctx) {
        mCtx = ctx;
    }

    /**
     * 刷新文件管理系统
     *
     * @param filename 文件名
     */
    public static void refresh(String filename) {
        refresh(filename, null);
    }

    /**
     * 刷新文件管理系统
     *
     * @param filename 文件名
     * @param listener 刷新完成回调
     */
    public static void refresh(String filename, MediaScannerConnection.OnScanCompletedListener listener) {
        refresh(new File(filename), listener);
    }

    /**
     * 刷新文件管理系统
     *
     * @param file 文件
     */
    public static void refresh(File file) {
        refresh(file, null);
    }

    /**
     * 刷新文件管理系统
     *
     * @param file     文件
     * @param listener 刷新完成回调
     */
    public static void refresh(File file, MediaScannerConnection.OnScanCompletedListener listener) {
        MediaScannerConnection.scanFile(mCtx, new String[]{file.toString()}, new String[]{getMimeType(file)}, listener);
    }

    /**
     * 获取文件（夹）大小
     *
     * @param filename 文件（夹）名称
     * @return 文件（夹）大小
     */
    public static long getSize(String filename) {
        return getSize(new File(filename));
    }

    /**
     * 获取文件（夹）大小
     *
     * @param file 文件（夹）
     * @return 文件（夹）大小
     */
    public static long getSize(File file) {
        long size = 0;
        if (null != file && file.exists()) {
            if (file.isFile()) {
                size += file.length();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File item : files) {
                    if (item.isFile()) {
                        size += item.length();
                    } else if (item.isDirectory()) {
                        size += getSize(item);
                    }
                }
            }
        }
        return size;
    }

    /**
     * 格式化文件（夹）大小
     *
     * @param filename 文件（夹）名称
     * @return 格式化后文件（夹）大小
     */
    public static String formatSize(String filename) {
        return formatSize(getSize(filename));
    }

    /**
     * 格式化文件（夹）大小
     *
     * @param file 文件（夹）对象
     * @return 格式化后文件（夹）大小
     */
    public static String formatSize(File file) {
        return formatSize(getSize(file));
    }

    /**
     * 格式化文件（夹）大小
     *
     * @param size 文件（夹）大小
     * @return 格式化后文件（夹）大小
     */
    public static String formatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            if (size == 0) {
                return "0MB";
            }
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        BigDecimal value;
        if (megaByte < 1) {
            value = new BigDecimal(Double.toString(kiloByte));
            return value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            value = new BigDecimal(Double.toString(megaByte));
            return value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            value = new BigDecimal(Double.toString(gigaByte));
            return value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        value = new BigDecimal(teraBytes);
        return value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建爱你的文件夹对象
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 创建文件
     *
     * @param filename 文件名
     * @return 创建的文件对象
     */
    public static File createFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除文件（夹）
     *
     * @param filename 文件
     */
    public static void delete(String filename) {
        delete(new File(filename));
    }

    /**
     * 删除文件（夹）
     *
     * @param file 文件
     */
    public static void delete(File file) {
        if (null == file || !file.exists()) {
            return;
        }
        if (file.isDirectory() && file.length() != 0) {
            for (File item : file.listFiles()) {
                delete(item);
            }
        }
        file.delete();
    }

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return 文件后缀
     */
    public static String getExt(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            if (filename.contains(".")) {
                return filename.substring(filename.lastIndexOf(".") + 1);
            }
            return "";
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return 后缀
     */
    public static String getExt(File file) {
        return null == file ? null : getExt(file.getName());
    }

    /**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return MimeType
     */
    public static String getMimeType(String filename) {
        String ext = getExt(filename);
        return TextUtils.isEmpty(ext) ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    /**
     * 获取文件MimeType
     *
     * @param file 文件
     * @return MimeType
     */
    public static String getMimeType(File file) {
        return null == file ? null : getMimeType(file.getName());
    }
}
