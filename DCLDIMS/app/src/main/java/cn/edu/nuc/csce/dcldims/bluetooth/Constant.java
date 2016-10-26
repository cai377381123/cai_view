package cn.edu.nuc.csce.dcldims.bluetooth;
//用于管理公共常量的常量类
public class Constant {
    //////////////重写协议///////////////////
    public static final String CMD_EQ1 = "1";
    public static final String CMD_EQ2 = "2";
    public static final String CMD_EQ_ALL = "3";
    //////////////重写协议END////////////////
    // 由Service中的Handler发送的消息类型
    public static final int MSG_READ = 2;
    public static final int MSG_DEVICE_NAME = 4;

    // 从Service中的Handler发来的主键名
    public static final String DEVICE_NAME = "device_name";

    public static final int REQUEST_CODE=1;//requestCode标识

    //蓝牙通信的命令
    public static final String COMMAND_HEAD="$";
    public static final String COMMAND_RESULT="*";
    public static final String  COMMAND_COMMUNICATION="WTC_CT";

    //监测设备1命令
    public static final String COMMAND_TXCS_ONE="DEV1_CT";
    public static final String COMMAND_SJJZ_ONE="DEV1_DC";
    public static final String COMMAND_SSJC_ONE="DEV1_RTS";
    //监测设备2命令
    public static final String COMMAND_TXCS_TWO="DEV2_CT";
    public static final String COMMAND_SJJZ_TWO="DEV2_DC";
    public static final String COMMAND_SSJC_TWO="DEV2_RTS";

    //监测设备1和2实时监测命令
    public static final String COMMAND_SSJC_ALL="DEV12_RTS";

    //接收数据
    public static final String DATA_HEAD="#";
}
