package com.tuotiansudai.dto.sms;


import java.io.Serializable;
import java.util.List;

public class LoanOutCompleteNotifyDto implements Serializable {

    private List<String> mobiles;

    private String loanName;

    private String baseRate;

    public LoanOutCompleteNotifyDto() {
    }

    public LoanOutCompleteNotifyDto(List<String> mobiles, String loanName, String baseRate) {
        this.mobiles = mobiles;
        this.loanName = loanName;
        this.baseRate = baseRate;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
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
