package com.yhy.all.of.tv.parse;

import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.component.base.BaseActivity;

import java.util.List;

/**
 * 播放源解析器
 * <p>
 * Created on 2023-04-13 22:03
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Parser {

    String name();

    String url();

    List<Chan> supportedChanList();

    boolean isVideoUrl(String url);

    void load(BaseActivity activity, MutableLiveData<String> liveData, String url);

    void stop(boolean destroy);
}
