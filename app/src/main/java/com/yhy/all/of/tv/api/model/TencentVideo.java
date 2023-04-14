package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2023-01-25 01:04
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class TencentVideo {
    /**
     * id
     */
    @SerializedName("id")
    public String id;
    /**
     * type
     */
    @SerializedName("type")
    public String type;
    /**
     * params
     */
    @SerializedName("params")
    public ParamsDTO params;
    /**
     * childrenList
     */
    @SerializedName("children_list")
    public ChildrenListDTO childrenList;
    /**
     * reportInfos
     */
    @SerializedName("report_infos")
    public ReportInfosDTO reportInfos;
    /**
     * operations
     */
    @SerializedName("operations")
    public OperationsDTO operations;
    /**
     * flipInfos
     */
    @SerializedName("flip_infos")
    public FlipInfosDTO flipInfos;

    public static class ParamsDTO {
        /**
         * type
         */
        @SerializedName("type")
        public String type;
        /**
         * title
         */
        @SerializedName("title")
        public String title;
        /**
         * cid
         */
        @SerializedName("cid")
        public String cid;
        /**
         * posterType
         */
        @SerializedName("poster_type")
        public String posterType;
        /**
         * secondTitle
         */
        @SerializedName("second_title")
        public String secondTitle;
        /**
         * newPicVt
         */
        @SerializedName("new_pic_vt")
        public String newPicVt;
        /**
         * columnId
         */
        @SerializedName("column_id")
        public String columnId;
        /**
         * newPicHz
         */
        @SerializedName("new_pic_hz")
        public String newPicHz;
        /**
         * epsodePubtime
         */
        @SerializedName("epsode_pubtime")
        public String epsodePubtime;
        /**
         * opinionScore
         */
        @SerializedName("opinion_score")
        public String opinionScore;
        /**
         * seriesName
         */
        @SerializedName("series_name")
        public String seriesName;
        /**
         * publishDate
         */
        @SerializedName("publish_date")
        public String publishDate;
    }

    public static class ChildrenListDTO {
    }

    public static class ReportInfosDTO {
    }

    public static class OperationsDTO {
    }

    public static class FlipInfosDTO {
    }
}
