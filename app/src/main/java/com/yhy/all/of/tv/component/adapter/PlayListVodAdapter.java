package com.yhy.all.of.tv.component.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.model.Video;

import java.util.ArrayList;

/**
 * Created on 2023-04-14 00:37
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class PlayListVodAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    private final SelectedItemMatcher mMatcher;

    public PlayListVodAdapter(SelectedItemMatcher matcher) {
        super(R.layout.item_series_flag, new ArrayList<>());
        mMatcher = matcher;
    }

    @Override
    protected void convert(BaseViewHolder helper, Video item) {
        helper.setVisible(R.id.tvSeriesFlagSelect, mMatcher.selected(item));
        helper.setText(R.id.tvSeriesFlag, item.title);
    }

    @FunctionalInterface
    public interface SelectedItemMatcher {

        boolean selected(Video video);
    }
}