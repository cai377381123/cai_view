package cn.edu.nuc.csce.dcldims.bluetooth;

import android.util.Log;

/**
 * Created by tdg on 2016/10/11.
 */

public class DevSingleData {
    //监测设备1 roll数据：4个字节，浮点
    //监测设备1 pitch数据：4个字节，浮点
    //CRC-16校验：2个字节
    //监测设备1电量数据：2个字节
    //无线传输控制器电量：2个字节

    private static final String TAG = "cn.edu.nuc.csce.DCLDIMS";
    public static double BATTERY_WARM_VALUE = 2.03;
    private double Roll;
    private double Pitch;
    private short CRC_value;
    private short CRC_Cal_value;
    private double Battery_Monitor;
    private double Battery_Controller;
    private double SUMRANGE = 4096.0;
    private double ADCRANCE = 3.3;
    private double ADCRANCE75 = 1.904;
    private double ADCRANCE124 = 1.246;



    DevSingleData(){
        Roll = 0;
        Pitch = 0;
        CRC_value = 0;
        CRC_Cal_value = 0;
        Battery_Monitor = 0;
        Battery_Controller = 0;
    }
    public DevSingleData(byte[] data){//14个字节
        int v0 = (data[3] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (data[2] & 0xff) << 16;
        int v2 = (data[1] & 0xff) << 8;
        int v3 = (data[0] & 0xff) ;
        Roll = Float.intBitsToFloat(v0 + v1 + v2 + v3);
        int v4 = (data[7] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v5 = (data[6] & 0xff) << 16;
        int v6 = (data[5] & 0xff) << 8;
        int v7 = (data[4] & 0xff) ;
        Pitch = Float.intBitsToFloat(v4 + v5 + v6 + v7);
        int v8 = (data[9] & 0xff) << 8;
        int v9 = (data[8] & 0xff) ;
        CRC_value = (short)(v8 + v9);
        int crc = CRC16.calcCrc16(new byte[] { data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]});
        Log.i(TAG,String.format("0x%02x", crc));
        CRC_Cal_value = (short)crc;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v10 = (data[11] & 0xff) << 8;
        int v11 = (data[10] & 0xff) ;
        Battery_Monitor = ((v10 + v11)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor = (Battery_Monitor > 1.0) ? 1.0 : Battery_Monitor;
        int v12 = (data[13] & 0xff) << 8;
        int v13 = (data[12] & 0xff) ;
        Battery_Controller = ((v12 + v13)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Controller = (Battery_Controller > 1.0) ? 1.0 : Battery_Controller;
    }

    public double getRoll() {
        return Roll;
    }
    public double getPitch() {
        return Pitch;
    }
    public short getCRC_value() {
        return CRC_value;
    }
    public short getCRC_Cal_value() { return CRC_Cal_value; }
    public double getBattery_Monitor() { return Battery_Monitor; }
    public double getBattery_Controller() { return Battery_Controller; }



    public void parseData(byte[] data){
        int v0 = (data[3] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (data[2] & 0xff) << 16;
        int v2 = (data[1] & 0xff) << 8;
        int v3 = (data[0] & 0xff) ;
        Roll = Float.intBitsToFloat(v0 + v1 + v2 + v3);
        int v4 = (data[7] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v5 = (data[6] & 0xff) << 16;
        int v6 = (data[5] & 0xff) << 8;
        int v7 = (data[4] & 0xff) ;
        Pitch = Float.intBitsToFloat(v4 + v5 + v6 + v7);
        int v8 = (data[9] & 0xff) << 8;
        int v9 = (data[8] & 0xff) ;
        CRC_value = (short)(v8 + v9);
        int crc = CRC16.calcCrc16(new byte[] { data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]});
        Log.i(TAG,"CRC_cal:"+String.format("0x%04x", crc));
        CRC_Cal_value = (short)crc;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v10 = (data[11] & 0xff) << 8;
        int v11 = (data[10] & 0xff) ;
        Battery_Monitor = ((v10 + v11)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor = (Battery_Monitor > 1.0) ? 1.0 : Battery_Monitor;
        int v12 = (data[13] & 0xff) << 8;
        int v13 = (data[12] & 0xff) ;
        Battery_Controller = ((v12 + v13)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Controller = (Battery_Controller > 1.0) ? 1.0 : Battery_Controller;
    }
}
