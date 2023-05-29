package com.yhy.all.of.tv.widget.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
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
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.evtor.Evtor;

/**
 * 视频解析提取器
 * <p>
 * Created on 2023-01-29 21:20
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParserWebViewDefault extends WebView implements ParserWebView {
    private AppCompatActivity mActivity;
    private String mUrl;

    public ParserWebViewDefault(@NonNull Context context) {
        this(context, null);
    }

    public ParserWebViewDefault(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParserWebViewDefault(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ParserWebViewDefault(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    @Override
    public void attach(AppCompatActivity activity, Parser parser, String url, MutableLiveData<String> liveData) {
        mActivity = activity;
        mUrl = url;

        setWebViewClient(new SysWebClient(parser, liveData));

        ViewGroup.LayoutParams lp = BuildConfig.DEBUG ? new ViewGroup.LayoutParams(400, 400) : new ViewGroup.LayoutParams(1, 1);
        MarginLayoutParams mlp = new MarginLayoutParams(lp);
        mlp.leftMargin = -1;
        mlp.topMargin = -1;
        activity.addContentView(this, mlp);
        setVisibility(BuildConfig.DEBUG ? VISIBLE : INVISIBLE);
    }

    @Override
    public void start() {
        loadUrl(mUrl);
    }

    @Override
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
        mActivity = null;
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
        private final Parser mParser;
        private final MutableLiveData<String> mLiveData;
        private boolean mExtracted = false;

        public SysWebClient(Parser parser, MutableLiveData<String> liveData) {
            mParser = parser;
            mLiveData = liveData;
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            parsingLog(url);
            judgeExtracted(url);
            return super.shouldInterceptRequest(view, url);
        }

        @Nullable
        @Override
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return shouldInterceptRequest(view, request.getUrl().toString());
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            parsingLog(url);
            judgeExtracted(url);
            super.onLoadResource(view, url);
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

        private void judgeExtracted(String url) {
            synchronized (ParserWebView.class) {
                if (mParser.isVideoUrl(url) && !mExtracted) {
                    mExtracted = true;
                    mLiveData.postValue(url);
                }
            }
        }

        private void parsingLog(String url) {
            synchronized (ParserWebView.class) {
                LogUtils.iTag(TAG, "LoadURL: " + url);
                Evtor.instance.subscribe("parserLog").emit(url);
            }
        }

        private void onParsedError(CharSequence error) {
            LogUtils.eTag(TAG, error);
            // Evtor.instance.subscribe("parsingError").emit(error.toString());
        }
    }
}
