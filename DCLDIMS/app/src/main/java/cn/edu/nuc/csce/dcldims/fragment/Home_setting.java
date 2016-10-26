package cn.edu.nuc.csce.dcldims.fragment;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.BoringLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nuc.csce.dcldims.MainActivity;
import cn.edu.nuc.csce.dcldims.bluetooth.BluetoothLeService;
import cn.edu.nuc.csce.dcldims.bluetooth.BluetoothUtils;
import cn.edu.nuc.csce.dcldims.bluetooth.Constant;
import cn.edu.nuc.csce.dcldims.bluetooth.DevSingleData;
import cn.edu.nuc.csce.dcldims.entity.Configurationparameter;
import cn.edu.nuc.csce.dcldims.utils.Util;
import cn.edu.nuc.csce.dcldims.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;


public class Home_setting extends Fragment implements View.OnClickListener {
    private static final String TAG = "cn.edu.nuc.csce.DCLDIMS";
    private LinearLayout btnShouquan;   //触发授权
    private TextView stateShouquan;     //授权状态显示
    private LinearLayout ll_files_listview;     //配置文件列表

    private TextView filesListview;       //显示当前配置文件名

    private LinearLayout ll_pg_name;     //触发填写项目名称
    private TextView pg_name;           //抬吊作业项目名称
    private LinearLayout ll_dopeople_name; //触发填写执行人的名字
    private TextView dopeopleName;      //执行人名字
    private LinearLayout ll_shoupeople_name;      //触发填写执行人的名字
    private TextView shoupeopleName;    //授权人名字

    private LinearLayout ll_hoistcase_state; //抬吊工况布局
    private TextView stateHoistcase;     //抬吊工况状态
    private LinearLayout ll_allmonitor_state; //安全检测布局
    private TextView stateAllmonitor;    //安全检测类型
    private TextView  frequency_state;  //实时监测频率
    private LinearLayout  ll_frequency_state;  //实时监测频率布局

    private LinearLayout ll_searchsblist; // 触发已经搜索到的设备list
    private TextView searchSblist;          //已经搜索到的设备list
    private LinearLayout ll_mearchssblist;  // 触发已经配对的设备list
    private TextView mearchSblist;          //已经配对的设备list

    private LinearLayout ll_insatllstate;   //监测设备1安装状态布局
    private TextView stateInsatll;         //监测设备1安装状态
    private LinearLayout ll_qzj_name;     // 触发填写起重机1名字
    private TextView qzj_name;               //起重机1名字
    private LinearLayout ll_qzj_drivername; //触发填写起重机司机1名字
    private TextView qzj_drivername;       //起重机司机1名字
    private LinearLayout ll_install_location ;  //安装位置1布局
    private TextView installLocation;       //安装位置1
    private LinearLayout ll_edit_goushenkey; //触发填写监测设备1钩绳值
    private TextView goushenkey;            //监测设备1钩绳值
    private TextView calibrationkey_roll;         //校准数据显示roll值
    private TextView calibrationkey_pitch;         //校准数据显示pitch值
    private Button btn_commucationCeShi;    //通信测试按钮1
    private Button btn_datacalibra;         //数据校准按钮1

    private LinearLayout ll_two_stateInsatll; //监测设备2安装状态布局
    private TextView two_stateInsatll;         //监测设备2安装状态
    private LinearLayout ll_two_qzj_name; //触发填写起重机2名字
    private TextView two_qzj_name;               //起重机2名字
    private LinearLayout ll_two_qzj_drivername;   //触发填写起重机司机2名字
    private TextView two_qzj_drivername;       //起重机司机2名字
    private LinearLayout ll_two_installLocation; // 安装位置2布局
    private TextView two_installLocation;       //安装位置2
    private LinearLayout ll_two_goushenkey;  // 触发填写钩绳值
    private TextView two_goushenkey;            //监测设备2钩绳值
    private TextView two_calibrationkey_roll;         //校准数据显示2roll值
    private TextView two_calibrationkey_pitch;         //校准数据显示2pitch值
    private Button btn_two_commucationCeShi;    //通信测试按钮2
    private Button btn_two_datacalibra;         //数据校准按钮2

    private LinearLayout ll_saveFile; //触发保存文件
    private LinearLayout ll_alter_shouquanpass; //触发授权密码
    private LinearLayout ll_alter_superpass;  //触发超级管理密码

    private String[] propertys=null; //配置文件的属性
    private String[] propertyszhi=null; //配置文件的属性值

    private Configurationparameter configurationparameter = new Configurationparameter();


    //蓝牙通信部分
    private BluetoothUtils mBluetoothUtils;
    private long recvBytes;        // 当前接收的字节数
    private long sendBytes;        // 当前发送的字节数
    //	    private boolean isTimerEnable; // 定时器的使能标志
    private StringBuilder mData;   // 要显示的数据
    //	    private Timer timer;
    private BluetoothAdapter btAdapter = null;// 本地蓝牙适配器
    private String connectedNameStr = null;// 已连接的设备名称
    private Button btn_searchsb;            //搜索蓝牙设备
    private Button btn_communication;      //通信测试
    private Boolean b_Eq1_Adjusting = false;//是否开始较准数据
    private Boolean b_Eq2_Adjusting = false;//是否开始较准数据
    private Boolean b_Eq1_communication = false;
    private Boolean b_Eq2_communication = false;

    private ArrayAdapter<String> myAdapterPaired;//已配对的
    private ArrayAdapter<String> myAdapterNew;//新发现的

    private AlertDialog ad_matchedsb_listview; //显示已经配对的蓝牙列表
    private AlertDialog ad_searchedsb_listview; // 显示未配对的蓝牙列表

    private List<String> receive_data = new ArrayList<String>();  //接收到的数据，解封装到此处
    private String DATA_JZ_COMMAND="";

    private String shared_path="/data/data/cn.edu.nuc.csce.dcldims/shared_prefs";//存放配置文件的路径
    /**
     * 点击dialog中的信息的更改
     */
    private TextView mesdianliangTextView ;
    private TextView  mestishiTextView;
    private TextView meschuandidataTextView;

////////////Bluetooth//////////////////////////////////////
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
//////////////////////////////////////////////////////////
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            ((MainActivity)getActivity()).setBluetoothLeService(mBluetoothLeService);
            if (!mBluetoothLeService.initialize()) {
                Log.i(TAG, "Unable to initialize Bluetooth in ServiceConnected");
                //finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
            mConnected = true;
            ((MainActivity)getActivity()).b_Blue_Connected = true;
            Toast.makeText(getActivity(),"蓝牙已连接",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            mConnected = false;
            ((MainActivity)getActivity()).b_Blue_Connected = false;
            Toast.makeText(getActivity(),"蓝牙已断开",Toast.LENGTH_SHORT).show();
        }
    };
    /**
     *    定义广播接收器接收收到的数据 然后进行解析
     */
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private BroadcastReceiver dataReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                ((MainActivity)getActivity()).b_Blue_Connected = true;
                Log.i(TAG,"Bluetooth connected!");
                Toast.makeText(getActivity(),"蓝牙已连接",Toast.LENGTH_SHORT).show();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                ((MainActivity)getActivity()).b_Blue_Connected = false;
                Log.i(TAG,"Bluetooth disconnected!");
                Toast.makeText(getActivity(),"蓝牙已断开",Toast.LENGTH_SHORT).show();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for (byte byteChar : data)
                        stringBuilder.append(String.format("%02X ", byteChar));
                    Log.i(TAG, "这是接收到的数据:" + stringBuilder.toString());
                    try {
                        if(b_Eq1_Adjusting || b_Eq2_Adjusting){
                            dataAdjusting(data);
                        }else {
                            checkCommunication(data);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothUtils = new BluetoothUtils(getActivity(), mHandler);
        mBluetoothUtils.initialize();
        Log.i(TAG,"Bluetooth initialize() called");
        mData = new StringBuilder();

        recvBytes = 0;
        sendBytes = 0;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_setting, container, false);
        initView(view);
        initListener();

        String[] app_filesNames=getFiles();
        if(app_filesNames[0].equals("无文件")) {
            filesListview.setText(app_filesNames[0]);
            Util.file_now_name=app_filesNames[0];
        }else{
            filesListview.setText(app_filesNames[0]);
            Util.file_now_name=app_filesNames[0];
            matchParameter(app_filesNames[0]);
        }
        return view;
    }

    public void initListener(){
        btn_commucationCeShi.setOnClickListener(this);
        btn_communication.setOnClickListener(this);
        btn_datacalibra.setOnClickListener(this);
        btn_searchsb.setOnClickListener(this);
        btn_two_commucationCeShi.setOnClickListener(this);
        btn_two_datacalibra.setOnClickListener(this);
        btnShouquan.setOnClickListener(this);

        ll_pg_name.setOnClickListener(this);
        ll_dopeople_name.setOnClickListener(this);
        ll_shoupeople_name.setOnClickListener(this);
        ll_qzj_name.setOnClickListener(this);
        ll_qzj_drivername.setOnClickListener(this);
        ll_edit_goushenkey.setOnClickListener(this);

        ll_two_qzj_name.setOnClickListener(this);
        ll_two_qzj_drivername .setOnClickListener(this);
        ll_two_goushenkey.setOnClickListener(this);

        ll_saveFile.setOnClickListener(this);
        ll_hoistcase_state.setOnClickListener(this);

        ll_insatllstate.setOnClickListener(this);
        ll_install_location.setOnClickListener(this);

        ll_two_stateInsatll.setOnClickListener(this);
        ll_two_installLocation.setOnClickListener(this);

        ll_files_listview.setOnClickListener(this);
        ll_frequency_state.setOnClickListener(this);

        ll_alter_shouquanpass.setOnClickListener(this);
        ll_alter_superpass.setOnClickListener(this);

        //ll_searchsblist.setOnClickListener(this);
        ll_mearchssblist.setOnClickListener(this);
    }

    public void initView(View view) {
        btnShouquan = (LinearLayout) view.findViewById(R.id.btn_shouquan);
        stateShouquan = (TextView) view.findViewById(R.id.state_shouquan);
        stateShouquan.setText(R.string.weishouquan);
        //stateShouquan.setText("已授权");
        filesListview= (TextView) view.findViewById(R.id.files_listview);
        pg_name= (TextView) view.findViewById(R.id.pg_name);
        dopeopleName = (TextView) view.findViewById(R.id.dopeople_name);
        shoupeopleName = (TextView) view.findViewById(R.id.shoupeople_name);

        ll_hoistcase_state = (LinearLayout) view.findViewById(R.id.ll_hoistcase_state);
        stateHoistcase = (TextView) view.findViewById(R.id.hoistcase_state);
        ll_allmonitor_state =(LinearLayout) view.findViewById(R.id.ll_allmonitor_state);
        stateAllmonitor= (TextView) view.findViewById(R.id.allmonitor_state);
        frequency_state =(TextView) view.findViewById(R.id.frequency_state);
        ll_frequency_state=(LinearLayout) view.findViewById(R.id.ll_frequency_state);


        searchSblist= (TextView) view.findViewById(R.id.searchsblist);
        mearchSblist = (TextView) view.findViewById(R.id.mearchssblist);

        btn_searchsb= (Button) view.findViewById(R.id.btn_searchsb);
        btn_communication = (Button) view.findViewById(R.id.btn_communication);

        ll_insatllstate =(LinearLayout) view.findViewById(R.id.ll_insatllstate);
        stateInsatll= (TextView) view.findViewById(R.id.insatllstate);
        qzj_name = (TextView) view.findViewById(R.id.qzj_name);
        qzj_drivername  = (TextView) view.findViewById(R.id.qzj_drivername);
        ll_install_location = (LinearLayout) view.findViewById(R.id.ll_install_location);
        installLocation = (TextView) view.findViewById(R.id.install_location);
        goushenkey = (TextView) view.findViewById(R.id.edit_goushenkey);
        calibrationkey_roll = (TextView) view.findViewById(R.id.calibrationkey_roll);
        calibrationkey_pitch = (TextView) view.findViewById(R.id.calibrationkey_pitch);
        btn_commucationCeShi = (Button) view.findViewById(R.id.btn_communicationceshi);
        btn_datacalibra = (Button) view.findViewById(R.id.btn_datajioazhun);

        ll_two_stateInsatll=(LinearLayout) view.findViewById(R.id.ll_two_stateInsatll);
        two_stateInsatll= (TextView) view.findViewById(R.id.two_stateInsatll);
        two_qzj_name = (TextView) view.findViewById(R.id.two_qzj_name);
        two_qzj_drivername  = (TextView) view.findViewById(R.id.two_qzj_drivername);
        ll_two_installLocation =(LinearLayout) view.findViewById(R.id.ll_two_installLocation);
        two_installLocation = (TextView) view.findViewById(R.id.two_installLocation);
        two_goushenkey = (TextView) view.findViewById(R.id.two_goushenkey);
        two_calibrationkey_roll = (TextView) view.findViewById(R.id.two_calibrationkey_roll);
        two_calibrationkey_pitch = (TextView) view.findViewById(R.id.two_calibrationkey_pitch);
        btn_two_commucationCeShi = (Button) view.findViewById(R.id.btn_two_communicationceshi);
        btn_two_datacalibra = (Button) view.findViewById(R.id.btn_two_datajioazhun);

        ll_pg_name = (LinearLayout) view.findViewById(R.id.ll_pg_name);
        ll_dopeople_name = (LinearLayout)view.findViewById(R.id.ll_dopeople_name);
        ll_shoupeople_name = (LinearLayout)view.findViewById(R.id.ll_shoupeople_name);
        ll_qzj_name =(LinearLayout) view.findViewById(R.id.ll_qzj_name);
        ll_qzj_drivername =(LinearLayout) view.findViewById(R.id.ll_qzj_drivername);
        ll_edit_goushenkey = (LinearLayout)view.findViewById(R.id.ll_edit_goushenkey);

        ll_two_qzj_name=(LinearLayout)view.findViewById(R.id.ll_two_qzj_name);
        ll_two_qzj_drivername =(LinearLayout) view.findViewById(R.id.ll_two_qzj_drivername);
        ll_two_goushenkey = (LinearLayout)view.findViewById(R.id.ll_two_goushenkey);

        ll_files_listview=(LinearLayout) view.findViewById(R.id.ll_files_listview);
        ll_searchsblist= (LinearLayout) view.findViewById(R.id.ll_searchsblist);
        ll_mearchssblist = (LinearLayout) view.findViewById(R.id.ll_mearchssblist);

        ll_saveFile = (LinearLayout) view.findViewById(R.id.ll_saveFile);
        ll_alter_shouquanpass = (LinearLayout) view.findViewById(R.id.ll_alter_shouquanpass);
        ll_alter_superpass = (LinearLayout) view.findViewById(R.id.ll_alter_superpass);




        doCheck();
    }

    /**
     * 检查是否授权
     */
    public void doCheck() {
        if(stateShouquan.getText().equals("未授权")){
            filesListview.setFocusable(false);
            searchSblist.setFocusable(false);
            mearchSblist.setFocusable(false);
            ll_hoistcase_state.setEnabled(false);
            ll_insatllstate.setEnabled(false);
            ll_allmonitor_state.setEnabled(false);
            ll_frequency_state.setEnabled(false);
            ll_install_location.setEnabled(false);
            ll_two_stateInsatll.setEnabled(false);
            ll_two_installLocation.setEnabled(false);
            ll_searchsblist.setEnabled(false);
            ll_mearchssblist.setEnabled(false);
            btn_searchsb.setEnabled(false);
            btn_communication.setEnabled(false);
            btn_commucationCeShi.setEnabled(false);
            btn_datacalibra.setEnabled(false);
            btn_two_commucationCeShi.setEnabled(false);
            btn_two_datacalibra.setEnabled(false);

            ll_pg_name.setEnabled(false);
            ll_dopeople_name.setEnabled(false);
            ll_shoupeople_name.setEnabled(false);
            ll_qzj_name.setEnabled(false);
            ll_qzj_drivername.setEnabled(false);
            ll_edit_goushenkey.setEnabled(false);

            ll_two_qzj_name.setEnabled(false);
            ll_two_qzj_drivername.setEnabled(false);
            ll_two_goushenkey.setEnabled(false);

            ll_files_listview.setEnabled(false);
            ll_searchsblist.setEnabled(false);
            ll_mearchssblist.setEnabled(false);
            ll_saveFile.setEnabled(false);

        }else {
            if(stateInsatll.getText().equals("安装")){
                doCheck_sb_one();
            }
            if(two_stateInsatll.getText().equals("安装")){
                doCheck_sb_two();
            }
            filesListview.setFocusable(true);

            searchSblist.setFocusable(true);
            mearchSblist.setFocusable(true);

            ll_hoistcase_state.setEnabled(true);
            ll_insatllstate.setEnabled(true);
            ll_allmonitor_state.setEnabled(true);
            ll_frequency_state.setEnabled(true);

            ll_two_stateInsatll.setEnabled(true);

            btn_searchsb.setEnabled(true);
            btn_communication.setEnabled(true);


            ll_pg_name.setEnabled(true);
            ll_dopeople_name.setEnabled(true);
            ll_shoupeople_name.setEnabled(true);


            ll_searchsblist.setEnabled(false);
            ll_mearchssblist.setEnabled(false);

            ll_files_listview.setEnabled(true);
            ll_searchsblist.setEnabled(true);
            ll_mearchssblist.setEnabled(true);
            ll_saveFile.setEnabled(true);
        }
    }

    private void initBluetooth(){
        mDeviceName = ((MainActivity)getActivity()).getBluetoothDeviceName();
        mDeviceAddress = ((MainActivity)getActivity()).getBluetoothDeviceAddress();
        Intent gattServiceIntent = new Intent(this.getActivity(), BluetoothLeService.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_alter_superpass:  set_ll_alter_superpass();  break;
            case R.id.ll_alter_shouquanpass:  set_ll_alter_shouquanpass();  break;
            case R.id.ll_frequency_state:  set_ll_frequency_state();  break;
            case R.id.ll_files_listview:  set_ll_files_listview();  break;
            case R.id.ll_two_installLocation:  set_ll_two_installLocation();  break;
            case R.id.ll_two_stateInsatll:  set_ll_two_stateInsatll();  break;
            case R.id.ll_install_location:  set_ll_install_location();  break;
            case R.id.ll_insatllstate:  set_ll_insatllstate();  break;
            case R.id.ll_hoistcase_state:  set_ll_hoistcase_state();  break;
            case R.id.btn_shouquan :  set_btn_shouquan();  break;
            case R.id.ll_pg_name:  set_ll_pg_name();  break;
            case R.id.ll_dopeople_name:  set_ll_dopeople_name();  break;
            case R.id.ll_shoupeople_name:  set_ll_shoupeople_name();  break;
            case R.id.ll_qzj_name:  set_ll_qzj_name();  break;
            case R.id.ll_qzj_drivername:  set_ll_qzj_drivername();  break;
            case R.id.ll_edit_goushenkey:  set_ll_edit_goushenkey();  break;
            case R.id.ll_two_qzj_name:  set_ll_two_qzj_name();  break;
            case R.id.ll_two_qzj_drivername: set_ll_two_qzj_drivername(); break;
            case R.id.ll_two_goushenkey:  set_ll_two_goushenkey(); break;
            case R.id.btn_searchsb:
                System.out.println(btn_searchsb.getText());
                if (btn_searchsb.getText().equals("搜索蓝牙设备")) {
                    mBluetoothUtils.scanBleDevice(true);
                } else if (btn_searchsb.getText().equals("断开连接")) {
                    mBluetoothUtils.checkGattConnected();
                    btn_searchsb.setText(R.string.scan_ble_device);
                }
                break;
            case R.id.ll_saveFile:
                try {
                  saveFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_searchsblist:
                //set_ll_searchsblist();
                break;
            case R.id.ll_mearchssblist:
                //set_ll_mearchssblist();
                break;
            case R.id.btn_communication:
                if(mConnected) {
                    b_Eq1_Adjusting = false;
                    b_Eq2_Adjusting = false;
                    b_Eq1_communication = false;
                    b_Eq2_communication = false;
                    mBluetoothLeService.writeData((Constant.CMD_EQ1).getBytes(), false);
                }else{
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_communicationceshi:
                if(mConnected) {
                    b_Eq1_Adjusting = false;
                    b_Eq2_Adjusting = false;
                    b_Eq1_communication = true;
                    b_Eq2_communication = false;
                    mBluetoothLeService.writeData((Constant.CMD_EQ1).getBytes(), false);
                }else{
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_two_communicationceshi:
                if(mConnected) {
                    b_Eq1_Adjusting = false;
                    b_Eq2_Adjusting = false;
                    b_Eq1_communication = false;
                    b_Eq2_communication = true;
                    mBluetoothLeService.writeData((Constant.CMD_EQ2).getBytes(), false);
                }else{
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_datajioazhun:
                if(mConnected) {
                    b_Eq1_Adjusting = true;
                    b_Eq2_Adjusting = false;
                    mBluetoothLeService.writeData((Constant.CMD_EQ1).getBytes(), false);
                }else{
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_two_datajioazhun:
                if(mConnected) {
                    b_Eq2_Adjusting = true;
                    b_Eq1_Adjusting = false;
                    mBluetoothLeService.writeData((Constant.CMD_EQ2).getBytes(), false);
                }else{
                    Toast.makeText(getActivity(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.i(TAG,"=========No Operation!!!");
        }
    }
    private void checkCommunication(byte [] buf) throws UnsupportedEncodingException {
        String s = mBluetoothUtils.bytesToString(buf);
        Log.i(TAG,s);
        DevSingleData dev = new DevSingleData(buf);


        if(b_Eq1_communication || b_Eq2_communication) {
            if (dev.getCRC_Cal_value() == dev.getCRC_value()) {
                Toast.makeText(getActivity(), "监测设备通信正常", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "DEV DATA:" + "Roll:" + dev.getRoll() + "," + "Pitch:" + dev.getPitch() + ","
                        + "CRC_value:" + Integer.toHexString(dev.getCRC_value() & 0xffff) + "," + "Bat Mon:" + dev.getBattery_Monitor() + "," + "Bat Con" + dev.getBattery_Controller());
            }else {
                Toast.makeText(getActivity(), "请确认开启设备", Toast.LENGTH_SHORT).show();
            }
        }else if( dev.getBattery_Controller() > 0.0 ){
            Toast.makeText(getActivity(),"通信正常",Toast.LENGTH_SHORT).show();
            Log.i(TAG,"DEV Battery:" + dev.getBattery_Controller());
        }
    }

    private void dataAdjusting(byte [] buf) throws UnsupportedEncodingException {
        String s = mBluetoothUtils.bytesToString(buf);
        Log.i(TAG,s);
        if(b_Eq1_Adjusting){
            DevSingleData dev1 = new DevSingleData(buf);
            calibrationkey_roll.setText( String.format( "%.2f",dev1.getRoll() ) );
            calibrationkey_pitch.setText(String.format( "%.2f",dev1.getPitch() ) );
            Log.i(TAG,"DEV1 DATA:" + "Roll:" +dev1.getRoll() + ","
                    + "Pitch:" + dev1.getPitch() + "," + "CRC:" + Integer.toHexString( dev1.getCRC_value() & 0xffff) + "," + "Bat Mon:" + dev1.getBattery_Monitor());
        }
        if(b_Eq2_Adjusting){
            DevSingleData dev2 = new DevSingleData(buf);
            two_calibrationkey_roll.setText( String.format( "%.2f",dev2.getRoll() ) );
            two_calibrationkey_pitch.setText( String.format( "%.2f",dev2.getPitch() ) );
            Log.i(TAG,"DEV2 DATA:" + "Roll:" + dev2.getRoll() + ","
                    + "Pitch:" + dev2.getPitch() + "," + "CRC:" + Integer.toHexString(dev2.getCRC_value() & 0xffff) + "," + "Bat Mon:" + dev2.getBattery_Monitor());
        }
    }


    /**
     * 处理从Service发来的消息的Handler
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothUtils.ENABLE_BLUETOOTH:
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1);
                    break;

                case BluetoothUtils.DEVICE_SCAN_STARTED:
                    btn_searchsb.setText(R.string.scanning);
                    break;

                case BluetoothUtils.DEVICE_SCAN_STOPPED:
                    btn_searchsb.setText(R.string.scan_ble_device);
                    break;
                case BluetoothUtils.DEVICE_SELECTED:
                    ((MainActivity)getActivity()).setBluetoothDeviceName(mBluetoothUtils.getBluetoothDeviceName());
                    ((MainActivity)getActivity()).setBluetoothDeviceAddress(mBluetoothUtils.getBluetoothDeviceAddress());
                    searchSblist.setText(mBluetoothUtils.getBluetoothDeviceName());
                    initBluetooth();
                    break;
                case BluetoothUtils.DEVICE_SCAN_COMPLETED:
                    mBluetoothUtils.creatDeviceListDialog();
                    btn_searchsb.setText(R.string.scan_ble_device);
                    break;

                case BluetoothUtils.DEVICE_CONNECTED:
                    //mDeviceName.setText(mBluetoothUtils.getDeviceName());
                    Log.i(TAG,"========Connected!");
                    break;

                case BluetoothUtils.DATA_SENDED:
                    Log.i(TAG,"========WRITE!!!!!");
//                    if (mDataSendFormat.getText().equals("Hex")) {
//                        mEditBox.setText(getFormattedString());
//                    }
//                    mSendBytes.setText(sendBytes + " ");
                    break;

                case BluetoothUtils.DATA_READED:
//                    try {
//                        displayData();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i(TAG,"========READ!!!!!");
//                    Log.i(TAG,((MainActivity)getActivity()).getBluetoothDeviceName()+((MainActivity)getActivity()).getBluetoothDeviceAddress());
                    break;

                case BluetoothUtils.CHARACTERISTIC_ACCESSIBLE:
                    btn_searchsb.setText(R.string.disconnect);
//	                    isTimerEnable = true;
//	                    setTimer(isTimerEnable);
                    break;

	                /*case DATA_REFRESH:
	                    mRecvBytes.setText(recvBytes + " ");
	                    mSendBytes.setText(sendBytes + " ");
	                    break;*/

                default:
                    break;
            }
        }
//        @Override
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case Constant.MSG_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // 创建接收的信息的字符串
//                    String readMessage = new String(readBuf, 0, msg.arg1);//用系统默认的字符集把字节数组的一个序列转换为字符串
//                    Toast.makeText(getActivity(),
//                            connectedNameStr + ":  " + readMessage,
//                            Toast.LENGTH_LONG).show();//显示从哪个设备接收的什么样的字符串
//                   if (readMessage!=null && !readMessage.equals("")){
//                    analysisCommand(readMessage);}
//                    break;
//                case Constant.MSG_DEVICE_NAME:
//                    // 获取已连接的设备名称，并弹出提示信息
//                    connectedNameStr = msg.getData().getString(
//                            Constant.DEVICE_NAME);
//                    Toast.makeText(getActivity(),
//                            "已连接到 " + connectedNameStr, Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//            }
//        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.i(TAG,"Setting Resumed.");
        getActivity().registerReceiver(dataReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        mBluetoothUtils.checkBluetoothEnabled();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"Setting Paused.");
        getActivity().unregisterReceiver(dataReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBluetoothUtils.checkDeviceScanning();
        mBluetoothUtils.checkGattConnected();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
        mBluetoothLeService = null;
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
  获取授权密码
   */
    public String getShouquanPass(){
        SharedPreferences perPreferences = getActivity().getSharedPreferences("configurationFile", getActivity().MODE_PRIVATE);
        String shouquanPass = perPreferences.getString("shouquanPass", null);
        return shouquanPass;
    }

    /**
  修改超级管理密码
   */
    public void alertSuperPass(String yuanpass,String newpass,String newpasssure){
        if(toMD5(yuanpass).equals(getSuperPass())){
            if(newpass.equals(newpasssure)){
                SharedPreferences perPreferences = getActivity().getSharedPreferences("configurationFile", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = perPreferences.edit();
                editor.putString("superPass",toMD5(newpass));
                editor.commit();
                Toast.makeText(getActivity(),"修改成功！请牢记新密码！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"两次输入的新密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"请输入正确的超级管理密码",Toast.LENGTH_SHORT).show();
        }

    }


    /**
  修改授权密码
   */
    public void alertShouquanPass(String yuanpass,String newpass,String newpasssure){
        if(toMD5(yuanpass).equals(getShouquanPass())){
            if(newpass.equals(newpasssure)){
                SharedPreferences perPreferences = getActivity().getSharedPreferences("configurationFile", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = perPreferences.edit();
                editor.putString("shouquanPass",toMD5(newpass));
                editor.commit();
                Toast.makeText(getActivity(),"修改成功！请牢记新密码！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"两次输入的新密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"请输入正确的授权密码",Toast.LENGTH_SHORT).show();
        }
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
   读取配置文件到配置文件javabean的实体类中
    */
    public void matchParameter(String loadFileName){
        System.out.println("您选中的文件的名字是"+loadFileName);
        String name = loadFileName .substring(0, loadFileName.lastIndexOf("."));
        SharedPreferences loadFile =getActivity().getSharedPreferences(name, getActivity().MODE_PRIVATE);
        configurationparameter.setPjname(loadFile.getString("pjname", ""));
//        System.out.println("这是匹配后文件的项目的名称" + loadFile.getString("pjname",""));
        configurationparameter.setDopeople(loadFile.getString("dopeople", ""));
        configurationparameter.setShoupeople(loadFile.getString("shoupeople", ""));
        configurationparameter.setDoublehoist(loadFile.getString("doublehoist", ""));
        configurationparameter.setAllmonitor(loadFile.getString("allmonitor", ""));
        configurationparameter.setFrequency(loadFile.getString("frequency", ""));
        configurationparameter.setQzjname(loadFile.getString("qzjname", ""));
        configurationparameter.setQzjdriver(loadFile.getString("qzjdriver", ""));
        configurationparameter.setQzjnametwo(loadFile.getString("qzjnametwo", ""));
        configurationparameter.setQzjdrivertwo(loadFile.getString("qzjdrivertwo", ""));
        configurationparameter.setGoushenkey(loadFile.getString("goushenkey", ""));
        configurationparameter.setGoushenkeytwo(loadFile.getString("goushenkeytwo", ""));
        configurationparameter.setInstallLocation(loadFile.getString("installLocation", ""));
        configurationparameter.setInstallLocationtwo(loadFile.getString("installLocationtwo", ""));
        configurationparameter.setStateInsatll(loadFile.getString("stateInsatll", "未安装"));
        configurationparameter.setTwo_stateInsatll(loadFile.getString("two_stateInsatll", "未安装"));
        configurationparameter.setCalibrationkeyrollkey(loadFile.getString("calibrationkeyrollkey", ""));
        configurationparameter.setCalibrationkeypitchkey(loadFile.getString("calibrationkeypitchkey", ""));
        configurationparameter.setTwocalibrationkeyrollkey(loadFile.getString("twocalibrationkeyrollkey",""));
        configurationparameter.setTwocalibrationkeypitchkey(loadFile.getString("twocalibrationkeypitchkey", ""));

        showKeys();
    }


    /**
       读取配置文件展示到页面上
        */
    public void showKeys(){

        pg_name.setText(configurationparameter.getPjname());           //抬吊作业项目名称
        dopeopleName.setText(configurationparameter.getDopeople());      //执行人名字
        shoupeopleName.setText(configurationparameter.getShoupeople());    //授权人名字
        stateHoistcase.setText(configurationparameter.getDoublehoist());     //抬吊工况状态

        qzj_name.setText(configurationparameter.getQzjname());               //起重机1名字
        qzj_drivername.setText(configurationparameter.getQzjdriver());       //起重机司机1名字
        goushenkey.setText(configurationparameter.getGoushenkey());            //监测设备1钩绳值

         stateAllmonitor.setText(configurationparameter.getAllmonitor());    //安全检测类型
          frequency_state.setText(configurationparameter.getFrequency());  //实时监测频率
         installLocation.setText(configurationparameter.getInstallLocation());       //安装位置1
        two_qzj_name.setText(configurationparameter.getQzjnametwo());               //起重机2名字
         two_qzj_drivername.setText(configurationparameter.getQzjdrivertwo());       //起重机司机2名字
         two_installLocation.setText(configurationparameter.getInstallLocationtwo());       //安装位置2
        two_goushenkey.setText(configurationparameter.getGoushenkeytwo());            //监测设备2钩绳值

        stateInsatll.setText(configurationparameter.getStateInsatll()); //监测设备1的安装状态
        two_stateInsatll.setText(configurationparameter.getTwo_stateInsatll()); //监测设备2的安装状态
    }

    /**
   获取现有的配置文件目录
    */
    public String[] getFiles(){
        String[] moren= new String[1];
        moren[0]="无文件";
        File file = new File(shared_path);
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
   保存配置文件
    */
    public void saveFile() throws ClassNotFoundException {
        if(stateHoistcase.getText().equals("双机抬吊")){
            if(stateInsatll.getText().equals("未安装") || two_stateInsatll.getText().equals("未安装")){
                Toast.makeText(getActivity(),"监测设备均未安装不可保存！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(stateHoistcase.getText().equals("单机抬吊")){
            if(stateInsatll.getText().equals("未安装") && two_stateInsatll.getText().equals("未安装")){
                Toast.makeText(getActivity(),"监测设备必须安装一个！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(stateHoistcase.getText().equals("单机抬吊")){
            if(stateInsatll.getText().equals("安装") && two_stateInsatll.getText().equals("安装")){
                Toast.makeText(getActivity(),"监测设备只能安装一个！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        final String[] fileName = {null};
        StringBuffer sb_time=new StringBuffer();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
        sb_time.append(df.format(new Date()).toString());
        fileName[0] =sb_time.toString();
        getProp();

        final int[] flag_empty = {0};
        final EditText etLogin_savefilename  = new EditText(getActivity());
        etLogin_savefilename.setText(fileName[0]);
        etLogin_savefilename.setSelection(fileName[0].length());
        final AlertDialog ad_savefilename = new AlertDialog.Builder(getActivity())
                .setMessage("文件名")
                .setView(etLogin_savefilename)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fileName[0] = etLogin_savefilename.getText().toString();
                                SharedPreferences confiFile = getActivity().getSharedPreferences(fileName[0], getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor confiEdit = confiFile.edit();
//                                System.out.println("属性的长度"+propertys.length);
                                for (int i = 0; i < propertys.length; i++) {
                                    if (!(propertyszhi[i].equals(""))) {
                                        confiEdit.putString(propertys[i], propertyszhi[i]);
                                    } else if(i==9||i==10){
                                        confiEdit.putString(propertys[i], propertyszhi[i]);
                                    }else{
                                        flag_empty[0] = 1;
                                        confiEdit.clear();
                                    }
                                }
                                if (flag_empty[0] == 0) {
                                    confiEdit.commit();
                                    filesListview.setText(fileName[0]+".xml");
                                    Util.file_now_name=fileName[0]+".xml";
                                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "请把参数填写完整!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();

        ad_savefilename.show();
    }



    /**
   获取配置文件javabean类的属性，即配置文件的字段的获取
    */
    public void getProp() throws ClassNotFoundException {
        getPropZhi();
        Class cl = Class.forName("cn.edu.nuc.csce.dcldims.entity.Configurationparameter");
        Field[] f = cl.getDeclaredFields();
        propertys = new String[f.length];
//        System.out.println("看看你这长度是多少？"+propertys.length);
        for(int i =0 ;i<f.length;i++){
            propertys[i]=f[i].getName();
         //  System.out.println("读取的顺序是："+propertys[i]);
        }
    }

    public void saveFile_tishiDialog(String mes_tishi){
        View savefile_tishi_view= LayoutInflater.from(getActivity()).inflate(R.layout.savefile_tishidialog,null);
        TextView tv = (TextView) savefile_tishi_view.findViewById(R.id.save_file_tishi_mes);
        tv.setText(mes_tishi);
        final AlertDialog ad_savefile_tishi = new AlertDialog.Builder(getActivity())
                .setMessage("新建文件，保存参数设置")
                .setView(savefile_tishi_view)
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();

        ad_savefile_tishi.show();
    }

    /**
   获取当前设置的各个属性值
    */
    public void getPropZhi(){
        configurationparameter.setPjname(pg_name.getText().toString());
        configurationparameter.setDopeople(dopeopleName.getText().toString());
        configurationparameter.setShoupeople(shoupeopleName.getText().toString());
        configurationparameter.setDoublehoist(stateHoistcase.getText().toString());
        configurationparameter.setAllmonitor(stateAllmonitor.getText().toString());
        configurationparameter.setFrequency(frequency_state.getText().toString());
        configurationparameter.setQzjname(qzj_name.getText().toString());
        configurationparameter.setQzjdriver(qzj_drivername.getText().toString());
        configurationparameter.setQzjnametwo(two_qzj_name.getText().toString());
        configurationparameter.setQzjdrivertwo(two_qzj_drivername.getText().toString());
        configurationparameter.setGoushenkey(goushenkey.getText().toString());
        configurationparameter.setGoushenkeytwo(two_goushenkey.getText().toString());
        configurationparameter.setInstallLocation(installLocation.getText().toString());
        configurationparameter.setInstallLocationtwo(two_installLocation.getText().toString());
        configurationparameter.setStateInsatll(stateInsatll.getText().toString());
        configurationparameter.setTwo_stateInsatll(two_stateInsatll.getText().toString());
        configurationparameter.setCalibrationkeyrollkey(calibrationkey_roll.getText().toString());
        configurationparameter.setCalibrationkeypitchkey(calibrationkey_pitch.getText().toString());
        configurationparameter.setTwocalibrationkeyrollkey(two_calibrationkey_roll.getText().toString());
        configurationparameter.setTwocalibrationkeypitchkey(two_calibrationkey_pitch.getText().toString());

        if(configurationparameter.getDoublehoist().equals("")){
           Toast.makeText(getActivity(),"请选择抬吊工况",Toast.LENGTH_SHORT).show();
            return;
        }
        if(configurationparameter.getDoublehoist().equals("双机抬吊")){
            if(configurationparameter.getQzjname().equals("")){
                String tishi_mes ="监测设备1-起重机名字未填写";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备1-起重机名字未填写",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getInstallLocation().equals("")){
                String tishi_mes ="监测设备1-未安装";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备1-未安装",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getGoushenkey().equals("")){
                String tishi_mes ="监测设备1-钩绳值未填写";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备1-钩绳值未填写",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getCalibrationkeyrollkey().equals("")){
                String tishi_mes ="监测设备1-校准数据不能为空";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备1-校准数据不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getCalibrationkeypitchkey().equals("")){
                String tishi_mes ="监测设备1-校准数据不能为空";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备1-校准数据不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getQzjnametwo().equals("")){
                String tishi_mes ="监测设备2-起重机名字未填写";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备2-起重机名字未填写",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getInstallLocationtwo().equals("")){
                String tishi_mes ="监测设备2-未安装";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备2-未安装",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getGoushenkeytwo().equals("")){
                String tishi_mes ="监测设备2-钩绳值未填写";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备2-钩绳值未填写",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getTwocalibrationkeyrollkey().equals("")){
                String tishi_mes ="监测设备2-校准数据不能为空";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备2-校准数据不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(configurationparameter.getTwocalibrationkeypitchkey().equals("")){
                String tishi_mes ="监测设备2-校准数据不能为空";
                saveFile_tishiDialog(tishi_mes);
                Toast.makeText(getActivity(),"监测设备2-校准数据不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(configurationparameter.getDoublehoist().equals("单机抬吊")){

            if(configurationparameter.getStateInsatll().equals("安装")){
                if(configurationparameter.getQzjname().equals("")){
                    String tishi_mes ="监测设备1-起重机名字未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备1-起重机名字未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getInstallLocation().equals("")){
                    String tishi_mes ="监测设备1-安装位置未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备1-安装位置未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getGoushenkey().equals("")){
                    String tishi_mes ="监测设备1-钩绳值未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备1-钩绳值未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getCalibrationkeyrollkey().equals("")){
                    String tishi_mes ="监测设备1-校准数据不能为空";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备1-校准数据不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getCalibrationkeypitchkey().equals("")){
                    String tishi_mes ="监测设备1-校准数据不能为空";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备1-校准数据不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(configurationparameter.getTwo_stateInsatll().equals("安装")){
                if(configurationparameter.getQzjnametwo().equals("")){
                    String tishi_mes ="监测设备2-起重机名字未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备2-起重机名字未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getInstallLocationtwo().equals("")){
                    String tishi_mes ="监测设备2-安装位置未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备2-安装位置未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getGoushenkeytwo().equals("")){
                    String tishi_mes ="监测设备2-钩绳值未填写";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备2-钩绳值未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getTwocalibrationkeyrollkey().equals("")){
                    String tishi_mes ="监测设备2-校准数据不能为空";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备2-校准数据不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(configurationparameter.getTwocalibrationkeypitchkey().equals("")){
                    String tishi_mes ="监测设备2-校准数据不能为空";
                    saveFile_tishiDialog(tishi_mes);
                    Toast.makeText(getActivity(),"监测设备2-校准数据不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        propertyszhi = new String[20];
        propertyszhi[0]=configurationparameter.getAllmonitor();
        propertyszhi[1]=configurationparameter.getCalibrationkeypitchkey();
        propertyszhi[2]=configurationparameter.getCalibrationkeyrollkey();
        propertyszhi[3] = configurationparameter.getDopeople();
        propertyszhi[4] = configurationparameter.getDoublehoist();
        propertyszhi[5] = configurationparameter.getFrequency();
        propertyszhi[6] = configurationparameter.getGoushenkey();
        propertyszhi[7] = configurationparameter.getGoushenkeytwo();
        propertyszhi[8] = configurationparameter.getInstallLocation();
        propertyszhi[9] = configurationparameter.getInstallLocationtwo();
        propertyszhi[10] = configurationparameter.getPjname();
        propertyszhi[11] = configurationparameter.getQzjdriver();
        propertyszhi[12] = configurationparameter.getQzjdrivertwo();
        propertyszhi[13] = configurationparameter.getQzjname();
        propertyszhi[14] = configurationparameter.getQzjnametwo();
        propertyszhi[15] = configurationparameter.getShoupeople();
        propertyszhi[16] = configurationparameter.getStateInsatll();
        propertyszhi[17] = configurationparameter.getTwo_stateInsatll();
        propertyszhi[18] = configurationparameter.getTwocalibrationkeypitchkey();
        propertyszhi[19] = configurationparameter.getTwocalibrationkeyrollkey();
    }


    /**
    删除配置文件
     */
    public void deleteConfigFile(String deleteFileName){
        File file = new File(shared_path,deleteFileName);
        if(file.exists()){
            file.delete();
            Toast.makeText(getActivity(),"配置文件删除成功！",Toast.LENGTH_SHORT).show();
        }
    }


    /**
  点击事件：
  修改超级管理密码
*/
    public void set_ll_alter_superpass(){
        final View view__alter_superpass=LayoutInflater.from(getActivity()).inflate(R.layout.alter_pass,null);
        AlertDialog ad_alter_superpass = new AlertDialog.Builder(getActivity())
                .setMessage("修改执行人参数设置授权密码")
                .setView(view__alter_superpass)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(getActivity(), etLogin_alter_shouquanpass.getText().toString(), Toast.LENGTH_SHORT).show();
                                EditText editText_super_yuanpass = (EditText)view__alter_superpass.findViewById(R.id.yuanpass);
                                EditText editText_super_newpass = (EditText)view__alter_superpass.findViewById(R.id.newpass);
                                EditText editText_super_newpasssure = (EditText)view__alter_superpass.findViewById(R.id.newpasstwo);
                                alertSuperPass(editText_super_yuanpass.getText().toString(), editText_super_newpass.getText().toString(), editText_super_newpasssure.getText().toString());
                            }
                        })
                .setNegativeButton("返回",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                            }
                        }).create();

        ad_alter_superpass.show();
    }

    /**
   点击事件：
   修改授权密码
*/
    public void set_ll_alter_shouquanpass(){

        final View view_alter_shouquanpass=LayoutInflater.from(getActivity()).inflate(R.layout.alter_pass,null);
        AlertDialog ad_alter_shouquanpass = new AlertDialog.Builder(getActivity())
                .setMessage("修改执行人参数设置授权密码")
                .setView(view_alter_shouquanpass)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(getActivity(), etLogin_alter_shouquanpass.getText().toString(), Toast.LENGTH_SHORT).show();
                                EditText editText_yuanpass = (EditText) view_alter_shouquanpass.findViewById(R.id.yuanpass);
                                EditText editText_newpass = (EditText) view_alter_shouquanpass.findViewById(R.id.newpass);
                                EditText editText_newpasssure = (EditText) view_alter_shouquanpass.findViewById(R.id.newpasstwo);
                                alertShouquanPass(editText_yuanpass.getText().toString(), editText_newpass.getText().toString(), editText_newpasssure.getText().toString());
                            }
                        })
                .setNegativeButton("返回",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                            }
                        }).create();

        ad_alter_shouquanpass.show();
    }
    /**
   点击事件：
   监测频率
*/
    public void set_ll_frequency_state(){
        int flag_frequency_state=0;
        if(frequency_state.getText().toString().equals("1s")){
            flag_frequency_state=1;
        }
        final String items_frequency_state[]={"0.5s","1s"};
        AlertDialog ad_frequency_state = new  AlertDialog.Builder(getActivity())
                .setTitle("实时监测频率")
                .setSingleChoiceItems(items_frequency_state, flag_frequency_state, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        frequency_state.setText(items_frequency_state[which]);
//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_frequency_state.show();
    }

    /**
点击事件：
点开文件列表
*/
    public void set_ll_files_listview(){
        final String[] filesNames=getFiles();
        if(filesNames[0].equals("无文件")){
            return ;
        }
        int flag_index=0;
        for(int i=0;i<filesNames.length;i++){
            if(filesListview.getText().toString().equals(filesNames[i])){
                flag_index=i;
            }
        }
        final int[] finalFlag_index = {flag_index};
        AlertDialog ad_files_listview = new  AlertDialog.Builder(getActivity())
                .setTitle("配置文件列表")
                .setSingleChoiceItems(filesNames, flag_index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalFlag_index[0] =which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        filesListview.setText(filesNames[finalFlag_index[0]]);
                        Util.file_now_name=filesNames[finalFlag_index[0]];
                        matchParameter(filesNames[finalFlag_index[0]]);
                    }
                })
                .setNeutralButton("取消",new DialogInterface.OnClickListener() {
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
点击事件：
监测设备2安装位置
*/
    public void set_ll_two_installLocation(){
        int flag__two_installLocation=0;
        if(two_installLocation.getText().toString().equals("司机左手方向")){
            flag__two_installLocation=1;
        }
        final String items_two_installLocation[]={"司机右手方向","司机左手方向"};
        AlertDialog ad_two_installLocation = new  AlertDialog.Builder(getActivity())
                .setTitle("监测设备2--安装位置")
                .setSingleChoiceItems(items_two_installLocation, flag__two_installLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        two_installLocation.setText(items_two_installLocation[which]);
//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_two_installLocation.show();
    }
    /**
      点击事件：
      监测设备2安装状态
    */
    public void set_ll_two_stateInsatll(){
        int flag_two_stateInsatll=0;
        if(two_stateInsatll.getText().toString().equals("未安装")){
            flag_two_stateInsatll=1;
        }
        final String items_two_stateInsatll[]={"安装","未安装"};
        AlertDialog ad_two_stateInsatll = new  AlertDialog.Builder(getActivity())
                .setTitle("监测设备2")
                .setSingleChoiceItems(items_two_stateInsatll, flag_two_stateInsatll, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (stateHoistcase.getText().toString().equals("双机抬吊")) {
                            two_stateInsatll.setText(items_two_stateInsatll[which]);
                                doCheck_sb_two();
                        }else {
                            if (stateHoistcase.getText().toString().equals("单机抬吊") && stateInsatll.getText().toString().equals("未安装")) {
                                two_stateInsatll.setText(items_two_stateInsatll[which]);
                                doCheck_sb_two();
                            } else {
                                two_stateInsatll.setText("未安装");
                                doCheck_sb_two();
                                Toast.makeText(getActivity(), "监测设备1已安装", Toast.LENGTH_SHORT).show();
                            }
                        }

//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_two_stateInsatll.show();
    }

    /**
 点击事件：
 监测设备1安装位置
*/
    public void set_ll_install_location(){
        int flag_install_location=0;
        if(installLocation.getText().toString().equals("司机左手方向")){
            flag_install_location=1;
        }
        final String items_install_location[]={"司机右手方向","司机左手方向"};
        AlertDialog ad_ll_install_location = new  AlertDialog.Builder(getActivity())
                .setTitle("监测设备1--安装位置")
                .setSingleChoiceItems(items_install_location, flag_install_location, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        installLocation.setText(items_install_location[which]);
//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_ll_install_location.show();

    }
    /**
  点击事件：
  监测设备1安装状态
*/
    public void set_ll_insatllstate(){

        int flag_installstate=0;
        if(stateInsatll.getText().toString().equals("未安装")){
            flag_installstate=1;
        }
        final String items_installstate[]={"安装","未安装"};
        AlertDialog ad_ll_insatllstate = new  AlertDialog.Builder(getActivity())
                .setTitle("监测设备1")
                .setSingleChoiceItems(items_installstate, flag_installstate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(stateHoistcase.getText().toString().equals("双机抬吊")) {
                            stateInsatll.setText(items_installstate[which]);
                                doCheck_sb_one();
                        }else {
                            if (stateHoistcase.getText().toString().equals("单机抬吊") && two_stateInsatll.getText().toString().equals("未安装")) {
                                stateInsatll.setText(items_installstate[which]);
                                doCheck_sb_one();
                            } else {
                                stateInsatll.setText("未安装");
                                doCheck_sb_one();
                                Toast.makeText(getActivity(), "监测设备2已安装", Toast.LENGTH_SHORT).show();
                            }
                        }

//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_ll_insatllstate.show();
    }
    /**
   点击事件：
   设置抬吊状态
*/
    public void set_ll_hoistcase_state(){
        int flag=0;
        if(stateHoistcase.getText().toString().equals("单机抬吊")){
            flag=1;
        }
        final String items[]={"双机抬吊","单机抬吊"};
        AlertDialog ad_hoistcase_state = new  AlertDialog.Builder(getActivity())
                .setTitle(R.string.hoistcase)
                .setSingleChoiceItems(items, flag, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stateHoistcase.setText(items[which]);
                        stateInsatll.setText("未安装");
                        two_stateInsatll.setText("未安装");
                        doCheck_sb_one();
                        doCheck_sb_two();
//                        Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        ad_hoistcase_state.show();
    }


          /**
           点击事件：
           授权
          */
          public void set_btn_shouquan(){
        final EditText etLogin_shouquan = new EditText(getActivity());
        etLogin_shouquan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog ad_shouquan = new AlertDialog.Builder(getActivity())
                .setMessage("输入参数设置授权密码")
                .setView(etLogin_shouquan)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (toMD5(etLogin_shouquan.getText().toString()).equals(getShouquanPass()) || toMD5(etLogin_shouquan.getText().toString()).equals(getSuperPass())) {
                                    if (toMD5(etLogin_shouquan.getText().toString()).equals(getSuperPass())) {
                                      MainActivity one= (MainActivity) getActivity(); one.setFlag_guanliyuan(1);
                                    }
                                    stateShouquan.setText(R.string.yishouquan);
                                    doCheck();
                                    Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    stateShouquan.setText(R.string.weishouquan);
                                    doCheck();
                                    Toast.makeText(getActivity(), "密码错误，授权失败", Toast.LENGTH_SHORT).show();
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
    /**
 点击事件：
 设置执行人的名字
 */
    public void set_ll_pg_name(){
        final EditText etLogin_pgname  = new EditText(getActivity());
        etLogin_pgname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        etLogin_pgname.setText(pg_name.getText().toString());
        etLogin_pgname.setSelection(pg_name.getText().toString().length());
        AlertDialog ad_pgname = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.pjname)
                .setView(etLogin_pgname)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pg_name.setText(etLogin_pgname.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_pgname.show();
    }


    /**
   点击事件：
   设置执行人的名字
   */
    public void set_ll_dopeople_name(){
        final EditText etLogin_dopeople_name = new EditText(getActivity());
        etLogin_dopeople_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        etLogin_dopeople_name.setText(dopeopleName.getText().toString());
        etLogin_dopeople_name.setSelection(dopeopleName.getText().toString().length());
        AlertDialog ad_dopeople_name = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dopeople)
                .setView(etLogin_dopeople_name)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dopeopleName.setText(etLogin_dopeople_name.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_dopeople_name.show();
    }


    /**
点击事件：
设置授权人的名字
*/
    public void set_ll_shoupeople_name(){
        final EditText etLogin_shoupeople_name = new EditText(getActivity());
        etLogin_shoupeople_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        etLogin_shoupeople_name.setText(shoupeopleName.getText().toString());
        etLogin_shoupeople_name.setSelection(shoupeopleName.getText().toString().length());
        AlertDialog ad_shoupeople_name = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.shoupeople)
                .setView(etLogin_shoupeople_name)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                shoupeopleName.setText(etLogin_shoupeople_name.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_shoupeople_name.show();
    }


    /**
点击事件：
设置监测设备1起重机的名字
 */
    public void set_ll_qzj_name(){

        final EditText etLogin_qzj_name = new EditText(getActivity());
        etLogin_qzj_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etLogin_qzj_name.setText(qzj_name.getText().toString());
        etLogin_qzj_name.setSelection(qzj_name.getText().toString().length());
        AlertDialog ad_qzj_name = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备1--起重机名称")
                .setView(etLogin_qzj_name)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                qzj_name.setText(etLogin_qzj_name.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_qzj_name.show();

    }

    /**
 点击事件：
 设置监测设备1起重机司机的名字
  */
    public void set_ll_qzj_drivername(){

        final EditText etLogin_qzj_drivername = new EditText(getActivity());
        etLogin_qzj_drivername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etLogin_qzj_drivername.setText(qzj_drivername.getText().toString());
        etLogin_qzj_drivername.setSelection(qzj_drivername.getText().toString().length());
        AlertDialog ad_qzj_drivername = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备1--起重机司机")
                .setView(etLogin_qzj_drivername)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                qzj_drivername.setText(etLogin_qzj_drivername.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_qzj_drivername.show();
    }



    /**
 点击事件：
 设置监测设备1钩绳的值
  */
    public void set_ll_edit_goushenkey(){
        final EditText etLogin_edit_goushenkey = new EditText(getActivity());
        etLogin_edit_goushenkey.setInputType(InputType.TYPE_CLASS_NUMBER);
        etLogin_edit_goushenkey.setText(goushenkey.getText().toString());
        etLogin_edit_goushenkey.setSelection(goushenkey.getText().toString().length());
        AlertDialog ad_edit_goushenkey = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备1--钩绳-铅垂线偏离夹角报警值（度）")
                .setView(etLogin_edit_goushenkey)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                if(Integer.parseInt(etLogin_edit_goushenkey.getText().toString())<=5) {
                                    goushenkey.setText(etLogin_edit_goushenkey.getText().toString());
                                }else {
                                    Toast.makeText(getActivity(),"输入的数值不合法",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_edit_goushenkey.show();
    }


    /**
 点击事件：
 设置监测设备2起重机的名字
  */
    public void set_ll_two_qzj_name(){
        final EditText etLogin_two_qzj_name = new EditText(getActivity());
        etLogin_two_qzj_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etLogin_two_qzj_name.setText(two_qzj_name.getText().toString());
        etLogin_two_qzj_name.setSelection(two_qzj_name.getText().toString().length());
        AlertDialog ad_two_qzj_name = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备2--起重机名称")
                .setView(etLogin_two_qzj_name)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                two_qzj_name.setText(etLogin_two_qzj_name.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                            }
                        }).create();

        ad_two_qzj_name.show();
    }


    /**
 点击事件：
 设置监测设备2起重机司机的名字
  */
    public void set_ll_two_qzj_drivername(){
        final EditText etLogin_two_qzj_drivername = new EditText(getActivity());
        etLogin_two_qzj_drivername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etLogin_two_qzj_drivername.setText(two_qzj_drivername.getText().toString());
        etLogin_two_qzj_drivername.setSelection(two_qzj_drivername.getText().toString().length());
        AlertDialog ad_two_qzj_drivername = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备2--起重机司机")
                .setView(etLogin_two_qzj_drivername)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                two_qzj_drivername.setText(etLogin_two_qzj_drivername.getText().toString());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        }).create();

        ad_two_qzj_drivername.show();
    }
    /**
  点击事件：
  设置监测设备2钩绳的值
   */
    public void set_ll_two_goushenkey(){
        final EditText etLogin_two_goushenkey = new EditText(getActivity());
        etLogin_two_goushenkey.setInputType(InputType.TYPE_CLASS_NUMBER);
        etLogin_two_goushenkey.setText(two_goushenkey.getText().toString());
        etLogin_two_goushenkey.setSelection(two_goushenkey.getText().toString().length());
        AlertDialog ad_two_goushenkey = new AlertDialog.Builder(getActivity())
                .setMessage("监测设备2--钩绳-铅垂线偏离夹角报警值（度）")
                .setView(etLogin_two_goushenkey)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                if(Integer.parseInt(etLogin_two_goushenkey.getText().toString())<=5) {
                                    two_goushenkey.setText(etLogin_two_goushenkey.getText().toString());
                                }else {
                                    Toast.makeText(getActivity(),"输入的数值不合法",Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                            }
                        }).create();

        ad_two_goushenkey.show();
    }

    /**
     * 监测设备1控制
     */
    private void doCheck_sb_one(){
        if(stateInsatll.getText().equals("安装")) {
            ll_qzj_name.setEnabled(true);
            ll_qzj_drivername.setEnabled(true);
            ll_edit_goushenkey.setEnabled(true);
            ll_install_location.setEnabled(true);
            btn_commucationCeShi.setEnabled(true);
            btn_datacalibra.setEnabled(true);
        }else{
            ll_qzj_name.setEnabled(false);
            ll_qzj_drivername.setEnabled(false);
            ll_edit_goushenkey.setEnabled(false);
            ll_install_location.setEnabled(false);
            btn_commucationCeShi.setEnabled(false);
            btn_datacalibra.setEnabled(false);
        }
    }

    /**
     * 监测设备2控制
     */
    private void doCheck_sb_two(){
        if(two_stateInsatll.getText().equals("安装")) {
            ll_two_qzj_name.setEnabled(true);
            ll_two_qzj_drivername.setEnabled(true);
            ll_two_goushenkey.setEnabled(true);
            ll_two_installLocation.setEnabled(true);
            btn_two_commucationCeShi.setEnabled(true);
            btn_two_datacalibra.setEnabled(true);
        }else{
            ll_two_qzj_name.setEnabled(false);
            ll_two_qzj_drivername.setEnabled(false);
            ll_two_goushenkey.setEnabled(false);
            ll_two_installLocation.setEnabled(false);
            btn_two_commucationCeShi.setEnabled(false);
            btn_two_datacalibra.setEnabled(false);
        }
    }
}
