package com.yhy.all.of.tv.model;

import com.yhy.all.of.tv.model.ems.VideoType;

import java.io.Serializable;
import java.util.List;

/**
 * 视频源信息
 * <p>
 * Created on 2023-01-20 02:48
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class Video implements Serializable {
    public String id;
    public String title;
    public String description;
    public float score;
    public String imgCover;
    public String pageUrl;
    public String channel;
    public VideoType type;
    public List<String> tags;
    public List<String> directors;
    public List<String> actors;
    public int year;
    public int month;
    public int day;
    public String updateStatus;

    /**
     * 剧集
     */
    public List<Video> episodes;

    @Override
    public String toString() {
        return "Video{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", score=" + score +
            ", imgCover='" + imgCover + '\'' +
            ", pageUrl='" + pageUrl + '\'' +
            ", channel='" + channel + '\'' +
            ", type=" + type +
            ", tags=" + tags +
            ", directors=" + directors +
            ", actors=" + actors +
            ", year=" + year +
            ", month=" + month +
            ", day=" + day +
            ", episodes=" + episodes +
            '}';
    }
}
