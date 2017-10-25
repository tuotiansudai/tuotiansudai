package com.tuotiansudai.console.dto;


import java.io.Serializable;

public class PayrollDetailDto implements Serializable {

    private String payrollId;

    private String userName;

    private String mobile;

    private long amount;

    public PayrollDetailDto(){

    }

    public PayrollDetailDto(String userName, String mobile, long amount) {
        this.userName = userName;
        this.mobile = mobile;
        this.amount = amount;
    }

    public String getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(String payrollId) {
        this.payrollId = payrollId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
