package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2023-01-24 21:27
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class IQiYiVideo {

    /**
     * order
     */
    @SerializedName("order")
    public Integer order;
    /**
     * desc
     */
    @SerializedName("desc")
    public String desc;
    /**
     * description
     */
    @SerializedName("description")
    public String description;
    /**
     * date
     */
    @SerializedName("date")
    public DateDTO date;
    /**
     * showDate
     */
    @SerializedName("showDate")
    public String showDate;
    /**
     * qiyiPublishDate
     */
    @SerializedName("qiyiPublishDate")
    public Long qiyiPublishDate;
    /**
     * url
     */
    @SerializedName("url")
    public String url;
    /**
     * duration
     */
    @SerializedName("duration")
    public Double duration;
    /**
     * moviePageTop
     */
    @SerializedName("moviePageTop")
    public Integer moviePageTop;
    /**
     * tag
     */
    @SerializedName("tag")
    public String tag;
    /**
     * creator
     */
    @SerializedName("creator")
    public List<CreatorDTO> creator;
    /**
     * contributor
     */
    @SerializedName("contributor")
    public List<ContributorDTO> contributor;
    /**
     * guest
     */
    @SerializedName("guest")
    public List<?> guest;
    /**
     * pingback
     */
    @SerializedName("pingback")
    public PingbackDTO pingback;
    /**
     * prevue
     */
    @SerializedName("prevue")
    public PrevueDTO prevue;
    /**
     * isGame
     */
    @SerializedName("isGame")
    public Boolean isGame;
    /**
     * isAd
     */
    @SerializedName("isAd")
    public Boolean isAd;
    /**
     * isPca
     */
    @SerializedName("isPca")
    public Boolean isPca;
    /**
     * disableCollect
     */
    @SerializedName("disableCollect")
    public Boolean disableCollect;
    /**
     * disabledStateMode
     */
    @SerializedName("disabledStateMode")
    public String disabledStateMode;
    /**
     * adPosition
     */
    @SerializedName("adPosition")
    public Integer adPosition;
    /**
     * dqUpdatestatus
     */
    @SerializedName("dq_updatestatus")
    public String dqUpdatestatus;
    /**
     * entityId
     */
    @SerializedName("entity_id")
    public Long entityId;
    /**
     * albumId
     */
    @SerializedName("album_id")
    public Long albumId;
    /**
     * tvId
     */
    @SerializedName("tv_id")
    public Long tvId;
    /**
     * title
     */
    @SerializedName("title")
    public String title;
    /**
     * firstEpisodeTitle
     */
    @SerializedName("firstEpisodeTitle")
    public String firstEpisodeTitle;
    /**
     * filmId
     */
    @SerializedName("film_id")
    public Long filmId;
    /**
     * channelId
     */
    @SerializedName("channel_id")
    public Integer channelId;
    /**
     * snsScore
     */
    @SerializedName("sns_score")
    public float snsScore;
    /**
     * hotScore
     */
    @SerializedName("hot_score")
    public Integer hotScore;
    /**
     * isEpisode
     */
    @SerializedName("is_episode")
    public Boolean isEpisode;
    /**
     * nativeUrl
     */
    @SerializedName("native_url")
    public String nativeUrl;
    /**
     * playUrl
     */
    @SerializedName("play_url")
    public String playUrl;
    /**
     * pageUrl
     */
    @SerializedName("page_url")
    public String pageUrl;
    /**
     * isDownloadAllowed
     */
    @SerializedName("is_download_allowed")
    public Boolean isDownloadAllowed;
    /**
     * payMark
     */
    @SerializedName("pay_mark")
    public String payMark;
    /**
     * payMarkUrl
     */
    @SerializedName("pay_mark_url")
    public String payMarkUrl;
    /**
     * payMarkShow
     */
    @SerializedName("pay_mark_show")
    public String payMarkShow;
    /**
     * markTypeShow
     */
    @SerializedName("mark_type_show")
    public Integer markTypeShow;
    /**
     * posterMarkType
     */
    @SerializedName("poster_mark_type")
    public Integer posterMarkType;
    /**
     * cornerMark
     */
    @SerializedName("cornerMark")
    public String cornerMark;
    /**
     * timeLength
     */
    @SerializedName("time_length")
    public Integer timeLength;
    /**
     * imageCover
     */
    @SerializedName("image_cover")
    public String imageCover;
    /**
     * imageUrlNormal
     */
    @SerializedName("image_url_normal")
    public String imageUrlNormal;
    /**
     * imageUrlDynamicNormal
     */
    @SerializedName("image_url_dynamic_normal")
    public String imageUrlDynamicNormal;
    /**
     * isIvg
     */
    @SerializedName("is_ivg")
    public Boolean isIvg;
    /**
     * tagPcw
     */
    @SerializedName("tag_pcw")
    public String tagPcw;
    /**
     * uploaderId
     */
    @SerializedName("uploader_id")
    public Long uploaderId;
    /**
     * uploader
     */
    @SerializedName("uploader")
    public UploaderDTO uploader;
    /**
     * displayName
     */
    @SerializedName("display_name")
    public String displayName;
    /**
     * shortDisplayName
     */
    @SerializedName("short_display_name")
    public String shortDisplayName;
    /**
     * contentType
     */
    @SerializedName("content_type")
    public String contentType;
    /**
     * recom
     */
    @SerializedName("recom")
    public RecomDTO recom;
    /**
     * isMemberOnly
     */
    @SerializedName("isMemberOnly")
    public Boolean isMemberOnly;
    /**
     * isMemberFree
     */
    @SerializedName("isMemberFree")
    public Boolean isMemberFree;
    /**
     * bossStatus
     */
    @SerializedName("bossStatus")
    public String bossStatus;
    /**
     * currentPeriod
     */
    @SerializedName("current_period")
    public Integer currentPeriod;
    /**
     * liveSummaryId
     */
    @SerializedName("live_summary_id")
    public Integer liveSummaryId;

    public static class DateDTO {
        /**
         * day
         */
        @SerializedName("day")
        public Integer day;
        /**
         * month
         */
        @SerializedName("month")
        public Integer month;
        /**
         * year
         */
        @SerializedName("year")
        public Integer year;
    }

    public static class PingbackDTO {
        /**
         * ht
         */
        @SerializedName("ht")
        public String ht;
    }

    public static class PrevueDTO {
        /**
         * entityId
         */
        @SerializedName("entity_id")
        public Long entityId;
        /**
         * channelId
         */
        @SerializedName("channel_id")
        public Integer channelId;
        /**
         * title
         */
        @SerializedName("title")
        public String title;
        /**
         * playUrl
         */
        @SerializedName("play_url")
        public String playUrl;
        /**
         * imageUrl
         */
        @SerializedName("image_url")
        public String imageUrl;
        /**
         * previewStart
         */
        @SerializedName("preview_start")
        public Integer previewStart;
        /**
         * previewEnd
         */
        @SerializedName("preview_end")
        public Integer previewEnd;
    }

    public static class UploaderDTO {
        /**
         * id
         */
        @SerializedName("id")
        public Long id;
        /**
         * name
         */
        @SerializedName("name")
        public String name;
        /**
         * icon
         */
        @SerializedName("icon")
        public String icon;
        /**
         * updateTimestamp
         */
        @SerializedName("updateTimestamp")
        public Integer updateTimestamp;
    }

    public static class RecomDTO {
        /**
         * id
         */
        @SerializedName("id")
        public Long id;
        /**
         * title
         */
        @SerializedName("title")
        public String title;
        /**
         * pingback
         */
        @SerializedName("pingback")
        public PingbackDTO pingback;
        /**
         * extension
         */
        @SerializedName("extension")
        public ExtensionDTO extension;
        /**
         * thirdEntityId
         */
        @SerializedName("third_entity_id")
        public Integer thirdEntityId;

        public static class PingbackDTO {
            /**
             * docId
             */
            @SerializedName("docId")
            public Long docId;
        }

        public static class ExtensionDTO {
            /**
             * sort
             */
            @SerializedName("sort")
            public Integer sort;
            /**
             * score
             */
            @SerializedName("score")
            public Double score;
        }
    }

    public static class CreatorDTO {
        /**
         * id
         */
        @SerializedName("id")
        public String id;
        /**
         * name
         */
        @SerializedName("name")
        public String name;
    }

    public static class ContributorDTO {
        /**
         * id
         */
        @SerializedName("id")
        public String id;
        /**
         * name
         */
        @SerializedName("name")
        public String name;
    }
}
