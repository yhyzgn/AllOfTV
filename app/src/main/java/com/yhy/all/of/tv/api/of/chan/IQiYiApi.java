package com.yhy.all.of.tv.api.of.chan;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.IQiYiVideo;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2023-01-24 21:08
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class IQiYiApi {
    public static final IQiYiApi instance = new IQiYiApi();

    private final Gson gson;

    private IQiYiApi() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    public void page(MutableLiveData<List<Video>> liveData, int page, VideoType type, int mode) throws Exception {
        OkGo.<String>get("https://mesh.if.iqiyi.com/portal/videolib/pcw/data")
            .params("ret_num", 30)
            .params("page_id", Math.max(1, page))
            .params("channel_id", type == VideoType.FILM ? 1 : 2)
            .params("mode", mode)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String result = response.body();
                    try {
                        JSONObject jo = new JSONObject(result);
                        JSONArray ja = jo.getJSONArray("data");
                        String json = ja.toString();

                        List<IQiYiVideo> list = gson.fromJson(json, new TypeToken<>() {
                        });
                        List<Video> res = list.stream().map(it -> {
                            Video vd = new Video();
                            vd.id = it.filmId + "";
                            vd.title = it.title;
                            vd.description = it.description;
                            vd.score = it.snsScore;
                            vd.imgCover = it.imageUrlNormal;
                            vd.pageUrl = it.pageUrl;
                            vd.channel = "爱奇艺";
                            vd.type = type;
                            vd.year = it.date.year;
                            vd.month = it.date.month;
                            vd.day = it.date.day;
                            vd.tags = TextUtils.isEmpty(it.tag) ? null : Arrays.stream(it.tag.split(",")).collect(Collectors.toList());
                            vd.directors = it.creator.stream().map(dto -> dto.name).collect(Collectors.toList());
                            vd.actors = it.contributor.stream().map(dto -> dto.name).collect(Collectors.toList());
                            if (type == VideoType.FILM) {
                                // 电影类型的话，把自己添加到剧集里
                                vd.episodes = Lists.of(vd);
                            }
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
