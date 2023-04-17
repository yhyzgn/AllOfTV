package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2023-04-18 03:10
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class YouKuEpisode implements Serializable {
    /**
     * id
     */
    @SerializedName("id")
    public int id;
    /**
     * level
     */
    @SerializedName("level")
    public int level;
    /**
     * type
     */
    @SerializedName("type")
    public int type;
    /**
     * typeName
     */
    @SerializedName("typeName")
    public Object typeName;
    /**
     * more
     */
    @SerializedName("more")
    public boolean more;
    /**
     * traceId
     */
    @SerializedName("traceId")
    public String traceId;
    /**
     * style
     */
    @SerializedName("style")
    public Object style;
    /**
     * render
     */
    @SerializedName("render")
    public Object render;
    /**
     * data
     */
    @SerializedName("data")
    public DataDTO data;
    /**
     * nodes
     */
    @SerializedName("nodes")
    public Object nodes;
    /**
     * layout
     */
    @SerializedName("layout")
    public Object layout;
    /**
     * header
     */
    @SerializedName("header")
    public Object header;
    /**
     * contentId
     */
    @SerializedName("contentId")
    public Object contentId;
    /**
     * raxConfig
     */
    @SerializedName("raxConfig")
    public Object raxConfig;
    /**
     * config
     */
    @SerializedName("config")
    public Object config;

    public static class DataDTO {
        /**
         * img
         */
        @SerializedName("img")
        public String img;
        /**
         * titleLine
         */
        @SerializedName("titleLine")
        public int titleLine;
        /**
         * videoType
         */
        @SerializedName("videoType")
        public String videoType;
        /**
         * summaryType
         */
        @SerializedName("summaryType")
        public String summaryType;
        /**
         * title
         */
        @SerializedName("title")
        public String title;
        /**
         * stage
         */
        @SerializedName("stage")
        public String stage;
        /**
         * subtitle
         */
        @SerializedName("subtitle")
        public String subtitle;
        /**
         * paid
         */
        @SerializedName("paid")
        public int paid;
        /**
         * action
         */
        @SerializedName("action")
        public ActionDTO action;

        public static class ActionDTO {
            /**
             * type
             */
            @SerializedName("type")
            public String type;
            /**
             * value
             */
            @SerializedName("value")
            public String value;
            /**
             * contentType
             */
            @SerializedName("contentType")
            public Object contentType;
            /**
             * contentValue
             */
            @SerializedName("contentValue")
            public Object contentValue;
            /**
             * reportDisabled
             */
            @SerializedName("reportDisabled")
            public boolean reportDisabled;
            /**
             * extra
             */
            @SerializedName("extra")
            public ExtraDTO extra;
            /**
             * callback
             */
            @SerializedName("callback")
            public Object callback;
            /**
             * reportConfig
             */
            @SerializedName("reportConfig")
            public Object reportConfig;
            /**
             * report
             */
            @SerializedName("report")
            public ReportDTO report;

            public static class ExtraDTO {
                /**
                 * showId
                 */
                @SerializedName("showId")
                public String showId;
                /**
                 * extraParams
                 */
                @SerializedName("extraParams")
                public ExtraParamsDTO extraParams;
                /**
                 * playMode
                 */
                @SerializedName("playMode")
                public String playMode;

                public static class ExtraParamsDTO {
                    /**
                     * allowMulti
                     */
                    @SerializedName("allowMulti")
                    public boolean allowMulti;
                    /**
                     * playMode
                     */
                    @SerializedName("playMode")
                    public String playMode;
                }
            }

            public static class ReportDTO {
                /**
                 * pageName
                 */
                @SerializedName("pageName")
                public String pageName;
                /**
                 * arg1
                 */
                @SerializedName("arg1")
                public String arg1;
                /**
                 * spmAB
                 */
                @SerializedName("spmAB")
                public String spmAB;
                /**
                 * spmC
                 */
                @SerializedName("spmC")
                public String spmC;
                /**
                 * spmD
                 */
                @SerializedName("spmD")
                public String spmD;
                /**
                 * scmAB
                 */
                @SerializedName("scmAB")
                public String scmAB;
                /**
                 * scmC
                 */
                @SerializedName("scmC")
                public String scmC;
                /**
                 * scmD
                 */
                @SerializedName("scmD")
                public String scmD;
                /**
                 * index
                 */
                @SerializedName("index")
                public int index;
                /**
                 * reportDataOpt
                 */
                @SerializedName("reportDataOpt")
                public boolean reportDataOpt;
                /**
                 * trackInfo
                 */
                @SerializedName("trackInfo")
                public TrackInfoDTO trackInfo;
                /**
                 * utParam
                 */
                @SerializedName("utParam")
                public Object utParam;

                public static class TrackInfoDTO {
                    /**
                     * componentId
                     */
                    @SerializedName("component_id")
                    public String componentId;
                    /**
                     * pvvVid
                     */
                    @SerializedName("pvv_vid")
                    public String pvvVid;
                    /**
                     * componentInstanceId
                     */
                    @SerializedName("component_instance_id")
                    public int componentInstanceId;
                    /**
                     * utdid
                     */
                    @SerializedName("utdid")
                    public String utdid;
                    /**
                     * servertime
                     */
                    @SerializedName("servertime")
                    public long servertime;
                    /**
                     * rscR
                     */
                    @SerializedName("rsc_r")
                    public String rscR;
                    /**
                     * videoid
                     */
                    @SerializedName("videoid")
                    public int videoid;
                    /**
                     * rscS
                     */
                    @SerializedName("rsc_s")
                    public String rscS;
                    /**
                     * pageid
                     */
                    @SerializedName("pageid")
                    public String pageid;
                    /**
                     * lifecycle
                     */
                    @SerializedName("lifecycle")
                    public int lifecycle;
                    /**
                     * drawerid
                     */
                    @SerializedName("drawerid")
                    public String drawerid;
                    /**
                     * materialId
                     */
                    @SerializedName("material_id")
                    public String materialId;
                    /**
                     * pvvSid
                     */
                    @SerializedName("pvv_sid")
                    public String pvvSid;
                    /**
                     * videoId
                     */
                    @SerializedName("video_id")
                    public int videoId;
                    /**
                     * cmsReqId
                     */
                    @SerializedName("cms_req_id")
                    public String cmsReqId;
                }
            }
        }
    }
}
