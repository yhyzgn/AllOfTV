package com.yhy.all.of.tv.widget.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.azhon.appupdate.view.NumberProgressBar;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.yhy.all.of.tv.R;
import com.youbenzi.mdtool.tool.MDTool;

/**
 * 更新提示弹窗
 * <p>
 * Created on 2023-05-18 10:02
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class UpdateDialog extends BaseDialog {

    private final TextView tvName;
    private final TextView tvTime;
    private final TextView tvLog;
    private final NumberProgressBar npbDownload;
    private final LinearLayout llBtnBox;
    private final Button btnConfirm;
    private final Button btnCancel;

    private OnUpdatedListener mOnUpdatedListener;

    public UpdateDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_update);

        tvName = findViewById(R.id.tvName);
        tvTime = findViewById(R.id.tvTime);
        tvLog = findViewById(R.id.tvLog);
        npbDownload = findViewById(R.id.npbDownload);
        llBtnBox = findViewById(R.id.llBtnBox);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(v -> {
            if (null != mOnUpdatedListener) {
                mOnUpdatedListener.onUpdated();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.requestFocus();
    }

    public UpdateDialog setName(String name) {
        tvName.setText(name);
        return this;
    }

    public UpdateDialog setTime(String time) {
        tvTime.setText(time);
        return this;
    }

    public UpdateDialog setLog(String log) {
        log = MDTool.markdown2Html(log);
        log = EmojiConverter.getInstance().toUnicode(log);
        tvLog.setText(Html.fromHtml(log, Html.FROM_HTML_MODE_COMPACT));
        return this;
    }

    public void setProgress(int max, int current) {
        npbDownload.setMax(max);
        npbDownload.setProgress(current);
    }

    public UpdateDialog setOnUpdatedListener(OnUpdatedListener listener) {
        mOnUpdatedListener = listener;
        return this;
    }

    @Override
    public void show() {
        super.show();

        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width *= 0.6;
        getWindow().setAttributes(params);
    }

    public void startDownloading() {
        llBtnBox.setVisibility(View.GONE);
        npbDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }

    @FunctionalInterface
    public interface OnUpdatedListener {

        void onUpdated();
    }
}
