package com.shop_exercise.fragments;

import com.example.shop_exercise.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
	return view;
	}
}
