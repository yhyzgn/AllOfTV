package com.yhy.all.of.tv.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-11 10:31
 * version: 1.0.0
 * desc   : 系统工具类
 */
public abstract class SysUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;

    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    public static void init(Context context) {
        ctx = context;
    }

    /**
     * 获取版本名称
     *
     * @return 版本名称
     */
    public static String getVersionName() {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            // 得到apk的功能清单文件:为了防止出错直接使用getPackageName()方法获得包名
            PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            // 返回版本名称
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public static long getVersionCode() {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            // 得到apk的功能清单文件:为了防止出错直接使用getPackageName()方法获得包名
            PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            // 返回版本号
            // Android 8.0 +
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            }
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前APP的名称
     *
     * @return 当前APP的名称
     */
    public static String getAppName() {
        PackageManager pm = ctx.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) pm.getApplicationLabel(ai);
    }

    /**
     * 获取进程id
     *
     * @return 进程id
     */
    public static int getProcessId() {
        return Process.myPid();
    }

    /**
     * 获取进程名称
     *
     * @return 进程名称
     */
    public static String getProcessName() {
        int pid = getProcessId();
        String processName = null;
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (null == am) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        PackageManager pm = ctx.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = i.next();
            try {
                if (info.pid == pid) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型
     */
    public static String getDeviceType() {
        return Build.MODEL;
    }

    /**
     * 检查是否可安装应用
     *
     * @return 是否可安装
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static boolean canInstall() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ctx.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 判断是否注册过广播
     *
     * @param action 广播Action
     * @return 是否注册过
     */
    public static boolean isReceiverRegisted(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        PackageManager pm = ctx.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryBroadcastReceivers(intent, 0);
        if (resolveInfoList != null && !resolveInfoList.isEmpty()) {
            //查询到相应的BroadcastReceiver
            return true;
        }
        return false;
    }

    /**
     * 判断某个Activity是否在前台
     *
     * @param clazz Activity字节码对象
     * @return 是否在前台
     */
    public static boolean isForeground(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }

        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (null == am) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (clazz.getName().equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取ApplicationId
     *
     * @return ApplicationId
     */
    public static String getApplicationId() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);

            String appId = null;
            if (applicationInfo != null) {
                appId = applicationInfo.packageName;
                if (TextUtils.isEmpty(appId)) {
                    appId = applicationInfo.processName;
                }
            }
            if (TextUtils.isEmpty(appId)) {
                appId = ctx.getPackageName();
            }
            return appId;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回当前的应用是否处于前台显示状态
     *
     * @param packageName 包名
     * @return 是否处于前台运行
     */
    public static boolean isTopProcess(String packageName) {
        ActivityManager am = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo process : list) {
            /*
            在6.0/7.0等新版本中 可能还有另外几种状态:
            1.RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING(应用在前台时锁屏幕)，RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE(应用开启了服务,然后锁屏幕,此时服务还是在前台运行)
            可以根据自己的实际情况决定上面列出的2个状态,是否算作前台状态;
             */
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 模拟键盘点击事件
     *
     * @param keyCode keyCode
     */
    public static void sendKeyCode(final int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建一个Instrumentation对象
                    Instrumentation inst = new Instrumentation();
                    // 调用inst对象的按键模拟方法
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取当前电量
     *
     * @return 电量
     */
    public static int getBattery() {
        BatteryManager bm = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static boolean gpsOpened() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean networkOpened() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean wifiOpened() {
        // 从系统服务中获取无线网络管理器
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        return wm.isWifiEnabled();
    }

    public static boolean isNetworkType(NetworkType type) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo.getType() == type.code;
    }

    public enum NetworkType {
        MOBILE(0),
        WIFI(1),
        ;

        final int code;

        NetworkType(int code) {
            this.code = code;
        }
    }

    /**
     * 回调
     *
     * @param <T> 传回的数据类型
     */
    @FunctionalInterface
    public interface Callback<T> {

        /**
         * 回调
         *
         * @param result 传回的数据
         */
        void onResult(T result);
    }
}
