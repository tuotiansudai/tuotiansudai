package com.tuotiansudai.dto.sms;


import java.util.List;

public class SmsLoanOutNotifyDto {

    private List<String> mobile;

    private String loanName;

    private String baseRate;

    public List<String> getMobile() {
        return mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }
}
