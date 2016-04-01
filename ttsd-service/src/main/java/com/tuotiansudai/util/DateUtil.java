package com.tuotiansudai.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public static int differenceSeconds(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse(date1);
            end = dfs.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)(end.getTime() - begin.getTime()) / 1000;
    }

}
