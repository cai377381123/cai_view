package cn.edu.nuc.csce.dcldims.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nuc.csce.dcldims.MainActivity;
import cn.edu.nuc.csce.dcldims.bluetooth.BluetoothLeService;
import cn.edu.nuc.csce.dcldims.bluetooth.Constant;
import cn.edu.nuc.csce.dcldims.bluetooth.DevAllData;
import cn.edu.nuc.csce.dcldims.bluetooth.DevSingleData;
import cn.edu.nuc.csce.dcldims.view.PianlifangweiView;
import cn.edu.nuc.csce.dcldims.view.Pianlijiao2View;
import cn.edu.nuc.csce.dcldims.R;
import cn.edu.nuc.csce.dcldims.utils.Util;
import cn.edu.nuc.csce.dcldims.view.Pianlifangwei2View;
import cn.edu.nuc.csce.dcldims.view.PianlijiaoView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Home_safe extends Fragment implements View.OnClickListener {
    private static final String TAG = "cn.edu.nuc.csce.DCLDIMS";
    private static int iScale = 1;
    private BluetoothLeService mBluetoothLeService;
    private int sendInteval = 300;
    private LinearLayout ll_one; //监测设备1
    private LinearLayout ll_two; //监测设备2
    private TextView home_safe_one_jcsb_name;   //监测设备1的名字
    private TextView home_safe_one_goushenkey;  //监测设备1的钩绳值
    private TextView home_safe_one_goushen_pljj; //监测设备1的钩绳偏离夹角
    private TextView home_safe_one_jcsb_dianliang; //监测设备1的电池电量

    private TextView home_safe_two_jcsb_name;   //监测设备2的名字
    private TextView home_safe_two_goushenkey;  //监测设备2的钩绳值
    private TextView home_safe_two_goushen_pljj; //监测设备2的钩绳偏离夹角
    private TextView home_safe_two_jcsb_dianliang; //监测设备2的电池电量

    private TextView home_safe_two_wxcskzq_dianliang;//无线传输设备控制电量

    private LinearLayout btn_home_safe_jclx; //监测类型
    private TextView home_safe_jclx; //监测类型显示

    private LinearLayout btn_home_safe_jcpl;//监测频率
    private TextView home_safe_jcpl; //监测频率显示

    private TextView home_safe_tishi_message; //提示信息显示
    private String now_file_name;    //提示信息处显示的当前配置文件的名字
    private String huifang_file_name="";    //回放监测文件的名字

    //监测设备1的相关信息
    private double one_roll=0.0;
    private double one_pitch=0.0;
    private String one_installLoaction="";
    private double one_px=0.0;
    private double one_py=0.0;
    private double one_roll_save=0.0;
    private double one_pitch_save=0.0;

    //监测设备2的相关信息
    private double two_roll=0.0;
    private double two_pitch=0.0;
    private String two_installLoaction="";
    private double two_px=0.0;
    private double two_py=0.0;
    private double two_roll_save=0.0;
    private double two_pitch_save=0.0;

    private Button btn_control_begin;      //开始监测按钮
    private Button btn_control_pause;   //暂停监测
    private Button btn_control_stop;   // 停止监测
    private Boolean flag_createnewfile=false; //创建新的监测文件的标志
    private Boolean flag_over=false; //监测结束

    private int flag_sb_num=0;     //判断单双机抬掉的标记为
    private byte[] temp_buffer20 =  new byte[20];
    private byte[] temp_buffer6 = new byte[6];
    private byte[] temp_buffer26 = new byte[26];

    private List<String> receive_data = new ArrayList<String>();  //接收到的数据，解封装到此处

    private DevSingleData dev1;
    private DevSingleData dev2;
    private DevAllData devall;

    //L值
    private double L=170/Math.sin(3);
    private PianlijiaoView one_pianlijiao_view;
    private Pianlijiao2View two_pianlijiao_view;
    private PianlifangweiView one_pianlifangwei_view;
    private Pianlifangwei2View two_pianlifangwei_view;
    private String mingling="";   //实时监测每次发送的命令
    private int flag_begin=0;    //开始监测的标志(修改后可暂停发送)
    private int sleep_time=1000;  //监测频率

    private HashMap<Integer,String> huifang_file_item =new HashMap<Integer,String>();  //当前回放文件的所有数据
    private List<String> huifang_data=new ArrayList<String>();//回放的时候读取分析出来的单条数据
    private String now_time="";   //监测文件的名字

    private LinearLayout ll_huifangfiles_listview;//触发监测文件列表
    private TextView huifangfiles_listview;//显示当前回放文件名字

    private int now_huifang_file_nums=0;
    private int item_num_jishu=1;//当前回放进度指针

    //可拖动的进度条部分
    private SeekBar seekbar_huifang = null;

    private Button btn_shipin_fuwei;  //回放进度的复位
    private Button btn_kaishi_zanting;  //回放进度的开始暂停
    private Boolean flag_begin_pause =false;//开始暂停的标志位

    private Boolean flag_begin_databufen=false;  //文件中监测数据开始标志
    private String FILES_PATH="/data/data/cn.edu.nuc.csce.dcldims/";  ///data/data/com.hoist/files/

    private int flag_shouquanchenggong=0;

    //报警部分变量
    private SoundPool sp;
    private HashMap<Integer,Integer> spMap;


    private BroadcastReceiver dataReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE_FOR_MONITOR.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for (byte byteChar : data)
                        stringBuilder.append(String.format("%02X ", byteChar));
                    Log.i(TAG, "In Monitor接收到的数据:" + stringBuilder.toString());
                    try {
                        analysisCommand(data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().registerReceiver(dataReceiver,makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(dataReceiver);
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE_FOR_MONITOR);
        return intentFilter;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_safe, container, false);
        initView(view);

        //初始化报警
        sp = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        spMap = new HashMap<Integer,Integer>();
        spMap.put(1, sp.load(getActivity(),R.raw.notice, 1));
        bindListener();
        if(!(Util.file_now_name.equals(""))) {
            matchParameter(Util.file_now_name);
        }
        now_file_name=Util.file_now_name;
        if(!huifang_file_name.equals("")){
            huifangfiles_listview.setText(huifang_file_name);
        }
        //IntentFilter filter = new IntentFilter();
        //filter.addAction("cn.edu.nuc.csce.dcldims.home_setting_data");
        //getActivity().registerReceiver(dataReceiver, filter);

        return view;
    }

    /**
     * 更新实时监测中的ui使用的线程
     */
        public void refreshMonitorUI(int devNum) {
            Message mes =new Message();
            if(1 == devNum) {
                mes.what = 1;
                mesHandler.sendMessage(mes);
            }else if(2 == devNum){
                mes.what=2;
                mesHandler.sendMessage(mes);
            }else if(3 == devNum){
                mes.what=3;
                mesHandler.sendMessage(mes);
            }
        }

    public String bytesToString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
            sb.append(' ');
        }
        return sb.toString();
    }
    /**
     * 解析数据包
     * @param buf
     */
    public  void analysisCommand(byte[] buf) throws UnsupportedEncodingException {
        String s = bytesToString(buf);
        Log.i(TAG, "In Monitor:"+s);
        if(1 == flag_sb_num){
            dev1 = new DevSingleData(buf);
            Log.i(TAG,"In Monitor，DEV1 DATA:" + dev1.getRoll() + ","
                    + dev1.getPitch() + "," + dev1.getCRC_value() + "," + dev1.getBattery_Monitor()
                    + dev1.getBattery_Controller());
            Log.i(TAG,"EQ1,CRC_CAL_1=" + dev1.getCRC_Cal_value() + "CRC_1=" + dev1.getCRC_value());
            if(dev1.getCRC_Cal_value() ==  dev1.getCRC_value()) {
                countRollAndPitch(dev1.getRoll(), dev1.getPitch());
            }
            refreshMonitorUI(1);
        }
        if(2 == flag_sb_num){
            dev2 = new DevSingleData(buf);
            Log.i(TAG,"In Monitor，DEV2 DATA:" + dev2.getRoll() + ","
                    + dev2.getPitch() + "," + dev2.getCRC_value() + "," + dev2.getBattery_Monitor()
                    + dev2.getBattery_Controller());
            Log.i(TAG,"EQ2,CRC_CAL_1=" + dev2.getCRC_Cal_value() + "CRC_1=" + dev2.getCRC_value());
            if(dev2.getCRC_Cal_value() ==  dev2.getCRC_value()) {
                countRollAndPitchTwo(dev2.getRoll(), dev2.getPitch());
            }
            refreshMonitorUI(2);
        }
        if(3 == flag_sb_num){
            if(20 == buf.length)
            {
                temp_buffer20=buf;
            }else if (6 == buf.length) {
                temp_buffer6 = buf;
                System.arraycopy(temp_buffer20, 0, temp_buffer26, 0, temp_buffer20.length);
                System.arraycopy(temp_buffer6, 0, temp_buffer26, temp_buffer20.length, temp_buffer6.length);
                devall = new DevAllData(temp_buffer26);
                Log.i(TAG, "In Monitor，DEVALL DATA:" + devall.getRoll_1() + ","
                        + devall.getPitch_1() + "," + devall.getCRC_value_1() + "," + devall.getBattery_Monitor_1()+ ","
                        + devall.getRoll_2() + ","
                        + devall.getPitch_2() + "," + devall.getCRC_value_2() + "," + devall.getBattery_Monitor_2()+ ","
                        + devall.getBattery_Controller());
                Log.i(TAG,"ALL,CRC_CAL_1=" + devall.getCRC_Cal_value_1() + "CRC_1=" + devall.getCRC_value_1());
                if(devall.getCRC_Cal_value_1() == devall.getCRC_value_1()) {
                    countRollAndPitch(devall.getRoll_1(), devall.getPitch_1());
                }
                Log.i(TAG,"ALL,CRC_CAL_2=" + devall.getCRC_Cal_value_2() + "CRC_2=" + devall.getCRC_value_2());
                if(devall.getCRC_Cal_value_2() == devall.getCRC_value_2()) {
                    countRollAndPitchTwo(devall.getRoll_2(), devall.getPitch_2());
                }
                refreshMonitorUI(3);
            }
        }
    }

    /**
     * 设置点击事件的监听
     */
    private void bindListener() {
        btn_home_safe_jclx.setOnClickListener(this);
        btn_home_safe_jcpl.setOnClickListener(this);
        btn_control_begin.setOnClickListener(this);
        btn_control_pause .setOnClickListener(this);
        btn_control_stop.setOnClickListener(this);
        ll_huifangfiles_listview.setOnClickListener(this);
        seekbar_huifang.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        btn_shipin_fuwei.setOnClickListener(this);
        btn_kaishi_zanting.setOnClickListener(this);
    }


    /**
     * 初始化控件
     * @param view
     */
    public void initView(View view){
        home_safe_one_jcsb_name= (TextView) view.findViewById(R.id.home_safe_one_jcsb_name);
         home_safe_one_goushenkey = (TextView) view.findViewById(R.id.home_safe_one_goushenkey);  //监测设备1的钩绳值
         home_safe_one_goushen_pljj= (TextView) view.findViewById(R.id.home_safe_one_goushen_pljj); //监测设备1的钩绳偏离夹角
        home_safe_one_jcsb_dianliang= (TextView) view.findViewById(R.id.home_safe_one_jcsb_dianliang); //监测设备1的电池电量

         home_safe_two_jcsb_name= (TextView) view.findViewById(R.id.home_safe_two_jcsb_name);   //监测设备2的名字
         home_safe_two_goushenkey = (TextView) view.findViewById(R.id.home_safe_two_goushenkey);  //监测设备2的钩绳值
         home_safe_two_goushen_pljj = (TextView) view.findViewById(R.id.home_safe_two_goushen_pljj); //监测设备2的钩绳偏离夹角
         home_safe_two_jcsb_dianliang = (TextView) view.findViewById(R.id.home_safe_two_jcsb_dianliang); //监测设备2的电池电量

         home_safe_two_wxcskzq_dianliang = (TextView) view.findViewById(R.id.home_safe_two_wxcskzq_dianliang);//无线传输设备控制电量
         btn_home_safe_jclx= (LinearLayout) view.findViewById(R.id.btn_home_safe_jclx); //监测类型
         home_safe_jclx = (TextView) view.findViewById(R.id.home_safe_jclx); //监测类型显示

         btn_home_safe_jcpl = (LinearLayout) view.findViewById(R.id.btn_home_safe_jcpl);//监测频率
         home_safe_jcpl = (TextView) view.findViewById(R.id.home_safe_jcpl); //监测频率显示

         home_safe_tishi_message= (TextView) view.findViewById(R.id.home_safe_tishi_message); //提示信息显示


        btn_control_begin= (Button) view.findViewById(R.id.btn_control_begin);
        btn_control_pause = (Button) view.findViewById(R.id.btn_control_pause);
        btn_control_stop = (Button) view.findViewById(R.id.btn_control_stop);

        ll_one = (LinearLayout) view.findViewById(R.id.ll_one);
        ll_two= (LinearLayout) view.findViewById(R.id.ll_two);

        //偏离角图的展示
        one_pianlijiao_view= (PianlijiaoView) view.findViewById(R.id.one_pianlijiao_view);
        two_pianlijiao_view= (Pianlijiao2View) view.findViewById(R.id.two_pianlijiao_view);

        //偏离方位图的展示
        one_pianlifangwei_view = (PianlifangweiView) view.findViewById(R.id.one_pianlifangwei_view);
        two_pianlifangwei_view= (Pianlifangwei2View) view.findViewById(R.id.two_pianlifangwei_view);

        ll_huifangfiles_listview= (LinearLayout) view.findViewById(R.id.ll_huifangfiles_listview);
        huifangfiles_listview= (TextView) view.findViewById(R.id.huifangfiles_listview);

        seekbar_huifang= (SeekBar) view.findViewById(R.id.seekbar_huifang);

        btn_shipin_fuwei= (Button) view.findViewById(R.id.btn_shipin_fuwei);
        btn_kaishi_zanting= (Button) view.findViewById(R.id.btn_kaishi_zanting);

    }

    /**
     * 实时更新提示信息
     * @param name
     */
    public void setHome_safe_tishi_message(String name){
        home_safe_tishi_message.setText(name);
    }

    /**
     * 读取第一个页面文件中保存的设备的roll和pitch的值
     */
    public void goRefresh(){
        refreshJiemianUI();
    }

    /**
     * 更新UI的一系列操作
     */
    private Handler mesHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0: matchParameter(Util.file_now_name);
                    break;
                case 1:
                    home_safe_one_jcsb_dianliang.setText( (int)(dev1.getBattery_Monitor() * 100) + "%");
                    home_safe_two_wxcskzq_dianliang.setText((int)(dev1.getBattery_Controller() * 100) + "%");
                    home_safe_one_goushen_pljj.setText(String.valueOf(countPianlijiao(one_roll, one_pitch)));
                    one_pianlijiao_view.setPianli_dushu(iScale * countPianlijiao(one_roll, one_pitch));
                    one_pianlijiao_view.postInvalidate();
                    countPianlifangwei();
                    one_pianlifangwei_view.setZhishiqi_x((float) one_px);
                    one_pianlifangwei_view.setZhishiqi_y((float) one_py);
                    one_pianlifangwei_view.postInvalidate();
                    String tmp_roll =  String.format("%d",(int)(dev1.getBattery_Monitor() * 100));
                    String tmp_pitch = String.format("%d",(int)(dev1.getBattery_Controller() * 100));
                    save_jiancedata("1,"+String.valueOf( countPianlijiao(one_roll, one_pitch))+"," + String.valueOf(one_px)+"," + String.valueOf(one_py)+"," + tmp_roll+"," + tmp_pitch);
                    break;
                case 2:
                    if(dev2 != null) {
                        home_safe_two_jcsb_dianliang.setText((int) (dev2.getBattery_Monitor() * 100) + "%");
                        home_safe_two_wxcskzq_dianliang.setText((int) (dev2.getBattery_Controller() * 100) + "%");
                    }
                    home_safe_two_goushen_pljj.setText(String.valueOf(countPianlijiao(two_roll, two_pitch)));
                    two_pianlijiao_view.setPianli_dushu(iScale * countPianlijiao(two_roll, two_pitch));
                    two_pianlijiao_view.postInvalidate();
                    countPianlifangwei();
                    two_pianlifangwei_view.setZhishiqi_x((float) two_px);
                    two_pianlifangwei_view.setZhishiqi_y((float) two_py);
                    two_pianlifangwei_view.postInvalidate();
                    String tmp_roll2 =  String.format("%d",(int)(dev2.getBattery_Monitor() * 100));
                    String tmp_pitch2 = String.format("%d",(int)(dev2.getBattery_Controller() * 100));
                    save_jiancedata("2,"+String.valueOf( countPianlijiao(two_roll, two_pitch)) + "," + String.valueOf(two_px) +","+ String.valueOf(two_py)+"," + tmp_roll2+"," + tmp_pitch2);
                    break;
                case 3:
                    home_safe_one_jcsb_dianliang.setText( (int)(devall.getBattery_Monitor_1() * 100) + "%");
                    home_safe_two_wxcskzq_dianliang.setText((int)(devall.getBattery_Controller() * 100) + "%");
                    home_safe_one_goushen_pljj.setText(String.valueOf(countPianlijiao(one_roll, one_pitch)));
                    one_pianlijiao_view.setPianli_dushu(iScale * countPianlijiao(one_roll, one_pitch));
                    one_pianlijiao_view.postInvalidate();
                    countPianlifangwei();
                    one_pianlifangwei_view.setZhishiqi_x((float) one_px);
                    one_pianlifangwei_view.setZhishiqi_y((float) one_py);
                    one_pianlifangwei_view.postInvalidate();
                    String tmp_roll_1 =  String.format("%d",(int)(devall.getBattery_Monitor_1() * 100));
                    String tmp_pitch_1 = String.format("%d",(int)(devall.getBattery_Controller() * 100));
                    save_jiancedata("1,"+String.valueOf( countPianlijiao(one_roll, one_pitch))+","
                            + String.valueOf(one_px)+"," + String.valueOf(one_py)+"," + tmp_roll_1+"," + tmp_pitch_1);
                    //////////////////////////
                    home_safe_two_jcsb_dianliang.setText((int) (devall.getBattery_Monitor_2() * 100) + "%");
                    home_safe_two_wxcskzq_dianliang.setText((int) (devall.getBattery_Controller() * 100) + "%");
                    home_safe_two_goushen_pljj.setText(String.valueOf(countPianlijiao(two_roll, two_pitch)));
                    two_pianlijiao_view.setPianli_dushu(iScale * countPianlijiao(two_roll, two_pitch));
                    two_pianlijiao_view.postInvalidate();
                    countPianlifangwei();
                    two_pianlifangwei_view.setZhishiqi_x((float) two_px);
                    two_pianlifangwei_view.setZhishiqi_y((float) two_py);
                    two_pianlifangwei_view.postInvalidate();
                    String tmp_roll_2 =  String.format("%d",(int)(devall.getBattery_Monitor_2() * 100));
                    String tmp_pitch_2 = String.format("%d",(int)(devall.getBattery_Controller() * 100));
                    save_jiancedata("2,"+String.valueOf( countPianlijiao(two_roll, two_pitch)) + ","
                            + String.valueOf(two_px) +","+ String.valueOf(two_py)+"," + tmp_roll_2+"," + tmp_pitch_2);
                    break;
            }
        }
    };

    /**
     * 更新ui的线程具体实现
     */
        public void refreshJiemianUI() {
            Message mes =new Message();
            mes.what = 0;
            mesHandler.sendMessage(mes);
        }

    /**
     * 执行各个值得内容的匹配
     * @param loadFileName
     */
    public void matchParameter(String loadFileName) {
        ll_one.setVisibility(View.GONE);
        ll_two.setVisibility(View.GONE);
        now_file_name=loadFileName;
        if (loadFileName.equals("无文件")){
            return;
        }
        System.out.println("这是安全检测页面中的信息"+now_file_name);
        String name = loadFileName.substring(0, loadFileName.lastIndexOf("."));
        System.out.println("这是安全检测页面中的信息"+name);
        SharedPreferences loadFile = getActivity().getSharedPreferences(name, getActivity().MODE_PRIVATE);
        if (loadFile.getString("stateInsatll", "未安装").equals("安装")) {
            flag_sb_num=1;
            ll_one.setVisibility(View.VISIBLE);
            home_safe_one_jcsb_name.setText(loadFile.getString("qzjname", ""));   //监测设备1的名字
            home_safe_one_goushenkey.setText(loadFile.getString("goushenkey", ""));  //监测设备1的钩绳值
            one_roll_save=Double.parseDouble(loadFile.getString("calibrationkeyrollkey", "0.0")); //监测设备1的roll值
            one_pitch_save=Double.parseDouble(loadFile.getString("calibrationkeypitchkey", "0.0")); //监测设备1的pitch值
            one_installLoaction=loadFile.getString("installLocation", "");//监测设备1的安装位置
            home_safe_one_goushen_pljj.setText(String.valueOf(countPianlijiao(one_roll_save,one_pitch_save)));
        } else {
            ll_one.setVisibility(View.GONE);
        }
        if (loadFile.getString("two_stateInsatll", "未安装").equals("安装")) {
            if(flag_sb_num==1){
                flag_sb_num=3;
            }else {
             flag_sb_num=2;
            }
            ll_two.setVisibility(View.VISIBLE);
            home_safe_two_jcsb_name.setText(loadFile.getString("qzjnametwo", ""));   //监测设备2的名字
            home_safe_two_goushenkey.setText(loadFile.getString("goushenkeytwo", ""));  //监测设备2的钩绳值
            two_roll_save=Double.parseDouble(loadFile.getString("twocalibrationkeyrollkey", "0.0"));//监测设备2的roll值
            two_pitch_save=Double.parseDouble(loadFile.getString("twocalibrationkeypitchkey", "0.0"));//监测设备2的pitch值
            two_installLoaction=loadFile.getString("installLocationtwo", "");//监测设备2的安装位置
            home_safe_two_goushen_pljj.setText(String.valueOf(countPianlijiao(two_roll_save, two_pitch_save)));

        } else {
            ll_two.setVisibility(View.GONE);
        }

    }

    /**
     * 计算——钩绳-铅垂线偏离夹角
     * @param roll_key
     * @param pitch_key
     * @return
     */
    public double countPianlijiao(double roll_key,double pitch_key){
        //y = acosd(cosd(pitch)*cosd(roll));
        double theta=(180.0/Math.PI)*(Math.acos(Math.cos(roll_key*Math.PI/180.0) * Math.cos(pitch_key*Math.PI/180.0)));
        BigDecimal bd = new BigDecimal(theta);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);//四舍五入
        Log.i(TAG,"PianLiJiao:" + bd.doubleValue() + ",roll:" + roll_key + ",pitch:" + pitch_key);
        int chazhi=19;
        switch (flag_sb_num){
            case 1: chazhi=Math.abs(bd.intValue()-Integer.parseInt(home_safe_one_goushenkey.getText().toString()));
                break;
            case 2:chazhi=Math.abs(bd.intValue()-Integer.parseInt(home_safe_two_goushenkey.getText().toString()));
                break;
            case 3:chazhi=Math.abs(bd.intValue()-Integer.parseInt(home_safe_one_goushenkey.getText().toString()));
                break;
        }
//        for(int i=0;i<20-chazhi;i++){
//            playSounds(1, 1);
//        }
        return bd.doubleValue();
    }

    /**
     * 计算roll和pitch 的值
     * @param roll_key
     * @param pitch_key
     */
    public void countRollAndPitch(double roll_key,double pitch_key){
        one_roll=roll_key-one_roll_save;
        one_pitch=pitch_key-one_pitch_save;
        Log.i(TAG,"Roll" + roll_key + ",Roll Save:" + one_roll_save + ",one_roll:" + one_roll);
        Log.i(TAG,"Pitch" + pitch_key + ",Pitch Save:" + one_pitch_save + ",one_pitch:" + one_pitch);
    }
    public void countRollAndPitchTwo(double roll_key,double pitch_key){
        two_roll=roll_key-two_roll_save;
        two_pitch=pitch_key-two_pitch_save;
        Log.i(TAG,"Roll" + roll_key + ",Roll Save:" + two_roll_save + ",two_roll:" + two_roll);
        Log.i(TAG,"Pitch" + pitch_key + ",Pitch Save:" + two_pitch_save + ",two_pitch:" + two_pitch);
    }

    /**
     * 计算——钩绳-铅垂线偏离方位
     */
    public void countPianlifangwei(){
        switch (flag_sb_num){
            case 1:
                if (one_installLoaction.equals("司机右手方向")) {
                    //one_px = L * Math.sin(one_roll);
                    //one_py = L * Math.cos(one_roll) * Math.sin(one_pitch);
                    //x = -L*cosd(pitch)*sind(roll);
                    //y =  L*sind(pitch);
                    one_px = -L * Math.cos(one_pitch*Math.PI/180.0) * Math.sin(one_roll*Math.PI/180.0);
                    one_py =  L * Math.sin(one_pitch*Math.PI/180.0);
                } else if (one_installLoaction.equals("司机左手方向")) {
                    //one_px = -L * Math.sin(one_roll);
                    //one_py = -L * Math.cos(one_roll) * Math.sin(one_pitch);
                    //x = L*cosd(pitch)*sind(roll);
                    //y = -L*sind(pitch);
                    one_px = L * Math.cos(one_pitch*Math.PI/180.0) * Math.sin(one_roll*Math.PI/180.0);
                    one_py = -L * Math.sin(one_pitch*Math.PI/180.0);
                }
                System.out.println("坐标轴的x值为" + one_px + "y的值为" + one_py);
                break;
            case 2:
                if (two_installLoaction.equals("司机右手方向")){
                    //two_px=L*Math.sin(two_roll);
                    //two_py=L*Math.cos(two_roll)*Math.sin(two_pitch);
                    two_px = -L * Math.cos(two_pitch*Math.PI/180.0) * Math.sin(two_roll*Math.PI/180.0);
                    two_py =  L * Math.sin(two_pitch*Math.PI/180.0);
                }else if (two_installLoaction.equals("司机左手方向")){
                    //two_px=-L*Math.sin(two_roll);
                    //two_py=-L*Math.cos(two_roll)*Math.sin(two_pitch);
                    two_px = L * Math.cos(two_pitch*Math.PI/180.0) * Math.sin(two_roll*Math.PI/180.0);
                    two_py = -L * Math.sin(two_pitch*Math.PI/180.0);
                }
                break;
            case 3:
                if (one_installLoaction.equals("司机右手方向")) {
                    //one_px = L * Math.sin(one_roll);
                    //one_py = L * Math.cos(one_roll) * Math.sin(one_pitch);
                    one_px = -L * Math.cos(one_pitch*Math.PI/180.0) * Math.sin(one_roll*Math.PI/180.0);
                    one_py =  L * Math.sin(one_pitch*Math.PI/180.0);
                    Log.i(TAG,"右手1，x=" + one_px +",y=" + one_py + ",roll:" + one_roll +",pitch:" + one_pitch);
                } else if (one_installLoaction.equals("司机左手方向")) {
                    //one_px = -L * Math.sin(one_roll);
                    //one_py = -L * Math.cos(one_roll) * Math.sin(one_pitch);
                    one_px = L * Math.cos(one_pitch*Math.PI/180.0) * Math.sin(one_roll*Math.PI/180.0);
                    one_py = -L * Math.sin(one_pitch*Math.PI/180.0);
                    Log.i(TAG,"左手1，x=" + one_px +",y=" + one_py);
                }
                if (two_installLoaction.equals("司机右手方向")){
                    //two_px=L*Math.sin(two_roll);
                    //two_py=L*Math.cos(two_roll)*Math.sin(two_pitch);
                    two_px = -L * Math.cos(two_pitch*Math.PI/180.0) * Math.sin(two_roll*Math.PI/180.0);
                    two_py =  L * Math.sin(two_pitch*Math.PI/180.0);
                    Log.i(TAG,"右手2，x=" + two_px +",y=" + two_py+ ",roll:" + two_roll +",pitch:" + two_pitch);
                }else if (two_installLoaction.equals("司机左手方向")){
                    //two_px=-L*Math.sin(two_roll);
                    //two_py=-L*Math.cos(two_roll)*Math.sin(two_pitch);
                    two_px = L * Math.cos(two_pitch*Math.PI/180.0) * Math.sin(two_roll*Math.PI/180.0);
                    two_py = -L * Math.sin(two_pitch*Math.PI/180.0);
                    Log.i(TAG,"左手2，x=" + two_px +",y=" + two_py);
                }
                break;
        }
    }

    /**
     * 点击事件的具体设置
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_safe_jclx:
                set_ll_jclx_state();
                break;
            case R.id.btn_home_safe_jcpl:
                set_ll_jcpl_state();
                break;
            case R.id.btn_control_begin:
                if(  ((MainActivity)getActivity()).b_Blue_Connected ) {
                    Log.i(TAG, "Begin monitor=============");
                    mBluetoothLeService = ((MainActivity) getActivity()).getBluetoothLeService();
                    flag_begin = 0;
                    set_btn_control();
                }else {
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_control_pause:
                flag_begin=1;
                flag_createnewfile=false;
                sp.pause(spMap.get(1));
                //暂停监测
                break;
            case R.id.btn_control_stop:
                flag_begin=1;
                flag_createnewfile=true;
                flag_over=true;
                //停止监测
                break;
            case R.id.ll_huifangfiles_listview:
                set_ll_huifangfiles_listview();
                break;
            case R.id.btn_shipin_fuwei:
                set_btn_shipin_fuwei();
                break;
            case R.id.btn_kaishi_zanting:
                set_btn_kaishi_zanting();
                break;
        }
    }


    /**
     * 发送命令线程
     */
    private Runnable sendMingLing = new Runnable() {
        int i = 0;

        @Override
        public void run() {

         while(flag_begin==0){
             switch (flag_sb_num){
                 case 0:return;
                 case 1:
                     mBluetoothLeService.writeData((Constant.CMD_EQ1).getBytes(), true);
                     i = 1;
                     break;
                 case 2:
                     mBluetoothLeService.writeData((Constant.CMD_EQ2).getBytes(), true);
                     i = 1;
                     break;
                 case 3:
                     mBluetoothLeService.writeData((Constant.CMD_EQ_ALL).getBytes(), true);
                     i = 2;
                     break;
             }
             try {
                     Thread.sleep(sleep_time);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
        }
    };


    /**
     *监测回放更新UI具体操作
     */
    private Handler huifang_handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:home_safe_one_jcsb_dianliang.setText(huifang_data.get(4) + "%");
                    home_safe_two_wxcskzq_dianliang.setText(huifang_data.get(5) + "%");
                    home_safe_one_goushen_pljj.setText(huifang_data.get(1));
                    one_pianlijiao_view.setPianli_dushu(Double.parseDouble(huifang_data.get(1)) * iScale);
                    one_pianlijiao_view.postInvalidate();
                    one_pianlifangwei_view.setZhishiqi_x(Float.parseFloat(huifang_data.get(2)));
                    one_pianlifangwei_view.setZhishiqi_y(Float.parseFloat(huifang_data.get(3)));
                    one_pianlifangwei_view.postInvalidate();
                    break;
                case 2:
                    home_safe_two_jcsb_dianliang.setText(huifang_data.get(4) + "%");
                    home_safe_two_wxcskzq_dianliang.setText(huifang_data.get(5) + "%");
                    home_safe_two_goushen_pljj.setText(huifang_data.get(1));
                    two_pianlijiao_view.setPianli_dushu(Double.parseDouble(huifang_data.get(1)) * iScale);
                    two_pianlijiao_view.postInvalidate();
                    two_pianlifangwei_view.setZhishiqi_x(Float.parseFloat(huifang_data.get(2)));
                    two_pianlifangwei_view.setZhishiqi_y(Float.parseFloat(huifang_data.get(3)));
                    two_pianlifangwei_view.postInvalidate();
                    break;
                case 3:
                    home_safe_one_jcsb_dianliang.setText(huifang_data.get(4) + "%");
                    home_safe_two_jcsb_dianliang.setText(huifang_data.get(8) + "%");
                    home_safe_two_wxcskzq_dianliang.setText(huifang_data.get(9) + "%");
                    home_safe_one_goushen_pljj.setText(huifang_data.get(1));
                    home_safe_two_goushen_pljj.setText(huifang_data.get(5));
                    one_pianlijiao_view.setPianli_dushu(Double.parseDouble(huifang_data.get(1)) * iScale);
                    one_pianlijiao_view.postInvalidate();
                    two_pianlijiao_view.setPianli_dushu(Double.parseDouble(huifang_data.get(5)) * iScale);
                    two_pianlijiao_view.postInvalidate();
                    one_pianlifangwei_view.setZhishiqi_x(Float.parseFloat(huifang_data.get(2)));
                    one_pianlifangwei_view.setZhishiqi_y(Float.parseFloat(huifang_data.get(3)));
                    one_pianlifangwei_view.postInvalidate();
                    two_pianlifangwei_view.setZhishiqi_x(Float.parseFloat(huifang_data.get(6)));
                    two_pianlifangwei_view.setZhishiqi_y(Float.parseFloat(huifang_data.get(7)));
                    two_pianlifangwei_view.postInvalidate();
                    break;
                case 4:
                    flag_begin_pause=false;
                    btn_kaishi_zanting.setText("开始");
                    item_num_jishu=1;
                    break;
            }
        }
    };
    /**
     * 更新回放UI线程
     */
        public void refreshHuifangUI() {
            Message mes =new Message();
            if(huifang_data.get(0).equals("1")) {
                mes.what = 1;
                huifang_handler.sendMessage(mes);
            }else if(huifang_data.get(0).equals("2")){
                mes.what=2;
                huifang_handler.sendMessage(mes);
            }else if(huifang_data.get(0).equals("3")){
                mes.what=3;
                huifang_handler.sendMessage(mes);
            }
        }

    /**
     * 回放读取文件信息线程
     */
    private Runnable   read_runnable=new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("这是默认的值"+item_num_jishu);
               // item_num_jishu=1
                    String lineTxt ="";
                   for(int i=item_num_jishu;i<=huifang_file_item.size();i++) {
                       if (flag_begin_pause) {
                           System.out.println("这是默认的gensui值" + i);
                           seekbar_huifang.setProgress(i);
                           huifang_data.clear();
                           lineTxt = huifang_file_item.get(i);
                           String[] readdatas = lineTxt.split(",");
                           for (int j = 0; j < readdatas.length; j++) {
                               huifang_data.add(readdatas[j]);
//                               System.out.println(readdatas[j]);
                           }
                           refreshHuifangUI();
                           Thread.sleep(sleep_time);
                       }
                   }
                Message mes =new Message();
                    mes.what = 4;
                    huifang_handler.sendMessage(mes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 回放实时监测过程
     */
    public void huifang(){
        try {
            huifang_file_item.clear();
            now_huifang_file_nums=0;
            File file = new File(FILES_PATH+"files/" + huifang_file_name);
            if (file.exists()) { //判断文件是否存在
                System.out.println("文件存在可以继续");
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                System.out.println("开始读文件");
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println("试读："+lineTxt);
                    if(lineTxt.equals("开始")){
                        flag_begin_databufen=true;
                    }
                    if(flag_begin_databufen) {
                        now_huifang_file_nums++;
                        huifang_file_item.put(now_huifang_file_nums, lineTxt);
                    }
                }
                read.close();
                initSeekBar(now_huifang_file_nums);
//                System.out.println("yuantou共有多少数据"+now_huifang_file_nums);
//                String[] setjiemian=huifang_file_item.get(0).split(",");
//                int a=Integer.parseInt(setjiemian[0]);
//                switch (a){
//                    case 1:
//                        ll_one.setVisibility(View.VISIBLE);
//                        ll_two.setVisibility(View.GONE);
//                        break;
//                    case 2:
//                        ll_one.setVisibility(View.GONE);
//                        ll_two.setVisibility(View.VISIBLE);
//                        break;
//                    case 3:
//                        ll_one.setVisibility(View.VISIBLE);
//                        ll_two.setVisibility(View.VISIBLE);
//                        break;
//                }
                flag_begin_databufen=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(read_runnable).start();
    }

    /**
     *  初始化进度条
     * @param now_huifang_file_nums
     */
    public void initSeekBar(int now_huifang_file_nums){
        seekbar_huifang.setEnabled(true);
        seekbar_huifang.setMax(now_huifang_file_nums);
    }

    /**
     * seekbar的监听事件
     */
    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
           if(seekBar.getProgress()==0){
               item_num_jishu=1;
           }else {
               item_num_jishu = seekBar.getProgress();
           }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(seekBar.getProgress()==0){
                item_num_jishu=1;
            }else {
                item_num_jishu=seekBar.getProgress();
            }
            flag_begin_pause=false;
            btn_kaishi_zanting.setText("开始");
        }
    }

    /**
     * 保存每次的实时监测数据供回放的使用
     */
    public  void save_jiancedata(String data){
        System.out.println("要保存的数据为"+data);
        File file=new File(FILES_PATH+now_time);
        System.out.println("保存文件的名字" + file.getName());
        huifang_file_name=file.getName();
        if(!file.exists()){
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = getActivity().openFileOutput(file.getName(), Context.MODE_APPEND);
            BufferedWriter bufferWritter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferWritter.write(data);
            bufferWritter.write("\n");
            bufferWritter.close();
            if(file.exists()){
                System.out.println("文件存在");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 开始/暂停按钮的点击事件
     */

    public void set_btn_kaishi_zanting(){
         if(flag_begin_pause==false){
             flag_begin_pause=true;
             btn_kaishi_zanting.setText("暂停");
             if(seekbar_huifang.getProgress()==0){
                  item_num_jishu=1;
             }else{
                 item_num_jishu=seekbar_huifang.getProgress();
             }
             if(home_safe_jcpl.getText().equals("0.5s")){
                 sleep_time=500;
             }else if(home_safe_jcpl.getText().equals("1s")){
                 sleep_time=1000;
             }
             huifang();
         }else{
             flag_begin_pause=false;
             btn_kaishi_zanting.setText("开始");
         }
    }
    /**
     * 复位按钮的点击事件
     */
    public void  set_btn_shipin_fuwei(){
        seekbar_huifang.setProgress(0);
        item_num_jishu=1;
        btn_kaishi_zanting.setText("开始");
        flag_begin_pause=false;
    }
    /**
    点击事件：控制实时监测的开始与暂停
     */
    public void set_btn_control() {
        playSounds(1, 1);
        //发送实时监测的命令包
        if (home_safe_jcpl.getText().equals("1s")){
            sleep_time=1000;
        }
        if (home_safe_jcpl.getText().equals("0.5s")){
            sleep_time=500;
        }
        if(flag_over){
            flag_createnewfile=true;
        }
        if (now_time.equals("")){
            flag_createnewfile=true;
        }
        if(flag_createnewfile) {
            final String[] fileName = {null};
            StringBuffer sb_time = new StringBuffer();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
            sb_time.append(df.format(new Date()).toString());
            fileName[0] = sb_time.toString();
            now_time = fileName[0];
            flag_over=false;
            String name = now_file_name.substring(0, now_file_name.lastIndexOf("."));
            SharedPreferences loadFile = getActivity().getSharedPreferences(name, getActivity().MODE_PRIVATE);
            save_jiancedata("配置文件：" + home_safe_tishi_message.getText().toString());
            save_jiancedata("项目名称：" + loadFile.getString("pjname", ""));
            save_jiancedata("执行人：" + loadFile.getString("dopeople", ""));
            save_jiancedata("授权人：" + loadFile.getString("shoupeople", ""));
            save_jiancedata("抬掉工况：：" + loadFile.getString("doublehoist", ""));
            if (flag_sb_num == 1) {
                save_jiancedata("监测设备1安装状态：" + loadFile.getString("stateInsatll", "未安装"));
                save_jiancedata("监测设备1起重机名称：" + loadFile.getString("qzjname", ""));
                save_jiancedata("监测设备1起重机司机：" + loadFile.getString("qzjdriver", ""));
                save_jiancedata("监测设备1安装位置：" + loadFile.getString("installLocation", ""));
                save_jiancedata("监测设备1校准数据：" + loadFile.getString("calibrationkeyrollkey", "") + "," + loadFile.getString("calibrationkeypitchkey", ""));
                save_jiancedata("监测设备1报警值：" + loadFile.getString("goushenkey", ""));
                save_jiancedata("开始");
            } else if (flag_sb_num == 2) {
                save_jiancedata("监测设备2安装状态：" + loadFile.getString("two_stateInsatll", "未安装"));
                save_jiancedata("监测设备2起重机名称：" + loadFile.getString("qzjnametwo", ""));
                save_jiancedata("监测设备2起重机司机：" + loadFile.getString("qzjdrivertwo", ""));
                save_jiancedata("监测设备2安装位置：" + loadFile.getString("installLocationtwo", ""));
                save_jiancedata("监测设备2校准数据：" + loadFile.getString("twocalibrationkeyrollkey", "") + "," + loadFile.getString("twocalibrationkeypitchkey", ""));
                save_jiancedata("监测设备2报警值：" + loadFile.getString("goushenkeytwo", ""));
                save_jiancedata("开始");
            } else if (flag_sb_num == 3) {
                save_jiancedata("监测设备1安装状态：" + loadFile.getString("stateInsatll", "未安装"));
                save_jiancedata("监测设备1起重机名称：" + loadFile.getString("qzjname", ""));
                save_jiancedata("监测设备1起重机司机：" + loadFile.getString("qzjdriver", ""));
                save_jiancedata("监测设备1安装位置：" + loadFile.getString("installLocation", ""));
                save_jiancedata("监测设备1校准数据：" + loadFile.getString("calibrationkeyrollkey", "") + "," + loadFile.getString("calibrationkeypitchkey", ""));
                save_jiancedata("监测设备1报警值：" + loadFile.getString("goushenkey", ""));

                save_jiancedata("监测设备2安装状态：" + loadFile.getString("two_stateInsatll", "未安装"));
                save_jiancedata("监测设备2起重机名称：" + loadFile.getString("qzjnametwo", ""));
                save_jiancedata("监测设备2起重机司机：" + loadFile.getString("qzjdrivertwo", ""));
                save_jiancedata("监测设备2安装位置：" + loadFile.getString("installLocationtwo", ""));
                save_jiancedata("监测设备2校准数据：" + loadFile.getString("twocalibrationkeyrollkey", "") + "," + loadFile.getString("twocalibrationkeypitchkey", ""));
                save_jiancedata("监测设备2报警值：" + loadFile.getString("goushenkeytwo", ""));
                save_jiancedata("开始");
            }
        }
        new Thread(sendMingLing).start();
    }

    /**
点击事件：
设置监测类型
*/
    public void set_ll_jclx_state(){
        int flag=0;
        if(home_safe_jclx.getText().toString().equals("暂停监测")){
            flag=2;
        }
        final String items[]={"实时监测","过程回放","暂停监测"};
        final String[] choose_result = {""};
        AlertDialog ad_jclx_state = new  AlertDialog.Builder(getActivity())
                .setTitle(R.string.safemonitor)
                .setSingleChoiceItems(items, flag, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose_result[0] = items[which];
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        home_safe_jclx.setText(choose_result[0]);
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_jclx_state.show();
    }

    /**
  点击事件：
  监测频率
*/
    public void set_ll_jcpl_state(){
        int flag_frequency_state=0;
        if(home_safe_jcpl.getText().toString().equals("1s")){
            flag_frequency_state=1;
        }
        final String items_frequency_state[]={"0.5s","1s"};
        AlertDialog ad_jcpl_state = new AlertDialog.Builder(getActivity())
                .setTitle("实时监测频率")
                .setSingleChoiceItems(items_frequency_state, flag_frequency_state, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        home_safe_jcpl.setText(items_frequency_state[which]);
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
        ad_jcpl_state.show();
    }


    /**
     * 监测文件列表
     */
    public void set_ll_huifangfiles_listview(){
        final String[] filesNames=getFiles();
        if(filesNames.length==1 && filesNames[0].equals("无文件")){
            return ;
        }
        int flag_index=0;
        for(int i=0;i<filesNames.length;i++){
            if(huifangfiles_listview.getText().toString().equals(filesNames[i])){
                flag_index=i;
            }
        }
        final int[] finalFlag_index = {flag_index};
        AlertDialog ad_files_listview = new  AlertDialog.Builder(getActivity())
                .setTitle("监测文件列表")
                .setSingleChoiceItems(filesNames, flag_index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalFlag_index[0] =which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        huifangfiles_listview.setText(filesNames[finalFlag_index[0]]);
                        huifang_file_name = filesNames[finalFlag_index[0]];
                        flag_begin_pause = true;
                        btn_kaishi_zanting.setText("暂停");
                        item_num_jishu = 1;
                        huifang();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("删除",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteConfigFile(filesNames[finalFlag_index[0]]);
                            }
                        }).create();
        ad_files_listview.show();
    }


    /**
   删除监测文件
    */
    public void deleteConfigFile(final String deleteFileName){
        MainActivity one= (MainActivity) getActivity();
        if(one.getFlag_guanliyuan()==1) {
            File file = new File(FILES_PATH, deleteFileName);
            if (file.exists()) {
                file.delete();
                Toast.makeText(getActivity(), "监测文件删除成功！", Toast.LENGTH_SHORT).show();
            }
        }else{
            final EditText etLogin_shouquan = new EditText(getActivity());
            etLogin_shouquan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            AlertDialog ad_shouquan = new AlertDialog.Builder(getActivity())
                    .setMessage("输入管理员密码")
                    .setView(etLogin_shouquan)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (toMD5(etLogin_shouquan.getText().toString()).equals(getSuperPass())) {
                                        File file = new File(FILES_PATH, deleteFileName);
                                        if (file.exists()) {
                                            file.delete();
                                            Toast.makeText(getActivity(), "监测文件删除成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "密码错误，删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();

            ad_shouquan.show();
        }
    }

    /**
获取超级管理密码
 */
    public String getSuperPass(){
        SharedPreferences perPreferences = getActivity().getSharedPreferences("configurationFile", getActivity().MODE_PRIVATE);
        String superPass = perPreferences.getString("superPass", null);
        return superPass;
    }

    /**
  MD5加密
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
    /**
       获取现有的配置文件目录
    */
    public String[] getFiles(){
        String[] moren= new String[1];
        moren[0]="无文件";
        File file = new File(FILES_PATH);
        File [] savefiles = file.listFiles();
        ArrayList<String> filesNamess_list=new ArrayList<String>();

        if (savefiles==null){
            System.out.println("对不起，你取的文件夹中是空的");
            return moren;
        }
        for(int j=0;j<savefiles.length;j++) {
            if(savefiles[j].isFile()) {
                filesNamess_list.add(savefiles[j].getName());
                System.out.println("读取文件夹中的文件的名字："+savefiles[j].getName());
            }
        }
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
     * 广播接收器的注销
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 获取系统的声音
     * @param sound
     * @param number
     */
    public void playSounds(int sound, int number){
        AudioManager am = (AudioManager)getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
        sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
    }

}