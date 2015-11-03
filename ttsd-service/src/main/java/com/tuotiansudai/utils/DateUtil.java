package com.tuotiansudai.utils;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/8.
 */
public class DateUtil {

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
