package com.tuotiansudai.cfca.util;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.CertUtil;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class SecurityUtil {
    private static Session session = null;

    static {
        String deviceName = JCrypto.JSOFT_LIB;
        try {
            JCrypto.getInstance().initialize(deviceName, null);
            session = JCrypto.getInstance().openSession(deviceName);
        } catch (PKIException e) {
            e.printStackTrace();
        }
    }

    public static KeyStore loadAnxinSignKey(String keyPath, String password) throws GeneralSecurityException, IOException {
        InputStream keyStoreFis = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStoreFis = new ClassPathResource(keyPath).getInputStream();
            keyStore.load(keyStoreFis, password.toCharArray());
            return keyStore;
        } finally {
            if (keyStoreFis != null) {
                keyStoreFis.close();
            }
        }
    }

    public static String p7SignMessageDetach(String jksPath, String jksPWD, String alias, String sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromJKS(jksPath, jksPWD, alias);
            X509Cert signCert = CertUtil.getCertFromJKS(jksPath, jksPWD, alias);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes("UTF-8"), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }

    public static String p7SignMessageDetach(KeyStore keyStore, String jksPWD, String alias, String sourceData) throws PKIException {
        if (keyStore == null) {
            throw new PKIException("JKSFile keyStore should not be null");
        }
        if (jksPWD == null) {
            throw new PKIException("JKSFile jksPwd should not be null");
        }
        if (alias == null) {
            throw new PKIException("JKSFile alias should not be null");
        }
        try {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, jksPWD.toCharArray());
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate == null) {
                throw new PKIException("no such alias cert!");
            }
            X509Cert signCert = new X509Cert(certificate.getEncoded());
            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes("UTF-8"), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }
}
