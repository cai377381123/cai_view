package com.administrator.pull_to_refresh.Util;

import android.util.Log;
import android.widget.Toast;

import com.administrator.pull_to_refresh.fragment.OneFragment;
import com.administrator.pull_to_refresh.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class DataUtil {
    public static final String MENU_KEY_GROUP_NAME = "menu_key_group_name";

    public static final String MENU_KEY_ITEM_NAME = "menu_key_item_name";

    public static final String MENU_KEY_FRAGMENT = "menu_key_fragment";

    public static List<HashMap<String, Object>> getMenuList() {
        List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> menuItem = new HashMap<String, Object>();
        menuItem.put(MENU_KEY_GROUP_NAME, "包含内容");
        menuItem.put(MENU_KEY_ITEM_NAME, "TextView");
        menuItem.put(MENU_KEY_FRAGMENT, new OneFragment());

        menuList.add(menuItem);

        menuItem = new HashMap<String, Object>();
        menuItem.put(MENU_KEY_GROUP_NAME, "刷新类型");
        menuItem.put(MENU_KEY_ITEM_NAME, "释放刷新");
        menuItem.put(MENU_KEY_FRAGMENT, new TwoFragment());

        menuList.add(menuItem);

        return menuList;
    }
}
