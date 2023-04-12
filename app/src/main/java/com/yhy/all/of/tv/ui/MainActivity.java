package com.yhy.all.of.tv.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.ChanRegister;
import com.yhy.all.of.tv.component.adapter.HomeTabAdapter;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.component.base.BaseLazyFragment;
import com.yhy.all.of.tv.widget.NoScrollViewPager;
import com.yhy.all.of.tv.widget.ViewObj;
import com.yhy.router.annotation.Router;

import java.util.ArrayList;
import java.util.List;

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

    private LinearLayout topLayout;
    private TextView tvName;
    private ImageView tvWifi;
    private ImageView tvFind;
    private ImageView tvStyle;
    private ImageView tvDrawer;
    private ImageView tvMenu;
    private LinearLayout contentLayout;
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
    private long mExitTime = 0;

    private byte topHide = 0;

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
        tvStyle = $(R.id.tvStyle);
        tvDrawer = $(R.id.tvDrawer);
        tvMenu = $(R.id.tvMenu);

        contentLayout = $(R.id.contentLayout);
        trvTab = $(R.id.trvTab);
        nvpTab = $(R.id.nvpTab);

        mTabAdapter = new HomeTabAdapter();
        trvTab.setLayoutManager(new V7LinearLayoutManager(this, 0, false));
        trvTab.setSpacingWithMargins(0, AutoSizeUtils.dp2px(this, 10.0f));
        trvTab.setAdapter(mTabAdapter);
    }

    @Override
    protected void initData() {
        mTabAdapter.setNewInstance(ChanRegister.instance.getChanList().get(0).tabList());

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

        trvTab.requestFocus();
    }

    @Override
    protected void initEvent() {
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
                    // if ((baseLazyFragment instanceof GridFragment) && !sortAdapter.getItem(position).filters.isEmpty()) {// 弹出筛选
                    //     ((GridFragment) baseLazyFragment).showFilter();
                    // } else if (baseLazyFragment instanceof UserFragment) {
                    //     showSiteSwitch();
                    // }
                }
            }
        });
    }

    private void loadTab(Chan.Tab tab) {

    }

    @Override
    protected void setDefault() {
    }

    private void showFilterIcon(int count) {
        boolean activated = count > 0;
        currentView.findViewById(R.id.tvFilter).setVisibility(View.VISIBLE);
        ImageView imgView = currentView.findViewById(R.id.tvFilter);
        imgView.setColorFilter(activated ? getThemeColor() : Color.WHITE);
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
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 20.0f)),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 0.0f))),
                    ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 50.0f)),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 1.0f))),
                    ObjectAnimator.ofFloat(this.topLayout, "alpha", 1.0f, 0.0f));
            animatorSet.setDuration(250);
            animatorSet.start();
            tvName.setFocusable(false);
            tvWifi.setFocusable(false);
            tvFind.setFocusable(false);
            tvStyle.setFocusable(false);
            tvDrawer.setFocusable(false);
            tvMenu.setFocusable(false);
            return;
        }
        // Show Top =======================================================
        if (!hide && topHide == 1) {
            animatorSet.playTogether(ObjectAnimator.ofObject(viewObj, "marginTop", new IntEvaluator(),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 0.0f)),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 20.0f))),
                    ObjectAnimator.ofObject(viewObj, "height", new IntEvaluator(),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 1.0f)),
                            Integer.valueOf(AutoSizeUtils.mm2px(this, 50.0f))),
                    ObjectAnimator.ofFloat(this.topLayout, "alpha", 0.0f, 1.0f));
            animatorSet.setDuration(250);
            animatorSet.start();
            tvName.setFocusable(true);
            tvWifi.setFocusable(true);
            tvFind.setFocusable(true);
            tvStyle.setFocusable(true);
            tvDrawer.setFocusable(true);
            tvMenu.setFocusable(true);
        }
    }
}
