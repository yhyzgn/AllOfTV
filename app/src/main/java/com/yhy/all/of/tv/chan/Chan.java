package com.yhy.all.of.tv.chan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.cache.KV;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;
import com.yhy.all.of.tv.utils.Md5Utils;

import java.io.Serializable;
import java.util.List;

/**
 * 频道接口
 * <p>
 * Created on 2023-04-06 22:41
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Chan {

    String name();

    List<Tab> tabList();

    void loadPlayList(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData);

    default void loadPlayListWithCache(AppCompatActivity activity, Video root, MutableLiveData<Video> liveData) {
        // 先查缓存
        Video video = KV.instance.getVideo(Md5Utils.gen(root.pageUrl));
        if (null != video) {
            liveData.postValue(video);
            return;
        }
        loadPlayList(activity, root, liveData);
    }

    class Tab implements Serializable {
        public final Chan chan;
        public final VideoType type;
        public final TabLoader loader;

        public Tab(Chan chan, VideoType type, TabLoader loader) {
            this.chan = chan;
            this.type = type;
            this.loader = loader;
        }

        public static Tab create(Chan chan, VideoType type, TabLoader loader) {
            return new Tab(chan, type, loader);
        }
    }

    @FunctionalInterface
    interface TabLoader {

        void load(MutableLiveData<List<Video>> liveData, Bundle params) throws Exception;
    }
}