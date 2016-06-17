package com.administrator.pull_to_refresh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.administrator.pull_to_refresh.Adapter.DrawerAdapter;
import com.administrator.pull_to_refresh.R;
import com.administrator.pull_to_refresh.Util.DataUtil;


/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class DrawerFragment extends Fragment {

    private ListView listView;
    private DrawerAdapter drawerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_drawer_menu,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.drawer_menu_list);
        drawerAdapter = new DrawerAdapter(getActivity(), DataUtil.getMenuList());
        listView.setAdapter(drawerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerAdapter.setSelectedIndex(position);

                if (getActivity() instanceof OnDrawerMenuItemClickListener) {
                    ((OnDrawerMenuItemClickListener) getActivity()).onItemClick(drawerAdapter.getItem(position));
                }
            }
        });


        //触发点击第一个menu
        try {
            listView.performItemClick(listView.getAdapter().getView(0, null, null), 0, listView.getItemIdAtPosition(0));
        } catch (Exception e) {
        }


    }

    public interface OnDrawerMenuItemClickListener {
        public void onItemClick(Object itemData);
    }
}
