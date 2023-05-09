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
public class AiDouParser extends AbstractParser {
    @Override
    public String name() {
        return "爱豆";
    }

    @Override
    public String url() {
        return "https://jx.aidouer.net/?url=";
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
        // https://sf9-dycdn-tos.pstatp.com/obj/tos-cn-i-8gu37r9deh/9b860d81fc5d4b6287bc63dabf93418f?filename=1.mp4
        // https://299e080dc35899458b6a00839d3962c7.rdt.tfogc.com:49156/om.tc.qq.com/video.dispatch.tc.qq.com/gzc_1000102_0b53qiadsaaac4aaf32kcnsmbawdhghaapka.f10217.mp4?mkey=766dbd3e442e07525e02de7191e93e76&sdtfrom=v5010&vkey=965C269338F6C37F705EF6B057970E8E4BA2BC810683EFE1F32B1F60A653D4587783C220F183C63AB4F281264FB46681219496862384D65CA9BC8101F8F22F2EB0F86713914C2863A14CC9E72A07DBDAB9578242A9676C9D72F7F621AC6DDB68C635B8552882DD14846434702C39D39CB3E070E327F876AF5F5FDDAAE150F050C166B611335C6C6B&sdtfrom=v1010&type=mp4&platform=10901&br=117&fmt=10217&ver=0&sp=1&guid=5b4ff2442b806e5e2feeac21460754bb&cip=116.249.125.231&cpro=29&cisp=1&stdfrom=1100&proto=https
        return url.contains(".mp4");
    }
}
