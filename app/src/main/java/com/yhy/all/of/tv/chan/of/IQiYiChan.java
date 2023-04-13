package com.yhy.all.of.tv.chan.of;

import com.yhy.all.of.tv.api.of.chan.IQiYiApi;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;
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
public class IQiYiChan implements Chan {
    @Override
    public String name() {
        return "爱奇艺";
    }

    @Override
    public List<Tab> tabList() {
        return Lists.of(
                Tab.create(this, VideoType.FILM, (liveData, params) -> IQiYiApi.instance.page(liveData, params.getInt("page"), VideoType.FILM, 11)),
                Tab.create(this, VideoType.EPISODE, (liveData, params) -> IQiYiApi.instance.page(liveData, params.getInt("page"), VideoType.EPISODE, 11))
        );
    }
}
