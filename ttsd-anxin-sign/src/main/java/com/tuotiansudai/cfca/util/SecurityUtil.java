package com.tuotiansudai.cfca.util;

import java.security.PrivateKey;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.Base64;
import cfca.sadk.util.CertUtil;
import cfca.sadk.util.EncryptUtil;
import cfca.sadk.util.HashUtil;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.P10Request;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;
import com.tuotiansudai.cfca.constant.SystemConst;

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

    public static String getSignCert(String pfxPath, String pfxPWD) throws PKIException {
        try {
            X509Cert x509Cert = CertUtil.getCertFromPFX(pfxPath, pfxPWD);

            return Base64.toBase64String(x509Cert.getEncoded());
        } catch (Exception e) {
            throw new PKIException("get signature cert fail", e);
        }
    }

    public static String p1SignMessage(String pfxPath, String pfxPWD, byte[] sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPWD);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p1SignMessage(Mechanism.SHA256_RSA, sourceData, privateKey, session);
            return new String(signatureByte, SystemConst.DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new PKIException("P1 signature message fail", e);
        }
    }

    public static String p7SignByHash(String pfxPath, String pfxPWD, byte[] sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPWD);
            X509Cert x509Cert = CertUtil.getCertFromPFX(pfxPath, pfxPWD);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignByHash(Mechanism.SHA1_RSA, sourceData, privateKey, x509Cert, session);
            return new String(signatureByte, SystemConst.DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new PKIException("P7 hash signature fail", e);
        }
    }

    public static String p7SignMessageDetach(String pfxPath, String pfxPWD, String sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPWD);
            X509Cert signCert = CertUtil.getCertFromPFX(pfxPath, pfxPWD);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes(), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }

    public static String p7SignMessageDetach(String jksPath, String jksPWD, String alias, String sourceData) throws PKIException {
        try {
            PrivateKey privateKey = KeyUtil.getPrivateKeyFromJKS(jksPath, jksPWD, alias);
            X509Cert signCert = CertUtil.getCertFromJKS(jksPath, jksPWD, alias);

            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes(SystemConst.DEFAULT_CHARSET), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }

    public static Signature verifyP7Detach(byte[] original, byte[] signature) throws PKIException {
        Signature sign = new Signature();
        if (!sign.p7VerifyMessageDetach(original, signature, session)) {
            throw new PKIException("verify P7 detach signature fail");
        }
        return sign;
    }

    public static Signature verifyP7Detach(String original, String signature) throws PKIException {
        return verifyP7Detach(CommonUtil.getBytes(original), CommonUtil.getBytes(signature));
    }

    public static X509Cert getX509CertFromSignature(String signature) throws PKIException {
        return new Signature().getSignerX509CertFromP7SignData(CommonUtil.getBytes(signature));
    }

    public static String encryptBySM4(String plaintext, String password) throws PKIException {
        return CommonUtil.getString(EncryptUtil.encryptMessageBySM4(CommonUtil.getBytes(plaintext), password));
    }

    public static String decryptBySM4(String ciphertext, String password) throws PKIException {
        return CommonUtil.getString(EncryptUtil.decryptMessageBySM4(CommonUtil.getBytes(ciphertext), password));
    }

    public static byte[] hashBySM3(byte[] original) throws Exception {
        return HashUtil.SM2HashMessageByBCWithoutZValue(original);
    }

    public static P10Request getP10Request() throws PKIException {
        return new P10Request(session);
    }

    public static String getP10(P10Request p10Request, int length) throws PKIException {
        return CommonUtil.getString(p10Request.generatePKCS10Request(new Mechanism(Mechanism.SHA1_RSA), length, session));
    }
}
