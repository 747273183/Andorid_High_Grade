package com.example.udpserver.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClientBiz {

    private String serverIP="192.168.56.1";
    private InetAddress inetAddress;
    private int serverPort=7777;
    private DatagramSocket socket;
    private Scanner scanner;

    private static final String TAG = "UDPClient";

    public UDPClientBiz()
    {
        try {
            inetAddress=InetAddress.getByName(serverIP);
            socket=new DatagramSocket();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public interface  OnMsgReturnedListener
    {
        void onMsgReturned(String msg);
        void onError(Exception ex);
    }

    private Handler handler=new Handler(Looper.getMainLooper());
    public void sendMsg(final String msg, final OnMsgReturnedListener onMsgReturnedListener)
    {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //发送数据到服务端
                    byte[] clientMsgBytes = msg.getBytes();
                    DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes, clientMsgBytes.length, inetAddress, serverPort);
                    socket.send(clientPacket);

                    //接收服务端的数据
                    byte[] buf = new byte[1024];
                    DatagramPacket serverPacket = new DatagramPacket(buf, 0, buf.length);
                    socket.receive(serverPacket);

                    String hostAddress = serverPacket.getAddress().getHostAddress();
                    int port = serverPacket.getPort();
                    final String serverMsg = new String(serverPacket.getData(), 0, serverPacket.getLength());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMsgReturnedListener.onMsgReturned(serverMsg);
                        }
                    });


                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMsgReturnedListener.onError(e);
                        }
                    });

                }
            }
        }.start();

    }

    public  void onDestory()
    {
        if (socket!=null)
        {
            socket.close();
        }
    }



}
