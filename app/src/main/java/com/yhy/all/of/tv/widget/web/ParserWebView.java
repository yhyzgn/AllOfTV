package com.yhy.all.of.tv.widget.web;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.yhy.all.of.tv.parse.Parser;

/**
 * Created on 2023-02-08 12:49
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ParserWebView {

    void attach(AppCompatActivity activity, Parser parser, String url, MutableLiveData<String> liveData);

    void start();

    void stop(boolean destroy);
}
