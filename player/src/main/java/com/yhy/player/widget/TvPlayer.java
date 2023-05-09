package com.yhy.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.yhy.player.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TV 播放器
 * <p>
 * Created on 2023-04-20 10:17
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class TvPlayer extends FrameLayout implements LifecycleEventObserver {
    private final static String TAG = "TvPlayer";

    /**
     * 控制面板隐藏的延迟时间长度
     */
    private final static long MS_DELAY_CTRL_DISMISS = 3000L;
    private StyledPlayerView spvExo;
    private TextView tvTitle;
    private TextClock tcNow;
    private ImageView ivPlayOrPause;
    private ProgressBar pbLoading;
    private SilenceTimeBar stbDrag;
    private SilenceTimeBar stbPosition;
    private ExoPlayer mPlayer;

    /**
     * 上一次操作时的时间戳
     */
    private long mLastOperateMillis;

    private Timer mTimer;

    private ViewGroup mOriginalParentContainer;

    private OnStartedListener mOnStartedListener;

    private OnPositionChangedListener mOnPositionChangedListener;

    public TvPlayer(@NonNull Context context) {
        this(context, null);
    }

    public TvPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TvPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.widget_tv_player, this);
        spvExo = rootView.findViewById(R.id.spv_exo);
        tvTitle = rootView.findViewById(R.id.tv_title);
        tcNow = rootView.findViewById(R.id.tc_now);
        ivPlayOrPause = rootView.findViewById(R.id.iv_play_or_pause);
        pbLoading = rootView.findViewById(R.id.pb_loading);
        stbDrag = rootView.findViewById(R.id.stb_drag);
        stbPosition = rootView.findViewById(R.id.stb_position);

        mPlayer = new ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build();
        mPlayer.setPlayWhenReady(true);
        mPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);

        spvExo.setUseController(false);
        spvExo.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_NEVER);
        spvExo.setPlayer(mPlayer);

        initListener();
    }

    private void initListener() {
        mPlayer.addListener(mExoListener);
    }

    public void play(String title, String url, long seekToMs) {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        MediaItem mi = new MediaItem.Builder()
            .setTag(url)
            .setUri(url)
            .build();

        tvTitle.setText(title);
        mPlayer.setMediaItem(mi);

        mPlayer.prepare();
        mPlayer.seekTo(seekToMs);
    }

    private void resolveTimer() {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                post(() -> {
                    // 记录进度
                    timePosition();

                    // 控制面板显示隐藏
                    timeControlPanel();
                });
            }
        }, 0, 1000);
    }

    private void timePosition() {
        long duration = mPlayer.getDuration();
        long bufferedPosition = mPlayer.getBufferedPosition();
        long currentPosition = mPlayer.getCurrentPosition();

        Log.i(TAG, "duration = " + duration + ", bufferedPosition = " + bufferedPosition + ", currentPosition = " + currentPosition);
        refreshPosition(duration, bufferedPosition, currentPosition);
        if (null != mOnPositionChangedListener) {
            mOnPositionChangedListener.onChanged(duration, bufferedPosition, currentPosition);
        }
    }

    private void timeControlPanel() {
        long now = System.currentTimeMillis();
        if (now - mLastOperateMillis >= MS_DELAY_CTRL_DISMISS) {
            // 隐藏
            tvTitle.setVisibility(GONE);
            tcNow.setVisibility(GONE);
            stbDrag.setVisibility(GONE);
            stbPosition.setVisibility(VISIBLE);
        } else {
            // 显示
            if (isInFullScreen()) {
                tvTitle.setVisibility(VISIBLE);
                tcNow.setVisibility(VISIBLE);
            }
            stbDrag.setVisibility(VISIBLE);
            stbPosition.setVisibility(GONE);
        }
    }

    private void refreshPosition(long duration, long bufferedPosition, long currentPosition) {
        stbDrag.setDuration(duration);
        stbDrag.setBufferedPosition(bufferedPosition);
        stbDrag.setPosition(currentPosition);

        stbPosition.setDuration(duration);
        stbPosition.setBufferedPosition(bufferedPosition);
        stbPosition.setPosition(currentPosition);
    }

    public boolean isInFullScreen() {
        return null != mOriginalParentContainer;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void seekForward() {
        mPlayer.seekForward();
    }

    public void seekBack() {
        mPlayer.seekBack();
    }

    public void enterFullScreen(AppCompatActivity activity) {
        postDelayed(() -> {
            mOriginalParentContainer = (ViewGroup) getParent();

            ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            mOriginalParentContainer.removeView(this);
            contentView.addView(this, params);

            // 先默认啦
            // hideSystemUI(activity);
        }, 200);
    }

    public boolean backFromFullScreen(AppCompatActivity activity) {
        if (null == mOriginalParentContainer) {
            return false;
        }

        ViewGroup parentView = (ViewGroup) getParent();
        parentView.removeView(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mOriginalParentContainer.addView(this, params);

        // 先默认啦
        // showSystemUI(activity);
        mOriginalParentContainer = null;
        return true;
    }

    private void hideSystemUI(AppCompatActivity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void showSystemUI(AppCompatActivity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    private void internalOnStarted() {
        resolveTimer();
    }

    private final Player.Listener mExoListener = new Player.Listener() {

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            pbLoading.setVisibility(isLoading ? VISIBLE : GONE);
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Log.i(TAG, "playbackState = " + playbackState);
            switch (playbackState) {
                case Player.STATE_READY -> {
                    // 准备就绪自动播放
                    mPlayer.play();

                    internalOnStarted();

                    if (null != mOnStartedListener) {
                        mOnStartedListener.onStarted();
                    }
                }
            }
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            Log.i(TAG, "playWhenReady = " + playWhenReady + ", reason = " + reason);
        }

        @Override
        public void onPlayerError(@NonNull PlaybackException error) {
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        operated();
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        operated();
        return super.dispatchTouchEvent(ev);
    }

    private void operated() {
        // 记录本次操作的时间戳
        mLastOperateMillis = System.currentTimeMillis();
        timeControlPanel();
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_PAUSE -> {
                mPlayer.pause();
            }
            case ON_RESUME -> {
                mPlayer.play();
            }
            case ON_DESTROY -> {
                mPlayer.stop();
                mPlayer.release();
                mPlayer.removeListener(mExoListener);
            }
        }
    }

    @FunctionalInterface
    public interface OnStartedListener {

        void onStarted();
    }

    @FunctionalInterface
    public interface OnPositionChangedListener {

        void onChanged(long duration, long bufferedPosition, long currentPosition);
    }
}
