package com.yhy.all.of.tv.component.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;

/**
 * Created on 2023-04-13 00:42
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class HomeTabAdapter extends BaseQuickAdapter<Chan.Tab, BaseViewHolder> {

    public HomeTabAdapter() {
        super(R.layout.item_home_tab_rv, Lists.of());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Chan.Tab tab) {
        holder.setText(R.id.tvTitle, tab.type.name);
    }
}
