package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;

public class ZeroShoppingPrizeConfigModel implements Serializable {

    private long id;
    private ZeroShoppingPrize prize;
    private int prizeTotal;
    private int prizeSurplus;

    public ZeroShoppingPrizeConfigModel(long id, ZeroShoppingPrize prize, int prizeTotal, int prizeSurplus) {
        this.id = id;
        this.prize = prize;
        this.prizeTotal = prizeTotal;
        this.prizeSurplus = prizeSurplus;
    }

    public ZeroShoppingPrizeConfigModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ZeroShoppingPrize getPrize() {
        return prize;
    }

    public void setPrize(ZeroShoppingPrize prize) {
        this.prize = prize;
    }

    public int getPrizeTotal() {
        return prizeTotal;
    }

    public void setPrizeTotal(int prizeTotal) {
        this.prizeTotal = prizeTotal;
    }

    public int getPrizeSurplus() {
        return prizeSurplus;
    }

    public void setPrizeSurplus(int prizeSurplus) {
        this.prizeSurplus = prizeSurplus;
    }
}
