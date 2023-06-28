package com.yhy.all.of.tv.widget.web.sonic;

import android.os.Bundle;

import com.tencent.sonic.sdk.SonicSessionClient;
import com.yhy.all.of.tv.widget.web.internal.SonicWebView;

import java.util.HashMap;

/**
 * Created on 2023-06-28 11:32
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class SonicSessionClientImpl extends SonicSessionClient {
    private SonicWebView mWebView;

    public SonicSessionClientImpl(SonicWebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void loadUrl(String url, Bundle extraData) {
        mWebView.loadUrl(url, extraData);
    }

    @Override
    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers) {
        mWebView.loadDataWithBaseUrlAndHeader(baseUrl, data, mimeType, encoding, historyUrl, headers);
    }
}
