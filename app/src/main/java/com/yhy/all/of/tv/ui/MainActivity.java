package com.yhy.all.of.tv.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager.widget.ViewPager;

import com.azhon.appupdate.listener.OnDownloadListenerAdapter;
import com.azhon.appupdate.manager.DownloadManager;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.yhy.all.of.tv.BuildConfig;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.api.model.VersionInfo;
import com.yhy.all.of.tv.api.of.github.GithubApi;
import com.yhy.all.of.tv.cache.KV;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.ChanRegister;
import com.yhy.all.of.tv.component.adapter.HomeTabAdapter;
import com.yhy.all.of.tv.component.adapter.SelectDialogAdapter;
import com.yhy.all.of.tv.component.adapter.VideoPagerAdapter;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.component.base.BaseLazyFragment;
import com.yhy.all.of.tv.ui.fragment.GridFragment;
import com.yhy.all.of.tv.utils.FileUtils;
import com.yhy.all.of.tv.utils.JsonUtils;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.utils.SysUtils;
import com.yhy.all.of.tv.widget.DefaultTransformer;
import com.yhy.all.of.tv.widget.FixedSpeedScroller;
import com.yhy.all.of.tv.widget.NoScrollViewPager;
import com.yhy.all.of.tv.widget.ViewObj;
import com.yhy.all.of.tv.widget.dialog.SelectDialog;
import com.yhy.all.of.tv.widget.dialog.UpdateDialog;
import com.yhy.router.annotation.Router;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 主页面
 * <p>
 * Created on 2023-04-05 17:08
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Router(url = "/activity/main")
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private final static String CURRENT_CHAN_NAME = "current_chan_name";
    private final static String FLAG_CHECKOUT_CHAN_EXT_NAME = "flag_checkout_chan_ext";
    private final static Integer FLAG_CHECKOUT_CHAN_EXT_VALUE = 1024;
    private final static int STICKY_EXIT_MS = 3000;

    private LinearLayout topLayout;
    private TextView tvName;
    private ImageView tvWifi;
    private ImageView tvFind;
    private ImageView tvSettings;
    private TvRecyclerView trvTab;
    private NoScrollViewPager nvpTab;
    private HomeTabAdapter mTabAdapter;

    private final List<BaseLazyFragment> fragments = new ArrayList<>();

    private View currentView;
    private boolean isDownOrUp = false;
    private boolean sortChange = false;
    private int currentSelected = 0;
    private int sortFocused = 0;
    public View sortFocusView = null;
    private final Handler mHandler = new Handler();

    private byte topHide = 0;

    private List<Chan> mChanList;
    private Chan mCurrentChan;
    private VideoPagerAdapter pageAdapter;

    private long mLastExitTime;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        topLayout = $(R.id.topLayout);
        tvName = $(R.id.tvName);
        tvWifi = $(R.id.tvWifi);
        tvFind = $(R.id.tvFind);
        tvSettings = $(R.id.tvSettings);

        LinearLayout contentLayout = $(R.id.contentLayout);
        trvTab = $(R.id.trvTab);
        nvpTab = $(R.id.nvpTab);

        mTabAdapter = new HomeTabAdapter();
        trvTab.setLayoutManager(new V7LinearLayoutManager(this, 0, false));
        trvTab.setSpacingWithMargins(0, AutoSizeUtils.dp2px(this, 10.0f));
        trvTab.setAdapter(mTabAdapter);

        setLoadSir(contentLayout);
    }

    @Override
    protected void initData() {
        showLoading();

        // 新版本检查
        checkNewVersion();

        mChanList = ChanRegister.instance.getChanList();

        String currentChanName = KV.instance.kv().getString(CURRENT_CHAN_NAME);
        mCurrentChan = !TextUtils.isEmpty(currentChanName) ? ChanRegister.instance.getChanByName(currentChanName) : mChanList.get(0);

        mTabAdapter.setNewInstance(mCurrentChan.tabList());

        if (isNetworkAvailable()) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                tvWifi.setImageDrawable(getResources().getDrawable(R.drawable.hm_wifi));
            } else if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                tvWifi.setImageDrawable(getResources().getDrawable(R.drawable.hm_mobile));
            } else if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET) {
                tvWifi.setImageDrawable(getResources().getDrawable(R.drawable.hm_lan));
            }
        }

        tvName.setText(mCurrentChan.name());
        trvTab.requestFocus();

        initViewPager();

        trvTab.requestFocus();

        showSuccess();
    }

    private void initViewPager() {
        mCurrentChan.tabList().forEach(it -> fragments.add(GridFragment.newInstance(it)));
        pageAdapter = new VideoPagerAdapter(getSupportFragmentManager(), fragments);

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this, new AccelerateInterpolator());
            field.set(nvpTab, scroller);
            scroller.setDuration(300);
        } catch (Exception e) {
        }

        nvpTab.setPageTransformer(true, new DefaultTransformer());
        nvpTab.setAdapter(pageAdapter);
        nvpTab.setCurrentItem(currentSelected, false);
    }

    @Override
    protected void initEvent() {
        tvName.setOnClickListener(v -> showChanDialog());

        tvWifi.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        tvFind.setOnClickListener(v -> {
            warning("开发中");
        });

        tvSettings.setOnClickListener(v -> {
            warning("开发中");
        });

        trvTab.setOnItemListener(new TvRecyclerView.OnItemListener() {
            public void onItemPreSelected(TvRecyclerView tvRecyclerView, View view, int position) {
                if (view != null && !MainActivity.this.isDownOrUp) {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(250).start();
                    TextView tv = view.findViewById(R.id.tvTitle);
                    tv.getPaint().setFakeBoldText(false);
                    tv.setTextColor(MainActivity.this.getResources().getColor(R.color.color_FFFFFF_50));
                    tv.invalidate();
                    view.findViewById(R.id.tvFilter).setVisibility(View.GONE);
                }
            }

            public void onItemSelected(TvRecyclerView tvRecyclerView, View view, int position) {
                if (view != null) {
                    MainActivity.this.currentView = view;
                    MainActivity.this.isDownOrUp = false;
                    MainActivity.this.sortChange = true;
                    view.animate().scaleX(1.1f).scaleY(1.1f).setInterpolator(new BounceInterpolator()).setDuration(250).start();
                    TextView tv = view.findViewById(R.id.tvTitle);
                    tv.getPaint().setFakeBoldText(true);
                    tv.setTextColor(MainActivity.this.getResources().getColor(R.color.color_FFFFFF));
                    tv.invalidate();

                    Chan.Tab tab = mTabAdapter.getItem(position);
                    loadTab(tab);
                    MainActivity.this.sortFocusView = view;
                    MainActivity.this.sortFocused = position;
                    mHandler.removeCallbacks(mDataRunnable);
                    mHandler.postDelayed(mDataRunnable, 200);
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                if (itemView != null && currentSelected == position) {
                    BaseLazyFragment baseLazyFragment = fragments.get(currentSelected);
                    // Do Nothing
                }
            }
        });
    }

    private void loadTab(Chan.Tab tab) {
    }

    @Override
    protected void setDefault() {
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (topHide < 0) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                // 按了菜单键
                showChanDialog();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - mLastExitTime > STICKY_EXIT_MS) {
            mLastExitTime = now;
            warning("再按一次退出应用");
            return;
        }
        super.onBackPressed();
    }

    private void showChanDialog() {
        SelectDialog<Chan> dialog = new SelectDialog<>(this);
        // Multi Column Selection
        int spanCount = (int) Math.floor(mChanList.size() / 10.0f);
        if (spanCount <= 1) spanCount = 1;
        if (spanCount >= 3) spanCount = 3;

        TvRecyclerView tvRecyclerView = dialog.findViewById(R.id.list);
        tvRecyclerView.setLayoutManager(new V7GridLayoutManager(dialog.getContext(), spanCount));
        LinearLayout cl_root = dialog.findViewById(R.id.cl_root);
        ViewGroup.LayoutParams clp = cl_root.getLayoutParams();
        if (spanCount != 1) {
            clp.width = AutoSizeUtils.mm2px(dialog.getContext(), 400 + 260 * (spanCount - 1));
        }

        dialog.setTip(getString(R.string.dia_source));
        dialog.setAdapter(new SelectDialogAdapter.SelectDialogInterface<>() {
            @Override
            public void click(Chan chan, int pos) {
                dialog.dismiss();

                checkToChan(chan);
            }

            @Override
            public String getDisplay(Chan chan) {
                return chan.name();
            }
        }, new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull Chan oldItem, @NonNull @NotNull Chan newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull Chan oldItem, @NonNull @NotNull Chan newItem) {
                return oldItem.name().equals(newItem.name());
            }
        }, mChanList, ChanRegister.instance.getPositionOfChan(mCurrentChan));
        dialog.show();
    }

    private void checkToChan(Chan chan) {
        mCurrentChan = chan;
        KV.instance.kv().putString(CURRENT_CHAN_NAME, chan.name());
        restartPage();
    }

    private void restartPage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(FLAG_CHECKOUT_CHAN_EXT_NAME, FLAG_CHECKOUT_CHAN_EXT_VALUE);
        startActivity(intent);
        finish();
    }

    private void showFilterIcon(int count) {
        boolean activated = count > 0;
        currentView.findViewById(R.id.tvFilter).setVisibility(View.VISIBLE);
        ImageView imgView = currentView.findViewById(R.id.tvFilter);
        imgView.setColorFilter(activated ? getThemeColor() : Color.WHITE);
    }

    private void checkNewVersion() {
        if (BuildConfig.DEBUG) {
            // 开发模式下不检查了
            return;
        }

        Intent intent = getIntent();
        if (null != intent) {
            int flagCheckoutChanExt = intent.getIntExtra(FLAG_CHECKOUT_CHAN_EXT_NAME, 0);
            if (flagCheckoutChanExt == FLAG_CHECKOUT_CHAN_EXT_VALUE) {
                // 此时是在切换频道
                return;
            }
        }

        // 仅在真正的应用启动时进行版本检查
        GithubApi.instance.versionQuery(vi -> {
            if (null != vi && null != vi.assets && !vi.assets.isEmpty()) {
                // 存在版本信息
                LogUtils.iTag(TAG, JsonUtils.toJson(vi));
                if (!TextUtils.isEmpty(vi.name) && vi.name.contains("_")) {
                    int buildNumber = Integer.parseInt(vi.name.split("_")[1]);
                    long versionCode = SysUtils.getVersionCode();
                    if (versionCode < buildNumber) {
                        // 发现新版本
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        // 设置时区UTC
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        // 格式化，转当地时区时间
                        Date date = null;
                        try {
                            date = df.parse(vi.publishedAt);
                        } catch (ParseException e) {
                            LogUtils.eTag(TAG, e);
                        }
                        df.applyPattern("yyyy-MM-dd HH:mm:ss");
                        // 默认时区
                        df.setTimeZone(TimeZone.getDefault());

                        UpdateDialog ud = new UpdateDialog(this)
                            .setName(vi.name)
                            .setTime(null != date ? df.format(date) : vi.publishedAt)
                            .setLog(vi.body);
                        ud.setOnUpdatedListener(() -> {
                                downloadApk(vi, new OnDownloadListenerAdapter() {
                                    @Override
                                    public void start() {
                                        ud.startDownloading();
                                    }

                                    @Override
                                    public void downloading(int max, int progress) {
                                        ud.setProgress(max, progress);
                                    }

                                    @Override
                                    public void done(@NonNull File apk) {
                                        ud.dismiss();
                                    }
                                });
                            })
                            .show();
                    }
                }
            }
        });
    }

    private void downloadApk(VersionInfo version, OnDownloadListenerAdapter downloadListenerAdapter) {
        List<VersionInfo.AssetsBean> assets = version.assets.stream().filter(it -> it.browserDownloadUrl.endsWith(".apk")).collect(Collectors.toList());
        if (assets.isEmpty()) {
            return;
        }
        VersionInfo.AssetsBean asset = assets.get(0);
        String apkUrl = asset.browserDownloadUrl;
        String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);

        DownloadManager manager = new DownloadManager.Builder(this)
            .apkName(apkName)
            .apkUrl("https://ghp.ci/" + apkUrl)
            .apkDescription(!TextUtils.isEmpty(version.body) ? version.body : "检查到新版本")
            .smallIcon(R.mipmap.ic_launcher)
            .apkSize(FileUtils.formatSize(asset.size))
            .showNotification(true)
            .forcedUpgrade(true)
            .jumpInstallPage(true)
            .showNewerToast(false)
            .showBgdToast(false)
            .onDownloadListener(downloadListenerAdapter)
            .build();

        manager.download();
    }

    private final Runnable mDataRunnable = new Runnable() {
        @Override
        public void run() {
            if (sortChange) {
                sortChange = false;
                if (sortFocused != currentSelected) {
                    currentSelected = sortFocused;
                    nvpTab.setCurrentItem(sortFocused, false);
                    changeTop(sortFocused != 0);
                }
            }
        }
    };

    private void changeTop(boolean hide) {
        ViewObj viewObj = new ViewObj(topLayout, (ViewGroup.MarginLayoutParams) topLayout.getLayoutParams());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                topHide = (byte) (hide ? 1 : 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        // Hide Top =======================================================
        if (hide && topHide == 0) {
            animatorSet.playTogether(ObjectAnimator.ofObject(viewObj, "marginTop", new IntEvaluator(),
                    AutoSizeUtils.mm2px(this, 20.0f),
                    AutoSizeUtils.mm2px(this, 0.0f)),
                ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
                    AutoSizeUtils.mm2px(this, 50.0f),
                    AutoSizeUtils.mm2px(this, 1.0f)),
                ObjectAnimator.ofFloat(this.topLayout, "alpha", 1.0f, 0.0f));
            animatorSet.setDuration(250);
            animatorSet.start();
            tvName.setFocusable(false);
            tvWifi.setFocusable(false);
            tvFind.setFocusable(false);
            tvSettings.setFocusable(false);
            return;
        }
        // Show Top =======================================================
        if (!hide && topHide == 1) {
            animatorSet.playTogether(ObjectAnimator.ofObject(viewObj, "marginTop", new IntEvaluator(),
                    AutoSizeUtils.mm2px(this, 0.0f),
                    AutoSizeUtils.mm2px(this, 20.0f)),
                ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
                    AutoSizeUtils.mm2px(this, 1.0f),
                    AutoSizeUtils.mm2px(this, 50.0f)),
                ObjectAnimator.ofFloat(this.topLayout, "alpha", 0.0f, 1.0f));
            animatorSet.setDuration(250);
            animatorSet.start();
            tvName.setFocusable(true);
            tvWifi.setFocusable(true);
            tvFind.setFocusable(true);
            tvSettings.setFocusable(true);
        }
    }
}
