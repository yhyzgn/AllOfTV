package com.yhy.all.of.tv.model.ems;

/**
 * 视频类型
 * <p>
 * Created on 2023-01-20 03:15
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public enum VideoType {
    /**
     * 电影
     */
    FILM(100, "电影"),

    /**
     * 剧集
     */
    EPISODE(200, "电视剧"),

    /**
     * 未知，不指定类型
     */
    UNKNOWN(10000, "未知"),
    ;

    public final Integer code;
    public final String name;

    VideoType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
