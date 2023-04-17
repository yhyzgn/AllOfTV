package com.yhy.all.of.tv.parse;

import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.App;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.ui.DetailActivity;
import com.yhy.all.of.tv.widget.web.ParserWebView;
import com.yhy.all.of.tv.widget.web.ParserWebViewDefault;
import com.yhy.all.of.tv.widget.web.ParserWebViewX5;

/**
 * Created on 2023-04-13 22:12
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractParser implements Parser {
    protected BaseActivity mActivity;
    protected App mApp;

    private ParserWebView mWv;

    @Override
    public void load(BaseActivity activity, MutableLiveData<String> liveData, String url) {
        mActivity = activity;
        mApp = mActivity.mApp;

        // 先移除上次的，优化页面 view 栈
        DetailActivity detailActivity = null;
        if (activity instanceof DetailActivity) {
            detailActivity = (DetailActivity) mActivity;
            ParserWebView lastWebView = detailActivity.getLastParserWebView();
            if (null != lastWebView) {
                lastWebView.stop(true);
            }
        }

        mWv = mApp.isX5Already() ? new ParserWebViewX5(mActivity) : new ParserWebViewDefault(mActivity);
        mWv.attach(mActivity, this, url() + url, liveData);
        // 记录一下本次的 WebView
        if (null != detailActivity) {
            detailActivity.setLastParserWebView(mWv);
        }
        
        mWv.start();
    }

    @Override
    public void stop(boolean destroy) {
        if (null != mWv) {
            mWv.stop(destroy);
        }
    }
}
