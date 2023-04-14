package com.yhy.all.of.tv.vm;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.model.Video;

import java.util.List;

/**
 * Created on 2023-04-13 12:42
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class SourceVM extends ViewModel {
    public MutableLiveData<List<Video>> videoListData = new MutableLiveData<>();

    public void loadVideoList(Chan.Tab tab, int page) {
        if (null == tab || null == tab.loader) {
            return;
        }
        Bundle params = new Bundle();
        params.putInt("page", page);
        try {
            tab.loader.load(videoListData, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
