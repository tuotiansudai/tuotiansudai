package com.tuotiansudai.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tuotian on 15/8/23.
 */
public class DateCompare {
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE="yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND="yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HOUR="yyyy-MM-dd HH";
    public static final String YEAR_MONTH_DAY="yyyy-MM-dd";
    public static Integer compareDateStr(String firstDateStr,String firstFormatStr,String secondDateStr,String secondFormatStr){
        Integer result = null;
        SimpleDateFormat firstSimpleDateFormat = new SimpleDateFormat(firstFormatStr);
        SimpleDateFormat secondSimpleDateFormat = new SimpleDateFormat(secondFormatStr);
        try {
            Date firstDate = firstSimpleDateFormat.parse(firstDateStr);
            Date secondDate = secondSimpleDateFormat.parse(secondDateStr);
            Calendar firstCalendar = Calendar.getInstance();
            Calendar secondCalendar = Calendar.getInstance();
            firstCalendar.setTime(firstDate);
            secondCalendar.setTime(secondDate);
            result = firstCalendar.compareTo(secondCalendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Integer compareDate(Date firstDate,Date secondDate){
        Integer result = null;
        Calendar firstCalendar = Calendar.getInstance();
        Calendar secondCalendar = Calendar.getInstance();
        firstCalendar.setTime(firstDate);
        secondCalendar.setTime(secondDate);
        result = firstCalendar.compareTo(secondCalendar);
        return result;
    }
}
