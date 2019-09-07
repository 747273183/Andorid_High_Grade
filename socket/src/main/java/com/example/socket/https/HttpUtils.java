package com.example.socket.https;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static InputStream is;

    public interface HttpListener {
        void onSuccess(String context);
        void onFail(Exception ex);
    }

    public  static void doGet(final String urlStr, final HttpListener listener)
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url=new URL(urlStr);
                   HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                   conn.setDoInput(true);
                   conn.setDoOutput(true);
                   conn.setRequestMethod("GET");
                   conn.setReadTimeout(5000);
                   conn.setConnectTimeout(5000);
                   conn.connect();

                    is = conn.getInputStream();
                    int len=-1;
                    InputStreamReader reader=new InputStreamReader(is);
                    char[] buf=new char[2048];
                    StringBuffer content=new StringBuffer();
                    while ((len= reader.read(buf))!=-1)
                    {
                        content.append(new String(buf,0,len));
                    }

                    listener.onSuccess(content.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFail(e);
                }finally {
                    try {
                        if (is!=null)
                        {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
