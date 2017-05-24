package com.tuotiansudai.paywrapper.ghb.security.provider;

import com.tuotiansudai.paywrapper.ghb.security.GHBSecurityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

// http://www.cnblogs.com/lianghuilin/archive/2013/04/15/3DES.html
public class DES {
    private static final Log logger = LogFactory.getLog(DES.class);

    //定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";
    // TODO: load key
    private static final String PASSWORD_CRYPT_KEY = "2012PinganVitality075522628888ForShenZhenBelter075561869839";

    public static String encrypt(String origin) {
        return new String(encryptBytes(origin.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String decrypt(String encrypted) {
        return new String(decryptBytes(encrypted.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    private static byte[] encryptBytes(byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);    //生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm);    //实例化负责加密/解密的Cipher工具类
            c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式
            return c1.doFinal(src);
        } catch (Exception e) {
            logger.error("3DES encrypt failed", e);
            throw new GHBSecurityException("3DES encrypt failed", e);
        }
    }

    private static byte[] decryptBytes(byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
            return c1.doFinal(src);
        } catch (Exception e) {
            logger.error("3DES decrypt failed", e);
            throw new GHBSecurityException("3DES decrypt failed", e);
        }
    }


    /*
     * 根据字符串生成密钥字节数组
     * @param keyStr 密钥字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组

        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if (key.length > temp.length) {
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
