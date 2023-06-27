package com.yhy.all.of.tv.component.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
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
    private static final int WHAT_LONG_PRESS = 2048;
    private static final long THRESHOLD_LONG_PRESS = 1500L;
    private boolean mIsLongPressed;
    private Timer mLongPressTimer;
    private final Handler mLongPressHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what != WHAT_LONG_PRESS) {
                return;
            }
            mIsLongPressed = true;
            handleLongPress(msg.arg1);
        }
    };

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

    private void handleLongPress(int keyCode) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen()) {
            if (null != mLongPressTimer) {
                mLongPressTimer.cancel();
            }
            mLongPressTimer = new Timer();
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT -> {
                    mLongPressTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> player().seekBack());
                        }
                    }, 0, 100);
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    mLongPressTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> player().seekForward());
                        }
                    }, 0, 100);
                }
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 全屏状态下才处理这些事件
        if (player().isInFullScreen()) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        // 延时发送长按消息
                        Message msg = new Message();
                        msg.what = WHAT_LONG_PRESS;
                        msg.arg1 = event.getKeyCode();
                        mLongPressHandler.sendMessageDelayed(msg, THRESHOLD_LONG_PRESS);
                    }
                    break;
                case KeyEvent.ACTION_UP:
                    // 长按时间清除消息
                    mLongPressHandler.removeMessages(WHAT_LONG_PRESS);
                    // 可能是长按事件
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            if (mIsLongPressed) {
                                if (null != mLongPressTimer) {
                                    mLongPressTimer.cancel();
                                    mLongPressTimer = null;
                                    return true;
                                }
                                mIsLongPressed = false;
                            } else {
                                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    player().seekBack();
                                } else {
                                    player().seekForward();
                                }
                                return true;
                            }
                        }
                        case KeyEvent.KEYCODE_DPAD_CENTER -> {
                            player().playOrPause();
                            return true;
                        }
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
