package com.tuotiansudai.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountConverter {

    public static long convertStringToCent(String amount) {
        if (StringUtils.isEmpty(amount)) {
            return 0;
        }
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
}
