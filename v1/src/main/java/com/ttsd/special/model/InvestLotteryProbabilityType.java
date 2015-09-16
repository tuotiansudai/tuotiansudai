package com.ttsd.special.model;

/**
 * Created by Administrator on 2015/9/9.
 */
public enum InvestLotteryProbabilityType {

    ONE(new int[]{100}),
    TEN(new int[]{70,100}),
    ONEHUNDRED(new int[]{60,90,100}),
    THREEHUNDERD(new int[]{55,85,95,100}),
    FIVEHUNDRED(new int[]{20,50,80,99,100});

    private final int[] intScale;

    InvestLotteryProbabilityType(int[] ints) {
        this.intScale = ints;
    }

    public int[] getIntScale() {
        return intScale;
    }

}
