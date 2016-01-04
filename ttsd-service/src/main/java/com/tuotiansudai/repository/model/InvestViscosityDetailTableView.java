package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.List;

public class InvestViscosityDetailTableView implements Serializable {

    long sumAmount;

    List<InvestViscosityDetailView> items;

    public InvestViscosityDetailTableView() {

    }

    public InvestViscosityDetailTableView(long sumAmount, List<InvestViscosityDetailView> items) {
        this.sumAmount = sumAmount;
        this.items = items;
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
}
