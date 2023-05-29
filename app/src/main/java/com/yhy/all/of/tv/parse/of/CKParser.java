package com.yhy.all.of.tv.parse.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.AbstractParser;

import java.util.List;

/**
 * 爱豆
 * <p>
 * Created on 2023-05-09 14:38
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class CKParser extends AbstractParser {
    @Override
    public String name() {
        return "CK";
    }

    @Override
    public String url() {
        return "https://www.ckplayer.vip/jiexi/?url=";
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
        // https://cdn.oss-cn-m3u8.tv-nanjing-chengdu.myqcloud.com.zh188.net/PlayVideo.php?url=M3EvQmdSaEtGZ2c0NWU0MnFqWHA3SjYrQTcwL2FwaldmLytnMC9wTVNwbHNod1R5T2hpcjBhRlh2ZzAwUGd5WW5XZFFIV3p3QlhZZlRsWWdOK25mejZ4ZXdwSkZsbU9Md3BGaTBjN2VHWlpRd2E2bGcvZHJycEU0ckpUY3Zqck9lYnk5Lyt3NGJFeEI0RGpGSjVXUXFGU0tRcEIzWnkza3FmaEM0R1M5MUQzalFkcHZJS0Ryc1pEaThSOXJsOVFZL0NMNldkS1JjdjYycVRURGhqZGlza0xiOXFNRE5vbjRQcDg0RTUvV25ReHhod0c2eVZBK2QybUt5L00yNjJud0hRMUdlV0ZkVHR4amJabW5UYURUT1E9PQ==.m3u8
        return url.endsWith(".m3u8");
    }
}
