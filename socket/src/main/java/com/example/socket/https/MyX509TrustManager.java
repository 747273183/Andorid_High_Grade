package com.example.socket.https;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {

    private X509Certificate x509Certificate;

    public MyX509TrustManager(X509Certificate x509Certificate)
    {
        this.x509Certificate=x509Certificate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate certificate:chain)
            {
                certificate.checkValidity();
                try {
                    certificate.verify(x509Certificate.getPublicKey());
                } catch (Exception e) {
                   throw  new CertificateException(e);
                }
            }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
