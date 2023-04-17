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
public class NuoXunParser extends AbstractParser {
    @Override
    public String name() {
        return "诺讯";
    }

    @Override
    public String url() {
        return "https://www.nxflv.com/?url=";
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
        // https://api.nxflv.com/Cache/YouKu/ca580a5f1cd19562aa7a216a529bd61b.m3u8
        // https://2f0e9f8a4970eabbb2bc4c13495e7127.rdt.tfogc.com:49156/variety.tc.qq.com/q0033rtpdxb.mp4?vkey=4DA41EB60CC615A9ACF8AE8F81CBDC5669655A23EB2F0294DACA2C7EBBA842459C0F586A0E83110F92BA89BECF843988450BBFCAE9F8AF1B96137E7CA8E67C7CA57AFAF56DD7545A9757522A627A5CCC795139BF2963DAB508181BD085782745906C1DF0373A6CA9931344C5C4DF16E2034B41811E2C38D5D20D8B4F00C3F27E7FE016386D65C89B&QQ=335583&From=www.nxflv.com
        return url.endsWith(".m3u8")  // 爱奇艺
            || url.contains(".mp4?vkey=") // 腾讯
            ;
    }
}
