package com.example.shop_exercise;

import java.util.List;

import com.shop_execise.view.MyViewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyViewpagerAdapter extends FragmentPagerAdapter {

	MyViewpager myviewpager;
	FragmentManager fm;
	List<Fragment> fragments;
	
	public MyViewpagerAdapter(FragmentManager fm,MyViewpager myViewpager,List<Fragment> fragments){
		super(fm);
		this.fragments = fragments;
		this.fm = fm;
		this.myviewpager = myviewpager;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
	

	

	
}
