package com.yhy.all.of.tv.parse.of;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.AbstractParser;

import java.util.List;

/**
 * 盘古解析
 * <p>
 * Created on 2023-05-29 16:01
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class IM1907Parser extends AbstractParser {
    @Override
    public String name() {
        return "IM1907";
    }

    @Override
    public String url() {
        return "https://im1907.top/?jx=";
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
        // https://play.hhuus.com/play/neggx2re/index.m3u8
        // https://vv.jisuzyv.com/play/Xe0QOENa/index.m3u8
        return url.endsWith("index.m3u8") // 爱奇艺 腾讯
                ;
    }
}
