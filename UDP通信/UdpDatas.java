package com.hoist.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Administrator on 2016/10/1 0001.
 */

public class UdpDatas {
    private DatagramSocket dgSocket=new DatagramSocket();

    public UdpDatas() throws SocketException {
    }

    public void BroadcastIP()throws Exception{
        System.out.println("定义前");
        System.out.println("send before.");
        byte b[]="#DEV12_RTS,1,4,56,4,5,46,67,12".getBytes();

        DatagramPacket dgPacket=new DatagramPacket(b,b.length, InetAddress.getByName("192.168.43.255"),6001);
        dgSocket.send(dgPacket);
        dgSocket.close();
        System.out.print("send finish");
    }

    public String receiveIP() throws Exception{
        System.out.println("进入方法第一条");
        DatagramSocket dgSocketr=new DatagramSocket(6001);
        byte[] by=new byte[1024];
        DatagramPacket packet=new DatagramPacket(by,by.length);
        System.out.println("进入方法第二条");
        dgSocketr.receive(packet);
        String str=new String(packet.getData() , packet.getOffset() , packet.getLength());
        System.out.println("接收到数据大小:"+str.length());
        System.out.println("接收到的数据为："+str);
        dgSocketr.close();
        System.out.println("recevied message is ok.");
        return str;
    }
}
