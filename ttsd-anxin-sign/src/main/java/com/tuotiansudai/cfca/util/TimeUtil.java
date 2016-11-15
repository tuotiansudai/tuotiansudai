package com.tuotiansudai.cfca.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static final String FORMAT_14 = "yyyyMMddHHmmss";
    public static final String FORMAT_14_TEXT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_8 = "yyyyMMdd";
    public static final String FORMAT_8_TEXT = "yyyy-MM-dd";
    public static final String FORMAT_6 = "HHmmss";
    public static final String FORMAT_6_TEXT = "HH:mm:ss";

    public static String getTimeInFormat(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getDateInFormat(String time, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(time);
    }

    public static String getCurrentTime(String format) {
        return getTimeInFormat(new Date(), format);
    }

    public static Date getOffsetDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static String getChangedFormat(String time, String fromFormat, String toFormat) throws ParseException {
        return new SimpleDateFormat(toFormat).format(new SimpleDateFormat(fromFormat).parse(time));
    }
}
