package com.administrator.pull_to_refresh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.pull_to_refresh.R;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class TwoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two,container,false);
        return view;
    }
}
