package com.yhy.all.of.tv.parse.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.AbstractParser;

import java.util.List;

/**
 * JY-Player
 * <p>
 * Created on 2023-06-06 9:52
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class JYParser extends AbstractParser {
    @Override
    public String name() {
        return "JY";
    }

    @Override
    public String url() {
        return "https://jx.playerjy.com/?url=";
    }

    @Override
    public List<Chan> supportedChanList() {
        return Lists.of(
            new IQiYiChan(),
            new TencentChan(),
            new YouKuChan()
        );
    }

    @Override
    public boolean isVideoUrl(String url) {
        // https://cache.we-vip.com:2096/wweebb/index.m3u8?data=MwTwYe4eNbjbAxNjEwNyYmJjg4MzVmYmVlM2QyNDdlZDlhODZiOTYyNTc1NzBjZmVl
        return url.contains(".m3u8?data=");
    }
}
