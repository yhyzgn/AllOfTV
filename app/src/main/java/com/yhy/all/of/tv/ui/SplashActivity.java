package com.yhy.all.of.tv.ui;

import androidx.constraintlayout.widget.ConstraintLayout;

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

    private ConstraintLayout clRoot;

    @Override
    protected int layout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        clRoot = $(R.id.clRoot);
    }

    @Override
    protected void initData() {
        setLoadSir(clRoot);
        showLoading();

        // 进入主页
        clRoot.postDelayed(() -> {
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
