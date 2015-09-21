package com.tuotiansudai.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountUtil {

    public static long convertStringToCent(String amount) {
        Pattern pattern = Pattern.compile("^\\d+\\.\\d{2}$");
        Matcher matcher = pattern.matcher(amount);
        if (matcher.matches()) {
            String[] split = amount.split("\\.");
            long integer = Long.parseLong(split[0]);
            int fraction = Integer.parseInt(split[1]);
            return integer * 100 + fraction;
        }
        return 0;
    }

    public static String convertCentToString(long amount) {
        int MULTIPLIER = 100;
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
        Matcher matcher = pattern.matcher(String.valueOf(amount));
        if (matcher.matches()) {
            return new BigDecimal(String.valueOf(amount)).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        }
        return "0";
    }
}
