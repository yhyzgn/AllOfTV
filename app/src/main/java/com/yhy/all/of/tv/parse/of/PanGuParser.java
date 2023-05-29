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
public class PanGuParser extends AbstractParser {
    @Override
    public String name() {
        return "盘古";
    }

    @Override
    public String url() {
        return "https://www.pangujiexi.cc/jiexi.php?url=";
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
        // https://2682523f981a340b2dbf94860681e7d2.rdt.tfogc.com:49156/om.tc.qq.com/video.dispatch.tc.qq.com/gzc_1000102_0b53vaa3waabjiajjktfizsmdkgdxosadp2a.f10217.mp4?mkey=6a30793107fb6c6fb8100d1e58837e790e&name=M3U8TV&vkey=DDCBF725394D2CE1226D3DBF1D14FDF24CB8C13E11D54D7EDDA1F080D2B0D13387396D77A7AB87D709E646D69B7EFFDC208A42EABB6EF15851755BA8F95EE3FA9D664B1E324B88E6F50BFDC090856E459820FA6C7AAA28C40E444B17824C4F19437FF5562016870A2BB79DE255AD1AF5D1746642B906DAFF6F5801E6A3253B12&sdtfrom=v1010&type=mp4&platform=10901&br=117&fmt=10217&ver=0&sp=1&guid=7648102217445883&cip=222.71.216.36&cpro=31&cisp=1&stdfrom=1100&proto=https
        return url.contains(".m3u8?vkey=")
            || url.contains(".mp4?vkey=")
            || url.contains("storage.360buyimg.com") && url.contains(".jpg?Expires=") // 爱奇艺/优酷
            || url.contains("video.dispatch.tc.qq.com") && url.contains(".mp4?sdtfrom=") // 腾讯
            ;
    }
}
