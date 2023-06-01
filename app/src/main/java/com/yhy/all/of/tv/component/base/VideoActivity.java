package com.yhy.all.of.tv.component.base;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.yhy.player.widget.TvPlayer;

import java.util.Timer;
import java.util.TimerTask;

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
    private Timer mLongPressTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 退出提示回调
        player().setExitFullToastyCallback(this::warning);
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
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen()) {
            if (null != mLongPressTimer) {
                mLongPressTimer.cancel();
            }
            mLongPressTimer = new Timer();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_LEFT -> {
                        mLongPressTimer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                player().seekBack();
                            }
                        }, 0, 100);
                        return true;
                    }
                    case KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        mLongPressTimer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                player().seekForward();
                            }
                        }, 0, 100);
                        return true;
                    }
                }
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen()) {
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
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen()) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (null != mLongPressTimer) {
                        mLongPressTimer.cancel();
                        mLongPressTimer = null;
                        return true;
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
