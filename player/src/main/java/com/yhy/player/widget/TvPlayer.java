package com.yhy.player.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.yhy.player.R;

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

    private StyledPlayerView spvExo;
    private ConstraintLayout clController;
    private TextView tvTitle;
    private ImageView ivPlayOrPause;
    private ProgressBar pbLoading;
    private SilenceTimeBar stbDrag;
    private SilenceTimeBar stbPosition;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private ExoPlayer mPlayer;

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
        clController = rootView.findViewById(R.id.cl_controller);
        tvTitle = rootView.findViewById(R.id.tv_title);
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

    public void play(String title, String url) {
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
        mPlayer.play();

        stbDrag.setDuration(65536);
        stbDrag.setPosition(32001);
        stbDrag.setBufferedPosition(42001);

        stbPosition.setDuration(65536);
        stbPosition.setPosition(32001);
        stbPosition.setBufferedPosition(42001);
    }

    private final Player.Listener mExoListener = new Player.Listener() {
        @Override
        public void onIsLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
        }

        @Override
        public void onPlayerError(PlaybackException error) {
        }
    };

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
}
