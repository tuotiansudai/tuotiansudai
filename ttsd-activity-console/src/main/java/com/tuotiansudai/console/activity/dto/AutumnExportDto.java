package com.tuotiansudai.console.activity.dto;

import java.util.Date;
import java.util.List;

public class AutumnExportDto {

    private String name;

    private long totalAmount;

    private Date investTime;

    private String prize;

    private List<UserItemExportDto> userItemExportDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public List<UserItemExportDto> getUserItemExportDtos() {
        return userItemExportDtos;
    }

    public void setUserItemExportDtos(List<UserItemExportDto> userItemExportDtos) {
        this.userItemExportDtos = userItemExportDtos;
    }
}
