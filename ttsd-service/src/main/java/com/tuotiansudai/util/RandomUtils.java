package com.tuotiansudai.util;

import java.util.Random;
import java.util.List;

import com.google.common.base.Strings;

public class RandomUtils {

    private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encryptLoginName(String loginName, List<String> showLoginNameList, String queryLoginName, int showLength) {

        String encryptLoginName;
        if (Strings.isNullOrEmpty(loginName)) {
            if (showLoginNameList.contains(queryLoginName)) {
                encryptLoginName = RandomUtils.generateLowerString(3) + showChar(showLength);
            } else {
                encryptLoginName = queryLoginName.substring(0, 3) + showChar(showLength);
            }
        } else {
            if (loginName.equalsIgnoreCase(queryLoginName)) {
                encryptLoginName = queryLoginName;
            } else {
                if (showLoginNameList.contains(queryLoginName) && !loginName.equalsIgnoreCase(queryLoginName)) {
                    encryptLoginName = RandomUtils.generateLowerString(3) + showChar(showLength);
                } else {
                    encryptLoginName = queryLoginName.substring(0, 3) + showChar(showLength);
                }
            }
        }
        return encryptLoginName;
    }

    private static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    private static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    private static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    private static String showChar(int showLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < showLength; i++) {
            sb.append('*');
        }
        return sb.toString();
    }
}
