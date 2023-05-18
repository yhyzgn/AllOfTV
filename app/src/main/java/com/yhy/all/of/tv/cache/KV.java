package com.yhy.all.of.tv.cache;

import android.app.Application;

import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.parse.Parser;
import com.yhy.all.of.tv.parse.ParserRegister;
import com.yhy.all.of.tv.utils.JsonUtils;
import com.yhy.all.of.tv.utils.LogUtils;

import io.fastkv.FastKV;

/**
 * Created on 2023-02-03 19:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class KV {
    public static final KV instance = new KV();
    private static final String TAG = "KV";

    private FastKV kv;

    private KV() {
    }

    public void init(Application application) {
        kv = new FastKV.Builder(application.getFilesDir().getAbsolutePath(), "fast_kv").build();
    }

    public FastKV kv() {
        return kv;
    }

    public void storePosition(String tag, long position) {
        LogUtils.dTag(TAG, "storePosition: " + tag + ", position = " + position);
        kv.putLong("position:" + tag, position);
    }

    public long getPosition(String tag) {
        return kv.getLong("position:" + tag, 0);
    }

    public void removePosition(String tag) {
        kv.remove("position:" + tag);
    }

    public void storeVideo(String tag, Video video) {
        kv.putString("video:" + tag, JsonUtils.toJson(video));
    }

    public Video getVideo(String tag) {
        return JsonUtils.fromJson(kv.getString("video:" + tag), Video.class);
    }

    public void removeVideo(String tag) {
        kv.remove("video:" + tag);
    }

    public void storeEpisode(String tag, int index) {
        kv.putInt("episode:" + tag, index);
    }

    public int getEpisode(String tag) {
        return kv.getInt("episode:" + tag);
    }

    public void removeEpisode(String tag) {
        kv.remove("episode:" + tag);
    }

    public void storeParser(String tag, Parser parser) {
        kv.putString("parser:" + tag, parser.name());
    }

    public Parser getParser(String tag, String chanName) {
        String parser = kv.getString("parser:" + tag);
        return ParserRegister.instance.findSupported(parser, chanName);
    }

    public void removeParser(String tag) {
        kv.remove("parser:" + tag);
    }
}
