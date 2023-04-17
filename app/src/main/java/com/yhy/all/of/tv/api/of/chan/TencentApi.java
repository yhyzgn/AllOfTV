package com.yhy.all.of.tv.api.of.chan;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.model.TencentVideo;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.internal.Maps;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
public class TencentApi {
    public static final TencentApi instance = new TencentApi();

    private final Gson gson;

    private TencentApi() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    public void page(MutableLiveData<List<Video>> liveData, int page, VideoType type, int mode) throws Exception {
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
                "channel_id", type == VideoType.FILM ? "100173" : type == VideoType.EPISODE ? "100113" : "100105",
                "filter_params", "itype=-1&iarea=-1&characteristic=-1&year=-1&charge=-1&sort=75",
                "page", pg
            ));

        OkGo.<String>post("https://pbaccess.video.qq.com/trpc.vector_layout.page_view.PageService/getPage?video_appid=3000010")
            .headers("cookie", "tvfe_boss_uuid=77ce84b0af77c334; video_platform=2; video_guid=f5aa0d25e4aa0b62; pgv_pvid=8172569500; pgv_info=ssid=s4222040415")
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
                            List<Integer> publishDate = Arrays.stream(it.params.publishDate.split("-")).map(Integer::valueOf).collect(Collectors.toList());
                            Video vd = new Video();
                            vd.id = it.params.cid;
                            vd.title = it.params.title;
                            vd.description = it.params.secondTitle;
                            vd.score = null == it.params.opinionScore ? 0 : Float.parseFloat(it.params.opinionScore);
                            vd.imgCover = it.params.newPicVt;
                            vd.pageUrl = "https://v.qq.com/x/cover/" + it.params.cid + ".html";
                            vd.channel = "腾讯";
                            vd.type = type;
                            vd.year = publishDate.get(0);
                            vd.month = publishDate.get(1);
                            vd.day = publishDate.get(2);
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

    public String getHtmlPage(String videoId) {
        String pageUrl = "https://v.qq.com/x/cover/" + videoId + ".html";
        OkGo.<String>get(pageUrl)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String html = response.body();
                    // 提取 __pinia 全局变量
                    String pinia = html.replaceAll(".*?<script>.*?window.__pinia=(.*?)<script>.*?", "$1");
                    LogUtils.i(pinia);
                }
            });
        return "";
    }

    /**
     * 获取视频播放列表
     *
     * @param root     视频根信息
     * @param liveData 加载回调
     */
    public void playList(Video root, MutableLiveData<Video> liveData) {
    }
}
