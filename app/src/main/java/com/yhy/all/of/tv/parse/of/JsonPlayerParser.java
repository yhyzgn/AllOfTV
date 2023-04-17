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
public class JsonPlayerParser extends AbstractParser {
    @Override
    public String name() {
        return "JsonPlayer";
    }

    @Override
    public String url() {
        return "https://jx.jsonplayer.com/player/?url=";
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
        // https://110.42.2.115:9092/c/m3u8_301/d14977a5a27cbea7ce0d6dd3f6d917c9.m3u8?vkey=2fadEjZDjnhVZtuNt6zEqWKJ6djffjLJf0_vzFTM
        // https://110.42.2.115:9092/c/qq_301/omd9c6b174751ce0373beb4a55e1b4209e.mp4?vkey=5d086MpQ3l_88doDUFDeEe6aeyxHKUkKt35x0oQy
        return url.contains(".m3u8?vkey=") // 爱奇艺
            || url.contains(".mp4?vkey=") // 腾讯
            ;
    }
}
