package com.tuotiansudai.console.bi.repository.model;

import java.io.Serializable;
import java.util.List;

public class InvestViscosityDetailTableView implements Serializable {

    long sumAmount;

    int totalCount;

    List<InvestViscosityDetailView> items;

    public InvestViscosityDetailTableView() {

    }

    public InvestViscosityDetailTableView(long sumAmount, int totalCount, List<InvestViscosityDetailView> items) {
        this.sumAmount = sumAmount;
        this.items = items;
        this.totalCount = totalCount;
    }

    public List<InvestViscosityDetailView> getItems() {
        return items;
    }

    public void setItems(List<InvestViscosityDetailView> items) {
        this.items = items;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
