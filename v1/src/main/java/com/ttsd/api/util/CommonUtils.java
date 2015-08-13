package com.ttsd.api.util;


public class CommonUtils {

    public static String encryptUserName(String userName) {

        if (userName.length() > 3) {

            userName = userName.substring(0, 3) + "***";

        } else {

            userName += "***";

        }
        return userName;
    }

    public static String convertRealMoneyByType(double money, String type) {
        if ("ti_balance".equals(type)){
            return "+" + money;
        }else if("to_balance".equals(type) || "to_frozen".equals(type) ) {
            return "-" + money;
        }
        return "" + money;
    }
}
