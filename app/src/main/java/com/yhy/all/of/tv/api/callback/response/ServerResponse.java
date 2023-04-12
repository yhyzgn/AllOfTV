package com.yhy.all.of.tv.api.callback.response;

import java.io.Serializable;

/**
 * Created on 2022-09-22 16:17
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServerResponse<T> implements Serializable {
    public String code;
    public String msg;
    public T data;
}
