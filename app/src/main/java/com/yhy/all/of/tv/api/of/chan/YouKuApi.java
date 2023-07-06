package com.yhy.all.of.tv.api.of.chan;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.YouKuEpisode;
import com.yhy.all.of.tv.api.model.YouKuVideo;
import com.yhy.all.of.tv.internal.Maps;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.TabType;
import com.yhy.all.of.tv.rand.IpRand;
import com.yhy.all.of.tv.rand.UserAgentRand;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.widget.web.JsExtractWebView;
import com.yhy.all.of.tv.widget.web.sonic.Sonic;

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

    public void page(MutableLiveData<List<Video>> liveData, int page, TabType type, int mode) throws Exception {
        String typeStr = type == TabType.FILM ? "电影" : type == TabType.EPISODE ? "电视剧" : "纪录片";
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
            .headers("referer", "https://youku.com/channel/webmovie/list?filter=type_" + URLEncoder.encode(typeStr, "utf-8") + "&spm=a2hja.14919748_WEBTV_JINGXUAN.drawer3.d_tags_2")
            .headers("User-Agent", UserAgentRand.get())
            .headers("X-Forwarded-For", IpRand.get())
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
                            vd.score = type == TabType.FILM && null != it.summary ? Float.parseFloat(it.summary) : 0;
                            vd.imgCover = it.img;
                            vd.pageUrl = "https:" + it.videoLink;
                            vd.channel = "优酷";
                            vd.type = type;
                            vd.updateStatus = type != TabType.FILM ? it.summary : "";
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
    public void playList(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData) {
        // 电影和剧集都通过 html 加载
        getHtmlPage(activity, root, liveData);
    }

    private void getHtmlPage(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData) {
        String pageUrl = root.pageUrl;

        MutableLiveData<String> tempLiveData = new MutableLiveData<>();
        JsExtractWebView wv = new JsExtractWebView(activity).attach(activity, pageUrl, tempLiveData, "window.__INITIAL_DATA__");
        Sonic sonic = new Sonic(activity, wv);

        tempLiveData.observe(activity, data -> {
            LogUtils.i("data", data);
            if (!TextUtils.isEmpty(data)) {
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONObject joData = jo.getJSONObject("data");

                    jo = joData.getJSONObject("model").getJSONObject("detail").getJSONObject("data");
                    JSONArray jaNodes = jo.getJSONArray("nodes").getJSONObject(0).getJSONArray("nodes");

                    // 简介
                    root.description = jaNodes.getJSONObject(0).getJSONArray("nodes").getJSONObject(0).getJSONObject("data").getString("desc");

                    // 总集数
                    root.episodesTotal = joData.getJSONObject("data").getJSONObject("data").getJSONObject("extra").optInt("episodeTotal", 1);

                    // 播放列表
                    JSONArray ja = jaNodes.getJSONObject(2).getJSONArray("nodes");
                    LogUtils.i(ja.toString());
                    List<YouKuEpisode> list = gson.fromJson(ja.toString(), new TypeToken<>() {
                    });

                    root.episodes = list.stream()
                        .filter(it -> Objects.equals(it.data.videoType, "正片"))
                        .map(it -> {
                            Video vd = new Video();
                            vd.id = it.id + "";
                            vd.title = it.data.title.replaceAll("^.*?(第\\d+集).*?$", "$1");
                            vd.pageUrl = "https://v.youku.com/v_show/id_" + it.data.action.value + ".html";
                            return vd;
                        }).collect(Collectors.toList());

                    liveData.postValue(root);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                liveData.postValue(null);
            }
            sonic.destroy();
            wv.stop(true);
        });

        if (sonic.load(pageUrl)) {
            // sonic 已加载，无需 wv 再加载
            return;
        }

        wv.start();
    }
}
