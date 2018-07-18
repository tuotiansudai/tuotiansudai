package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.request.UmpRechargeRequestDto;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qduljs2011 on 2018/7/10.
 */
public class RechargeModel implements Serializable{
    private long id;

    private String loginName;

    private long amount;

    private String bankCode;

    private BankRechargeStatus status;

    private Source source;

    private boolean fastPay;

    private boolean publicPay;

    private String channel;

    private Date createdTime;

    public RechargeModel() {

    }

    public RechargeModel(UmpRechargeRequestDto dto) {
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.bankCode = dto.getBankCode();
        this.loginName = dto.getLoginName();
        this.status = BankRechargeStatus.WAIT_PAY;
        this.source = Source.WEB;
        this.fastPay = dto.isFastPay();
        this.publicPay = dto.isPublicPay();
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public BankRechargeStatus getStatus() {
        return status;
    }

    public void setStatus(BankRechargeStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }

    public boolean isPublicPay() {
        return publicPay;
    }

    public void setPublicPay(boolean publicPay) {
        this.publicPay = publicPay;
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
