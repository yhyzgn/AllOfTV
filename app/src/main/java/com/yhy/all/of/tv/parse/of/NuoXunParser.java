package com.yhy.all.of.tv.parse.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.AbstractParser;

import java.util.List;

/**
 * JsonPlayer
 * <p>
 * Created on 2023-04-13 22:06
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class NuoXunParser extends AbstractParser {
    @Override
    public String name() {
        return "诺讯";
    }

    @Override
    public String url() {
        return "https://www.nxflv.com/?url=";
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
        // https://api.nxflv.com/Cache/YouKu/ca580a5f1cd19562aa7a216a529bd61b.m3u8
        return url.endsWith(".m3u8");
    }
}
