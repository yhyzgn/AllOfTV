package com.yhy.all.of.tv.component.router;

import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.router.callback.Callback;
import com.yhy.router.common.Transmitter;

/**
 * Created on 2023-01-20 01:23
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class FinishRouterCallback implements Callback {
    private BaseActivity mActivity;

    public FinishRouterCallback(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onSuccess(Transmitter transmitter) {
        mActivity.finish();
        mActivity = null;
    }

    @Override
    public void onError(Transmitter transmitter, Throwable e) {
        mActivity.error(e.getMessage());
    }

    public static FinishRouterCallback get(BaseActivity activity) {
        return new FinishRouterCallback(activity);
    }
}
