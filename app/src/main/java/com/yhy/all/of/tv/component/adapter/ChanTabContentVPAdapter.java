package com.yhy.all.of.tv.component.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.ui.fragment.VpMainFragment;

import java.util.List;

/**
 * 频道页面适配器
 * <p>
 * Created on 2023-01-24 00:40
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChanTabContentVPAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "ContentViewPagerAdapter";

    private final List<Chan.Tab> mTabList;

    public ChanTabContentVPAdapter(@NonNull FragmentManager fm, List<Chan.Tab> tabList) {
        super(fm);
        mTabList = tabList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Chan.Tab tab = mTabList.get(position);
        return VpMainFragment.newInstance(position, tab);
    }

    @Override
    public int getCount() {
        return mTabList == null ? 0 : mTabList.size();
    }
}
