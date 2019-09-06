package com.example.socket.biz;

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

public class TCPClientBiz {

    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public void destory() {

        try {
            if (is!=null)
            {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (os!=null)
                {
                    os.close();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket!=null)
                {
                    socket.close();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface OnMsgComingListener
    {
        void onMsgComing(String msg);
        void onError(Exception ex);
    }

    private OnMsgComingListener listener;

    public void setListener(OnMsgComingListener listener) {
        this.listener = listener;
    }

    public TCPClientBiz() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    socket = new Socket("192.168.43.250", 9090);
                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    final BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line=null;
                    while ((line=br.readLine())!=null)
                    {
                        final String finalLine = line;
                        handler.post(new Runnable() {
                               @Override
                               public void run() {
                                   if (listener!=null)
                                   {
                                       listener.onMsgComing(finalLine);
                                   }
                               }
                           });

                    }


                } catch (final IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener!=null)
                            {
                                listener.onError(e);
                            }
                        }
                    });

                }
            }
        }.start();

    }

    private Handler handler=new Handler(Looper.getMainLooper());

    public void sendMsg(final String msg)
    {
       new Thread(){
           @Override
           public void run() {
               super.run();
               try {
                   BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                   bw.write(msg);
                   bw.newLine();
                   bw.flush();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }.start();


    }


}
