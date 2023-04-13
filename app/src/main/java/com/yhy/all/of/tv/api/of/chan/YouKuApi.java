package com.yhy.all.of.tv.api.of.chan;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.YouKuVideo;
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

                        List<YouKuVideo> list = gson.fromJson(json, new TypeToken<List<YouKuVideo>>() {
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
}
