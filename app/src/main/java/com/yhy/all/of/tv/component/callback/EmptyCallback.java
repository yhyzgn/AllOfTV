package com.yhy.all.of.tv.component.callback;

import com.kingja.loadsir.callback.Callback;
import com.yhy.all.of.tv.R;

/**
 * Created on 2023-04-12 23:36
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.loadsir_empty_layout;
    }
}
