package com.tuotiansudai.utils;

public class AutoInvestMonthPeriod {
    public static AutoInvestMonthPeriod Month_1 = new AutoInvestMonthPeriod(1, "1月期");
    public static AutoInvestMonthPeriod Month_2 = new AutoInvestMonthPeriod(1 << 1, "2月期");
    public static AutoInvestMonthPeriod Month_3 = new AutoInvestMonthPeriod(1 << 2, "3月期");
    public static AutoInvestMonthPeriod Month_4 = new AutoInvestMonthPeriod(1 << 3, "4月期");
    public static AutoInvestMonthPeriod Month_5 = new AutoInvestMonthPeriod(1 << 4, "5月期");
    public static AutoInvestMonthPeriod Month_6 = new AutoInvestMonthPeriod(1 << 5, "6月期");
    public static AutoInvestMonthPeriod Month_7 = new AutoInvestMonthPeriod(1 << 6, "7月期");
    public static AutoInvestMonthPeriod Month_8 = new AutoInvestMonthPeriod(1 << 7, "8月期");
    public static AutoInvestMonthPeriod Month_9 = new AutoInvestMonthPeriod(1 << 8, "9月期");
    public static AutoInvestMonthPeriod Month_10 = new AutoInvestMonthPeriod(1 << 9, "10月期");
    public static AutoInvestMonthPeriod Month_11 = new AutoInvestMonthPeriod(1 << 10, "11月期");
    public static AutoInvestMonthPeriod Month_12 = new AutoInvestMonthPeriod(1 << 11, "12月期");

    public static AutoInvestMonthPeriod[] AllPeriods = new AutoInvestMonthPeriod[]{
            Month_1, Month_2, Month_3, Month_4, Month_5, Month_6, Month_7, Month_8, Month_9, Month_10, Month_11, Month_12
    };

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
