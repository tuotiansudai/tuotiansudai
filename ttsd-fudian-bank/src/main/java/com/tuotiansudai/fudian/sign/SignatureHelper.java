package com.tuotiansudai.fudian.sign;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.util.OrderIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Enumeration;

@Component
public class SignatureHelper {

    private static Logger logger = LoggerFactory.getLogger(SignatureHelper.class);

    private static MyCertificate PFX;

    private static MyCertificate CER;

    private static final Gson gson = new GsonBuilder().create();

    private final BankConfig bankConfig;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SignatureHelper(BankConfig bankConfig, ResourceLoader resourceLoader, RedisTemplate<String, String> redisTemplate) throws Exception {
        this.redisTemplate = redisTemplate;
        this.bankConfig = bankConfig;

        try (InputStream pfxInputStream = resourceLoader.getResource(this.bankConfig.getPfxPath()).getInputStream()) {
            char[] charKeyPass = this.bankConfig.getPfxPassword().toCharArray();

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(pfxInputStream, charKeyPass);
            Enumeration<String> enumeration = ks.aliases();

            if (enumeration.hasMoreElements()) {
                String keyAliases = enumeration.nextElement();

                SignatureHelper.PFX = new MyCertificate((X509Certificate) ks.getCertificate(keyAliases),
                        (PrivateKey) ks.getKey(keyAliases, charKeyPass),
                        ks.getCertificate(keyAliases).getPublicKey());
            }
        }

        try (InputStream cerInputStream = resourceLoader.getResource(this.bankConfig.getCerPath()).getInputStream()) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(cerInputStream);
            SignatureHelper.CER = new MyCertificate(x509Certificate, null, x509Certificate.getPublicKey());
        }

    }

    public <T extends BaseRequestDto> void sign(T dto) {
        dto.setMerchantNo(this.bankConfig.getMerchant());
        dto.setOrderNo(OrderIdGenerator.generate(redisTemplate));
        Class<?> dtoClass = dto.getClass();
        try {
            Method returnUrlMethod = dtoClass.getMethod("setReturnUrl", String.class);
            Method notifyUrlMethod = dtoClass.getMethod("setNotifyUrl", String.class);
            String returnUrl = MessageFormat.format("{0}/{1}", this.bankConfig.getCallbackReturnUrl(), dto.getExtMark());
            String notifyUrl = MessageFormat.format("{0}/{1}", this.bankConfig.getCallbackNotifyUrl(), dto.getExtMark());
            returnUrlMethod.invoke(dto, returnUrl);
            notifyUrlMethod.invoke(dto, notifyUrl);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }

        String originalData = gson.toJson(dto);
        logger.info("origin data: {}", originalData);

        try {
            byte[] signedData = originalData.getBytes("utf-8");
            Signature signet = Signature.getInstance(SignatureHelper.PFX.getCert().getSigAlgName());
            signet.initSign(SignatureHelper.PFX.getPrivateKey());
            signet.update(signedData);
            String sign = this.byteToHex(signet.sign());

            String signedJson = gson.toJson(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("merchantNo", this.bankConfig.getMerchant())
                    .put("data", originalData)
                    .put("sign", sign)
                    .put("certInfo", this.byteToHex(SignatureHelper.PFX.getCert().getEncoded()))
                    .build()));


            logger.info("origin data: {}, sign: {}", originalData, signedData);

            dto.setRequestData(signedJson);

        } catch (Exception e) {
            dto.setRequestData(null);
            logger.error(MessageFormat.format("sign error, data {}", originalData), e);
        }
    }

    public boolean verifySign(String data) throws SecurityException {
        if (Strings.isNullOrEmpty(data)) {
            logger.warn("data is empty");
            return false;
        }

        JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
        String content = jsonObject.get("content").toString();
        String sign = jsonObject.get("sign").getAsString();

        try {
            byte[] inDataBytes = content.getBytes("utf-8");
            byte[] signaturePEM = this.checkPEM(sign);
            byte[] signDataBytes = signaturePEM != null ? Base64.getDecoder().decode(signaturePEM) : this.hexToByte(sign);

            Signature signet = Signature.getInstance(SignatureHelper.CER.getCert().getSigAlgName());
            signet.initVerify(SignatureHelper.CER.getPublicKey());
            signet.update(inDataBytes);
            return signet.verify(signDataBytes);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("verify sign exception, data: {}", data), ex);
        }

        return false;
    }

    private String byteToHex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (byte aB : b) {
            String tmp = Integer.toHexString(aB & 0xFF);
            if (tmp.length() == 1) {
                hs.append("0");
            }
            hs.append(tmp);
        }
        return hs.toString().toUpperCase();
    }

    private byte[] hexToByte(String hexCert) {
        if (Strings.isNullOrEmpty(hexCert)) {
            return new byte[0];
        }

        byte[] b = hexCert.getBytes();
        if (b.length % 2 != 0) {
            return new byte[0];
        }

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }

        return b2;
    }

    private byte[] checkPEM(String data) {
        byte[] dataBytes = this.hexToByte(data);

        String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/+= \r\n-";

        for (byte aParamArrayOfByte : dataBytes) {
            if (str1.indexOf(aParamArrayOfByte) == -1) {
                return null;
            }
        }

        StringBuilder localStringBuffer = new StringBuilder(dataBytes.length);
        String str2 = new String(dataBytes);

        for (int j = 0; j < str2.length(); ++j) {
            if (str2.charAt(j) != ' ' && str2.charAt(j) != '\r' && str2.charAt(j) != '\n') {
                localStringBuffer.append(str2.charAt(j));
            }
        }

        return localStringBuffer.toString().getBytes();
    }
}
