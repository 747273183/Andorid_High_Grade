package com.example.udpserver.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPServer {
    private InetAddress inetAddress;
    private int port = 7777;
    private DatagramSocket socket;
    private static final String TAG = "UDPServer";
    private Scanner scanner;

    public UDPServer() {
        try {
            inetAddress = InetAddress.getLocalHost();
            socket = new DatagramSocket(port, inetAddress);
            scanner = new Scanner(System.in);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                //接收来自客户端的消息
                byte[] buf = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
                socket.receive(receivedPacket);
                String hostAddress = receivedPacket.getAddress().getHostAddress();
                int port = receivedPacket.getPort();
                String clientMsg = new String(receivedPacket.getData(), 0,
                        receivedPacket.getLength());
                Log.d(TAG, hostAddress + ":" + clientMsg);
                //向客户端发送消息
                String sendMsg = scanner.next();
                DatagramPacket sendP = new DatagramPacket(sendMsg.getBytes(), sendMsg.getBytes().length,
                        receivedPacket.getSocketAddress());
                socket.send(sendP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UDPServer().start();
    }
}
