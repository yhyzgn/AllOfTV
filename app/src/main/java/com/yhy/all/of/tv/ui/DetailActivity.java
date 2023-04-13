package com.yhy.all.of.tv.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.google.common.base.Joiner;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7LinearLayoutManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.cache.KV;
import com.yhy.all.of.tv.component.adapter.SeriesFlagAdapter;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.parse.ParserRegister;
import com.yhy.all.of.tv.widget.TVPlayer;
import com.yhy.router.EasyRouter;
import com.yhy.router.annotation.Autowired;
import com.yhy.router.annotation.Router;

import java.util.List;
import java.util.Objects;

import me.jessyan.autosize.utils.ScreenUtils;

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
public class DetailActivity extends BaseActivity {
    @Autowired("chanName")
    public String mChanName;
    @Autowired("video")
    public Video mVideo;

    private MutableLiveData<String> mLiveData;

    private LinearLayout llRoot;
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

        tvPlayer.getBackButton().setVisibility(View.GONE);
        tvPlayer.getTitleTextView().setVisibility(View.GONE);
        tvPlayer.getFullscreenButton().setVisibility(View.GONE);

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

        tvName.setText(mVideo.title);
        tvSite.setText("片源：" + mChanName);
        tvActor.setText("演员：" + (null != mVideo.actors ? Joiner.on(" ").join(mVideo.actors) : ""));
        tvDirector.setText("导演：" + (null != mVideo.directors ? Joiner.on(" ").join(mVideo.directors) : ""));
        tvDes.setText("简介：" + mVideo.description);

        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, url -> {
            tvPlayer.setUp(url, false, mVideo.title);
            tvPlayer.setSeekOnStart(KV.instance.kv().getLong(mVideo.pageUrl, 0));
            tvPlayer.startPlayLogic();

            // 停止 WebView 加载
            getCurrentParser().stop(true);
        });

        mParserList = ParserRegister.instance.supportedParserList(mChanName);
        loadVideoAndPlay();

        mSeriesFlagAdapter = new SeriesFlagAdapter(parser -> Objects.equals(getCurrentParser().name(), parser.name()));
        trvParser.setAdapter(mSeriesFlagAdapter);
        mSeriesFlagAdapter.setNewInstance(mParserList);

        showSuccess();
    }

    @Override
    protected void initEvent() {
        tvPlayer.setGSYVideoProgressListener(new GSYVideoProgressListener() {
            @Override
            public void onProgress(long progress, long secProgress, long currentPosition, long duration) {
                if (currentPosition == duration) {
                    KV.instance.kv().remove(mVideo.pageUrl);
                    return;
                }
                if (currentPosition > 0 && null != mVideo) {
                    KV.instance.kv().putLong(mVideo.pageUrl, currentPosition);
                }
            }
        });

        tvFullScreen.setOnClickListener(v -> {
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
    protected void onPause() {
        super.onPause();
        tvPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvPlayer.release();
    }

    @Override
    public void onBackPressed() {
        if (tvPlayer.isIfCurrentIsFullscreen()) {
            tvPlayer.onBackFullscreen();
            return;
        }
        super.onBackPressed();
    }

    private void loadVideoAndPlay() {
        tvPlayer.release();
        getCurrentParser().load(this, mLiveData, mVideo.pageUrl);
    }

    private Parser getCurrentParser() {
        return mParserList.get(mCurrentParserIndex);
    }

    private void toggleScreen(boolean fullscreen) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ViewGroup.LayoutParams params = tvPlayer.getLayoutParams();
        if (!fullscreen) {
            params.width = mPlayerWidth;
            params.height = mPlayerHeight;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            int[] sizes = ScreenUtils.getScreenSize(this);
            params.width = sizes[0];
            params.height = sizes[1];
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        tvPlayer.setLayoutParams(params);
        tvPlayer.setKeepScreenOn(fullscreen);
    }
}
