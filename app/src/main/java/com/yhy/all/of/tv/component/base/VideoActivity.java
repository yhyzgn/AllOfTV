package com.yhy.all.of.tv.component.base;

import android.view.KeyEvent;

import com.yhy.player.widget.TvPlayer;

/**
 * 视频播放器页面基类
 * <p>
 * Created on 2023-04-14 11:03
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class VideoActivity extends BaseActivity {

    @Override
    public void onBackPressed() {
        if (player().backFromFullScreen(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player().resume();
    }

    @Override
    protected void onDestroy() {
        player().destroy();
        super.onDestroy();
    }

    protected abstract TvPlayer player();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen() && event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT -> {
                    player().seekBack();
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    player().seekForward();
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_CENTER -> {
                    player().playOrPause();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
