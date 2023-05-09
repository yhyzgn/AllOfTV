package com.yhy.all.of.tv.component.base;

/**
 * 视频播放器页面基类
 * <p>
 * Created on 2023-04-14 11:03
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class VideoActivity extends BaseActivity /* implements VideoAllCallBack, GSYVideoProgressListener */ {
    protected boolean mIsPlaying = false;
    protected boolean mIsPausing = false;

    protected long mCurrentPosition = 0;

    private boolean seekLock = false;

    @Override
    public void onBackPressed() {
        // if (GSYVideoManager.backFromWindowFull(this)) {
        //    return;
        //}
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // player().getCurrentPlayer().onVideoPause();
        mIsPausing = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // player().getCurrentPlayer().onVideoResume(true);
        mIsPausing = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsPlaying) {
            // player().getCurrentPlayer().release();
        }
    }

    // protected abstract GSYBaseVideoPlayer player();
    //
    // protected abstract GSYVideoOptionBuilder optionBuilder(String url, long position);

    protected abstract boolean shouldAutoPlay();

    protected abstract String videoTag();

    protected void onFullScreen() {
    }

    // private void seekBack() {
    //    seekLock = true;
    //    mCurrentPosition -= 10;
    //    long seek = Math.max(0, mCurrentPosition);
    //    player().seekTo(seek);
    //    seekLock = false;
    //}
    //
    // private void seekForward() {
    //    seekLock = true;
    //    mCurrentPosition += 10;
    //    long total = player().getDuration();
    //    long seek = Math.min(total, mCurrentPosition);
    //    player().seekTo(seek);
    //    seekLock = false;
    //}
    //
    // private void playOrPause() {
    //    if (player().isInPlayingState()) {
    //        player().onVideoPause();
    //        return;
    //    }
    //    player().onVideoResume();
    //}
    //
    //@Override
    // public boolean dispatchKeyEvent(KeyEvent event) {
    //    // 全屏状态下才处理这些事件
    //    if (player().isIfCurrentIsFullscreen() && event.getAction() == KeyEvent.ACTION_UP) {
    //        switch (event.getKeyCode()) {
    //            case KeyEvent.KEYCODE_DPAD_LEFT -> {
    //                seekBack();
    //                return true;
    //            }
    //            case KeyEvent.KEYCODE_DPAD_RIGHT -> {
    //                seekForward();
    //                return true;
    //            }
    //            case KeyEvent.KEYCODE_DPAD_CENTER -> {
    //                playOrPause();
    //                return true;
    //            }
    //        }
    //    }
    //    return super.dispatchKeyEvent(event);
    //}
    //
    //@Override
    // public void onProgress(long progress, long secProgress, long currentPosition, long duration) {
    //    if (!seekLock) {
    //        mCurrentPosition = currentPosition;
    //    }
    //    if (currentPosition == duration) {
    //        KV.instance.kv().remove(videoTag());
    //        return;
    //    }
    //    if (currentPosition > 0) {
    //        KV.instance.kv().putLong(videoTag(), currentPosition);
    //    }
    //}
    //
    //@Override
    // public void onStartPrepared(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onPrepared(String url, Object... objects) {
    //    onPlayStarted(url, objects);
    //    mIsPlaying = true;
    //}
    //
    //@Override
    // public void onClickStartIcon(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickStartError(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickStop(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickStopFullscreen(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickResume(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickResumeFullscreen(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickSeekbar(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickSeekbarFullscreen(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onAutoComplete(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onComplete(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onEnterFullscreen(String url, Object... objects) {
    //    onFullScreen();
    //}
    //
    //@Override
    // public void onQuitFullscreen(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onQuitSmallWidget(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onEnterSmallWidget(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onTouchScreenSeekVolume(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onTouchScreenSeekPosition(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onTouchScreenSeekLight(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onPlayError(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickStartThumb(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickBlank(String url, Object... objects) {
    //}
    //
    //@Override
    // public void onClickBlankFullscreen(String url, Object... objects) {
    //}
}
