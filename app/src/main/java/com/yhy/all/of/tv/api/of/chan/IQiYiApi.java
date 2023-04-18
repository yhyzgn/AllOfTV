package com.yhy.all.of.tv.api.of.chan;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.tencent.smtt.utils.Md5Utils;
import com.yhy.all.of.tv.api.model.IQiYiVideo;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
public class IQiYiApi {
    public static final IQiYiApi instance = new IQiYiApi();

    private static final String SIGN_SECRET = "howcuteitis";

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
                            vd.id = it.tvId + "";
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
                            vd.updateStatus = it.dqUpdatestatus;
                            vd.tags = TextUtils.isEmpty(it.tag) ? null : Arrays.stream(it.tag.split(",")).collect(Collectors.toList());
                            vd.directors = it.creator.stream().map(dto -> dto.name).collect(Collectors.toList());
                            vd.actors = it.contributor.stream().map(dto -> dto.name).collect(Collectors.toList());
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
        if (root.type == VideoType.FILM) {
            // 电影的话，把自己加到播放列表就行了
            root.episodes = Lists.of(gson.fromJson(gson.toJson(root), Video.class));
            liveData.postValue(root);
            return;
        }

        // 剧集
        Map<String, Object> body = buildBodyMap(Long.parseLong(root.id));
        List<String> pairList = genPairList(body);
        String sign = genSign(pairList);
        body.put("sign", sign);

        GetRequest<String> req = OkGo.<String>get("https://mesh.if.iqiyi.com/tvg/pcw/base_info");
        body.forEach((k, v) -> req.params(k, v.toString()));
        req.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String result = response.body();
                try {
                    JSONObject jo = new JSONObject(result);
                    JSONArray ja = jo.getJSONObject("data").getJSONObject("template").getJSONObject("pure_data").getJSONArray("selector_bk");
                    jo = ja.getJSONObject(0).getJSONObject("videos");

                    List<String> pageKeys = gson.fromJson(jo.getJSONArray("page_keys").toString(), new TypeToken<>() {
                    });

                    Map<String, List<IQiYiVideo>> featurePaged = gson.fromJson(jo.getJSONObject("feature_paged").toString(), new TypeToken<>() {
                    });

                    root.episodes = pageKeys.stream()
                        .map(featurePaged::get)
                        .filter(it -> null != it && !it.isEmpty())
                        .flatMap(Collection::stream)
                        .filter(it -> Objects.equals("1", it.contentType)) // 1：正片，3：预告片
                        .map(it -> {
                            Video vd = new Video();
                            vd.title = it.title.replaceAll("^.*?(第\\d+集).*?$", "$1");
                            vd.pageUrl = it.pageUrl;
                            vd.channel = "爱奇艺";
                            return vd;
                        }).collect(Collectors.toList());
                    liveData.postValue(root);
                } catch (JSONException e) {
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

    private Map<String, Object> buildBodyMap(long videoId) {
        long millis = System.currentTimeMillis();
        String deviceId = "51590163f960fcb600764d1fb22fa7d1";

        Map<String, Object> map = new HashMap<>();
        map.put("entity_id", videoId);
        map.put("timestamp", millis);
        map.put("src", "pcw_tvg");
        map.put("vip_status", 0);
        map.put("vip_type", "");
        map.put("auth_cookie", "");
        map.put("device_id", deviceId);
        map.put("user_id", "");
        map.put("app_version", "4.0.0");

        return map;
    }

    private List<String> genPairList(Map<String, Object> body) {
        return body.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey())).map(it -> it.getKey() + "=" + it.getValue()).collect(Collectors.toList());
    }

    private String genSign(List<String> pairList) {
        pairList.add("secret_key=howcuteitis");
        String value = Joiner.on("&").join(pairList);
        return Md5Utils.getMD5(value).toUpperCase();
    }
}
