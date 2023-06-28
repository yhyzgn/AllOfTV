package com.yhy.all.of.tv.widget.web.sonic;

import android.content.Context;

import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionClient;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.yhy.all.of.tv.widget.web.internal.SonicWebView;

/**
 * Created on 2023-06-28 12:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class Sonic {

    private final Context mCtx;
    private final SonicWebView mWebView;
    private SonicSession mSession;
    private SonicSessionClient mClient;

    public Sonic(Context context, SonicWebView webView) {
        mCtx = context;
        mWebView = webView;

        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(mCtx.getApplicationContext()), new SonicConfig.Builder().build());
        }
    }

    public boolean load(String url) {
        mSession = SonicEngine.getInstance().createSession(url, new SonicSessionConfig.Builder().build());
        if (null != mSession) {
            mClient = new SonicSessionClientImpl(mWebView);
            mSession.bindClient(mClient);
        }

        if (null != mClient) {
            mClient.clientReady();
            return true;
        }

        return false;
    }

    public void destroy() {
        if (null != mSession) {
            mSession.destroy();
            mSession = null;
        }
    }
}
