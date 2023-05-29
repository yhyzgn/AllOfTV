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
        // 爱奇艺/优酷：https://110.42.2.247:9092/c/m3u8_301/93b24b662abb90df0f79a5dc6e3d869c.m3u8?vkey=62a0dI6j_jtMFpiPwTXYtOJ7v5kTitt3exMGTfRM
        // 腾讯：https://110.42.2.247:9092/c/qq_301/91a9666ea75179bb4038b7e64b279d59.mp4?vkey=1abflCzMaWxCwxI8fRNDT5RLs8PpaIL3uSBsHXRr

        // 爱奇艺/优酷：https://storage.360buyimg.com/satisfaction/f25a3511-037e-4e18-8835-6c44e05fde2e.jpg?Expires=1685426463&AccessKey=j6SFUpPYU982lF3x&Signature=PHX7%2FQVehBpYBwqrutSbOaEDWj0%3D
        // 腾讯：https://4a63e6105eae2e703b267ee6307cfbdd.rdt.tfogc.com:49156/om.tc.qq.com/video.dispatch.tc.qq.com/gzc_1000102_0b53vaa3waabjiajjktfizsmdkgdxosadp2a.f10217.mp4?sdtfrom=v5010&vkey=724043563D324EB054F71BE7A8F4CE03E1F273484EF05C1FE692675BEC163C4CC58B5988369787F541EB5102559A7ACDBC8598D22C862A1CFF6EE43BFD43451A90037E7E4F11C0BB5E96238CC402716FDAC01826BCC99A99EC382FE31AAF518FCEE37A3054377AC0CE8BBA7870FA4EAEE5A803800E55F6A2C5542059951BDCFC180F1A2D8FD45AD9D7A313866DB0A4D9&sdtfrom=v1010&type=mp4&platform=10901&br=117&fmt=10217&ver=0&sp=1&guid=d563aebe9bb3341e
        return url.contains(".m3u8?vkey=")
            || url.contains(".mp4?vkey=")
            || url.contains("storage.360buyimg.com") && url.contains(".jpg?Expires=") // 爱奇艺/优酷
            || url.contains("video.dispatch.tc.qq.com") && url.contains(".mp4?sdtfrom=") // 腾讯
            ;
    }
}
