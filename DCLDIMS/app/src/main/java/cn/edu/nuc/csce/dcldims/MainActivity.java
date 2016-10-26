package cn.edu.nuc.csce.dcldims;


import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import cn.edu.nuc.csce.dcldims.adapter.ViewpagerAdapter;
import cn.edu.nuc.csce.dcldims.bluetooth.BluetoothLeService;
import cn.edu.nuc.csce.dcldims.fragment.Home_help;
import cn.edu.nuc.csce.dcldims.fragment.Home_safe;
import cn.edu.nuc.csce.dcldims.fragment.Home_setting;
import cn.edu.nuc.csce.dcldims.utils.Util;
import cn.edu.nuc.csce.dcldims.view.BottomTab;


import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private String bluetoothDeviceName;
    private String bluetoothDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    public Boolean b_Blue_Connected = false;

    public BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    public void setBluetoothLeService(BluetoothLeService bluetoothLeService) {
        mBluetoothLeService = bluetoothLeService;
    }

    public static final int TAB_HOME_SETTING = 0;
    public static final int TAB_HOME_MONITOR = 1;
    public static final int TAB_HOME_HELP = 2;

    private int flag_guanliyuan=0;// 管理员标志

    public int getFlag_guanliyuan() {
        return flag_guanliyuan;
    }

    public void setFlag_guanliyuan(int flag_guanliyuan) {
        this.flag_guanliyuan = flag_guanliyuan;
    }

    private ViewPager viewPager;
    private ViewpagerAdapter viewpagerAdapter;

    private Home_setting home_setting;
    private Home_safe home_safe;
    private Home_help home_help;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private int oldposition=0;

    private List<BottomTab> mTabs = new ArrayList<BottomTab>();
    private BottomTab tab_setting;
    private BottomTab tab_monitor;
    private BottomTab tab_help;
    private View view;
    private ContentResolver mContentResolver;

    private boolean isFirstIn = false;//判断是否是第一次进入该软件
    private Button btn_over_app; //退出按钮

    public String getBluetoothDeviceName() {
        return bluetoothDeviceName;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        this.bluetoothDeviceName = bluetoothDeviceName;
    }

    public String getBluetoothDeviceAddress() {
        return bluetoothDeviceAddress;
    }

    public void setBluetoothDeviceAddress(String bluetoothDeviceAddress) {
        this.bluetoothDeviceAddress = bluetoothDeviceAddress;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
        setContentView(view);
        //禁止锁屏
        view.setKeepScreenOn(true);
        initView();
        doCheckOne();
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        initEvent();
    }

    /**
     * 初始化所有事件
     */
    private void initEvent()
    {

        viewPager.setOnPageChangeListener(this);

    }

    private void doCheckOne() {
        SharedPreferences perPreferences = this.getSharedPreferences("configurationFile", this.MODE_PRIVATE);
        isFirstIn = perPreferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            Toast.makeText(MainActivity.this, "请您及时更改密码，初始授权密码为：123456，超级管理员密码为：654321", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.putString("superPass", toMD5("654321"));
            editor.putString("shouquanPass",toMD5("123456"));
            editor.commit();
        }
    }


    public void initView() {
        tab_setting = (BottomTab) findViewById(R.id.home_tab_setting);
        tab_monitor = (BottomTab) findViewById(R.id.home_tab_monitor);
        tab_help = (BottomTab) findViewById(R.id.home_tab_help);
        btn_over_app= (Button)findViewById(R.id.btn_over_app);

        mTabs.add(tab_setting);
        mTabs.add(tab_monitor);
        mTabs.add(tab_help);
        tab_setting.setOnClickListener(this);
        tab_monitor.setOnClickListener(this);
        tab_help.setOnClickListener(this);
        btn_over_app.setOnClickListener(this);


        tab_setting.setIconAlpha(1.0f);

        home_setting = new Home_setting();
        home_safe = new Home_safe();
        home_help = new Home_help();


        fragments.add(home_setting);
        fragments.add(home_safe);
        fragments.add(home_help);

        viewPager= (ViewPager) findViewById(R.id.home_viewpager);

        viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setOffscreenPageLimit(0);
    }


    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()){
            case R.id.home_tab_setting:
                mTabs.get(0).setIconAlpha(1.0f);
                viewPager.setCurrentItem(TAB_HOME_SETTING, false);
                oldposition=0;
                break;
            case R.id.home_tab_monitor:
                mTabs.get(1).setIconAlpha(1.0f);
                viewPager.setCurrentItem(TAB_HOME_MONITOR, false);
                if(oldposition!=2) {
                    view=fragments.get(0).getView();
                    Home_setting home_setting = new Home_setting();
                    home_setting.initView(view);
                }
                oldposition=1;
                break;
            case R.id.home_tab_help:
                mTabs.get(2).setIconAlpha(1.0f);
                viewPager.setCurrentItem(TAB_HOME_HELP, false);
                oldposition=2;
                break;
            case R.id.btn_over_app:
                home_setting.onDestroy();
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //判断是否有配置文件
    public String[] getFiles(){
        String[] moren= new String[1];
        moren[0]="无文件";

        File file = new File("/data/data/cn.edu.nuc.csce.dcldims/shared_prefs");
        File [] savefiles = file.listFiles();
        if(savefiles.length==1){
            return moren;
        }
        ArrayList<String> filesNamess_list=new ArrayList<String>();
        for(int j=0;j<savefiles.length;j++) {
            if(savefiles[j].isFile()) {
                filesNamess_list.add(savefiles[j].getName());
            }
        }
        filesNamess_list.remove("configurationFile.xml");
        String[] filesNamess =new String[filesNamess_list.size()];
        for(int i=0;i<filesNamess_list.size();i++){
            filesNamess[filesNamess_list.size()- 1 - i] = filesNamess_list.get(i);
        }

        if(filesNamess==null){
            return moren;
        }else {
            return filesNamess;
        }
    }
    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < mTabs.size(); i++)
        {
            mTabs.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0)
        {
            BottomTab left = mTabs.get(position);
            BottomTab right = mTabs.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    /**
     * 实时同步设置页面和监测页面保持一致
     */
    private android.os.Handler mesHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                home_safe.goRefresh();
        }
    };
    private Runnable safe_ui=new Runnable() {
        @Override
        public void run() {
       mesHandler.sendEmptyMessage(0);
        }
    };
    @Override
    public void onPageSelected(int position) {
        if(position==1&& oldposition!=2){
            view=fragments.get(0).getView();
            Home_setting home_setting = new Home_setting();
            home_setting.initView(view);
            //一下内容是为了同步安全监测的内容
            new Thread(safe_ui).start();
                view = fragments.get(1).getView();
                Home_safe home_safe = new Home_safe();
                home_safe.initView(view);
                home_safe.setHome_safe_tishi_message(Util.file_now_name);
        }
        oldposition=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * MD5加密
     */
    public String toMD5(String plainText) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            System.out.println("32位: " + buf.toString());// 32位的加密
            return buf.toString();

            // System.out.println("16位: " + buf.toString().substring(8, 24));// 16位的加密，其实就是32位加密后的截取
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setBoolean(String systemSettingKey, boolean enabled) {
    android.provider.Settings.System.putInt(mContentResolver,
            systemSettingKey, enabled ? 1 : 0);
      }
    public void setLockPatternEnabled(boolean enabled) {
        setBoolean(android.provider.Settings.System.LOCK_PATTERN_ENABLED,
                enabled);
    }
}
