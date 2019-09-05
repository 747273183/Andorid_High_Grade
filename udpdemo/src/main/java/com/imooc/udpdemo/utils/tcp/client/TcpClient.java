package com.imooc.udpdemo.utils.tcp.client;

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

public class TcpClient {
    private Scanner mScanner;

    public TcpClient() {
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }

    public void start() {
        try {
            Socket socket = new Socket("192.168.1.121", 9090);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            new Thread() {
                @Override
                public void run() {
                    String line = null;
                    try {
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            while (true) {
                String msg = getMsg();
                bw.write(msg);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMsg() {
        return mScanner.next();
    }


    public static void main(String[] args) {
        new TcpClient().start();
    }


}
