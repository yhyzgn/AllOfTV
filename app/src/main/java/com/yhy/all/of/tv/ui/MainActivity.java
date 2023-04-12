package com.yhy.all.of.tv.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.Group;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.ChanRegister;
import com.yhy.all.of.tv.component.adapter.ChanTabContentVPAdapter;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.component.presenter.TabVideoTypePresenter;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.utils.ViewUtils;
import com.yhy.all.of.tv.widget.ScaleConstraintLayout;
import com.yhy.all.of.tv.widget.TabHorizontalGridView;
import com.yhy.all.of.tv.widget.TabVerticalGridView;
import com.yhy.all.of.tv.widget.TabViewPager;
import com.yhy.router.annotation.Router;

import java.util.List;

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
public class MainActivity extends BaseActivity implements ViewTreeObserver.OnGlobalFocusChangeListener, View.OnFocusChangeListener {
    private final static List<Chan> CHAN_LIST = ChanRegister.instance.getChanList();
    private static final String TAG = "MainActivity";
    private DrawerLayout dlMain;
    private LinearLayoutCompat clMain;
    private TabVerticalGridView vgvMenu;
    private ScaleConstraintLayout sclSearch;
    private ScaleConstraintLayout sclHistory;
    private ScaleConstraintLayout sclFavor;
    private ScaleConstraintLayout sclSettings;
    private View mLastFocusedView;
    private TabHorizontalGridView hgTitle;
    private TabViewPager vpContent;
    private TabVideoTypePresenter mVideoTypePresenter;
    private ArrayObjectAdapter mVideoTypeAdapter;
    private ChanTabContentVPAdapter mVpAdapter;

    private boolean isFirstIn = true;
    private int mCurrentPageIndex = 0;
    private boolean isSkipTabFromViewPager = false;

    private TextView mOldTitle;

    private final OnChildViewHolderSelectedListener onChildViewHolderSelectedListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subPosition) {
            super.onChildViewHolderSelected(parent, child, position, subPosition);

            if (child != null && position != mCurrentPageIndex) {
                child.itemView.requestFocus();

                Log.e(TAG, "onChildViewHolderSelected: 000 isSkipTabFromViewPager" + isSkipTabFromViewPager);
                TextView currentTitle = child.itemView.findViewById(R.id.tv_scv_item);
                if (isSkipTabFromViewPager) {
                    Log.e(TAG, "onChildViewHolderSelected: 111");

                    if (mOldTitle != null) {
                        Log.e(TAG, "onChildViewHolderSelected: 222");

                        mOldTitle.setTextColor(getResources().getColor(R.color.colorWhite));
                        Paint paint = mOldTitle.getPaint();
                        if (paint != null) {
                            paint.setFakeBoldText(false);
                            // viewpager切页标题不刷新，调用invalidate刷新
                            mOldTitle.invalidate();
                        }
                    }
                    currentTitle.setTextColor(getResources().getColor(R.color.colorBlue));
                    Paint paint = currentTitle.getPaint();
                    if (paint != null) {
                        paint.setFakeBoldText(true);
                        // viewpager切页标题不刷新，调用invalidate刷新
                        currentTitle.invalidate();
                    }
                }
                mOldTitle = currentTitle;
            }

            isSkipTabFromViewPager = false;
            Log.e(TAG, "onChildViewHolderSelected mViewPager != null: " + (vpContent != null) + " position:" + position);
            setCurrentItemPosition(position);
        }
    };

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        dlMain = $(R.id.dl_main);
        clMain = $(R.id.cl_main);

        vgvMenu = $(R.id.vgv_menu);

        sclSearch = $(R.id.scl_search);
        sclHistory = $(R.id.scl_history);
        sclFavor = $(R.id.scl_favor);
        sclSettings = $(R.id.scl_settings);

        hgTitle = $(R.id.hg_title);
        vpContent = $(R.id.vp_content);

        vgvMenu.setNumColumns(1);
        vgvMenu.setVerticalSpacing(ViewUtils.dp2px(2));

        ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ChanPresenter());
        CHAN_LIST.forEach(adapter::add);
        ItemBridgeAdapter iba = new ItemBridgeAdapter(adapter);
        vgvMenu.setAdapter(iba);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(iba, FocusHighlight.ZOOM_FACTOR_LARGE, false);

        hgTitle.setHorizontalSpacing(ViewUtils.dp2px(4));
        mVideoTypePresenter = new TabVideoTypePresenter();
        mVideoTypeAdapter = new ArrayObjectAdapter(mVideoTypePresenter);
        ItemBridgeAdapter tempAdapter = new ItemBridgeAdapter(mVideoTypeAdapter);
        hgTitle.setAdapter(tempAdapter);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(tempAdapter, FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

        vpContent.setOffscreenPageLimit(2);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        dlMain.addDrawerListener(new ActionBarDrawerToggle(this, dlMain, android.R.string.yes, android.R.string.cancel) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                clMain.setTranslationX(slideX);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                vgvMenu.requestFocus();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hgTitle.getChildAt(hgTitle.getSelectedPosition()).requestFocus();
            }
        });

        vgvMenu.addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subPosition) {
                super.onChildViewHolderSelected(parent, child, position, subPosition);
                Log.e(TAG, "onChildViewHolderSelected: " + position);
                if (vgvMenu == null) {
                    return;
                }
                Log.e(TAG, "onChildViewHolderSelected: " + "　isPressUp:" + vgvMenu.isPressUp() + " isPressDown:" + vgvMenu.isPressDown());
                menuToChan(position);
            }
        });

        sclSearch.setOnFocusChangeListener(this);
        sclHistory.setOnFocusChangeListener(this);
        sclFavor.setOnFocusChangeListener(this);
        sclSettings.setOnFocusChangeListener(this);

        hgTitle.addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subPosition) {
                super.onChildViewHolderSelected(parent, child, position, subPosition);
                child.itemView.setOnFocusChangeListener(MainActivity.this);
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        hgTitle.addOnChildViewHolderSelectedListener(onChildViewHolderSelectedListener);

        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected position: " + position);
                if (isFirstIn) {
                    isFirstIn = false;
                } else {
                    isSkipTabFromViewPager = true;
                }
                if (position != mCurrentPageIndex) {
                    hgTitle.setSelectedPosition(position);
                }
            }
        });
    }

    @Override
    protected void setDefault() {
        vgvMenu.postDelayed(() -> menuToChan(vgvMenu.getSelectedPosition()), 200);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.iTag(TAG, event.getAction(), event.getKeyCode());
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MENU:
                        // 菜单按钮
                        if (toggleMenu()) {
                            return true;
                        }
                        break;
                    case KeyEvent.KEYCODE_BACK:
                        if (closeMenu()) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            rememberLastFocusedView(v);
        }
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        Log.e(TAG, "onGlobalFocusChanged newFocus: " + newFocus);
        Log.e(TAG, "onGlobalFocusChanged oldFocus: " + oldFocus);
        if (newFocus == null || oldFocus == null) {
            return;
        }
        if (newFocus.getId() == R.id.tv_scv_item && oldFocus.getId() == R.id.tv_scv_item) {
            ((TextView) newFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);
            ((TextView) oldFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) oldFocus).getPaint().setFakeBoldText(false);
        } else if (newFocus.getId() == R.id.tv_scv_item && oldFocus.getId() != R.id.tv_scv_item) {
            ((TextView) newFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);
        } else if (newFocus.getId() != R.id.tv_scv_item && oldFocus.getId() == R.id.tv_scv_item) {
            ((TextView) oldFocus).setTextColor(getResources().getColor(R.color.colorBlue));
            ((TextView) oldFocus).getPaint().setFakeBoldText(true);
        }
    }

    private boolean openMenu() {
        if (dlMain.isOpen()) {
            return false;
        }
        dlMain.open();
        View currentMenuView = vgvMenu.getChildAt(vgvMenu.getSelectedPosition());
        if (null != currentMenuView) {
            currentMenuView.requestFocus();
        }
        return true;
    }

    private boolean closeMenu() {
        if (!dlMain.isOpen()) {
            return false;
        }
        dlMain.close();
        if (null != mLastFocusedView) {
            mLastFocusedView.requestFocus();
        }
        return true;
    }

    private boolean toggleMenu() {
        if (dlMain.isOpen()) {
            return closeMenu();
        }
        return openMenu();
    }

    private void rememberLastFocusedView(View view) {
        mLastFocusedView = view;
    }

    private void menuToChan(int position) {
        LogUtils.iTag(TAG, "当前菜单选中项", position);
        if (position < 0 || position >= CHAN_LIST.size()) {
            return;
        }

        Chan chan = CHAN_LIST.get(position);
        refreshPager(chan);
    }

    private Chan currentChan() {
        return CHAN_LIST.get(vgvMenu.getSelectedPosition());
    }

    private Chan.Tab currentTab() {
        return currentChan().tabList().get(hgTitle.getSelectedPosition());
    }

    private void refreshPager(Chan chan) {
        LogUtils.iTag(TAG, "Chan = " + chan.name());
        mVideoTypeAdapter.removeItems(0, mVideoTypeAdapter.size());
        mVideoTypeAdapter.addAll(0, chan.tabList());

        mVpAdapter = new ChanTabContentVPAdapter(getSupportFragmentManager(), chan.tabList());
        vpContent.setAdapter(mVpAdapter);
    }

    private void setCurrentItemPosition(int position) {
        if (vpContent != null && position != mCurrentPageIndex) {
            mCurrentPageIndex = position;
            vpContent.setCurrentItem(position);
        }
    }

    public View getHgTitle() {
        return hgTitle;
    }

    public Group getGroup() {
        return null;
    }

    private static class ChanPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            AppCompatTextView tv = new AppCompatTextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
            tv.setPadding(0, ViewUtils.dp2px(2), 0, ViewUtils.dp2px(2));
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.selector_focus_bg_corner15_without_default_bg);
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            AppCompatTextView tv = (AppCompatTextView) viewHolder.view;
            Chan chan = (Chan) item;
            tv.setText(chan.name());

            viewHolder.view.setFocusable(true);
            viewHolder.view.setFocusableInTouchMode(true);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }
}
