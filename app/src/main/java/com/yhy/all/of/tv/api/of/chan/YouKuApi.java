package com.yhy.all.of.tv.api.of.chan;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.KuVideo;
import com.yhy.all.of.tv.internal.Maps;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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

    public List<Video> page(int page, VideoType type, int mode) throws Exception {
        ListenableFuture<List<Video>> future = new AbstractFuture<List<Video>>() {
            {
                String typeStr = type == VideoType.FILM ? "电影" : type == VideoType.EPISODE ? "电视剧" : "纪录片";
                Map<String, Object> json = Maps.of("type", typeStr, "sort", 7);

                OkGo.<String>get("https://www.youku.com/category/data")
                        .headers("referer", "https://www.youku.com/category/show/type_" + URLEncoder.encode(typeStr, "utf-8") + ".html?spm=a2ha1.14919748_WEBMOVIE_SEC00MOVIE00REYING.drawer3.1")
                        .params("params", gson.toJson(json))
                        .params("optionRefresh", 1)
                        .params("pageNo", page)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String result = response.body();
                                try {
                                    JSONObject jo = new JSONObject(result).getJSONObject("data").getJSONObject("filterData");
                                    JSONArray ja = jo.getJSONArray("listData");
                                    String json = ja.toString();

                                    List<KuVideo> list = gson.fromJson(json, new TypeToken<List<KuVideo>>() {
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

                                    set(res);
                                } catch (JSONException e) {
                                    LogUtils.e(e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                LogUtils.e(response.getException().getMessage());
                                setException(response.getException());
                            }
                        });
            }
        };
        return future.get();
    }
}
