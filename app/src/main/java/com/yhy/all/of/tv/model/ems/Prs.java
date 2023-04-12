package com.yhy.all.of.tv.model.ems;

import java.util.Arrays;
import java.util.Objects;

/**
 * 解析平台枚举
 * <p>
 * Created on 2023-01-29 21:41
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Prs {
    PAN_GU(1, "盘古", "https://www.pangujiexi.cc/jiexi.php?url="),
    OK(2, "OK解析", "https://okjx.cc/?url="),
    CK(3, "CK解析", "https://www.ckplayer.vip/jiexi/?url="),
    IM_1907(4, "IM1907", "https://im1907.top/?jx="),
    YE_MU(5, "夜幕", "https://www.yemu.xyz/?url="),
    JSON_PLAYER(6, "JsonPlayer", "https://jx.jsonplayer.com/player/?url="),
    M3U8_TV(7, "M3U8.TV", "https://jx.m3u8.tv/jiexi/?url="),
    AI_DOU_ER(8, "爱豆儿", "https://jx.aidouer.net/?url="),
    XIA_MI(9, "虾米", "https://jx.xmflv.com/?url="),
    DV4K(10, "4K", "https://jx.4kdv.com/?url="),
    NX_FLV(11, "诺讯", "https://www.nxflv.com/?url="),
    BI_LIN(12, "比邻", "https://video.bilnn.com/api.php?url="),
    ;

    private final Integer code;
    private final String name;
    private final String url;

    Prs(Integer code, String name, String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public static Prs parse(int code) {
        return Arrays.stream(Prs.values()).filter(it -> Objects.equals(it.code, code)).findFirst().orElse(M3U8_TV);
    }
}
