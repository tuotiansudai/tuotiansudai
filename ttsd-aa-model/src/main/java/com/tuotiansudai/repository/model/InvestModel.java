package com.tuotiansudai.repository.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InvestModel implements Serializable {
    /**
     * 投资ID
     */
    private long id;
    /**
     * 银行订单号
     */
    private String bankOrderNo;
    /**
     * 银行订单日期
     */
    private String bankOrderDate;
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


    private String contractNo;
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
     * 应用安装来源渠道
     */
    private String channel;
    /**
     * 是否为无密投资
     */
    private boolean isNoPasswordInvest;
    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 交易完成时间
     */
    private Date tradingTime;

    /**
     * 投资时间
    */
    private Date investTime;

    /**
     * 成就
     */
    private List<InvestAchievement> achievements = Lists.newArrayList();

    /**
     * 手续费费率
     * */
    private double investFeeRate;

    public InvestModel() {
    }

    public InvestModel(long id, long loanId, Long transferInvestId, String loginName, long amount, double investFeeRate, boolean isNoPassword, Date investTime, Source source, String channel) {
        this.id = id;
        this.transferInvestId = transferInvestId;
        this.loginName = loginName;
        this.loanId = loanId;
        this.amount = amount;
        this.source = source;
        this.channel = channel;
        this.status = InvestStatus.WAIT_PAY;
        this.investTime = investTime;
        this.transferStatus = TransferStatus.TRANSFERABLE;
        this.investFeeRate = investFeeRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public void setBankOrderDate(String bankOrderDate) {
        this.bankOrderDate = bankOrderDate;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isNoPasswordInvest() {
        return isNoPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        isNoPasswordInvest = noPasswordInvest;
    }

    public Date getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(Date tradingTime) {
        this.tradingTime = tradingTime;
    }

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<InvestAchievement> achievements) {
        this.achievements = achievements;
    }

    public double getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(double investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
}
