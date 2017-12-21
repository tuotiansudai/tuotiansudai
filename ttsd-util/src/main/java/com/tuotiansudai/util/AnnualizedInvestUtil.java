package com.tuotiansudai.util;

public class AnnualizedInvestUtil {

    public static long annualizedInvestAmount(long investAmount, int duration) {
        switch (duration) {
            case 90:
                return investAmount / 4;
            case 180:
                return investAmount / 2;
            case 360:
                return investAmount;
        }
        return 0l;
    }
}
