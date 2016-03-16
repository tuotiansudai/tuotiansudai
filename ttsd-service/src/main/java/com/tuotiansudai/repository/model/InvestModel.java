package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestModel implements Serializable {
    /**
     * 投资ID
     */
    private long id;

    /**
     * 转让人投资id
     */
    private Long transferInvestId;
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
     * 转让状态
     */
    private TransferStatus transferStatus;
    /**
     * 投资来源
     */
    private Source source;
    /**
     * 是否为自动投资
     */
    private boolean isAutoInvest;
    /**
     * 应用安装来源渠道
     */
    private String channel;
    /**
     * 创建时间
     */
    private Date createdTime = new Date();

    public InvestModel(){

    }

    public InvestModel(long id, long loanId, Long transferInvestId, long amount, String loginName, Source source, String channel) {
        this.id = id;
        this.transferInvestId = transferInvestId;
        this.loginName = loginName;
        this.loanId = loanId;
        this.amount = amount;
        this.source = source;
        this.channel = channel;
        this.status = InvestStatus.WAIT_PAY;
        this.isAutoInvest = Source.AUTO == source;
        this.createdTime = new Date();
        this.transferStatus = TransferStatus.TRANSFERABLE;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(Long transferInvestId) {
        this.transferInvestId = transferInvestId;
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

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
