package com.example.udpserver.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClient {

    private String serverIP="10.20.8.23";
    private InetAddress inetAddress;
    private int serverPort=7777;
    private DatagramSocket socket;
    private Scanner scanner;

    private static final String TAG = "UDPClient";

    public UDPClient()
    {
        try {
            inetAddress=InetAddress.getByName(serverIP);
            socket=new DatagramSocket();
            scanner=new Scanner(System.in);
            scanner.useDelimiter("\n");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        try {
            //发送数据到服务端
            System.out.println("Client说:");
            String clientMsg=scanner.next();
            byte[] clientMsgBytes= clientMsg.getBytes();
            DatagramPacket clientPacket=new DatagramPacket(clientMsgBytes,clientMsgBytes.length,inetAddress,serverPort);
            socket.send(clientPacket);
            //接收服务端的数据

            byte[] buf=new byte[1024];
            DatagramPacket serverPacket=new DatagramPacket(buf,0,buf.length);
            socket.receive(serverPacket);

            String hostAddress = serverPacket.getAddress().getHostAddress();
            int port = serverPacket.getPort();
            String serverMsg=new String(serverPacket.getData(),0,serverPacket.getLength());
            Log.d(TAG, hostAddress+":"+serverMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UDPClient().start();
    }
}
