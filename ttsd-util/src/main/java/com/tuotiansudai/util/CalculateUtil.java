package com.tuotiansudai.util;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.Date;

public class CalculateUtil {

    public static double calculatePercentage(long divisor, long divided, int scale) {
        return new BigDecimal(((double)divisor/divided) * 100).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Date calculateMaxDate(){
        return new DateTime(9999, 12, 31, 0, 0, 0).toDate();
    }

    //两个日期之间的天数（包括startTime和endTime）
    public static int calculateDuration(Date startTime, Date endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return 0;
        }
        return Days.daysBetween(new DateTime(startTime).withTimeAtStartOfDay(), new DateTime(endTime)).getDays() + 1;
    }
}
