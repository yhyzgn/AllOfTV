package com.yhy.all.of.tv.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import androidx.lifecycle.ViewModelProvider;

import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.component.adapter.GridAdapter;
import com.yhy.all.of.tv.component.base.BaseLazyFragment;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.utils.FastClickCheckUtils;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.vm.SourceVM;
import com.yhy.all.of.tv.widget.LoadMoreView;
import com.yhy.router.EasyRouter;

import java.util.Stack;

/**
 * Created on 2023-04-13 11:23
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class GridFragment extends BaseLazyFragment {
    private static final String TAG = "GridFragment";
    private Chan.Tab mTab;
    private TvRecyclerView mGridView;
    private SourceVM sourceVM;
    private GridAdapter gridAdapter;
    private BaseLoadMoreModule loadMoreModule;
    private int page = 1;
    private int maxPage = 100;
    private boolean isLoad = false;
    private boolean isTop = true;
    private View focusedView = null;

    private static class GridInfo {
        public Chan.Tab tab;
        public TvRecyclerView mGridView;
        public GridAdapter gridAdapter;
        public int page = 1;
        public int maxPage = 1;
        public boolean isLoad = false;
        public View focusedView = null;
    }

    Stack<GridInfo> mGrids = new Stack<>(); // ui栈

    public static GridFragment newInstance(Chan.Tab tab) {
        return new GridFragment().setTab(tab);
    }

    public GridFragment setTab(Chan.Tab tab) {
        mTab = tab;
        return this;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_grid;
    }

    @Override
    protected void init() {
        initView();
        initViewModel();
        initData();
    }

    public boolean isFolderMode() {
        return false;
    }

    // 保存当前页面
    private void saveCurrentView() {
        if (this.mGridView == null) return;
        GridInfo info = new GridInfo();
        info.tab = this.mTab;
        info.mGridView = this.mGridView;
        info.gridAdapter = this.gridAdapter;
        info.page = this.page;
        info.maxPage = this.maxPage;
        info.isLoad = this.isLoad;
        info.focusedView = this.focusedView;
        this.mGrids.push(info);
    }

    // 丢弃当前页面，将页面还原成上一个保存的页面
    public boolean restoreView() {
        if (mGrids.empty()) return false;
        this.showSuccess();
        ((ViewGroup) mGridView.getParent()).removeView(this.mGridView); // 重父窗口移除当前控件
        GridInfo info = mGrids.pop();// 还原上次保存的控件
        this.mTab = info.tab;
        this.mGridView = info.mGridView;
        this.gridAdapter = info.gridAdapter;
        this.page = info.page;
        this.maxPage = info.maxPage;
        this.isLoad = info.isLoad;
        this.focusedView = info.focusedView;
        this.mGridView.setVisibility(View.VISIBLE);
        if (mGridView != null) mGridView.requestFocus();
        return true;
    }

    // 更改当前页面
    private void createView() {
        this.saveCurrentView(); // 保存当前页面
        if (mGridView == null) { // 从layout中拿view
            mGridView = findViewById(R.id.mGridView);
        } else { // 复制当前view
            TvRecyclerView v3 = new TvRecyclerView(this.mContext);
            v3.setSpacingWithMargins(10, 10);
            v3.setLayoutParams(mGridView.getLayoutParams());
            v3.setPadding(mGridView.getPaddingLeft(), mGridView.getPaddingTop(), mGridView.getPaddingRight(), mGridView.getPaddingBottom());
            v3.setClipToPadding(mGridView.getClipToPadding());
            ((ViewGroup) mGridView.getParent()).addView(v3);
            mGridView.setVisibility(View.GONE);
            mGridView = v3;
            mGridView.setVisibility(View.VISIBLE);
        }
        mGridView.setHasFixedSize(true);
        gridAdapter = new GridAdapter(isFolderMode());
        gridAdapter.setAnimationEnable(true);
        loadMoreModule = gridAdapter.getLoadMoreModule();
        loadMoreModule.setAutoLoadMore(true);
        loadMoreModule.setEnableLoadMoreIfNotFullPage(false);
        this.page = 1;
        this.isLoad = false;
    }

    private void initView() {
        this.createView();
        if (isFolderMode()) {
            mGridView.setLayoutManager(new V7LinearLayoutManager(this.mContext, 1, false));
        } else {
            mGridView.setLayoutManager(new V7GridLayoutManager(this.mContext, isBaseOnWidth() ? 5 : 6));
        }
        mGridView.setAdapter(gridAdapter);

        loadMoreModule.setOnLoadMoreListener(() -> {
            LogUtils.iTag(TAG, "加载下一页啦");
            loadMoreModule.setEnableLoadMore(true);
            sourceVM.loadVideoList(mTab, page);
        });

        mGridView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).setInterpolator(new BounceInterpolator()).start();
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(300).setInterpolator(new BounceInterpolator()).start();
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });
        mGridView.setOnInBorderKeyEventListener((direction, focused) -> {
            if (direction == View.FOCUS_UP) {
            }
            return false;
        });
        gridAdapter.setOnItemClickListener((adapter, view, position) -> {
            FastClickCheckUtils.check(view);
            Video video = gridAdapter.getData().get(position);
            if (video != null) {
                EasyRouter.getInstance()
                    .with(this)
                    .to("/activity/detail")
                    .param("chanName", mTab.chan.name())
                    .param("video", video)
                    .go();
            }
        });

        gridAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            FastClickCheckUtils.check(view);
            Video video = gridAdapter.getData().get(position);
            if (video != null) {
            }
            return true;
        });
        loadMoreModule.setLoadMoreView(new LoadMoreView());
        setLoadSir(mGridView);
    }

    private void initViewModel() {
        if (sourceVM != null) {
            return;
        }
        sourceVM = new ViewModelProvider(this).get(SourceVM.class);
        sourceVM.videoListData.observe(this, videos -> {
            if (null == videos || videos.isEmpty()) {
                if (page == 1) {
                    showEmpty();
                }
                if (page > maxPage) {
                    warning("没有更多了");
                    loadMoreModule.loadMoreEnd();
                } else {
                    loadMoreModule.loadMoreComplete();
                }
                loadMoreModule.setEnableLoadMore(false);
                return;
            }

            if (page == 1) {
                showSuccess();
                isLoad = true;
                gridAdapter.setNewInstance(videos);
            } else {
                gridAdapter.addData(videos);
            }
            page++;
            if (page > maxPage) {
                loadMoreModule.loadMoreEnd();
                loadMoreModule.setEnableLoadMore(false);
            } else {
                loadMoreModule.loadMoreComplete();
                loadMoreModule.setEnableLoadMore(true);
            }
        });
    }

    public boolean isLoad() {
        return isLoad || !mGrids.empty(); // 如果有缓存页的话也可以认为是加载了数据的
    }

    private void initData() {
        showLoading();
        isLoad = false;
        scrollTop();
        sourceVM.loadVideoList(mTab, page);
    }

    public boolean isTop() {
        return isTop;
    }

    public void scrollTop() {
        isTop = true;
        mGridView.scrollToPosition(0);
    }

    public void forceRefresh() {
        page = 1;
        initData();
    }
}