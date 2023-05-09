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
        // https://cdn.oss-cn-m3u8.tv-nanjing-chengdu.myqcloud.com.zh188.net/PlayVideo.php?url=M3EvQmdSaEtGZ2c0NWU0MnFqWHA3SjYrQTcwL2FwaldmLytnMC9wTVNwbHNod1R5T2hpcjBhRlh2ZzAwUGd5WW5XZFFIV3p3QlhZZlRsWWdOK25mejZ4ZXdwSkZsbU9Md3BGaTBjN2VHWlpRd2E2bGcvZHJycEU0ckpUY3Zqck9lYnk5Lyt3NGJFeEI0RGpGSjVXUXFPdm1GaGtLbDMydjFRMEs3eGJtaGRVTVBzR1lSd1Z5Vm9iMWNJOE5EeXRCNHY5VGpubFRXVjFSSndUMUUzQm1FU2xDajZhK0IyR1VMYis4cERYODd2dTZNQ1RWNGtxL3lnOW9FTTBQOFF3M05HdXdyVVBHcURIYVdKeUh1UDB3Wnc9PQ==.m3u8
        // https://cdn.oss-cn-m3u8.tv-nanjing-chengdu.myqcloud.com.zh188.net/PlayVideo.php?url=M3EvQmdSaEtGZ2c0NWU0MnFqWHA3SjYrQTcwL2FwaldmLytnMC9wTVNwbHNod1R5T2hpcjBhRlh2ZzAwUGd5WW5XZFFIV3p3QlhZZlRsWWdOK25mejZ4ZXdwSkZsbU9Md3BGaTBjN2VHWlpRd2E2bGcvZHJycEU0ckpUY3Zqck9lYnk5Lyt3NGJFeEI0RGpGSjVXUXFDdDR2NHlSY2EwbUFwYzB4ZlhJai9VbXg3MEM0Njk3Z2JJbTRoVnRkNFlPR2YrNWJiNFlUS0Y4V3JDRk9PK3FsY1dBczYrNGxHWFc1eUpQaUFBMVlQakdtSURONSszRDVUNUhFdW5xVzZUUHdSaDd3U1RyTnJYQTgzMDB1QUphcWc9PQ==.m3u8
        return url.contains("zh188.net") && url.endsWith(".m3u8") || // 爱奇艺
                url.contains("om.tc.qq.com") && url.contains(".mp4?from=M3U8TV") // 腾讯
                ;
    }
}