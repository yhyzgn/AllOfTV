package com.yhy.all.of.tv.parse;

import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.App;
import com.yhy.all.of.tv.component.base.BaseActivity;
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

        mWv = mApp.isX5Already() ? new ParserWebViewX5(mActivity) : new ParserWebViewDefault(mActivity);
        mWv.attach(mActivity, this, url() + url, liveData);
        mWv.start();
    }

    @Override
    public void stop(boolean destroy) {
        if (null != mWv) {
            mWv.stop(destroy);
        }
    }
}
