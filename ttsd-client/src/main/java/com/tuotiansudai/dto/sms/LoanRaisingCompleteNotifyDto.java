package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class LoanRaisingCompleteNotifyDto implements Serializable {

    @NotEmpty
    private List<String> mobiles;

    @NotEmpty
    private String loanRaisingStartDate;

    @NotEmpty
    private String loanName;

    @NotEmpty
    private String loanAmount;

    @NotEmpty
    private String loanDuration;

    @NotEmpty
    private String loanerName;

    @NotEmpty
    private String agentName;

    @NotEmpty
    private String loanRaisingCompleteTime;

    public LoanRaisingCompleteNotifyDto() {
    }

    public LoanRaisingCompleteNotifyDto(List<String> mobiles, String loanRaisingStartDate, String loanName, String loanAmount,
                                        String loanDuration, String loanerName, String agentName, String loanRaisingCompleteTime) {
        this.mobiles = mobiles;
        this.loanRaisingStartDate = loanRaisingStartDate;
        this.loanName = loanName;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.loanerName = loanerName;
        this.agentName = agentName;
        this.loanRaisingCompleteTime = loanRaisingCompleteTime;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getLoanRaisingStartDate() {
        return loanRaisingStartDate;
    }

    public void setLoanRaisingStartDate(String loanRaisingStartDate) {
        this.loanRaisingStartDate = loanRaisingStartDate;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(String loanDuration) {
        this.loanDuration = loanDuration;
    }

    public String getLoanerName() {
        return loanerName;
    }

    public void setLoanerName(String loanerName) {
        this.loanerName = loanerName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLoanRaisingCompleteTime() {
        return loanRaisingCompleteTime;
    }

    public void setLoanRaisingCompleteTime(String loanRaisingCompleteTime) {
        this.loanRaisingCompleteTime = loanRaisingCompleteTime;
    }
}
