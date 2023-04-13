package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2023-01-25 03:23
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class YouKuVideo {
    /**
     * summaryType
     */
    @SerializedName("summaryType")
    public String summaryType;
    /**
     * type
     */
    @SerializedName("type")
    public String type;
    /**
     * img
     */
    @SerializedName("img")
    public String img;
    /**
     * summary
     */
    @SerializedName("summary")
    public String summary;
    /**
     * title
     */
    @SerializedName("title")
    public String title;
    /**
     * subTitle
     */
    @SerializedName("subTitle")
    public String subTitle;
    /**
     * videoLink
     */
    @SerializedName("videoLink")
    public String videoLink;
    /**
     * rightTagText
     */
    @SerializedName("rightTagText")
    public String rightTagText;
}
