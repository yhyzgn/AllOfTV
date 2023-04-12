package com.yhy.all.of.tv.component.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.leanback.widget.Presenter;

import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.model.Video;
import com.yhy.all.of.tv.utils.ImgUtils;
import com.yhy.all.of.tv.utils.ViewUtils;

/**
 * Created on 2023-01-24 19:37
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class PageVideoPresenter extends Presenter {

    private static final String TAG = "PageVideoPresenter";
    private OnItemClickListener mOnItemClickListener;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        view.setOnFocusChangeListener((v, hasFocus) -> {
            AppCompatImageView ivCover = v.findViewById(R.id.iv_cover);
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                ViewCompat.animate(ivCover)
                        .scaleX(1.005f)
                        .scaleY(1.005f)
                        .translationZ(1.005f)
                        .start();
                ivCover.setElevation(ViewUtils.dp2px(18));
            } else {
                // 此处为失去焦点时的处理内容
                ViewCompat.animate(ivCover)
                        .scaleX(1)
                        .scaleY(1)
                        .translationZ(1)
                        .start();
                ivCover.setElevation(0);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Video) {
            ViewHolder vh = (ViewHolder) viewHolder;
            Video vd = (Video) item;
            ImgUtils.load(viewHolder.view.getContext(), vh.ivCover, vd.imgCover);
            vh.tvScore.setText(vd.score + "");
            vh.tvName.setText(vd.title);

            vh.view.setOnClickListener(v -> {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(v, vd);
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

        private final AppCompatImageView ivCover;
        private final AppCompatTextView tvScore;
        private final AppCompatTextView tvName;

        ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.iv_cover);
            tvScore = view.findViewById(R.id.tv_score);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Video video);
    }
}
