package com.tuotiansudai.cfca.util;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.CertUtil;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;

import java.security.PrivateKey;

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
}
