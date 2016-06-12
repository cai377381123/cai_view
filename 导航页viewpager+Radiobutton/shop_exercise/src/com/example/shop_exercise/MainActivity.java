package com.example.shop_exercise;

import java.util.ArrayList;
import java.util.List;

import com.shop_execise.view.MyViewpager;
import com.shop_exercise.fragments.HomeFragment;
import com.shop_exercise.fragments.MessageFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.os.Build;

public class MainActivity extends FragmentActivity implements OnClickListener {

	MyViewpager myviewpager;
	RadioButton btn_home;
	RadioButton btn_mess;

	MyViewpagerAdapter adapter;
	List<Fragment> fragments = new ArrayList<Fragment>();
	HomeFragment homeFragment;
	MessageFragment messageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		myviewpager = (MyViewpager) findViewById(R.id.viewpager);
		btn_home = (RadioButton) findViewById(R.id.btn_one);
		btn_mess = (RadioButton) findViewById(R.id.btn_two);

		btn_home.setOnClickListener(this);
		btn_mess.setOnClickListener(this);

		homeFragment = new HomeFragment();
		messageFragment = new MessageFragment();

		fragments.add(homeFragment);
		fragments.add(messageFragment);

		adapter = new MyViewpagerAdapter(getSupportFragmentManager(),
				myviewpager, fragments);
		myviewpager.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_one:
			myviewpager.setCurrentItem(0, true);
			Log.i("ceshi", "°´Å¥1");
			break;
		case R.id.btn_two:
			myviewpager.setCurrentItem(1, true);
			Log.i("ceshi", "°´Å¥2");
			break;
		}
	}

}
