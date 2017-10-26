package com.tuotiansudai.console.dto;

import com.tuotiansudai.repository.model.PayrollDetailModel;

import java.io.Serializable;
import java.util.List;

public class PayrollDataDto implements Serializable {
    private long totalAmount ;
    private long headCount;
    private List<PayrollDetailModel> payrollDetailModelList;
    private boolean status;
    private String message;

    public PayrollDataDto(){

    }
    public PayrollDataDto(boolean status, String message){
        this.status = status;
        this.message = message;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getHeadCount() {
        return headCount;
    }

    public void setHeadCount(long headCount) {
        this.headCount = headCount;
    }

    public List<PayrollDetailModel> getPayrollDetailModelList() {
        return payrollDetailModelList;
    }

    public void setPayrollDetailModelList(List<PayrollDetailModel> payrollDetailModelList) {
        this.payrollDetailModelList = payrollDetailModelList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

