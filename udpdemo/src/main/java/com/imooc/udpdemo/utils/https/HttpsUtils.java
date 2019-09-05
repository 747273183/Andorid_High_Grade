package com.imooc.udpdemo.utils.https;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by zhanghongyang01 on 17/4/23.
 */

/**
 * 知识点1:UI线程切换
 * 知识点2:乱码
 * <p>
 * 证书的颁发者是否在“根受信任的证书颁发机构列表”中
 * 证书是否过期
 * 证书的持有者是否和访问的网站一致
 */
public class HttpsUtils {

    private static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public static interface HttpListener {
        void success(String msg);

        void exception(Exception e);
    }


    public static void doGet(final Context context, final String urlStr, final HttpListener listener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
//                    X509Certificate serverCert = getCert(context);
//
//                    // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//                    TrustManager[] tm = {new MyX509TrustManager(serverCert)};
//                    SSLContext sslContext = SSLContext.getInstance("TLS");
//                    sslContext.init(null, tm, new java.security.SecureRandom());
//                    // 从上述SSLContext对象中得到SSLSocketFactory对象
//                    SSLSocketFactory ssf = sslContext.getSocketFactory();
//                    httpsUrlConn.setHostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
//                            return defaultHostnameVerifier.verify("localhost", session);
////                            return true;
//                        }
//                    });
//                    httpsUrlConn.setSSLSocketFactory(ssf);
                    httpsUrlConn.setSSLSocketFactory(getSslSocketFactory(context));

//                    httpsUrlConn.setHostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            Log.e("https", hostname + " ===");
//                            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
//                            return defaultHostnameVerifier.verify(hostname, session);
////                            return true;
//                        }
//                    });

                    httpsUrlConn.setDoOutput(true);
                    httpsUrlConn.setDoInput(true);
                    httpsUrlConn.setUseCaches(false);
                    // 设置请求方式（GET/POST）
                    httpsUrlConn.setRequestMethod("GET");
                    httpsUrlConn.setReadTimeout(5000);
                    httpsUrlConn.setConnectTimeout(5000);
                    httpsUrlConn.connect();
                    final StringBuffer sb = new StringBuffer();

                    InputStream is = httpsUrlConn.getInputStream();

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

    private static SSLSocketFactory getSslSocketFactory(Context context) {
        X509Certificate cert1 = getCert(context, "zhy.cer");
        X509Certificate cert2 = getCert(context, "srca.cer");

        String keyStoreType = KeyStore.getDefaultType();
        try {
            // 生成一个包含服务端证书的keystore
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null);
            keyStore.setCertificateEntry("srca", cert1);
            keyStore.setCertificateEntry("zhy", cert2);
            // 利用keystore生成一个TrustManager
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init(keyStore);

            // 生成一个SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509Certificate getCert(Context context, String certName) {
        try {
            InputStream is = context.getAssets().open(certName);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(is);
            return x509Certificate;
        } catch (IOException e) {
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
