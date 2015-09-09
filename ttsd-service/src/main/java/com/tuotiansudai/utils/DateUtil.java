package com.tuotiansudai.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static int judgeYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int year = Integer.parseInt(sdf.format(date).substring(0, 4));
        if(year%4==0 && year%100!=0 || year%400==0){
            return 366;
        } else {
            return 365;
        }
    }
}
