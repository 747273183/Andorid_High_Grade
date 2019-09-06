package com.example.socket.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTask extends Thread implements MsgPool.MsgComingListener {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;


    public ClientTask(Socket socket) {
        try {
            this.socket = socket;
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println("read=" + line);
                //转发消息到其他Socket
                MsgPool.getInstance().sendMsg(socket.getPort()+":"+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMsgComing(String msg) {
        try {
            outputStream.write(msg.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
