package com.tuotiansudai.paywrapper.ghb.security.provider;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String hash(String message) {
        return DigestUtils.md5Hex(message);
    }
}
