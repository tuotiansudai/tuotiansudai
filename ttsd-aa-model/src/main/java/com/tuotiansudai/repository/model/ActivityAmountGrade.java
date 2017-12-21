package com.tuotiansudai.repository.model;


import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ActivityAmountGrade {

    CASH_SNOWBALL1("CASH_SNOWBALL", 10000000l, 20000000l, 10000l),
    CASH_SNOWBALL2("CASH_SNOWBALL", 20000000l, 30000000l, 30000l),
    CASH_SNOWBALL3("CASH_SNOWBALL", 30000000l, 50000000l, 60000l),
    CASH_SNOWBALL4("CASH_SNOWBALL", 50000000l, 60000000l, 120000l),
    CASH_SNOWBALL5("CASH_SNOWBALL", 60000000l, Long.MAX_VALUE, 201800l),
    ;

    public static long getAwardAmount(String activityName, long amount){
        List<ActivityAmountGrade> list = Arrays.stream(ActivityAmountGrade.values())
                .filter(activityAmountGrade -> activityAmountGrade.activityName.equals(activityName)
                        && activityAmountGrade.minAmount <= amount
                        && amount < activityAmountGrade.maxAmount).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        return list.get(0).award;
    }

    private String activityName;
    private long minAmount;
    private long maxAmount;
    private long award;

    ActivityAmountGrade(String activityName, long minAmount, long maxAmount, long award) {
        this.activityName = activityName;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.award = award;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(long minAmount) {
        this.minAmount = minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getAward() {
        return award;
    }

    public void setAward(long award) {
        this.award = award;
    }
}
