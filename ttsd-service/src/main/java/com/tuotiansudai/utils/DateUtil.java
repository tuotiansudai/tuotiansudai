package com.tuotiansudai.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/6.
 */
public class DateUtil {

    static Logger logger = Logger.getLogger(DateUtil.class);

    public static int calculateIntervalDays(Date date, Date otherDate) {
        int num = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTmp = null;
        Date otherDateTmp = null;
        try {
            dateTmp = sdf.parse(sdf.format(date));
            otherDateTmp = sdf.parse(sdf.format(otherDate));
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (dateTmp != null && otherDateTmp != null) {
            long time = otherDateTmp.getTime() - dateTmp.getTime();
            num = (int) (time / (24 * 60 * 60 * 1000));
        }
        return num;
    }

    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

}
