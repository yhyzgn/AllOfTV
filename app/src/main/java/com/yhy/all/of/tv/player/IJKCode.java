package com.yhy.all.of.tv.player;

import java.util.LinkedHashMap;

/**
 * Created on 2023-04-13 17:09
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class IJKCode {
    private String name;
    private LinkedHashMap<String, String> option;
    private boolean selected;

    public void selected(boolean selected) {
        this.selected = selected;
        if (selected) {
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, String> getOption() {
        return option;
    }

    public void setOption(LinkedHashMap<String, String> option) {
        this.option = option;
    }
}