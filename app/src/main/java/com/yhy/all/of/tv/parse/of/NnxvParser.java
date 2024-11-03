package com.yhy.all.of.tv.parse.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.AbstractParser;

import java.util.List;

/**
 * Created on 2023-05-29 15:53
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class NnxvParser extends AbstractParser {
    @Override
    public String name() {
        return "七哥";
    }

    @Override
    public String url() {
        return "https://jx.nnxv.cn/tv.php?url=";
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
        // https://cache.m3u8.pw/Cache/qq/ac941ebae5ea8e9be9497fccfcd31232.m3u8?vkey=ad68AAZUCVMFBFYJBgQGVQpQVQYFBANQBwNUVAMCA10MVg0GAVQJ
        return url.contains(".m3u8?vkey=");
    }
}
