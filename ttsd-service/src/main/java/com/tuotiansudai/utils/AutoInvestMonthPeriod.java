package com.tuotiansudai.utils;

public class AutoInvestMonthPeriod {
    public static AutoInvestMonthPeriod Month_1 = new AutoInvestMonthPeriod(1);
    public static AutoInvestMonthPeriod Month_2 = new AutoInvestMonthPeriod(1 << 1);
    public static AutoInvestMonthPeriod Month_3 = new AutoInvestMonthPeriod(1 << 2);
    public static AutoInvestMonthPeriod Month_4 = new AutoInvestMonthPeriod(1 << 3);
    public static AutoInvestMonthPeriod Month_5 = new AutoInvestMonthPeriod(1 << 4);
    public static AutoInvestMonthPeriod Month_6 = new AutoInvestMonthPeriod(1 << 5);
    public static AutoInvestMonthPeriod Month_7 = new AutoInvestMonthPeriod(1 << 6);
    public static AutoInvestMonthPeriod Month_8 = new AutoInvestMonthPeriod(1 << 7);
    public static AutoInvestMonthPeriod Month_9 = new AutoInvestMonthPeriod(1 << 8);
    public static AutoInvestMonthPeriod Month_10 = new AutoInvestMonthPeriod(1 << 9);
    public static AutoInvestMonthPeriod Month_11 = new AutoInvestMonthPeriod(1 << 10);
    public static AutoInvestMonthPeriod Month_12 = new AutoInvestMonthPeriod(1 << 11);

    public static AutoInvestMonthPeriod[] AllPeriods = new AutoInvestMonthPeriod[]{
            Month_1, Month_2, Month_3, Month_4, Month_5, Month_6, Month_7, Month_8, Month_9, Month_10, Month_11, Month_12
    };

    private final int periodValue;

    public AutoInvestMonthPeriod(int period) {
        periodValue = period;
    }

    public AutoInvestMonthPeriod(int... period) {
        int mp = 0;
        for (int p : period) {
            mp = mp | p;
        }
        periodValue = mp;
    }

    public AutoInvestMonthPeriod(AutoInvestMonthPeriod... period) {
        int mp = 0;
        for (AutoInvestMonthPeriod p : period) {
            mp = mp | p.periodValue;
        }
        periodValue = mp;
    }

    public boolean contains(int period) {
        return (periodValue & period) == period;
    }

    public boolean contains(AutoInvestMonthPeriod period) {
        return contains(period.periodValue);
    }

    public int intValue() {
        return periodValue;
    }
}
