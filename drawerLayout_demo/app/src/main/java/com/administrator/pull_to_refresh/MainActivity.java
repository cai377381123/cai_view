package com.administrator.pull_to_refresh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.administrator.pull_to_refresh.Util.DataUtil;
import com.administrator.pull_to_refresh.fragment.DrawerFragment;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements DrawerFragment.OnDrawerMenuItemClickListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("toolbar菜单");
        //toolbar.setNavigationIcon(R.drawable.ic_favorite);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String mes = "";
                switch (item.getItemId()) {
                    case R.id.toolbar_delete:
                        mes = "delete_btn";
                        break;
                    case R.id.toolbar_grade:
                        mes = "grade_btn";
                        break;
                    case R.id.toolbar_info:
                        mes = "info_btn";
                        break;
                }
                Toast.makeText(MainActivity.this, mes, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.dl_menu, new DrawerFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public void onItemClick(Object itemData) {
        drawerLayout.closeDrawers();
        try {
            HashMap<String, Object> itemDataMap = (HashMap<String, Object>) itemData;
            Fragment fragment = (Fragment) (itemDataMap.get(DataUtil.MENU_KEY_FRAGMENT));

            setTitle((String)itemDataMap.get(DataUtil.MENU_KEY_ITEM_NAME));

            fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();

        } catch (Exception e) {
        }
    }
}
