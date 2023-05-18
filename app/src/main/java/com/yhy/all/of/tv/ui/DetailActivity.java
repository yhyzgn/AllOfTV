package com.yhy.all.of.tv.ui;

import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.common.base.Joiner;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.owen.tvrecyclerview.widget.V7StaggeredGridLayoutManager;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.cache.KV;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.ChanRegister;
import com.yhy.all.of.tv.component.adapter.PlayListVodAdapter;
import com.yhy.all.of.tv.component.adapter.SeriesFlagAdapter;
import com.yhy.all.of.tv.component.base.VideoActivity;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.parse.ParserRegister;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.utils.Md5Utils;
import com.yhy.all.of.tv.utils.ViewUtils;
import com.yhy.all.of.tv.widget.web.ParserWebView;
import com.yhy.evtor.Evtor;
import com.yhy.evtor.annotation.Subscribe;
import com.yhy.player.widget.TvPlayer;
import com.yhy.router.EasyRouter;
import com.yhy.router.annotation.Autowired;
import com.yhy.router.annotation.Router;

import java.util.List;
import java.util.Objects;

/**
 * 详情页
 * <p>
 * Created on 2023-04-13 16:35
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Router(url = "/activity/detail")
public class DetailActivity extends VideoActivity {
    private static final String TAG = "DetailActivity";
    private final static int STICKY_EXIT_FULL_MS = 3000;
    private final static int DURATION_FULL_ANIMATION = 300;
    @Autowired("chanName")
    public String mChanName;
    @Autowired("rootVideo")
    public Video mRootVideo;
    private String mRootTag;

    private MutableLiveData<String> mLiveData;

    private FrameLayout flPlayerAnchor;
    private FrameLayout flPlayerContainer;

    private RelativeLayout rlParsing;
    private TextView tvParsingLog;
    private TvPlayer tvPlayer;
    private TextView tvName;
    private TextView tvSite;
    private TextView tvYear;
    private TextView tvArea;
    private TextView tvLang;
    private TextView tvType;
    private TextView tvActor;
    private TextView tvDirector;
    private TextView tvDes;
    private TextView tvFullScreen;
    private TextView tvQuickSearch;
    private TextView tvSort;
    private TextView tvCollect;
    private ImageView tvPlayUrl;
    private TvRecyclerView trvParser;
    private TvRecyclerView trvPlayListVod;
    private List<Parser> mParserList;

    private int mCurrentParserIndex = 0;
    private SeriesFlagAdapter mSeriesFlagAdapter;
    private PlayListVodAdapter mPlayListVodAdapter;
    private int mCurrentPlayingIndex = 0;

    private ParserWebView mLastParserWebView;

    private int mOriginalX;

    private int mOriginalY;

    private int mOriginalWidth;

    private int mOriginalHeight;

    private int mScreenWidth;

    private int mScreenHeight;

    private long mLastExitFullTime;

    @Override
    protected int layout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        flPlayerAnchor = $(R.id.flPlayerAnchor);
        flPlayerContainer = $(R.id.flPlayerContainer);

        rlParsing = $(R.id.rlParsing);
        tvParsingLog = $(R.id.tvParsingLog);
        tvPlayer = $(R.id.tvPlayer);

        tvName = $(R.id.tvName);
        tvSite = $(R.id.tvSite);
        tvYear = $(R.id.tvYear);
        tvArea = $(R.id.tvArea);
        tvLang = $(R.id.tvLang);
        tvType = $(R.id.tvType);
        tvActor = $(R.id.tvActor);
        tvDirector = $(R.id.tvDirector);
        tvDes = $(R.id.tvDes);
        tvFullScreen = $(R.id.tvFullScreen);
        tvQuickSearch = $(R.id.tvQuickSearch);
        tvSort = $(R.id.tvSort);
        tvCollect = $(R.id.tvCollect);
        tvPlayUrl = $(R.id.ivPlayUrl);
        trvParser = $(R.id.trvParser);
        trvPlayListVod = $(R.id.trvPlayListVod);

        trvParser.setHasFixedSize(true);
        trvParser.setLayoutManager(new V7LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        trvPlayListVod.setHasFixedSize(true);
        trvPlayListVod.setLayoutManager(new V7StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL));

        setLoadSir($(R.id.vRoot));
    }

    @Override
    protected void initData() {
        showLoading();
        EasyRouter.getInstance().inject(this);
        Evtor.instance.register(this);

        mRootTag = Md5Utils.gen(mRootVideo.pageUrl);

        // 初始化一下
        getCurrentParser();

        refreshVideoInfo();

        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, url -> {
            Video vd = getCurrentPlayingVideo();
            // 用原始 url 来生成 tag，不能使用解析过的，因为每次解析可能都会变
            String vdTag = Md5Utils.gen(vd.pageUrl);

            // 获取播放进度并播放
            tvPlayer.play(vd.title, url, vdTag, KV.instance.getPosition(vdTag));

            tvPlayer.setVisibility(View.VISIBLE);
            rlParsing.setVisibility(View.GONE);

            // 停止 WebView 加载
            getCurrentParser().stop(true);
        });

        mParserList = ParserRegister.instance.supportedParserList(mChanName);

        // 加载播放列表
        loadPlayList();

        mSeriesFlagAdapter = new SeriesFlagAdapter(parser -> Objects.equals(getCurrentParser().name(), parser.name()));
        trvParser.setAdapter(mSeriesFlagAdapter);
        mSeriesFlagAdapter.setNewInstance(mParserList);

        mPlayListVodAdapter = new PlayListVodAdapter(video -> Objects.equals(getCurrentPlayingVideo().title, video.title));
        trvPlayListVod.setAdapter(mPlayListVodAdapter);

        // 全屏按钮获取焦点
        tvFullScreen.requestFocus();

        showSuccess();
    }

    @Override
    protected void initEvent() {
        flPlayerAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                flPlayerAnchor.getLocationOnScreen(location);
                mOriginalX = location[0];
                mOriginalY = location[1];
                mOriginalWidth = flPlayerAnchor.getWidth();
                mOriginalHeight = flPlayerAnchor.getHeight();

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mOriginalWidth, mOriginalHeight);
                lp.topMargin = mOriginalX;
                lp.leftMargin = mOriginalY;
                flPlayerContainer.setLayoutParams(lp);
                flPlayerContainer.setVisibility(View.VISIBLE);

                Size size = ViewUtils.getScreenSize(DetailActivity.this);
                mScreenWidth = size.getWidth();
                mScreenHeight = size.getHeight();

                // 移除
                flPlayerAnchor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvPlayer.setOnStartedListener(() -> {
            LogUtils.iTag(TAG, "开始播放了");
        });

        tvPlayer.setOnPositionChangedListener((url, tag, duration, bufferedPosition, currentPosition) -> {
            if (currentPosition == duration) {
                KV.instance.removePosition(tag);
                return;
            }
            KV.instance.storePosition(tag, currentPosition);
        });

        tvFullScreen.setOnClickListener(v -> {
            enterFullScreen();
        });

        tvQuickSearch.setOnClickListener(v -> {
            if (tvPlayer.isPlaying()) {
                tvPlayer.pause();
            } else {
                tvPlayer.resume();
            }
        });

        tvSort.setOnClickListener(v -> {
            tvPlayer.seekForward();
        });

        tvCollect.setOnClickListener(v -> {
            tvPlayer.seekBack();
        });

        mSeriesFlagAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == mCurrentParserIndex) {
                return;
            }
            int lastIndex = mCurrentParserIndex;
            mCurrentParserIndex = position;

            // 缓存一下解析器
            KV.instance.storeParser(mRootTag, getCurrentParser());

            adapter.notifyItemChanged(lastIndex);
            adapter.notifyItemChanged(mCurrentParserIndex);

            // 重新加载
            loadVideoAndPlay();
        });

        mPlayListVodAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == mCurrentPlayingIndex) {
                return;
            }

            int lastIndex = mCurrentPlayingIndex;
            mCurrentPlayingIndex = position;

            // 缓存下当前播放的集数
            KV.instance.storeEpisode(mRootTag, mCurrentPlayingIndex);

            adapter.notifyItemChanged(lastIndex);
            adapter.notifyItemChanged(mCurrentPlayingIndex);

            // 重新加载
            loadVideoAndPlay();
        });
    }

    @Override
    protected void setDefault() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Evtor.instance.unregister(this);
    }

    @Override
    protected TvPlayer player() {
        return tvPlayer;
    }

    @Override
    public void onBackPressed() {
        if (backFromFullScreen()) {
            return;
        }
        super.onBackPressed();
    }

    public void enterFullScreen() {
        if (tvPlayer.isInFullScreen()) {
            return;
        }

        Log.i(TAG, "Location.x = " + mOriginalX + ", Location.y = " + mOriginalY + ", OriginalWidth = " + mOriginalWidth + ", OriginalHeight = " + mOriginalHeight + ", ScreenWidth = " + mScreenWidth + ", ScreenHeight = " + mScreenHeight);

        // 执行动画
        ObjectAnimator scaleAnimationWidth = ObjectAnimator.ofObject(this, "width", new IntEvaluator(), mOriginalWidth, mScreenWidth);
        ObjectAnimator scaleAnimationHeight = ObjectAnimator.ofObject(this, "height", new IntEvaluator(), mOriginalHeight, mScreenHeight);
        ObjectAnimator translateAnimationX = ObjectAnimator.ofObject(this, "marginLeft", new IntEvaluator(), mOriginalX, 0);
        ObjectAnimator translateAnimationY = ObjectAnimator.ofObject(this, "marginTop", new IntEvaluator(), mOriginalY, 0);
        AnimatorSet as = new AnimatorSet();
        as.playTogether(scaleAnimationWidth, scaleAnimationHeight, translateAnimationX, translateAnimationY);
        as.setInterpolator(new FastOutSlowInInterpolator());
        as.setDuration(DURATION_FULL_ANIMATION);
        as.start();

        tvPlayer.setInFullScreen(true);
    }

    public boolean backFromFullScreen() {
        if (!tvPlayer.isInFullScreen()) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now - mLastExitFullTime > STICKY_EXIT_FULL_MS) {
            // 提示再按一次退出播放
            warning("再按一次退出播放");
            mLastExitFullTime = now;
            return true;
        }

        // 先执行动画
        ObjectAnimator scaleAnimationWidth = ObjectAnimator.ofObject(this, "width", new IntEvaluator(), mScreenWidth, mOriginalWidth);
        ObjectAnimator scaleAnimationHeight = ObjectAnimator.ofObject(this, "height", new IntEvaluator(), mScreenHeight, mOriginalHeight);
        ObjectAnimator translateAnimationX = ObjectAnimator.ofObject(this, "marginLeft", new IntEvaluator(), 0, mOriginalX);
        ObjectAnimator translateAnimationY = ObjectAnimator.ofObject(this, "marginTop", new IntEvaluator(), 0, mOriginalY);
        AnimatorSet as = new AnimatorSet();
        as.playTogether(scaleAnimationWidth, scaleAnimationHeight, translateAnimationX, translateAnimationY);
        as.setInterpolator(new FastOutSlowInInterpolator());
        as.setDuration(DURATION_FULL_ANIMATION);
        as.start();
        tvPlayer.setInFullScreen(false);
        return true;
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams params = flPlayerContainer.getLayoutParams();
        params.width = width;
        flPlayerContainer.setLayoutParams(params);
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams params = flPlayerContainer.getLayoutParams();
        params.height = height;
        flPlayerContainer.setLayoutParams(params);
    }

    public void setMarginLeft(int marginLeft) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) flPlayerContainer.getLayoutParams();
        params.leftMargin = marginLeft;
        flPlayerContainer.setLayoutParams(params);
    }

    public void setMarginTop(int marginTop) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) flPlayerContainer.getLayoutParams();
        params.topMargin = marginTop;
        flPlayerContainer.setLayoutParams(params);
    }

    private void refreshVideoInfo() {
        tvName.setText(mRootVideo.title);
        tvSite.setText("片源：" + mChanName);
        tvActor.setText("演员：" + (null != mRootVideo.actors ? Joiner.on(" ").join(mRootVideo.actors) : ""));
        tvDirector.setText("导演：" + (null != mRootVideo.directors ? Joiner.on(" ").join(mRootVideo.directors) : ""));
        tvDes.setText("简介：" + mRootVideo.description);
    }

    private void loadPlayList() {
        MutableLiveData<Video> playListLiveData = new MutableLiveData<>();
        playListLiveData.observe(this, video -> {
            if (null != video && null != video.episodes) {
                LogUtils.iTag(TAG, "播放列表加载成功", video);
                mRootVideo = video;

                // 电影或者剧集已经更新完啦
                if (mRootVideo.type == VideoType.FILM || mRootVideo.episodesTotal == mRootVideo.episodes.size()) {
                    // 缓存之
                    KV.instance.storeVideo(mRootTag, mRootVideo);
                }

                // 从缓存中获取当前播放到的集数
                mCurrentPlayingIndex = KV.instance.getEpisode(mRootTag);

                refreshVideoInfo();

                mPlayListVodAdapter.setNewInstance(video.episodes);
                // 开始播放
                loadVideoAndPlay();
            }
        });
        getCurrentChan().loadPlayListWithCache(this, mRootVideo, playListLiveData);
    }

    private void loadVideoAndPlay() {
        // 停止播放器
        tvPlayer.stop();
        rlParsing.setVisibility(View.VISIBLE);
        tvPlayer.setVisibility(View.GONE);
        getCurrentParser().load(this, mLiveData, getCurrentPlayingVideo().pageUrl);
    }

    private Video getCurrentPlayingVideo() {
        return mPlayListVodAdapter.getItem(mCurrentPlayingIndex);
    }

    private Parser getCurrentParser() {
        Parser parser = KV.instance.getParser(mRootTag, mChanName);
        mCurrentParserIndex = ParserRegister.instance.indexOfSupported(parser, mChanName);
        return parser;
    }

    private Chan getCurrentChan() {
        return ChanRegister.instance.getChanByName(mChanName);
    }

    public void setLastParserWebView(ParserWebView webView) {
        mLastParserWebView = webView;
    }

    public ParserWebView getLastParserWebView() {
        return mLastParserWebView;
    }

    @Subscribe("parserLog")
    public void parserLog(String text) {
        tvParsingLog.setText("正在解析：" + text);
    }

    @Subscribe("parsingError")
    public void parsingError(String error) {
        getCurrentParser().stop(true);
        tvParsingLog.setText("解析失败：" + error);
    }
}
