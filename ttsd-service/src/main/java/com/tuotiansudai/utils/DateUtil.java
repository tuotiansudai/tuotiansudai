package com.tuotiansudai.utils;

import java.util.Calendar;

import java.util.Date;

public class DateUtil {

    public static Date getFirstDayOfMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        cDay.set(Calendar.SECOND, 0);
        cDay.set(Calendar.MILLISECOND, 0);
        return cDay.getTime();
    }

    public static long differenceMinute(Date date1, Date date2) {
        long minutes=0;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        if (time1 < time2) {
            minutes = (time2 - time1)/(1000 * 60);
        }
        return minutes;
    }

}
