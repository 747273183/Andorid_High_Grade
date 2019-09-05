package com.imooc.udpdemo.utils.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zhanghongyang01 on 17/4/21.
 */

public class TcpServer {

    public void start() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MsgPool.getInstance().start();
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("ip :"
                        + clientSocket.getInetAddress().getHostAddress()
                        + ", port :" + clientSocket.getPort() + " online...");

                CommTask commTask = new CommTask(clientSocket);
                MsgPool.getInstance().addMsgComingListener(commTask);
                commTask.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        new TcpServer().start();
    }

}
