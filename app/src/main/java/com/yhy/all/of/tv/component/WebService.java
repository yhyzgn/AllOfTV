package com.yhy.all.of.tv.component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.router.annotation.Router;

/**
 * Web 进程服务
 * <p>
 * Created on 2023-06-28 10:59
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Router(url = "/service/web")
public class WebService extends Service {
    private static final String TAG = "WebService";

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.iTag(TAG, "进程【:web】已启动！");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
