package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.BankDto;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class BankModel implements Serializable {

    private long id;

    private String name;

    private String bankCode;

    private String imageUrl;

    private Integer seq;

    private long singleAmount;

    private long singleDayAmount;

    private String createdBy;

    private Date createdTime = new Date();

    private String updatedBy;

    private Date updatedTime;

    private boolean isBank;

    public BankModel() {
    }

    public BankModel(BankDto bankDto) {
        this.id = bankDto.getId();
        this.name = bankDto.getName();
        this.bankCode = bankDto.getBankCode();
        this.seq = bankDto.getSeq();
        this.imageUrl = bankDto.getImageUrl();
        this.singleAmount = AmountConverter.convertStringToCent(bankDto.getSingleAmount());
        this.singleDayAmount = AmountConverter.convertStringToCent(bankDto.getSingleDayAmount());
        this.createdBy = bankDto.getCreatedBy();
        this.createdTime = bankDto.getCreatedTime();
        this.updatedBy = bankDto.getUpdatedBy();
        this.updatedTime = bankDto.getUpdatedTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(long singleAmount) {
        this.singleAmount = singleAmount;
    }

    public long getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(long singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean getIsBank() {
        return isBank;
    }

    public void setIsBank(boolean isBank) {
        this.isBank = isBank;
    }

}
