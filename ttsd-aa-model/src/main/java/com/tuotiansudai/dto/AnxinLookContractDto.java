package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AnxinContractType;

import java.io.Serializable;

public class AnxinLookContractDto implements Serializable {

    private String loginName;

    private Long loanId;

    private Long investId;

    private AnxinContractType anxinContractType;

    public AnxinLookContractDto() {
    }

    public AnxinLookContractDto(String loginName, Long loanId, Long investId, AnxinContractType anxinContractType) {
        this.loginName = loginName;
        this.loanId = loanId;
        this.investId = investId;
        this.anxinContractType = anxinContractType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public AnxinContractType getAnxinContractType() {
        return anxinContractType;
    }

    public void setAnxinContractType(AnxinContractType anxinContractType) {
        this.anxinContractType = anxinContractType;
    }
}
