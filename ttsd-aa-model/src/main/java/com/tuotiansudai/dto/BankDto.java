package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class BankDto extends BaseDataDto implements Serializable {

    private long id;

    private String name;

    private String bankCode;

    private Integer seq;

    private String imageUrl;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String singleAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String singleDayAmount;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    private Boolean isBank;

    public BankDto(){
    }
    public BankDto(BankModel bankModel){
        this.id = bankModel.getId();
        this.name = bankModel.getName();
        this.seq = bankModel.getSeq();
        this.bankCode = bankModel.getBankCode();
        this.imageUrl = bankModel.getImageUrl();
        this.singleAmount = AmountConverter.convertCentToString(bankModel.getSingleAmount());
        this.singleDayAmount = AmountConverter.convertCentToString(bankModel.getSingleDayAmount());
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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(String singleAmount) {
        this.singleAmount = singleAmount;
    }

    public String getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(String singleDayAmount) {
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

    public Boolean getIsBank() {
        return isBank;
    }

    public void setIsBank(Boolean isBank) {
        this.isBank = isBank;
    }
}
