package com.example.socket.https;

import android.content.Context;

import com.example.socket.biz.TCPClientBiz;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpUtils {

    private static InputStream is;

    public interface HttpListener {
        void onSuccess(String context);
        void onFail(Exception ex);
    }

    public  static void doGet(final Context context, final String urlStr, final HttpListener listener)
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url=new URL(urlStr);
                   HttpsURLConnection conn= (HttpsURLConnection) url.openConnection();

                   //https
                    SSLContext sslContext=SSLContext.getInstance("TLS");
                    X509Certificate certificate=getCert(context);
                    TrustManager[] trustManagers={new MyX509TrustManager(certificate)};
                    sslContext.init(null,trustManagers,new SecureRandom());
                    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                    conn.setSSLSocketFactory(socketFactory);
                    conn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

                            return defaultHostnameVerifier.verify("kyfw.12306.cn",session);
                        }
                    });

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
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } finally {
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

    private static X509Certificate getCert(Context context) {
        try {
            context.getAssets().open("srca.cer");
            CertificateFactory certificateFactory=CertificateFactory.getInstance("X.509");
          return (X509Certificate) certificateFactory.generateCertificate(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
