package com.yhy.all.of.tv.widget.web.internal;

import android.os.Bundle;

import java.util.HashMap;

/**
 * Created on 2023-06-28 11:35
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SonicWebView {

    void loadUrl(String url, Bundle extraData);

    void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl);

    void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers);
}
