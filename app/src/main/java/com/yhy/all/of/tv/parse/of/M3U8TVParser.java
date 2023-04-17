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
public class M3U8TVParser extends AbstractParser {
    @Override
    public String name() {
        return "M3U8.TV";
    }

    @Override
    public String url() {
        return "https://jx.m3u8.tv/jiexi/?url=";
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
        // https://storage.360buyimg.com/satisfaction/b10c1ac1-bbc2-4ea6-a529-c19a4430e022.jpg?Expires=1675424024&AccessKey=j6SFUpPYU982lF3x&Signature=gqJUtGcrgNqlwMoVBwafkAhgPCQ%3D
        // https://110.42.2.115:9092/c/ali_301/3becc22bebba6503e3324cc1a38029d7.m3u8?vkey=c9c4rkmyDfph2eh4bGr2PUvRvICvILkqbXsy3MYT
        return url.contains("zh188.net") && url.contains("=.m3u8") || // 爱奇艺
                url.contains("om.tc.qq.com") && url.contains(".mp4?from=M3U8TV") // 腾讯
                ;
    }
}