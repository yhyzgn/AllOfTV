package com.yhy.all.of.tv.api;

import com.yhy.all.of.tv.rand.IpRand;
import com.yhy.all.of.tv.rand.UserAgentRand;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2022-10-14 15:59
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class RandHeaderInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.header("User-Agent", UserAgentRand.get());
        builder.header("X-Forwarded-For", IpRand.get());
        builder.header("X-Requested-With", "XMLHttpRequest");
        return chain.proceed(builder.build());
    }
}
