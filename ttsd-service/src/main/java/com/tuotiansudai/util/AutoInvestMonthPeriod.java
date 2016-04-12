package com.tuotiansudai.util;

import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.repository.model.LoanType;

import java.util.ArrayList;
import java.util.List;

public class AutoInvestMonthPeriod {

    // 值是这样的： [{1:天标}, {2:1月期}, {4:2月期}, {8:3月期}, {16:4月期}.....{value:name}]
    public static final AutoInvestMonthPeriod[] AllPeriods;

    public static final AutoInvestMonthPeriod Daily;
    public static final AutoInvestMonthPeriod Month_1;
    public static final AutoInvestMonthPeriod Month_2;
    public static final AutoInvestMonthPeriod Month_3;
    public static final AutoInvestMonthPeriod Month_4;
    public static final AutoInvestMonthPeriod Month_5;
    public static final AutoInvestMonthPeriod Month_6;
    public static final AutoInvestMonthPeriod Month_7;
    public static final AutoInvestMonthPeriod Month_8;
    public static final AutoInvestMonthPeriod Month_9;
    public static final AutoInvestMonthPeriod Month_10;
    public static final AutoInvestMonthPeriod Month_11;
    public static final AutoInvestMonthPeriod Month_12;


    static {
        List<AutoInvestMonthPeriod> periodList = new ArrayList<>(12);
        periodList.add(new AutoInvestMonthPeriod(1, "天标"));
        for (int i = 1; i <= 12; i++) {
            AutoInvestMonthPeriod p = new AutoInvestMonthPeriod(1 << i, i + "月期");
            periodList.add(p);
        }
        AllPeriods = periodList.toArray(new AutoInvestMonthPeriod[0]);

        Daily = AllPeriods[0];
        Month_1 = AllPeriods[1];
        Month_2 = AllPeriods[2];
        Month_3 = AllPeriods[3];
        Month_4 = AllPeriods[4];
        Month_5 = AllPeriods[5];
        Month_6 = AllPeriods[6];
        Month_7 = AllPeriods[7];
        Month_8 = AllPeriods[8];
        Month_9 = AllPeriods[9];
        Month_10 = AllPeriods[10];
        Month_11 = AllPeriods[11];
        Month_12 = AllPeriods[12];
    }

    public static void main(String[] args) {
        for(AutoInvestMonthPeriod p: AllPeriods){
            System.out.println(p.periodValue +": "+p.periodName);
        }
    }

    public static AutoInvestMonthPeriod generateFromLoanPeriod(LoanPeriodUnit loanPeriodUnit, int loanPeriod) {
        if(LoanPeriodUnit.MONTH == loanPeriodUnit) {
            return AllPeriods[loanPeriod];
        }else{
            return AllPeriods[0];
        }
    }

    public static AutoInvestMonthPeriod merge(int... period) {
        int mp = 0;
        for (int p : period) {
            mp = mp | p;
        }
        return new AutoInvestMonthPeriod(mp, "");
    }

    public static AutoInvestMonthPeriod merge(AutoInvestMonthPeriod... period) {
        int mp = 0;
        for (AutoInvestMonthPeriod p : period) {
            mp = mp | p.periodValue;
        }
        return new AutoInvestMonthPeriod(mp, "");
    }

    public static AutoInvestMonthPeriod[] split(int mergedPeriodsValue){
        List<AutoInvestMonthPeriod> selectedPeriods = new ArrayList<>();
        AutoInvestMonthPeriod mergedPeriod = new AutoInvestMonthPeriod(mergedPeriodsValue,"");
        for(AutoInvestMonthPeriod period : AllPeriods){
            if(mergedPeriod.contains(period)){
                selectedPeriods.add(period);
            }
        }
        return selectedPeriods.toArray(new AutoInvestMonthPeriod[0]);
    }


    private final int periodValue;
    private final String periodName;

    public AutoInvestMonthPeriod(int period, String name) {
        periodValue = period;
        periodName = name;
    }

    public boolean contains(int period) {
        return (periodValue & period) == period;
    }

    public boolean contains(AutoInvestMonthPeriod period) {
        return contains(period.periodValue);
    }

    public String getPeriodName() {
        return periodName;
    }

    public int getPeriodValue() {
        return periodValue;
    }


}
