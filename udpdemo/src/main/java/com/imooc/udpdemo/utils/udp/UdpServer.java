package com.imooc.udpdemo.utils.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by zhanghongyang01 on 17/4/20.
 */

public class UdpServer {

    private Scanner mScanner;
    private int port;
    private DatagramSocket mUdpSocket;


    public UdpServer() {
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            port = 7777;
            mUdpSocket = new DatagramSocket(port, inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        try {

            //创建DatagramSocket对象
            while (true) {
                byte[] buf = new byte[1024];  //定义byte数组
                DatagramPacket packet = new DatagramPacket(buf, buf.length);  //创建DatagramPacket对象
                // 接收数据包
                mUdpSocket.receive(packet);  //通过套接字接收数据

                String getMsg = new String(packet.getData(), 0, packet.getLength());
                int clientPort = packet.getPort();
                InetAddress clientAddress = packet.getAddress();
                String receiveMsg = "Address = " + clientAddress + " , port = "
                        + clientPort + " , 数据:" + getMsg;

                System.out.println(receiveMsg);

                String returnMsg = getReturnedMsg();

                // 客户端地址
                SocketAddress sendAddress = packet.getSocketAddress();
                String feedback = returnMsg;
                byte[] feedbackBytes = feedback.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(feedbackBytes, feedbackBytes.length, sendAddress); //封装返回给客户端的数据
                mUdpSocket.send(sendPacket);  //通过套接字反馈服务器数据
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        new UdpServer().start();
    }

    public String getReturnedMsg() {
        return "Server:" + mScanner.next();
    }
}
