package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanOutHistoryDto implements Serializable {

    private final static Logger logger = Logger.getLogger(LoanOutHistoryDto.class);

    @NotNull
    private Long id;

    @JsonProperty("reserve_account")
    @NotNull
    private String reserveAccount;

    @JsonProperty("agent_account")
    @NotNull
    private String agentAccount;

    @JsonProperty("interest_amount")
    @NotNull
    private Long interestAmount;

    @JsonProperty("deposit_amount")
    @NotNull
    private Long depositAmount;

    @JsonProperty("bill_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @NotNull
    private Date billDate;

    @JsonProperty("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @NotNull
    private Date createdTime;

    @JsonProperty("updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @NotNull
    private Date updateTime;

    @NotNull
    private LoanOutHistoryStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReserveAccount() {
        return reserveAccount;
    }

    public void setReserveAccount(String reserveAccount) {
        this.reserveAccount = reserveAccount;
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount;
    }

    public Long getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Long interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Long getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Long depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public LoanOutHistoryStatus getStatus() {
        return status;
    }

    public void setStatus(LoanOutHistoryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return "";
        }
    }
}
