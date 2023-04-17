package com.yhy.all.of.tv.chan.of;

import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.api.of.chan.TencentApi;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;

import java.util.List;

/**
 * 爱奇艺
 * <p>
 * Created on 2023-04-06 22:46
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class TencentChan implements Chan {
    @Override
    public String name() {
        return "腾讯";
    }

    @Override
    public List<Tab> tabList() {
        return Lists.of(
                Tab.create(this, VideoType.FILM, (liveData, params) -> TencentApi.instance.page(liveData, params.getInt("page"), VideoType.FILM, 11)),
                Tab.create(this, VideoType.EPISODE, (liveData, params) -> TencentApi.instance.page(liveData, params.getInt("page"), VideoType.EPISODE, 11))
        );
    }

    @Override
    public void loadPlayList(Video root, MutableLiveData<Video> liveData) {
    }
}
