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
        return "M3U8TV";
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
        // https://122.228.8.29:4433/Cache/qiyi/274faa6b6656e653f6733d5212b8f279.m3u8?vkey=6636633341564d485656514a4167494143514a5555565144414655485541465155464e5555564a5642674e5544674d4657675655
        // https://122.228.8.29:4433/Cache/hls/135bfdfd2ad755483c5adb9bfba2b543.m3u8?vkey=343266374277554141314d4342776b4456414e5741674543414134465531454f42514544436c554755776346425159444451514b
        return
                url.contains(".m3u8?vkey=") // 爱奇艺 腾讯
                ;
    }
}