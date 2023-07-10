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
import com.yhy.all.of.tv.api.model.TencentVideo;
import com.yhy.all.of.tv.internal.Maps;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.TabType;
import com.yhy.all.of.tv.rand.IpRand;
import com.yhy.all.of.tv.rand.UserAgentRand;
import com.yhy.all.of.tv.utils.DateUtils;
import com.yhy.all.of.tv.utils.LogUtils;
import com.yhy.all.of.tv.widget.web.JsExtractWebView;
import com.yhy.all.of.tv.widget.web.sonic.Sonic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class TencentApi {
    public static final TencentApi instance = new TencentApi();

    private final Gson gson;

    private TencentApi() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    public void page(MutableLiveData<List<Video>> liveData, int page, TabType type, int mode) throws Exception {
        String pg = (Math.max(1, page) - 1) + "";
        Map<String, Object> body = Maps.of(
            "page_bypass_params", Maps.of(
                "abtest_bypass_id", "f5aa0d25e4aa0b62",
                "params", Maps.of(
                    "caller_id", "3000010",
                    "channel_id", "100173",
                    "data_mode", "default",
                    "filter_params", "itype=-1&iarea=-1&characteristic=-1&year=-1&charge=-1&sort=75",
                    "page", pg,
                    "page_id", "channel_list_second_page",
                    "page_type", "operation",
                    "platform_id", "2",
                    "user_mode", "default"
                ),
                "scene", "operation"
            ),
            "page_context", Maps.of("page_index", pg),
            "page_params", Maps.of(
                "page_id", "channel_list_second_page",
                "page_type", "operation",
                "channel_id", type == TabType.FILM ? "100173" : type == TabType.EPISODE ? "100113" : "100105",
                "filter_params", "itype=-1&iarea=-1&characteristic=-1&year=-1&charge=-1&sort=75",
                "page", pg
            ));

        OkGo.<String>post("https://pbaccess.video.qq.com/trpc.vector_layout.page_view.PageService/getPage?video_appid=3000010")
            .headers("cookie", "tvfe_boss_uuid=77ce84b0af77c334; video_platform=2; video_guid=f5aa0d25e4aa0b62; pgv_pvid=8172569500; pgv_info=ssid=s4222040415")
            .headers("User-Agent", UserAgentRand.get())
            .headers("X-Forwarded-For", IpRand.get())
            .upJson(gson.toJson(body))
            .execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String result = response.body();
                    try {
                        JSONObject jo = new JSONObject(result);
                        JSONArray ja = jo.getJSONObject("data").getJSONArray("CardList");
                        jo = ja.getJSONObject(ja.length() - 1);
                        ja = jo.getJSONObject("children_list").getJSONObject("list").getJSONArray("cards");
                        String json = ja.toString();

                        List<TencentVideo> list = gson.fromJson(json, new TypeToken<>() {
                        });
                        List<Video> res = list.stream().map(it -> {
                            Video vd = new Video();
                            vd.id = it.params.cid;
                            vd.title = it.params.title;
                            vd.description = it.params.secondTitle;
                            vd.score = null == it.params.opinionScore ? 0 : Float.parseFloat(it.params.opinionScore);
                            vd.imgCover = it.params.newPicVt;
                            vd.pageUrl = "https://v.qq.com/x/cover/" + it.params.cid + ".html";
                            vd.channel = "腾讯";
                            vd.type = type;
                            if (!TextUtils.isEmpty(it.params.publishDate) && !it.params.publishDate.startsWith("0000")) {
                                LocalDate date = LocalDate.parse(it.params.publishDate, DateTimeFormatter.ofPattern(DateUtils.autoPattern(it.params.publishDate)));
                                vd.year = date.getYear();
                            }
                            vd.updateStatus = it.params.timeLong;
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
     * @param activity
     * @param root     视频根信息
     * @param liveData 加载回调
     */
    public void playList(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData) {
        // 电影和剧集都通过 html 加载
        getHtmlPage(activity, root, liveData);
    }

    private void getHtmlPage(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData) {
        String pageUrl = "https://v.qq.com/x/cover/" + root.id + ".html";

        MutableLiveData<String> tempLiveData = new MutableLiveData<>();
        JsExtractWebView wv = new JsExtractWebView(activity.getApplicationContext()).attach(activity, pageUrl, tempLiveData, "window.__PINIA__");
        Sonic sonic = new Sonic(activity, wv);

        tempLiveData.observe(activity, pinia -> {
            LogUtils.i("pinia", pinia);
            if (TextUtils.isEmpty(pinia) || Objects.equals("undefined", pinia)) {
                // 重试
                tempLiveData.removeObservers(activity);
                wv.stop(true);
                getHtmlPage(activity, root, liveData);
                return;
            }

            try {
                JSONObject jo = new JSONObject(pinia);

                JSONObject joCoverInfo = jo.getJSONObject("global").getJSONObject("coverInfo");

                // 演员表
                JSONArray actors = joCoverInfo.getJSONArray("leading_actor");
                root.actors = gson.fromJson(actors.toString(), new TypeToken<>() {
                });

                // 简介信息
                root.description = joCoverInfo.getString("description");

                // 总集数
                String episodeAll = joCoverInfo.optString("episode_all", "1");
                if (TextUtils.isEmpty(episodeAll) || "null".equals(episodeAll)) {
                    episodeAll = "1";
                }
                root.episodesTotal = Integer.parseInt(episodeAll);

                // 播放列表
                jo = jo.getJSONObject("episodeMain");
                JSONArray ja = jo.getJSONArray("listData").getJSONObject(0).getJSONArray("list").getJSONArray(0);
                List<Map<String, Object>> list = gson.fromJson(ja.toString(), new TypeToken<>() {
                });
                root.episodes = list.stream()
                    .filter(it -> Objects.equals(it.get("isNoStoreWatchHistory"), false)) // 过滤非正片（true：预告片，花絮等，false：正片）
                    .map(it -> {
                        Video vd = new Video();
                        vd.id = Objects.requireNonNull(it.get("vid")).toString();
                        vd.title = Objects.requireNonNull(it.get("playTitle")).toString().replaceAll("^.*?(第\\d+集).*?$", "$1");
                        vd.pageUrl = "https://v.qq.com/x/cover/" + root.id + "/" + vd.id + ".html";
                        return vd;
                    }).collect(Collectors.toList());
                liveData.postValue(root);
            } catch (JSONException e) {
                throw new RuntimeException(e);
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
