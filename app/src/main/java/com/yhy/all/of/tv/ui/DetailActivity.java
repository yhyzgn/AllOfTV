package com.yhy.all.of.tv.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.google.common.base.Joiner;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.tencent.smtt.utils.Md5Utils;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.component.adapter.SeriesFlagAdapter;
import com.yhy.all.of.tv.component.base.VideoActivity;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.parse.ParserRegister;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.widget.TVPlayer;
import com.yhy.evtor.Evtor;
import com.yhy.evtor.annotation.Subscribe;
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
    @Autowired("video")
    public Video mVideo;

    private MutableLiveData<String> mLiveData;

    private LinearLayout llRoot;
    private RelativeLayout rlParsing;
    private TextView tvParsingLog;
    private TVPlayer tvPlayer;
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
    private TvRecyclerView trvVod;
    private List<Parser> mParserList;

    private int mCurrentParserIndex = 0;
    private SeriesFlagAdapter mSeriesFlagAdapter;
    private int mPlayerWidth;
    private int mPlayerHeight;

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
        trvVod = $(R.id.trvVod);

        trvParser.setHasFixedSize(true);
        trvParser.setLayoutManager(new V7LinearLayoutManager(this, 0, false));

        mPlayerWidth = tvPlayer.getMeasuredWidth();
        mPlayerHeight = tvPlayer.getMeasuredHeight();

        setLoadSir(llRoot);
    }

    @Override
    protected void initData() {
        showLoading();
        EasyRouter.getInstance().inject(this);
        Evtor.instance.register(this);

        tvName.setText(mVideo.title);
        tvSite.setText("片源：" + mChanName);
        tvActor.setText("演员：" + (null != mVideo.actors ? Joiner.on(" ").join(mVideo.actors) : ""));
        tvDirector.setText("导演：" + (null != mVideo.directors ? Joiner.on(" ").join(mVideo.directors) : ""));
        tvDes.setText("简介：" + mVideo.description);

        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, url -> {
            playWithBuilder(url);
            tvPlayer.startPlayLogic();

            // 停止 WebView 加载
            getCurrentParser().stop(true);
        });

        mParserList = ParserRegister.instance.supportedParserList(mChanName);
        loadVideoAndPlay();

        mSeriesFlagAdapter = new SeriesFlagAdapter(parser -> Objects.equals(getCurrentParser().name(), parser.name()));
        trvParser.setAdapter(mSeriesFlagAdapter);
        mSeriesFlagAdapter.setNewInstance(mParserList);

        // 全屏按钮获取焦点
        tvFullScreen.requestFocus();

        showSuccess();
    }

    @Override
    protected void initEvent() {
        tvFullScreen.setOnClickListener(v -> {
            enterFullScreen();
        });

        tvQuickSearch.setOnClickListener(v -> {
        });

        tvSort.setOnClickListener(v -> {
        });

        tvCollect.setOnClickListener(v -> {
        });

        mSeriesFlagAdapter.setOnItemClickListener((adapter, view, position) -> {
            int lastIndex = mCurrentParserIndex;
            mCurrentParserIndex = position;
            adapter.notifyItemChanged(lastIndex);
            adapter.notifyItemChanged(mCurrentParserIndex);

            // 重新加载
            loadVideoAndPlay();
        });
    }

    @Override
    protected void setDefault() {
    }

    @Override
    protected GSYBaseVideoPlayer player() {
        return tvPlayer;
    }

    @Override
    protected GSYVideoOptionBuilder optionBuilder(String url, long position) {
        return new GSYVideoOptionBuilder()
            .setUrl(url)
            .setShowFullAnimation(true)
            .setShowPauseCover(true)
            .setSeekRatio(1.0f)
            .setLockLand(true)
            .setSeekOnStart(position);
    }

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
        return Md5Utils.getMD5(mVideo.pageUrl);
    }

    @Override
    protected void onPlayStarted(String url, Object... objects) {
        // 开始播放回调
        tvPlayer.setVisibility(View.VISIBLE);
        rlParsing.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Evtor.instance.unregister(this);
    }

    private void loadVideoAndPlay() {
        tvPlayer.getCurrentPlayer().release();
        rlParsing.setVisibility(View.VISIBLE);
        tvPlayer.setVisibility(View.GONE);
        getCurrentParser().load(this, mLiveData, mVideo.pageUrl);
    }

    private Parser getCurrentParser() {
        return mParserList.get(mCurrentParserIndex);
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
