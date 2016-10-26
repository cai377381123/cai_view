package cn.edu.nuc.csce.dcldims.bluetooth;

import android.util.Log;

/**
 * Created by tdg on 2016/10/11.
 */

public class DevAllData {//26 Byte
    //监测设备1 roll数据：4个字节，浮点
    //监测设备1 pitch数据：4个字节，浮点
    //CRC-16校验：2个字节
    //监测设备1电量数据：2个字节
    //监测设备2 roll数据：4个字节，浮点
    //监测设备2 pitch数据：4个字节，浮点
    //CRC-16校验：2个字节
    //监测设备2电量数据：2个字节
    //无线传输控制器电量：2个字节

    private static final String TAG = "cn.edu.nuc.csce.DCLDIMS";
    public static double BATTERY_WARM_VALUE = 2.03;
    private double Roll_1;
    private double Pitch_1;
    private short CRC_value_1;
    private short CRC_Cal_value_1;
    private double Battery_Monitor_1;
    private double Roll_2;
    private double Pitch_2;
    private short CRC_value_2;
    private short CRC_Cal_value_2;
    private double Battery_Monitor_2;
    private double Battery_Controller;
    private double SUMRANGE = 4096.0;
    private double ADCRANCE = 3.3;
    private double ADCRANCE75 = 1.904;
    private double ADCRANCE124 = 1.246;

    public double getRoll_1() {
        return Roll_1;
    }

    public double getPitch_1() {
        return Pitch_1;
    }

    public short getCRC_value_1() {
        return CRC_value_1;
    }

    public short getCRC_Cal_value_1() {
        return CRC_Cal_value_1;
    }

    public double getBattery_Monitor_1() {
        return Battery_Monitor_1;
    }

    public double getRoll_2() {
        return Roll_2;
    }

    public double getPitch_2() {
        return Pitch_2;
    }

    public short getCRC_value_2() {
        return CRC_value_2;
    }

    public short getCRC_Cal_value_2() {
        return CRC_Cal_value_2;
    }

    public double getBattery_Monitor_2() {
        return Battery_Monitor_2;
    }

    public double getBattery_Controller() {
        return Battery_Controller;
    }

    DevAllData(){
        Roll_1 = 0;
        Pitch_1 = 0;
        CRC_value_1 = 0;
        CRC_Cal_value_1 = 0;
        Battery_Monitor_1 = 0;
        Roll_2 = 0;
        Pitch_2 = 0;
        CRC_value_2 = 0;
        CRC_Cal_value_2 = 0;
        Battery_Monitor_2 = 0;
        Battery_Controller = 0;
    }
    public DevAllData(byte[] data){//26个字节
        int v0 = (data[3] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (data[2] & 0xff) << 16;
        int v2 = (data[1] & 0xff) << 8;
        int v3 = (data[0] & 0xff) ;
        Roll_1 = Float.intBitsToFloat(v0 + v1 + v2 + v3);
        int v4 = (data[7] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v5 = (data[6] & 0xff) << 16;
        int v6 = (data[5] & 0xff) << 8;
        int v7 = (data[4] & 0xff) ;
        Pitch_1 = Float.intBitsToFloat(v4 + v5 + v6 + v7);
        int v8 = (data[9] & 0xff) << 8;
        int v9 = (data[8] & 0xff) ;
        CRC_value_1 = (short)(v8 + v9);
        int crc1 = CRC16.calcCrc16(new byte[] { data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]});
        Log.i(TAG,"CRC_cal 1:"+String.format("0x%04x", crc1));
        CRC_Cal_value_1 = (short)crc1;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v10 = (data[11] & 0xff) << 8;
        int v11 = (data[10] & 0xff) ;
        Battery_Monitor_1 = ((v10 + v11)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor_1 = (Battery_Monitor_1 > 1.0) ? 1.0 : Battery_Monitor_1;

        int v12 = (data[15] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v13 = (data[14] & 0xff) << 16;
        int v14 = (data[13] & 0xff) << 8;
        int v15 = (data[12] & 0xff) ;
        Roll_2 = Float.intBitsToFloat(v12 + v13 + v14 + v15);
        int v16 = (data[19] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v17 = (data[18] & 0xff) << 16;
        int v18 = (data[17] & 0xff) << 8;
        int v19 = (data[16] & 0xff) ;
        Pitch_2 = Float.intBitsToFloat(v16 + v17 + v18 + v19);
        int v20 = (data[21] & 0xff) << 8;
        int v21 = (data[20] & 0xff) ;
        CRC_value_2 = (short)(v20 + v21);
        int crc2 = CRC16.calcCrc16(new byte[] { data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19]});
        Log.i(TAG,"CRC_cal 2:"+String.format("0x%04x", crc2));
        CRC_Cal_value_2 = (short)crc2;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v22 = (data[23] & 0xff) << 8;
        int v23 = (data[22] & 0xff) ;
        Battery_Monitor_2 = ((v22 + v23)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor_2 = (Battery_Monitor_2 >1.0) ? 1.0 : Battery_Monitor_2;

        int v24 = (data[25] & 0xff) << 8;
        int v25 = (data[24] & 0xff) ;
        Battery_Controller = ((v24 + v25)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Controller = (Battery_Controller > 1.0) ? 1.0 : Battery_Controller;
    }





    public void parseData(byte[] data){
        int v0 = (data[3] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (data[2] & 0xff) << 16;
        int v2 = (data[1] & 0xff) << 8;
        int v3 = (data[0] & 0xff) ;
        Roll_1 = Float.intBitsToFloat(v0 + v1 + v2 + v3);
        int v4 = (data[7] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v5 = (data[6] & 0xff) << 16;
        int v6 = (data[5] & 0xff) << 8;
        int v7 = (data[4] & 0xff) ;
        Pitch_1 = Float.intBitsToFloat(v4 + v5 + v6 + v7);
        int v8 = (data[9] & 0xff) << 8;
        int v9 = (data[8] & 0xff) ;
        CRC_value_1 = (short)(v8 + v9);
        int crc1 = CRC16.calcCrc16(new byte[] { data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]});
        Log.i(TAG,String.format("0x%02x", crc1));
        CRC_Cal_value_1 = (short)crc1;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v10 = (data[11] & 0xff) << 8;
        int v11 = (data[10] & 0xff) ;
        Battery_Monitor_1 = ((v10 + v11)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor_1 = (Battery_Monitor_1 > 1.0) ? 1.0 : Battery_Monitor_1;

        int v12 = (data[15] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v13 = (data[14] & 0xff) << 16;
        int v14 = (data[13] & 0xff) << 8;
        int v15 = (data[12] & 0xff) ;
        Roll_2 = Float.intBitsToFloat(v12 + v13 + v14 + v15);
        int v16 = (data[19] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v17 = (data[18] & 0xff) << 16;
        int v18 = (data[17] & 0xff) << 8;
        int v19 = (data[16] & 0xff) ;
        Pitch_2 = Float.intBitsToFloat(v16 + v17 + v18 + v19);
        int v20 = (data[21] & 0xff) << 8;
        int v21 = (data[20] & 0xff) ;
        CRC_value_2 = (short)(v20 + v21);
        int crc2 = CRC16.calcCrc16(new byte[] { data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19]});
        Log.i(TAG,String.format("0x%02x", crc2));
        CRC_Cal_value_2 = (short)crc2;
        //12位量程4096对应3.3V，采样后电池最高电压3.2
        int v22 = (data[23] & 0xff) << 8;
        int v23 = (data[22] & 0xff) ;
        Battery_Monitor_2 = ((v22 + v23)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Monitor_2 = (Battery_Monitor_2 >1.0) ? 1.0 : Battery_Monitor_2;

        int v24 = (data[25] & 0xff) << 8;
        int v25 = (data[24] & 0xff) ;
        Battery_Controller = ((v24 + v25)/SUMRANGE*ADCRANCE-ADCRANCE75)/ADCRANCE124;
        Battery_Controller = (Battery_Controller > 1.0) ? 1.0 : Battery_Controller;
    }
}
