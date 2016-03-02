package com.tuotiansudai.util;

import java.util.Random;
import java.util.List;
import com.google.common.base.Strings;

/**
 * Created by gengbeijun on 16/3/1.
 */
public class RandomUtils {
    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numberChar = "0123456789";

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    public static String showChar(int showLength){
        StringBuffer sb = new StringBuffer();
        for(int i =0 ;i <showLength;i++ ){
            sb.append('*');
        }
        return sb.toString();
    }

    public static String encryptLoginName(String loginName, List<String> showLoginNameList, String queryLoginName, int showLength){

        String encryptLoginName;
        if(Strings.isNullOrEmpty(loginName)){
            if (showLoginNameList.contains(queryLoginName)) {
                encryptLoginName = RandomUtils.generateLowerString(3) + showChar(showLength);
            } else {
                encryptLoginName = queryLoginName.substring(0, 3) + showChar(showLength);
            }
        }
        else{
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
}
