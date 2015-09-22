package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.RepayStatus;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class LoanRepayDto implements Serializable {
    private int index;
    private int pageSize;
    private String loanId;
    private String loginName;
    private String repayStartDate;
    private String repayEndDate;
    private RepayStatus repayStatus;



    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public RepayStatus getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(RepayStatus repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getRepayStartDate() {
        return repayStartDate;
    }

    public void setRepayStartDate(String repayStartDate) {
        this.repayStartDate = repayStartDate;
    }

    public String getRepayEndDate() {
        return repayEndDate;
    }

    public void setRepayEndDate(String repayEndDate) {
        this.repayEndDate = repayEndDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}