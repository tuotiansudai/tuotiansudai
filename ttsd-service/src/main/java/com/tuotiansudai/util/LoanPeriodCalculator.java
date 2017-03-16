package com.tuotiansudai.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.repository.model.LoanType;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;
import java.util.List;

public class LoanPeriodCalculator {

    // 放款当天到借款截止时间之间的天数以30天为一期
    public static int calculateLoanPeriods(Date recheckTime, Date deadline, LoanType loanType) {
        int duration = LoanPeriodCalculator.calculateDuration(recheckTime, deadline);
        if (duration < 1) {
            return 0;
        }
        int totalPeriods = duration % InterestCalculator.DAYS_OF_MONTH == 0 ? duration / InterestCalculator.DAYS_OF_MONTH : duration / InterestCalculator.DAYS_OF_MONTH + 1;
        return LoanPeriodUnit.DAY == loanType.getLoanPeriodUnit() ? 1 : totalPeriods;
    }

    public static List<Integer> calculateDaysOfPerPeriod(Date recheckTime, Date deadline, LoanType loanType) {
        int loanDuration = LoanPeriodCalculator.calculateDuration(recheckTime, deadline);
        if (loanDuration == 0) {
            return Lists.newArrayList();
        }

        int loanPeriods = LoanPeriodCalculator.calculateLoanPeriods(recheckTime, deadline, loanType);

        if (loanPeriods == 1) {
            return Lists.newArrayList(loanDuration);
        }

        List<Integer> daysOfPeriod = Lists.newArrayList();
        for (int index = 0; index < loanPeriods; index++) {
            if (index == 0) {
                daysOfPeriod.add(loanDuration % InterestCalculator.DAYS_OF_MONTH != 0 ? loanDuration % InterestCalculator.DAYS_OF_MONTH : InterestCalculator.DAYS_OF_MONTH);
                continue;
            }
            daysOfPeriod.add(InterestCalculator.DAYS_OF_MONTH);
        }
        return daysOfPeriod;
    }

    // 放款当天到借款截止时间之间的天数（包括放款当天和借款截止时间当天）
    public static int calculateDuration(Date startTime, Date endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return 0;
        }
        return Days.daysBetween(new DateTime(startTime).withTimeAtStartOfDay(), new DateTime(endTime)).getDays() + 1;
    }
}
