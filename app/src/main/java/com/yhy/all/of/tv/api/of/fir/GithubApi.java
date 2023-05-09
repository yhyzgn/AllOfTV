package com.yhy.all.of.tv.api.of.fir;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yhy.all.of.tv.api.callback.JsonCallback;
import com.yhy.all.of.tv.api.model.VersionInfo;

import java.util.function.Consumer;

/**
 * Created on 2023-01-26 01:24
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class GithubApi {
    private static final String API_TOKEN = "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";

    public static final GithubApi instance = new GithubApi();

    private GithubApi() {
    }

    public void versionQuery(Consumer<VersionInfo> consumer) {
        OkGo.<VersionInfo>get("https://api.github.com/repos/yhyzgn/AllOfTV/releases/latest")
            .headers("Accept", "application/vnd.github+json")
            .headers("Authorization", "Bearer " + API_TOKEN)
            .headers("X-GitHub-Api-Version", "2022-11-28")
            .execute(new JsonCallback<>() {
                @Override
                public void onSuccess(Response<VersionInfo> response) {
                    consumer.accept(response.body());
                }
            });
    }
}
