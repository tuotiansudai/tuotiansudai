package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.utils.AmountUtil;
import java.util.Date;

public class InvestModel {
    /**
     * 投资ID
     */
    private long id;
    /**
     * 投资人ID
     */
    private String loginName;
    /**
     * 标的ID
     */
    private long loanId;
    /**
     * 投资金额
     */
    private long amount;
    /**
     * 投资状态
     */
    private InvestStatus status;
    /**
     * 投资来源渠道
     */
    private Source source;
    /**
     * 是否为自动投资
     */
    private boolean isAutoInvest;
    /**
     * 创建时间
     */
    private Date createdTime = new Date();

    public InvestModel(){

    }

    public InvestModel(InvestDto dto){
        this.loginName = dto.getLoginName();
        this.amount = AmountUtil.convertStringToCent(dto.getAmount());
        this.loanId = Long.parseLong(dto.getLoanId());
        this.source = dto.getSource();
        this.status = InvestStatus.WAITING;
        this.isAutoInvest = false;
        this.createdTime = new Date();
    }

    public InvestModel(long loanId, long amount, String loginName, Source source) {
        this.loanId = loanId;
        this.amount = amount;
        this.loginName = loginName;
        this.source = source;
        this.status = InvestStatus.WAITING;
        this.isAutoInvest = false;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public InvestStatus getStatus() {
        return status;
    }

    public void setStatus(InvestStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isAutoInvest() {
        return isAutoInvest;
    }

    public void setIsAutoInvest(boolean isAutoInvest) {
        this.isAutoInvest = isAutoInvest;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
