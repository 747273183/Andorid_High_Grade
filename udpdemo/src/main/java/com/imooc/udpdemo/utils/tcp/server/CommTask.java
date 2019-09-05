package com.imooc.udpdemo.utils.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by zhanghongyang01 on 17/4/23.
 */

public class CommTask extends Thread
        implements MsgPool.MsgComingListener {

    private InputStream is;
    private OutputStream os;
    private Socket mSocket;

    public CommTask(Socket socket) {
        try {
            mSocket = socket;
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int port = mSocket.getPort();
        System.out.println(port + " is ready...");
        String line = null;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                System.out.println("read :" + line);
                MsgPool.getInstance().sendMsg(port + ":" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMsgComing(String msg) throws IOException {
            os.write(msg.getBytes());
            os.write("\n".getBytes());
            os.flush();
    }
}
