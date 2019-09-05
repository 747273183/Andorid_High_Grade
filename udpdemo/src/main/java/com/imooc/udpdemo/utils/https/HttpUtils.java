package com.imooc.udpdemo.utils.https;

import android.os.Handler;
import android.os.Looper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 知识点1:UI线程切换
 * 知识点2:乱码
 */
public class HttpUtils {

    private static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public static interface HttpListener {
        void success(String msg);

        void exception(Exception e);
    }

    public static void doGet(final String urlStr, final HttpListener listener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                    httpUrlConn.setDoOutput(true);
                    httpUrlConn.setDoInput(true);
                    httpUrlConn.setUseCaches(false);
                    // 设置请求方式（GET/POST）
                    httpUrlConn.setRequestMethod("GET");
                    httpUrlConn.setReadTimeout(5000);
                    httpUrlConn.setConnectTimeout(5000);

                    httpUrlConn.connect();


                    final StringBuffer sb = new StringBuffer();

                    InputStream is = httpUrlConn.getInputStream();

                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");

                    int len = -1;
                    char[] buf = new char[1024];
                    while ((len = isr.read(buf)) != -1) {
                        sb.append(new String(buf, 0, len));
                    }
                    sUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.success(sb.toString());
                        }
                    });
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.exception(e);
                }
            }
        }.start();
    }
}
