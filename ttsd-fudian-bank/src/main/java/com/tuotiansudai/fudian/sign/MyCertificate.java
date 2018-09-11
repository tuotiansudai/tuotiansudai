package com.tuotiansudai.fudian.sign;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class MyCertificate {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private X509Certificate cert;

    public MyCertificate(X509Certificate cert, PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.cert = cert;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public X509Certificate getCert() {
        return cert;
    }
}
