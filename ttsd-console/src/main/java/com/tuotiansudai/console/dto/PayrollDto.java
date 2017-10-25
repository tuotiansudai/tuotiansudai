package com.tuotiansudai.console.dto;


import java.io.Serializable;
import java.util.List;

public class PayrollDto implements Serializable {

    private String title;

    private boolean status;

    private long totalAmount;

    private long headCount;

    private List<PayrollDetailDto> payrollDetailDtoList;

    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PayrollDetailDto> getPayrollDetailDtoList() {
        return payrollDetailDtoList;
    }

    public void setPayrollDetailDtoList(List<PayrollDetailDto> payrollDetailDtoList) {
        this.payrollDetailDtoList = payrollDetailDtoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
