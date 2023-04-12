package com.yhy.all.of.tv.ui;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.component.router.FinishRouterCallback;
import com.yhy.router.EasyRouter;
import com.yhy.router.annotation.Router;

/**
 * 闪屏页
 * <p>
 * Created on 2023-04-05 16:39
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Router(url = "/activity/splash")
public class SplashActivity extends BaseActivity {

    private LinearLayoutCompat llLoading;

    @Override
    protected int layout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        llLoading = $(R.id.ll_loading);
    }

    @Override
    protected void initData() {
        // 进入主页
        llLoading.postDelayed(() -> {
            EasyRouter.getInstance()
                    .with(this)
                    .to("/activity/main")
                    .go(FinishRouterCallback.get(this));
        }, 300);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void setDefault() {
    }
}
