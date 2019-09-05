package com.imooc.udpdemo.utils.https;

import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by zhanghongyang01 on 17/4/23.
 */
public class MyX509TrustManager implements X509TrustManager {

    private X509Certificate mServerCert;

    public MyX509TrustManager(X509Certificate x509Certificate) {
        mServerCert = x509Certificate;
        Log.e("https", mServerCert.getPublicKey().toString());
    }


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        for (X509Certificate cert : chain) {
            // 检查证书是否过期,以及合法性校验
            cert.checkValidity();

            try {
                // 证书匹配校验
                cert.verify(mServerCert.getPublicKey());
            } catch (Exception e) {
                e.printStackTrace();
                throw new CertificateException(e.getMessage());
            }
            Log.e("https", cert.toString());
        }

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
