package com.yhy.all.of.tv.cache;

import android.app.Application;

import io.fastkv.FastKV;

/**
 * Created on 2023-02-03 19:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class KV {
    public static final KV instance = new KV();

    private FastKV kv;

    private KV() {
    }

    public void init(Application application) {
        kv = new FastKV.Builder(application.getFilesDir().getAbsolutePath(), "fast_kv").build();
    }

    public FastKV kv() {
        return kv;
    }
}
