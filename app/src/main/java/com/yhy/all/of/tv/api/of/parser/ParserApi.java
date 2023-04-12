package com.yhy.all.of.tv.api.of.parser;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Created on 2023-01-26 01:24
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParserApi {

    public static final ParserApi instance = new ParserApi();

    private ParserApi() {
    }

    public void parse(String url, Map<String, String> headers, Consumer<Boolean> consumer) {
        GetRequest<String> req = OkGo.get(url);
        if (null != headers && !headers.isEmpty()) {
            headers.forEach(req::headers);
        }
        req.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                // 是否是个 m3u8 文件地址
                consumer.accept(!TextUtils.isEmpty(body) && body.startsWith("#EXTM3U" + System.lineSeparator()));
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                consumer.accept(false);
            }
        });
    }

    public void danMu(String url, Consumer<String> consumer) {
        OkGo.<String>post("https://danmu.nxflv.com/Api.php")
                .isMultipart(true)
                .params("url", url)
                .params("uid", "37428673")
                .params("wap", 0)
                .params("type", "")
                .params("key", "aghijloxBFIKOR4579")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String res = response.body();
                        try {
                            JSONObject jo = new JSONObject(res);
                            String resUrl = jo.getString("url");
                            consumer.accept(resUrl);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
