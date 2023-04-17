package com.yhy.all.of.tv.api.of.chan;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.YouKuVideo;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.internal.Maps;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on 2023-01-24 21:08
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class YouKuApi {
    public static final YouKuApi instance = new YouKuApi();

    private final Gson gson;

    private YouKuApi() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    public void page(MutableLiveData<List<Video>> liveData, int page, VideoType type, int mode) throws Exception {
        String typeStr = type == VideoType.FILM ? "电影" : type == VideoType.EPISODE ? "电视剧" : "纪录片";
        Map<String, Object> json = Maps.of("type", typeStr, "sort", 7);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("subIndex", 24);
        sessionMap.put("trackInfo", Maps.of("parentdrawerid", "34441"));
        sessionMap.put("spmA", "a2h05");
        sessionMap.put("level", 2);
        sessionMap.put("spmC", "drawer2");
        sessionMap.put("spmB", "8165803_SHAIXUAN_ALL");
        sessionMap.put("index", 1);
        sessionMap.put("pageName", "page_channelmain_SHAIXUAN_ALL");
        sessionMap.put("scene", "search_component_paging");
        sessionMap.put("scmB", "manual");
        sessionMap.put("path", "EP941124,EP940910,EP940904,EP940903,EP940902");
        sessionMap.put("scmA", "20140719");
        sessionMap.put("scmC", "34441");
        sessionMap.put("from", "SHAIXUAN");
        sessionMap.put("id", 227939);
        sessionMap.put("category", typeStr);

        OkGo.<String>get("https://youku.com/category/data")
            .headers("referer", "https://youku.com/channel/webmovie/list?filter=type_" + URLEncoder.encode(typeStr, "utf-8") + "_sort_7&spm=a2hja.14919748_WEBMOVIE_JINGXUAN.drawer3.d_sort_2")
            .params("params", gson.toJson(json))
            .params("session", gson.toJson(sessionMap))
            .params("pageNo", page)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String result = response.body();
                    try {
                        JSONObject jo = new JSONObject(result).getJSONObject("data").getJSONObject("filterData");
                        JSONArray ja = jo.getJSONArray("listData");
                        String json = ja.toString();

                        List<YouKuVideo> list = gson.fromJson(json, new TypeToken<>() {
                        });
                        List<Video> res = list.stream().map(it -> {
                            Video vd = new Video();
                            vd.title = it.title;
                            vd.description = it.subTitle;
                            vd.score = type == VideoType.FILM && null != it.summary ? Float.parseFloat(it.summary) : 0;
                            vd.imgCover = it.img;
                            vd.pageUrl = "https:" + it.videoLink.substring(0, it.videoLink.indexOf("?"));
                            vd.channel = "优酷";
                            vd.type = type;
                            return vd;
                        }).collect(Collectors.toList());

                        liveData.postValue(res);
                    } catch (JSONException e) {
                        LogUtils.e(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(Response<String> response) {
                    LogUtils.e(response.getException().getMessage());
                    liveData.postValue(null);
                }
            });
    }


    /**
     * 获取视频播放列表
     *
     * @param root     视频根信息
     * @param liveData 加载回调
     */
    public void playList(Video root, MutableLiveData<Video> liveData) {
    }

    private void getHtmlPage(Video root, MutableLiveData<Video> liveData) {
        String pageUrl = "https://v.youku.com/v_show/id_XNTkyNjUwNTA4MA==.html?spm=a2hcb.12701310.app.5~5!3~5!3~5~5~5!4~5~5~5~A&s=aadfdb1cf537468a82c0";
        OkGo.<String>get(pageUrl)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String html = response.body();
                    // 提取 __pinia 全局变量
                    String pinia = html.replaceAll(".*?<script>.*?window.__pinia=(.*?)<script>.*?", "$1");
                    if (!TextUtils.isEmpty(pinia)) {
                        try {
                            JSONObject jo = new JSONObject(pinia).getJSONObject("episodeMain");
                            JSONArray ja = jo.getJSONArray("listData").getJSONObject(0).getJSONArray("list").getJSONArray(0);

                            List<Map<String, Object>> list = gson.fromJson(ja.toString(), new TypeToken<>() {
                            });
                            root.episodes = list.stream().map(it -> {
                                Video vd = new Video();
                                vd.id = Objects.requireNonNull(it.get("vid")).toString();
                                vd.title = Objects.requireNonNull(it.get("playTitle")).toString().replaceAll("^.*?(第\\d+集)$", "$1");
                                vd.pageUrl = "https://v.qq.com/x/cover/" + root.id + "/" + vd.id + ".html";
                                return vd;
                            }).collect(Collectors.toList());
                            liveData.postValue(root);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
    }
}
