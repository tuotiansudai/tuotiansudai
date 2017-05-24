package com.tuotiansudai.paywrapper.ghb.security.provider;

import com.tuotiansudai.paywrapper.ghb.security.GHBSecurityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

// 私钥加密， 公钥解密
public class RSA {

    private static final Log logger = LogFactory.getLog(RSA.class);

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    // TODO: load key
    private static final String PUBLIC_KEY = "RSAPublicKey";//公钥 GHB
    private static final String PRIVATE_KEY = "RSAPrivateKey";//私钥 TUOTIAN

    private static PrivateKey TUOTIAN_PRIVATE_KEY = loadPrivateKey();
    private static PublicKey GHB_PUBLIC_KEY = loadPublicKey();

    // https://my.oschina.net/jiangli0502/blog/171263
    private static PrivateKey loadPrivateKey() {
        byte[] keyBytes = BASE64.decodeBytes(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("load private key failed", e);
        }
        return null;
    }

    private static PublicKey loadPublicKey() {
        byte[] keyBytes = BASE64.decodeBytes(PUBLIC_KEY);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("load public key failed", e);
        }
        return null;
    }

    public static String sign(String data) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(TUOTIAN_PRIVATE_KEY);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return new String(signature.sign(), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            logger.error("sign data failed", e);
            throw new GHBSecurityException("sign data failed", e);
        }
    }

    public static boolean verify(String data, String sign) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(GHB_PUBLIC_KEY);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(BASE64.decodeBytes(sign));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            logger.error("verify sign failed", e);
            throw new GHBSecurityException("verify sign failed", e);
        }
    }
}
