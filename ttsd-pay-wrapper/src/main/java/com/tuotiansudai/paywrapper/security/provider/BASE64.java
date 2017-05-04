package com.tuotiansudai.paywrapper.security.provider;


import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class BASE64 {
    public static String encode(String origin) {
        return Base64.encodeBase64String(origin.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String encrypted) {
        return new String(decodeBytes(encrypted), StandardCharsets.UTF_8);
    }

    public static byte[] decodeBytes(String encrypted) {
        return Base64.decodeBase64(encrypted);
    }
}
