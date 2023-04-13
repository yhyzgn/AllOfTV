package com.yhy.all.of.tv.chan;

import com.yhy.all.of.tv.chan.of.IQiYiChan;
import com.yhy.all.of.tv.chan.of.TencentChan;
import com.yhy.all.of.tv.chan.of.YouKuChan;
import com.yhy.all.of.tv.internal.Lists;

import java.util.List;
import java.util.Objects;

/**
 * Created on 2023-04-06 22:51
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChanRegister {
    public final static ChanRegister instance = new ChanRegister();

    private final List<Chan> chanList;

    private ChanRegister() {
        chanList = Lists.of(
            // new RecommendChan(),
            new IQiYiChan(),
            new TencentChan(),
            new YouKuChan()
        );
    }

    public List<Chan> getChanList() {
        return chanList;
    }

    public Chan getChanByName(String name) {
        return chanList.stream().filter(it -> Objects.equals(name, it.name())).findFirst().orElse(null);
    }

    public int getPositionOfChan(Chan chan) {
        if (null == chan) {
            return -1;
        }
        for (int i = 0; i < chanList.size(); i++) {
            if (Objects.equals(chan.name(), chanList.get(i).name())) {
                return i;
            }
        }
        return -1;
    }
}
