package com.tuotiansudai.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountUtil {

    public static long convertStringToCent(String amount) {
        Pattern pattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
        Matcher matcher = pattern.matcher(amount);
        if (matcher.matches()) {
            String[] split = amount.split("\\.");
            long integer = Long.parseLong(split[0]);
            int fraction = 0;
            if (split.length > 1) {
                String fractionString = split[1];
                fraction = fractionString.length() == 1 ? Integer.parseInt(fractionString) * 10 : Integer.parseInt(fractionString);
            }
            return integer * 100 + fraction;
        }
        return 0;
    }

    public static String convertCentToString(long amount) {
        return String.format("%.2f", amount / 100D);
    }

    public static double div(long v1, long v2, int scale) {
        if (scale < 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
