package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class InvestJsonDetailDto extends InvestDetailDto {
    @JsonProperty("nextRepayAmount")
    public String getNextRepayAmountString() {
        return AmountUtil.convertCentToString(super.getNextRepayAmount());
    }

    @JsonIgnore
    @Override
    public long getNextRepayAmount() {
        return super.getNextRepayAmount();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getNextRepayDay() {
        return super.getNextRepayDate();
    }

    @JsonIgnore
    @Override
    public Date getNextRepayDate() {
        return super.getNextRepayDate();
    }

    public String getLoanStatusDesc() {
        return this.getLoanStatus().getDescription();
    }

    public String getLoanTypeName() {
        return this.getLoanType().getName();
    }

    public String getInvestStatus() {
        return this.getStatus().getDescription();
    }

    @JsonIgnore
    @Override
    public InvestStatus getStatus() {
        return super.getStatus();
    }

    @JsonIgnore
    @Override
    public LoanStatus getLoanStatus() {
        return super.getLoanStatus();
    }

    @JsonProperty("hasContract")
    public boolean hasContract() {
        LoanStatus ls = super.getLoanStatus();
        return (
               ls != LoanStatus.WAITING_VERIFY
            && ls != LoanStatus.PREHEAT
            && ls != LoanStatus.RAISING
            && ls != LoanStatus.CANCEL
        );
    }

    @JsonIgnore
    @Override
    public LoanType getLoanType() {
        return super.getLoanType();
    }

    @Override
    public String getUserReferrer() {
        if (StringUtils.isBlank(super.getUserReferrer())) {
            return "";
        } else {
            return super.getUserReferrer();
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public Date getCreatedTime() {
        return super.getCreatedTime();
    }

    public InvestJsonDetailDto(InvestDetailDto dto) {
        this.setLoanStatus(dto.getLoanStatus());
        this.setLoginName(dto.getLoginName());
        this.setAmount(dto.getAmount());
        this.setCreatedTime(dto.getCreatedTime());
        this.setId(dto.getId());
        this.setIsAutoInvest(dto.isAutoInvest());
        this.setLoanId(dto.getLoanId());
        this.setLoanName(dto.getLoanName());
        this.setLoanType(dto.getLoanType());
        this.setSource(dto.getSource());
        this.setStatus(dto.getStatus());
        this.setUserReferrer(dto.getUserReferrer());
        this.setNextRepayAmount(dto.getNextRepayAmount());
        this.setNextRepayDate(dto.getNextRepayDate());
    }
}
