package com.yhy.all.of.tv.widget.web.sonic;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;

import com.tencent.sonic.sdk.SonicRuntime;
import com.tencent.sonic.sdk.SonicSessionClient;
import com.yhy.all.of.tv.rand.UserAgentRand;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.utils.ToastUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created on 2023-06-28 11:27
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class SonicRuntimeImpl extends SonicRuntime {
    public SonicRuntimeImpl(Context context) {
        super(context);
    }

    @Override
    public void log(String tag, int level, String message) {
        switch (level) {
            case Log.ERROR:
                LogUtils.eTag(tag, message);
                break;
            case Log.DEBUG:
                LogUtils.dTag(tag, message);
                break;
            default:
                LogUtils.iTag(tag, message);
        }
    }

    @Override
    public String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    @Override
    public boolean setCookie(String url, List<String> cookies) {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.size() > 0) {
            CookieManager cookieManager = CookieManager.getInstance();
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getUserAgent() {
        return UserAgentRand.get();
    }

    @Override
    public String getCurrentUserAccount() {
        return "Sonic-Web";
    }

    @Override
    public boolean isSonicUrl(String url) {
        return true;
    }

    @Override
    public Object createWebResourceResponse(String mimeType, String encoding, InputStream data, Map<String, String> headers) {
        WebResourceResponse resourceResponse = new WebResourceResponse(mimeType, encoding, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.setResponseHeaders(headers);
        }
        return resourceResponse;
    }

    @Override
    public boolean isNetworkValid() {
        return true;
    }

    @Override
    public void showToast(CharSequence text, int duration) {
        ToastUtils.shortT(text);
    }

    @Override
    public void postTaskToThread(Runnable task, long delayMillis) {
        Thread thread = new Thread(task, "SonicThread");
        thread.start();
    }

    @Override
    public void notifyError(SonicSessionClient client, String url, int errorCode) {

    }
}
