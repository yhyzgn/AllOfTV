package com.yhy.all.of.tv.api.callback;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created on 2022-09-22 16:30
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class JsonCallback<T extends Serializable> extends AbsCallback<T> {
    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
        init();
    }

    public JsonCallback(Type type) {
        this.type = type;
        init();
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    private void init() {
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                assert genType != null;
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConverter<T> convert = new JsonConverter<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConverter<T> convert = new JsonConverter<>(type);
        return convert.convertResponse(response);
    }
}
