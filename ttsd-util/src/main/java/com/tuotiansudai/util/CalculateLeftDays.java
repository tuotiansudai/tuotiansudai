package com.tuotiansudai.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CalculateLeftDays {

    public static String calculateTransferApplicationLeftDays(Date repayDate){
        long leftDays = ChronoUnit.DAYS.between(LocalDate.now(), repayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return String.valueOf(leftDays > 0 ? leftDays : 0);
    }
}
