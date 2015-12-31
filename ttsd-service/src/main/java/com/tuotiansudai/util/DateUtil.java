package com.tuotiansudai.util;

import java.util.Date;

public class DateUtil {

    public static long differenceDay(Date date1, Date date2) {
        long minutes = 0;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        if (time1 < time2) {
            minutes = (time2 - time1) / (1000 * 60 * 60 * 24);
        }
        return minutes;
    }

}
