package com.yhy.player.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.player.R;
import com.yhy.player.utils.CutoutUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

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
    private final static int DURATION_FULL_ANIMATION = 600;

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

    private String mUrl;

    private String mTag;

    /**
     * 上一次操作时的时间戳
     */
    private long mLastOperateMillis;

    private long mDuration;

    private long mBufferedPosition;

    private long mCurrentPosition;

    private long mSeekingPosition;

    private Timer mTimer;

    private OnStartedListener mOnStartedListener;

    private OnPositionChangedListener mOnPositionChangedListener;

    private ExitFullToastyCallback mExitFullToastyCallback = text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

    private boolean mIsSeeking;

    private long mLastExitFullTime;

    private ViewGroup mOriginalParentContainer;

    private boolean mIsInFullScreen;

    private int mOriginalX;

    private int mOriginalY;

    private int mOriginalWidth;

    private int mOriginalHeight;

    private int mScreenWidth;

    private int mScreenHeight;


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

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mPlayer.addListener(mExoListener);

        // 禁止播放器的点击穿透
        spvExo.setOnTouchListener((v, event) -> true);
    }

    public void play(String title, String url, String tag, long seekToMs) {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        mUrl = url;
        mTag = tag;

        MediaItem mi = new MediaItem.Builder()
            .setTag(mTag)
            .setUri(mUrl)
            .build();
        headContentType(isHls -> post(() -> {
            if (isHls) {
                DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
                HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mi);
                mPlayer.setMediaSource(hlsMediaSource, false);
            } else {
                mPlayer.setMediaItem(mi);
            }
            tvTitle.setText(title);
            mPlayer.prepare();
            mPlayer.seekTo(seekToMs);
        }));
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
                mOnPositionChangedListener.onChanged(mUrl, mTag, duration, mBufferedPosition, mCurrentPosition);
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
        return mIsInFullScreen;
    }

    public void setInFullScreen(boolean isInFullScreen) {
        mIsInFullScreen = isInFullScreen;
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
        if (isInFullScreen()) {
            return;
        }

        postDelayed(() -> {
            // 记录播放器在屏幕中的坐标和大小信息
            int[] location = new int[2];
            getLocationOnScreen(location);
            mOriginalX = location[0];
            mOriginalY = location[1];
            mOriginalWidth = getWidth();
            mOriginalHeight = getHeight();
            Log.i(TAG, "Location.x = " + mOriginalX + ", Location.y = " + mOriginalY + ", OriginalWidth = " + mOriginalWidth + ", OriginalHeight = " + mOriginalHeight + ", ScreenWidth = " + mScreenWidth + ", ScreenHeight = " + mScreenHeight);

            // 操作 view
            ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            LayoutParams params = new LayoutParams(mOriginalWidth, mOriginalHeight);
            params.leftMargin = mOriginalX;
            params.topMargin = mOriginalY;
            mOriginalParentContainer = (ViewGroup) getParent();
            mOriginalParentContainer.removeView(this);
            contentView.addView(this, params);

            // 最后再执行动画
            ObjectAnimator scaleAnimationWidth = ObjectAnimator.ofObject(this, "width", new IntEvaluator(), mOriginalWidth, mScreenWidth);
            ObjectAnimator scaleAnimationHeight = ObjectAnimator.ofObject(this, "height", new IntEvaluator(), mOriginalHeight, mScreenHeight);
            ObjectAnimator translateAnimationX = ObjectAnimator.ofObject(this, "marginLeft", new IntEvaluator(), mOriginalX, 0);
            ObjectAnimator translateAnimationY = ObjectAnimator.ofObject(this, "marginTop", new IntEvaluator(), mOriginalY, 0);
            AnimatorSet as = new AnimatorSet();
            as.playTogether(scaleAnimationWidth, scaleAnimationHeight, translateAnimationX, translateAnimationY);
            as.setInterpolator(new FastOutSlowInInterpolator());
            as.setDuration(DURATION_FULL_ANIMATION);
            as.start();
            mIsInFullScreen = true;
        }, 20);
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

        // 先执行动画
        ObjectAnimator scaleAnimationWidth = ObjectAnimator.ofObject(this, "width", new IntEvaluator(), mScreenWidth, mOriginalWidth);
        ObjectAnimator scaleAnimationHeight = ObjectAnimator.ofObject(this, "height", new IntEvaluator(), mScreenHeight, mOriginalHeight);
        ObjectAnimator translateAnimationX = ObjectAnimator.ofObject(this, "marginLeft", new IntEvaluator(), 0, mOriginalX);
        ObjectAnimator translateAnimationY = ObjectAnimator.ofObject(this, "marginTop", new IntEvaluator(), 0, mOriginalY);
        AnimatorSet as = new AnimatorSet();
        as.playTogether(scaleAnimationWidth, scaleAnimationHeight, translateAnimationX, translateAnimationY);
        as.setInterpolator(new FastOutSlowInInterpolator());
        as.setDuration(DURATION_FULL_ANIMATION);
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后再操作 view
                ViewGroup parentView = (ViewGroup) getParent();
                parentView.removeView(TvPlayer.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mOriginalParentContainer.addView(TvPlayer.this, params);
                mOriginalParentContainer = null;
            }
        });
        as.start();
        mIsInFullScreen = false;
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 获取屏幕宽高
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        // 如果有刘海屏，需要加上刘海屏的宽高
        Size cutoutSize = CutoutUtils.size(getContext());
        mScreenWidth += cutoutSize.getWidth();
        mScreenHeight += cutoutSize.getHeight();
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        setLayoutParams(params);
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    public void setMarginLeft(int marginLeft) {
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.leftMargin = marginLeft;
        setLayoutParams(params);
    }

    public void setMarginTop(int marginTop) {
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.topMargin = marginTop;
        setLayoutParams(params);
    }

    private String formatTime(long millis) {
        return TIME_SDF.format(new Date(millis));
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
            pbLoading.setVisibility(isLoading && !mIsPlaying && mBufferedPosition == mCurrentPosition ? VISIBLE : GONE);
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

    /**
     * dp转px
     *
     * @param dpVal dp值
     * @return px值
     */
    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param spVal sp值
     * @return px值
     */
    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getContext().getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param pxVal px值
     * @return dp值
     */
    private float px2dp(float pxVal) {
        return (pxVal / getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * px转sp
     *
     * @param pxVal px值
     * @return sp值
     */
    private float px2sp(float pxVal) {
        return (pxVal / getContext().getResources().getDisplayMetrics().scaledDensity);
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

    public void headContentType(Consumer<Boolean> isHlsCallback) {
        OkGo.<String>head(mUrl).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String contentType = response.headers().get("Content-Type");
                if (!TextUtils.isEmpty(contentType)) {
                    isHlsCallback.accept(!contentType.startsWith("video"));
                }
            }
        });
    }

    @FunctionalInterface
    public interface OnStartedListener {

        void onStarted();
    }

    @FunctionalInterface
    public interface OnPositionChangedListener {

        void onChanged(String url, String tag, long duration, long bufferedPosition, long currentPosition);
    }

    @FunctionalInterface
    public interface ExitFullToastyCallback {

        void onToasty(String text);
    }
}
