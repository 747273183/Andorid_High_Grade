package com.imooc.udpdemo.utils.udp;

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

public class UdpClient {

    private Scanner mScanner;
    private String ip = "172.20.198.20";
    private int port = 7777;
    private InetAddress mInetAddress;
    private DatagramSocket mUdpSocket;  //创建套接字


    public UdpClient() {
        try {
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
            mInetAddress = InetAddress.getByName(ip);  //服务器地址
            mUdpSocket = new DatagramSocket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {

                String msg = getMsg();
                sendMsg(msg);
                receiveMsg();

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getMsg() {
        return mScanner.next();
    }

    private void receiveMsg() throws IOException {
        //接收服务器反馈数据
        byte[] backbuf = new byte[1024];
        DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
        mUdpSocket.receive(backPacket);  //接收返回数据
        String backMsg = new String(backbuf, 0, backPacket.getLength());
        System.out.println("Server:" + backMsg);

    }

    public void sendMsg(String msg) throws IOException {
        byte[] buf = msg.getBytes();
        //创建发送方的数据报信息
        DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, mInetAddress, port);
        mUdpSocket.send(dataGramPacket);  //通过套接字发送数据
    }

    public static void main(String[] args) {
        new UdpClient().start();
    }

}


