package com.imooc.udpdemo.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by zhanghongyang01 on 17/4/20.
 */

public class UdpClientBiz {

    private String ip = "192.168.56.1";
    private int port = 7777;
    private InetAddress mInetAddress;
    private DatagramSocket mUdpSocket;  //创建套接字

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public UdpClientBiz() {
        try {
            mInetAddress = InetAddress.getByName(ip);  //服务器地址
            mUdpSocket = new DatagramSocket();  //创建套接字
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public interface ReceiveMsgListener {
        void onReceive(String msg);
    }


    public void sendMsg(final String msg, final ReceiveMsgListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // send Msg
                    byte[] buf = msg.getBytes();
                    //创建发送方的数据报信息
                    DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, mInetAddress, port);
                    mUdpSocket.send(dataGramPacket);  //通过套接字发送数据

                    // receive Msg
                    byte[] backbuf = new byte[1024];
                    DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
                    mUdpSocket.receive(backPacket);  //接收返回数据
                    final String backMsg = new String(backbuf, 0, backPacket.getLength());

                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onReceive(backMsg);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}


