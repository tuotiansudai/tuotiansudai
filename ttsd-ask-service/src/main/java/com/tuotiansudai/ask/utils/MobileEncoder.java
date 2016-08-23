package com.tuotiansudai.ask.utils;

import com.google.common.base.Strings;

import java.util.regex.Pattern;

public class MobileEncoder {

    private static Pattern pattern = Pattern.compile("^(\\d){11}$");

    public static String encode(String mobile) {
        if (Strings.isNullOrEmpty(mobile) || mobile.length() != 11 || !pattern.matcher(mobile).matches()) {
            return mobile;
        }

        return mobile.replaceAll("^(\\d{3})\\d{4}(\\d{4})$", "$1****$2");
    }
}
