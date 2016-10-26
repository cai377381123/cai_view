package cn.edu.nuc.csce.dcldims.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class ViewpagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;
    public ViewpagerAdapter(FragmentManager fragmentManager,List<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
