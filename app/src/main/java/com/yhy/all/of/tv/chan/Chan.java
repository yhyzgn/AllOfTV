package com.yhy.all.of.tv.chan;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.model.ems.VideoType;

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