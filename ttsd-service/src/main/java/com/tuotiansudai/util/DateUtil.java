package com.tuotiansudai.util;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtil {

    public static Date getFirstDayOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return new DateTime().withDate(dateTime.getYear(), dateTime.getMonthOfYear(), 1).withTimeAtStartOfDay().toDate();
    }

    public static long differenceMinute(Date date1, Date date2) {
        long minutes = 0;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        if (time1 < time2) {
            minutes = (time2 - time1) / (1000 * 60);
        }
        return minutes;
    }

}
