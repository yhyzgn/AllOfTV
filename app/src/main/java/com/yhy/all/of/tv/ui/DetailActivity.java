package com.yhy.all.of.tv.ui;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.component.base.BaseActivity;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.widget.TVPlayer;
import com.yhy.router.EasyRouter;
import com.yhy.router.annotation.Autowired;
import com.yhy.router.annotation.Router;

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
    @Autowired("video")
    public Video mVideo;

    private TVPlayer tvPlayer;

    @Override
    protected int layout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        tvPlayer = $(R.id.tvPlayer);
    }


    @Override
    protected void initData() {
        EasyRouter.getInstance().inject(this);

        // 借用了jjdxm_ijkplayer的URL
        String source1 = "https://res.exexm.com/cw_145225549855002";
        String source2 = "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear3/prog_index.m3u8";

        tvPlayer.setUp(source1, false, "测试视频");
        tvPlayer.startPlayLogic();
    }

    @Override
    protected void initEvent() {

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
}
