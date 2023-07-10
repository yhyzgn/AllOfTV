package com.yhy.all.of.tv.widget.web;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.crash.h5.H5JavaScriptInterface;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yhy.all.of.tv.BuildConfig;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.utils.ViewUtils;
import com.yhy.all.of.tv.widget.web.internal.SonicWebView;

import java.util.HashMap;

/**
 * JS 提取器 WebView
 * <p>
 * Created on 2023-01-29 21:20
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsExtractWebView extends WebView implements SonicWebView, CrashReport.a {
    private static final String TAG = "JsExtractWebView";
    private AppCompatActivity mActivity;
    private String mUrl;
    private MutableLiveData<String> mLiveData;

    public JsExtractWebView(@NonNull Context context) {
        this(context, null);
    }

    public JsExtractWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsExtractWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewUtils.setDrawDuringWindowsAnimating(this);

        setFocusable(false);
        setFocusableInTouchMode(false);
        clearFocus();
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        final WebSettings settings = getSettings();
        settings.setDomStorageEnabled(true);
        settings.setNeedInitialFocus(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        if (BuildConfig.DEBUG) {
            settings.setBlockNetworkImage(false);
        } else {
            settings.setBlockNetworkImage(true);
        }
        settings.setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(0);
        }
        // 自动播放媒体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");

        // 添加回调
        addJavascriptInterface(this, "extractor");

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                CrashReport.setJavascriptMonitor(JsExtractWebView.this, true);
                view.getSettings().setBlockNetworkImage(false);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return false;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return true;
            }
        });
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        enabledCookie();
    }

    public JsExtractWebView attach(AppCompatActivity activity, String url, MutableLiveData<String> liveData, String jsCode) {
        mActivity = activity;
        mUrl = url;
        mLiveData = liveData;

        setWebViewClient(new SysWebClient(this, liveData, jsCode));

        ViewGroup.LayoutParams lp = BuildConfig.DEBUG ? new ViewGroup.LayoutParams(400, 400) : new ViewGroup.LayoutParams(1, 1);
        MarginLayoutParams mlp = new MarginLayoutParams(lp);
        mlp.leftMargin = -1;
        mlp.topMargin = -1;
        activity.addContentView(this, mlp);
        setVisibility(BuildConfig.DEBUG ? VISIBLE : INVISIBLE);
        return this;
    }

    public void start() {
        loadUrl(mUrl);
    }

    public void stop(boolean destroy) {
        if (null != mActivity) {
            mActivity.runOnUiThread(() -> {
                clearCache(true);
                stopLoading();
                loadUrl(null);
                if (destroy) {
                    removeAllViews();
                    destroy();
                }
            });
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    private void enabledCookie() {
        CookieManager instance = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.createInstance(getContext().getApplicationContext());
        }
        instance.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            instance.setAcceptThirdPartyCookies(this, true);
        }
    }

    @Override
    public String a() {
        return getUrl();
    }

    @Override
    public void b() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void a(String s) {
        loadUrl(s);
    }

    @Override
    public void a(H5JavaScriptInterface h5JavaScriptInterface, String s) {
        addJavascriptInterface(h5JavaScriptInterface, s);
    }

    @Override
    public CharSequence c() {
        return getContentDescription();
    }

    @Override
    public void loadUrl(String url, Bundle extraData) {
        loadUrl(url);
    }

    @Override
    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers) {
        loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private static class SysWebClient extends WebViewClient {
        private static final String TAG = "ParserWebViewDefault.SysWebClient";

        private final WebView mWv;
        private final MutableLiveData<String> mInnerLiveData;
        private final String mJsCode;

        private SysWebClient(WebView wv, MutableLiveData<String> liveData, String jsCode) {
            mWv = wv;
            mInnerLiveData = liveData;
            mJsCode = jsCode;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.endsWith("favicon.ico") || url.contains(".baidu.")) {
                webView.loadUrl(null);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWv.postDelayed(() -> view.loadUrl("javascript:window.extractor.onExtracted(JSON.stringify(" + mJsCode + "));"), 3000);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            onParsedError(description);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            onParsedError(error.getDescription());
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            onParsedError(errorResponse.getReasonPhrase());
        }

        private void onParsedError(CharSequence error) {
            LogUtils.eTag(TAG, error);
            // 这里可能只是部分 js 错误而已，故不做处理了
            // mInnerLiveData.postValue(null);
        }
    }

    @JavascriptInterface
    public void onExtracted(String result) {
        LogUtils.iTag(TAG, result);
        postDelayed(() -> mLiveData.postValue(result), 10);
    }
}
