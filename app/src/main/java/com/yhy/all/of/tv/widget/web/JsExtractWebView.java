package com.yhy.all.of.tv.widget.web;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.BuildConfig;
import com.yhy.all.of.tv.utils.LogUtils;

/**
 * JS 提取器 WebView
 * <p>
 * Created on 2023-01-29 21:20
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsExtractWebView extends WebView {
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
        this(context, attrs, defStyleAttr, 0);
    }

    public JsExtractWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(false);
        setFocusableInTouchMode(false);
        clearFocus();
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        final WebSettings settings = getSettings();
        settings.setNeedInitialFocus(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
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
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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
                loadUrl("about:blank");
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

    private static class SysWebClient extends WebViewClient {
        private static final String TAG = "ParserWebViewDefault.SysWebClient";

        private final WebView mWv;
        private final MutableLiveData<String> mInnerLiveData;
        private final String mJsCode;

        private SysWebClient(WebView wv, MutableLiveData<String> liveData, String jsCode) {
            mWv = wv;
            mInnerLiveData = new MutableLiveData<>();
            mJsCode = jsCode;
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
            mInnerLiveData.postValue(null);
        }
    }

    @JavascriptInterface
    public void onExtracted(String result) {
        LogUtils.iTag(TAG, result);
        postDelayed(() -> mLiveData.postValue(result), 10);
    }
}
