package com.yhy.all.of.tv.api.callback;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.yhy.all.of.tv.api.callback.response.ServerResponse;
import com.yhy.all.of.tv.api.callback.response.SimpleResponse;
import com.yhy.all.of.tv.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created on 2022-09-22 16:19
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonConverter<T extends Serializable> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConverter() {
    }

    public JsonConverter(Type type) {
        this.type = type;
    }

    public JsonConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                assert genType != null;
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            // noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            // noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            // noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = JsonUtils.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = JsonUtils.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != ServerResponse.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = JsonUtils.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            ServerResponse<?> serverResponse = null;
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                SimpleResponse simpleResponse = JsonUtils.fromJson(jsonReader, SimpleResponse.class);
                serverResponse = simpleResponse.toServerResponse();
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                serverResponse = JsonUtils.fromJson(jsonReader, type);
            }
            response.close();
            if (null == serverResponse) {
                throw new IllegalStateException("未知错误");
            }
            // 一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
            if (Objects.equals("0", serverResponse.code)) {
                // noinspection unchecked
                return (T) serverResponse;
            } else {
                // 直接将服务端的错误信息抛出，onError中可以获取
                throw new IllegalStateException(serverResponse.msg);
            }
        }
    }
}
