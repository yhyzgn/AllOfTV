package com.yhy.all.of.tv.component.base;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.yhy.all.of.tv.App;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.component.callback.EmptyCallback;
import com.yhy.all.of.tv.component.callback.LoadingCallback;
import com.yhy.player.utils.CutoutUtils;

import es.dmoral.toasty.Toasty;
import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Activity 基类
 * <p>
 * Created on 2023-01-19 16:38
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt {

    public App mApp;

    private LoadService mLoadService;

    private static float screenRatio = -100.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(true);
        vidHideSysBar();
        hideSysBar();
        // 设置刘海
        CutoutUtils.adaptCutoutAboveAndroidP(this, true);

        try {
            if (screenRatio < 0) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                screenRatio = (float) Math.max(screenWidth, screenHeight) / (float) Math.min(screenWidth, screenHeight);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }

        super.onCreate(savedInstanceState);

        mApp = (App) getApplication();

        beforeLayout();
        setContentView(layout());

        initView();
        initData();
        initEvent();
        setDefault();
        setScreenOn();
    }

    protected void beforeLayout() {
    }

    @LayoutRes
    protected abstract int layout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void setDefault();

    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    public void success(String text) {
        Toasty.success(this, text).show();
    }

    public void warning(String text) {
        Toasty.warning(this, text).show();
    }

    public void error(String text) {
        Toasty.error(this, text).show();
    }

    protected void setLoadSir(View view) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault().register(view, (Callback.OnReloadListener) v -> {
                initData();
            });
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    protected void showEmpty() {
        if (null != mLoadService) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    protected void showSuccess() {
        if (null != mLoadService) {
            mLoadService.showSuccess();
        }
    }

    @Override
    public float getSizeInDp() {
        return isBaseOnWidth() ? 1280 : 720;
    }

    @Override
    public boolean isBaseOnWidth() {
        return !(screenRatio >= 4.0f);
    }

    @Override
    public Resources getResources() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfCustomAdapt(super.getResources(), this);
        }
        return super.getResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vidHideSysBar();
        hideSysBar();
        hideSystemUI(true);
    }

    public static int isEdgeToEdgeEnabled(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android");
        if (resourceId > 0) {
            return resources.getInteger(resourceId);
        }
        return 0;
    }

    public void hideSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public void vidHideSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public void hideSystemUI(boolean shownavbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiVisibility |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            uiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            if (!shownavbar) {
                uiVisibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        }
    }

    public void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        }
    }

    public boolean hasPermission(String permission) {
        boolean has = true;
        try {
            has = PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    public boolean supportsPiPMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public boolean supportsTouch() {
        return getPackageManager().hasSystemFeature("android.hardware.touchscreen");
    }

    public void setScreenBrightness(float amt) {
        WindowManager.LayoutParams lparams = getWindow().getAttributes();
        lparams.screenBrightness = amt;
        getWindow().setAttributes(lparams);
    }

    public void setScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setScreenOff() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public int getThemeColor() {
        TypedArray a = obtainStyledAttributes(R.styleable.themeColor);
        int themeColor = a.getColor(R.styleable.themeColor_color_theme, 0);
        a.recycle();
        return themeColor;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
