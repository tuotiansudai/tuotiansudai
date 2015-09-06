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

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("参数scale必须为整数或零!");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
