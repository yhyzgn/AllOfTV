package com.yhy.all.of.tv.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yhy.all.of.tv.R;

/**
 * Created on 2023-04-13 12:32
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoadMoreView extends BaseLoadMoreView {

    @NonNull
    @Override
    public View getLoadComplete(@NonNull BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_end_view);
    }

    @NonNull
    @Override
    public View getLoadEndView(@NonNull BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_end_view);
    }

    @NonNull
    @Override
    public View getLoadFailView(@NonNull BaseViewHolder holder) {
        return holder.getView(R.id.load_more_load_fail_view);
    }

    @NonNull
    @Override
    public View getLoadingView(@NonNull BaseViewHolder holder) {
        return holder.getView(R.id.load_more_loading_view);
    }

    @NonNull
    @Override
    public View getRootView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_load_more, parent, false);
    }
}
