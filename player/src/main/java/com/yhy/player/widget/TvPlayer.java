package com.yhy.player.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
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
    private final static SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
    private final static int DELTA_SEEKING_MS = 15000;
    private final static int WHAT_HANDLER_MSG_SEEK_FORWARD = 1024;
    private final static int WHAT_HANDLER_MSG_SEEK_BACK = 2048;
    private final static int STICKY_EXIT_FULL_MS = 3000;

    /**
     * 控制面板隐藏的延迟时间长度
     */
    private final static long MS_DELAY_CTRL_DISMISS = 3000L;
    private StyledPlayerView spvExo;
    private TextView tvTitle;
    private TextClock tcNow;
    private ImageView ivPlayOrPause;
    private ProgressBar pbLoading;
    private LinearLayout llDrag;
    private TextView tvPosition;
    private TextView tvDuration;
    private SilenceTimeBar stbDrag;
    private SilenceTimeBar stbPosition;
    private ExoPlayer mPlayer;

    /**
     * 上一次操作时的时间戳
     */
    private long mLastOperateMillis;

    private long mDuration;

    private long mBufferedPosition;

    private long mCurrentPosition;

    private long mSeekingPosition;

    private Timer mTimer;

    private ViewGroup mOriginalParentContainer;

    private OnStartedListener mOnStartedListener;

    private OnPositionChangedListener mOnPositionChangedListener;

    private ExitFullToastyCallback mExitFullToastyCallback = text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

    private boolean mIsSeeking;

    private long mLastExitFullTime;

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
        View view = LayoutInflater.from(context).inflate(R.layout.widget_tv_player, this);
        spvExo = view.findViewById(R.id.spv_exo);
        tvTitle = view.findViewById(R.id.tv_title);
        tcNow = view.findViewById(R.id.tc_now);
        ivPlayOrPause = view.findViewById(R.id.iv_play_or_pause);
        pbLoading = view.findViewById(R.id.pb_loading);
        llDrag = view.findViewById(R.id.ll_drag);
        tvPosition = view.findViewById(R.id.tv_position);
        tvDuration = view.findViewById(R.id.tv_duration);
        stbDrag = view.findViewById(R.id.stb_drag);
        stbPosition = view.findViewById(R.id.stb_position);

        mPlayer = new ExoPlayer.Builder(context)
                .setSeekBackIncrementMs(DELTA_SEEKING_MS)
                .setSeekForwardIncrementMs(DELTA_SEEKING_MS)
                .build();
        mPlayer.setPlayWhenReady(true);
        mPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);

        spvExo.setUseController(false);
        spvExo.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_NEVER);
        spvExo.setPlayer(mPlayer);

        // 日期格式化模式设置为 UTC 0时区
        TIME_SDF.setTimeZone(TimeZone.getTimeZone("UTC"));

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
    }

    private void timeControlPanel() {
        if (mIsSeeking) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - mLastOperateMillis >= MS_DELAY_CTRL_DISMISS) {
            // 隐藏
            tvTitle.setVisibility(GONE);
            tcNow.setVisibility(GONE);
            llDrag.setVisibility(GONE);
            stbPosition.setVisibility(VISIBLE);
        } else {
            // 显示
            if (isInFullScreen()) {
                tvTitle.setVisibility(VISIBLE);
                tcNow.setVisibility(VISIBLE);
            }
            llDrag.setVisibility(VISIBLE);
            stbPosition.setVisibility(GONE);
        }
    }

    private void refreshPosition(long duration, long bufferedPosition, long currentPosition) {
        if (mDuration != duration) {
            mDuration = duration;
            stbDrag.setDuration(duration);
            stbPosition.setDuration(duration);
            tvDuration.setText(formatTime(duration));
        }

        mBufferedPosition = bufferedPosition;
        stbDrag.setBufferedPosition(mBufferedPosition);
        stbPosition.setBufferedPosition(mBufferedPosition);

        if (!mIsSeeking && mCurrentPosition != currentPosition) {
            mCurrentPosition = currentPosition;
            dragPosition(mCurrentPosition, false);
            if (null != mOnPositionChangedListener) {
                mOnPositionChangedListener.onChanged(duration, mBufferedPosition, mCurrentPosition);
            }
        }
    }

    private void dragPosition(long position, boolean showDrag) {
        if (showDrag) {
            llDrag.setVisibility(VISIBLE);
            stbPosition.setVisibility(GONE);
        } else {
            llDrag.setVisibility(GONE);
            stbPosition.setVisibility(VISIBLE);
        }
        stbDrag.setPosition(position);
        tvPosition.setText(formatTime(position));
        stbPosition.setPosition(position);
    }

    public boolean isInFullScreen() {
        return null != mOriginalParentContainer;
    }

    public void pause() {
        ivPlayOrPause.setImageResource(R.mipmap.ic_play);
        ivPlayOrPause.setVisibility(VISIBLE);
        mPlayer.pause();
    }

    public void resume() {
        ivPlayOrPause.setImageResource(R.mipmap.ic_pause);
        ivPlayOrPause.setVisibility(GONE);
        mPlayer.play();
    }

    public void playOrPause() {
        if (isPlaying()) {
            pause();
            return;
        }
        resume();
    }

    public void seekForward() {
        mHandler.removeMessages(WHAT_HANDLER_MSG_SEEK_FORWARD);
        mHandler.removeMessages(WHAT_HANDLER_MSG_SEEK_BACK);
        mIsSeeking = true;
        // 每次增加 10 s
        mSeekingPosition = (mSeekingPosition == 0 ? mCurrentPosition : mSeekingPosition) + DELTA_SEEKING_MS;
        mSeekingPosition = Math.min(mSeekingPosition, mDuration);
        dragPosition(mSeekingPosition, true);
        mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_MSG_SEEK_FORWARD, 1000);
    }

    public void seekBack() {
        mHandler.removeMessages(WHAT_HANDLER_MSG_SEEK_FORWARD);
        mHandler.removeMessages(WHAT_HANDLER_MSG_SEEK_BACK);
        mIsSeeking = true;
        // 每次增加 10 s
        mSeekingPosition = (mSeekingPosition == 0 ? mCurrentPosition : mSeekingPosition) - DELTA_SEEKING_MS;
        mSeekingPosition = Math.max(mSeekingPosition, 0);
        dragPosition(mSeekingPosition, true);
        mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_MSG_SEEK_BACK, 1000);
    }

    public void stop() {
        mPlayer.stop();
    }

    public void release() {
        mPlayer.release();
    }

    public void destroy() {
        stop();
        release();
        mPlayer.removeListener(mExoListener);
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void enterFullScreen(AppCompatActivity activity) {
        postDelayed(() -> {
            mOriginalParentContainer = (ViewGroup) getParent();

            ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            mOriginalParentContainer.removeView(this);
            contentView.addView(this, params);

        }, 200);
    }

    public boolean backFromFullScreen(AppCompatActivity activity) {
        if (!isInFullScreen()) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now - mLastExitFullTime > STICKY_EXIT_FULL_MS) {
            // 提示再按一次退出播放
            if (null != mExitFullToastyCallback) {
                mExitFullToastyCallback.onToasty("再按一次退出播放");
            }
            mLastExitFullTime = now;
            return true;
        }

        ViewGroup parentView = (ViewGroup) getParent();
        parentView.removeView(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mOriginalParentContainer.addView(this, params);

        mOriginalParentContainer = null;
        return true;
    }

    private String formatTime(long millis) {
        return TIME_SDF.format(new Date(millis));
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

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_HANDLER_MSG_SEEK_FORWARD, WHAT_HANDLER_MSG_SEEK_BACK -> {
                    // 播放进度调整
                    if (mSeekingPosition > 0) {
                        mPlayer.seekTo(mSeekingPosition);
                        mCurrentPosition = mSeekingPosition;
                    }
                    mIsSeeking = false;
                    mSeekingPosition = 0;
                }
            }
        }
    };

    private final Player.Listener mExoListener = new Player.Listener() {

        private boolean mIsPlaying = false;

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            pbLoading.setVisibility(isLoading && !mIsPlaying ? VISIBLE : GONE);
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            mIsPlaying = isPlaying;
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
                case Player.STATE_BUFFERING -> {
                    Log.d(TAG, "State-Buffering");
                }
                case Player.STATE_ENDED -> {
                    Log.d(TAG, "State-Ended");
                }
                case Player.STATE_IDLE -> {
                    Log.d(TAG, "State-Idle");
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
                pause();
            }
            case ON_RESUME -> {
                resume();
            }
            case ON_DESTROY -> {
                destroy();
            }
        }
    }

    public void setOnStartedListener(OnStartedListener listener) {
        mOnStartedListener = listener;
    }

    public void setOnPositionChangedListener(OnPositionChangedListener listener) {
        mOnPositionChangedListener = listener;
    }

    public void setExitFullToastyCallback(ExitFullToastyCallback callback) {
        mExitFullToastyCallback = callback;
    }

    @FunctionalInterface
    public interface OnStartedListener {

        void onStarted();
    }

    @FunctionalInterface
    public interface OnPositionChangedListener {

        void onChanged(long duration, long bufferedPosition, long currentPosition);
    }

    @FunctionalInterface
    public interface ExitFullToastyCallback {

        void onToasty(String text);
    }
}
