package com.example.socket.tcp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private Scanner scanner;

    public TcpClient() {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
    }

    public void start() {
        try {
            Socket socket = new Socket("192.168.43.250", 9090);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));


            //读取服务端发送的信息
            new Thread() {
                @Override
                public void run() {
                    super.run();
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

            //发送信息
            while (true) {
                String msg = scanner.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpClient().start();
    }

}
