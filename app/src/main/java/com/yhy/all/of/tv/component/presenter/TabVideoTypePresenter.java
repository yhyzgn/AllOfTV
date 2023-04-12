package com.yhy.all.of.tv.component.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.chan.Chan;

/**
 * 标题适配器
 * <p>
 * Created on 2023-01-20 03:35
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class TabVideoTypePresenter extends Presenter {

    private OnItemClickListener mOnItemClickListener;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Chan.Tab) {
            Chan.Tab tab = (Chan.Tab) item;
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.tvScvItem.setText(tab.type.name);

            vh.view.setOnClickListener(v -> {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(v, tab);
                }
            });
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private final TextView tvScvItem;

        ViewHolder(View view) {
            super(view);
            tvScvItem = view.findViewById(R.id.tv_scv_item);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Chan.Tab tab);
    }
}
