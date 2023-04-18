package com.yhy.all.of.tv.component.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.common.base.Joiner;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.utils.ImgUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created on 2023-04-13 11:26
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class GridAdapter extends BaseQuickAdapter<Video, BaseViewHolder> implements LoadMoreModule {
    private boolean mShowList = false;

    public GridAdapter(boolean l) {
        super(l ? R.layout.item_list : R.layout.item_grid, new ArrayList<>());
        this.mShowList = l;
    }

    @Override
    protected void convert(BaseViewHolder helper, Video item) {
        helper.setText(R.id.tvNote, "");
        helper.setText(R.id.tvName, item.title);
        helper.setText(R.id.tvActor, null != item.actors ? Joiner.on(" ").join(item.actors) : "");
        ImageView ivThumb = helper.getView(R.id.ivThumb);
        //由于部分电视机使用glide报错
        if (!TextUtils.isEmpty(item.imgCover)) {
            ImgUtils.load(ivThumb.getContext(), ivThumb, item.imgCover);
        } else {
            ivThumb.setImageResource(R.mipmap.ic_img_holder);
        }

        if (this.mShowList) {
            return;
        }

        helper.setVisible(R.id.tvYear, item.year > 0);
        helper.setText(R.id.tvYear, item.year + "");

        TextView tvLang = helper.getView(R.id.tvLang);
        tvLang.setVisibility(View.GONE);

        TextView tvArea = helper.getView(R.id.tvArea);
        tvArea.setVisibility(View.GONE);

        helper.setVisible(R.id.tvScore, item.score > 0);
        helper.setText(R.id.tvScore, item.score + "");

        helper.setVisible(R.id.tvNote, !TextUtils.isEmpty(item.updateStatus));
        helper.setText(R.id.tvNote, item.updateStatus);
    }

    @NotNull
    @Override
    public BaseLoadMoreModule addLoadMoreModule(@NotNull BaseQuickAdapter baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}