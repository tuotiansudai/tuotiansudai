package com.tuotiansudai.util;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

public class CalculateUtil {

    public static double calculatePercentage(long divisor, long divided, int scale) {
        return divided > 0 ? new BigDecimal(((double) divisor / divided) * 100).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue() : 0L;
    }

    public static Date calculateMaxDate() {
        return new DateTime(9999, 12, 31, 0, 0, 0).toDate();
    }
}
