package com.example.socket.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public void start()
    {
        try {
            ServerSocket serverSocket=new ServerSocket(9090);
            MsgPool.getInstance().start();
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("ip="+socket.getInetAddress().getHostAddress()+",port="+socket.getPort()+"is online...");

                //来一个客户端就开启一个新的线程处理该客户端的请求
                ClientTask clientTask = new ClientTask(socket);
                MsgPool.getInstance().addMsgComingListener(clientTask);
                clientTask.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
