package com.tuotiansudai.utils;

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
}
