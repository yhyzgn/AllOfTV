package com.yhy.all.of.tv.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.yhy.all.of.tv.R;
import com.yhy.all.of.tv.component.adapter.SelectDialogAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on 2023-04-13 10:38
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class SelectDialog<T> extends BaseDialog {

    private boolean muteCheck = false;

    public SelectDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_select);
    }

    public SelectDialog(@NonNull @NotNull Context context, int resId) {
        super(context);
        setContentView(resId);
    }

    public void setItemCheckDisplay(boolean shouldShowCheck) {
        muteCheck = !shouldShowCheck;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTip(String tip) {
        ((TextView) findViewById(R.id.title)).setText(tip);
    }

    public void setAdapter(SelectDialogAdapter.SelectDialogInterface<T> sourceBeanSelectDialogInterface, DiffUtil.ItemCallback<T> sourceBeanItemCallback, List<T> data, int select) {
        SelectDialogAdapter<T> adapter = new SelectDialogAdapter<>(sourceBeanSelectDialogInterface, sourceBeanItemCallback, muteCheck);
        adapter.setData(data, select);
        TvRecyclerView tvRecyclerView = ((TvRecyclerView) findViewById(R.id.list));
        tvRecyclerView.setAdapter(adapter);
        tvRecyclerView.setSelectedPosition(select);
        tvRecyclerView.post(() -> {
            tvRecyclerView.scrollToPosition(select);
            tvRecyclerView.setSelectionWithSmooth(select);
        });
    }
}