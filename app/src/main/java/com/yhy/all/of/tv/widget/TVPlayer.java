package com.yhy.all.of.tv.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yhy.all.of.tv.R;

/**
 * TV 播放器
 * <p>
 * Created on 2023-04-13 17:36
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class TVPlayer extends StandardGSYVideoPlayer {

    public TVPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public TVPlayer(Context context) {
        super(context);
        init();
    }

    public TVPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_player_control;
    }
}
