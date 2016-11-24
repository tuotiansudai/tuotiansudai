package com.tuotiansudai.util;

import java.math.BigDecimal;

public class CalculateUtil {

    public static double calculatePercentage(long divisor, long divided, int scale) {
        return new BigDecimal(((double)divisor/divided) * 100).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
