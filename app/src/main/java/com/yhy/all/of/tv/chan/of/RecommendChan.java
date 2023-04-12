package com.yhy.all.of.tv.chan.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import java.util.List;

/**
 * 推荐
 * <p>
 * Created on 2023-04-06 22:46
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class RecommendChan implements Chan {
    @Override
    public String name() {
        return "推荐";
    }

    @Override
    public List<Tab> tabList() {
        return Lists.of(
                Tab.create(VideoType.FILM, params -> {
                    LogUtils.i("推荐-电影加载");
                    return Lists.of();
                }),
                Tab.create(VideoType.EPISODE, params -> {
                    LogUtils.i("推荐-电视剧加载");
                    return Lists.of();
                })
        );
    }
}
