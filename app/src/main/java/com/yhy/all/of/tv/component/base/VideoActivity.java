package com.yhy.all.of.tv.component.base;

import android.view.KeyEvent;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yhy.all.of.tv.cache.KV;

/**
 * 视频播放器页面基类
 * <p>
 * Created on 2023-04-14 11:03
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class VideoActivity extends BaseActivity implements VideoAllCallBack, GSYVideoProgressListener {
    protected boolean mIsPlaying = false;
    protected boolean mIsPausing = false;

    private void initVideo() {
        GSYVideoManager.backFromWindowFull(this);
        if (null != player().getFullscreenButton()) {
            player().getFullscreenButton().setOnClickListener(v -> {
                enterFullScreen();
            });
        }
    }

    public void enterFullScreen() {
        // 不需要屏幕旋转，必须调该方法
        player().setNeedOrientationUtils(false);
        // 第一个true是否需要隐藏 actionBar，第二个true是否需要隐藏 statusBar
        player().startWindowFullscreen(this, false, false);
    }

    protected void playWithBuilder(String url) {
        initVideo();
        long position = KV.instance.kv().getLong(videoTag(), 0);
        GSYVideoOptionBuilder optionBuilder = optionBuilder(url, position);
        if (null != optionBuilder) {
            optionBuilder
                .setPlayTag(videoTag())
                .setVideoAllCallBack(this)
                .setGSYVideoProgressListener(this)
                .build(player());
            if (shouldAutoPlay()) {
                player().startPlayLogic();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player().getCurrentPlayer().onVideoPause();
        mIsPausing = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        player().getCurrentPlayer().onVideoResume(true);
        mIsPausing = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsPlaying) {
            player().getCurrentPlayer().release();
        }
    }

    protected abstract GSYBaseVideoPlayer player();

    protected abstract GSYVideoOptionBuilder optionBuilder(String url, long position);

    protected abstract boolean shouldAutoPlay();

    protected abstract String videoTag();

    protected void onFullScreen() {
    }

    private void seekBack() {
        long seek = Math.max(0, player().getCurrentPositionWhenPlaying() - 5);
        player().seekTo(seek);
    }

    private void seekForward() {
        long total = player().getDuration();
        long seek = Math.min(total, player().getCurrentPositionWhenPlaying() + 5);
        player().seekTo(seek);
    }

    private void playOrPause() {
        if (player().isInPlayingState()) {
            player().onVideoPause();
            return;
        }
        player().startPlayLogic();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isIfCurrentIsFullscreen() && event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    seekBack();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    seekForward();
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    playOrPause();
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onProgress(long progress, long secProgress, long currentPosition, long duration) {
        if (currentPosition == duration) {
            KV.instance.kv().remove(videoTag());
            return;
        }
        if (currentPosition > 0) {
            KV.instance.kv().putLong(videoTag(), currentPosition);
        }
    }

    @Override
    public void onStartPrepared(String url, Object... objects) {
    }

    @Override
    public void onPrepared(String url, Object... objects) {
        mIsPlaying = true;
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
    }

    @Override
    public void onClickStop(String url, Object... objects) {
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
    }

    @Override
    public void onClickResume(String url, Object... objects) {
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
    }

    @Override
    public void onComplete(String url, Object... objects) {
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        onFullScreen();
    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {
    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {
    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {
    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {
    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {
    }

    @Override
    public void onPlayError(String url, Object... objects) {
    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
    }
}
