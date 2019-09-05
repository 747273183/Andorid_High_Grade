package com.imooc.udpdemo.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by zhanghongyang01 on 17/4/21.
 */

public class TcpClientBiz {
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;

    public TcpClientBiz() {
        start();
    }

    public interface ReceiveMsgListener {
        void onReceive(String msg);
    }

    private ReceiveMsgListener mReceiveMsgListener;

    public void setReceiveMsgListener(ReceiveMsgListener listener) {
        mReceiveMsgListener = listener;
    }

    public void sendMsg(String msg) {
        try {
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mOs));
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 这里返回时需要时间的,可能在点击sendMsg的时候,这里还没有返回
                    mSocket = new Socket("192.168.0.102", 9090);
                    mIs = mSocket.getInputStream();
                    mOs = mSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startReadMsg();

            }
        }.start();

    }

    private void startReadMsg() {
        final BufferedReader br = new BufferedReader(new InputStreamReader(mIs));
        new Thread() {
            @Override
            public void run() {
                String line = null;
                try {
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        if (mReceiveMsgListener != null) {
                            final String finalLine = line;
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mReceiveMsgListener.onReceive(finalLine);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void destory(){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
