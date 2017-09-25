package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class LoanInvestAmountView extends InvestAchievementView implements Serializable{
    private String loanDesc;
    private String userName;

    public String getLoanDesc() {
        return loanDesc;
    }

    public void setLoanDesc(String loanDesc) {
        this.loanDesc = loanDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
