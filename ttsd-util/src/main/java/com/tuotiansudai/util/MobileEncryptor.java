package com.tuotiansudai.util;

public class MobileEncryptor {

    public static String encryptWebMiddleMobile(String mobile) {
        return mobile.substring(0, 3) + MobileEncryptor.showChar(4) + mobile.substring(7);
    }

    public static String encryptAppMiddleMobile(String mobile) {
        return mobile.substring(0, 3) + MobileEncryptor.showChar(4) + mobile.substring(7);
    }

    public static String showChar(int showLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < showLength; i++) {
            sb.append('*');
        }
        return sb.toString();
    }
}
