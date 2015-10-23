package com.tuotiansudai.utils;


import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getFirstDayOfMonth(Date date)   {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        cDay.set(Calendar.SECOND, 0);
        cDay.set(Calendar.MILLISECOND, 0);
        return cDay.getTime();
    }

}
