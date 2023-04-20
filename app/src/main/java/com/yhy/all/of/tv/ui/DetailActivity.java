package com.yhy.all.of.tv.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.common.base.Joiner;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.owen.tvrecyclerview.widget.V7StaggeredGridLayoutManager;
import com.tencent.smtt.utils.Md5Utils;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.ChanRegister;
import com.yhy.all.of.tv.component.adapter.PlayListVodAdapter;
import com.yhy.all.of.tv.component.adapter.SeriesFlagAdapter;
import com.yhy.all.of.tv.component.base.VideoActivity;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.parse.ParserRegister;
import com.yhy.all.of.tv.utils.LogUtils;
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
    @Autowired("chanName")
    public String mChanName;
    @Autowired("rootVideo")
    public Video mRootVideo;

    private MutableLiveData<String> mLiveData;

    private LinearLayout llRoot;
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

    @Override
    protected int layout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        llRoot = $(R.id.llRoot);
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

        setLoadSir(llRoot);
    }

    @Override
    protected void initData() {
        showLoading();
        EasyRouter.getInstance().inject(this);
        Evtor.instance.register(this);

        refreshVideoInfo();

        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, url -> {
            // TODO 开始播放
            //playWithBuilder(url);
            //tvPlayer.startPlayLogic();

            tvPlayer.play("asdf", url);

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
        tvFullScreen.setOnClickListener(v -> {
            //enterFullScreen();
        });

        tvQuickSearch.setOnClickListener(v -> {
        });

        tvSort.setOnClickListener(v -> {
        });

        tvCollect.setOnClickListener(v -> {
        });

        mSeriesFlagAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == mCurrentParserIndex) {
                return;
            }
            int lastIndex = mCurrentParserIndex;
            mCurrentParserIndex = position;
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
            adapter.notifyItemChanged(lastIndex);
            adapter.notifyItemChanged(mCurrentPlayingIndex);

            // 重新加载
            loadVideoAndPlay();
        });
    }

    @Override
    protected void setDefault() {
    }

    //@Override
    //protected GSYBaseVideoPlayer player() {
    //    return tvPlayer;
    //}
    //
    //@Override
    //protected GSYVideoOptionBuilder optionBuilder(String url, long position) {
    //    return new GSYVideoOptionBuilder()
    //        .setUrl(url)
    //        .setShowFullAnimation(true)
    //        .setShowPauseCover(true)
    //        .setSeekRatio(1.0f)
    //        .setLockLand(true)
    //        .setSeekOnStart(position);
    //}

    @Override
    protected void onFullScreen() {
        LogUtils.iTag(TAG, "全屏播放了");
    }

    @Override
    protected boolean shouldAutoPlay() {
        return true;
    }

    @Override
    protected String videoTag() {
        return Md5Utils.getMD5(getCurrentPlayingVideo().pageUrl);
    }

    @Override
    protected void onPlayStarted(String url, Object... objects) {
        // 开始播放回调
        //tvPlayer.setVisibility(View.VISIBLE);
        //rlParsing.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Evtor.instance.unregister(this);
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
                refreshVideoInfo();

                mPlayListVodAdapter.setNewInstance(video.episodes);
                // 开始播放
                loadVideoAndPlay();
            }
        });
        getCurrentChan().loadPlayList(this, mRootVideo, playListLiveData);
    }

    private void loadVideoAndPlay() {
        // TODO 释放播放器
        //tvPlayer.getCurrentPlayer().release();
        //rlParsing.setVisibility(View.VISIBLE);
        //tvPlayer.setVisibility(View.GONE);
        getCurrentParser().load(this, mLiveData, getCurrentPlayingVideo().pageUrl);
    }

    private Video getCurrentPlayingVideo() {
        return mPlayListVodAdapter.getItem(mCurrentPlayingIndex);
    }

    private Parser getCurrentParser() {
        return mParserList.get(mCurrentParserIndex);
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
