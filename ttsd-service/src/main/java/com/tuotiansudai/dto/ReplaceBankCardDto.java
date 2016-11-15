package com.tuotiansudai.dto;


import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.util.BankCardUtil;

import java.util.Date;

public class ReplaceBankCardDto {

    private long id;
    private String loginName;
    private String mobile;
    private String oldCard;
    private String applyCard;
    private Date applyDate;
    private BankCardStatus status;
    private String remark;

    public ReplaceBankCardDto() {
    }

    public ReplaceBankCardDto(long id, String loginName, String mobile,String oldCode, String oldNum, String applyCode, String applyNum, Date applyDate, BankCardStatus status, String remark) {
        this.id = id;
        this.loginName = loginName;
        this.mobile = mobile;
        this.oldCard = Strings.isNullOrEmpty(oldCode) ? "" : BankCardUtil.getBankName(oldCode) + String.format("(尾号%s)",oldNum.length() == 19 ? oldNum.substring(oldNum.length() - 4,oldNum.length())  : "");
        this.applyCard = Strings.isNullOrEmpty(applyCode) ? "" : BankCardUtil.getBankName(applyCode) + String.format("(尾号%s)",applyNum.length() == 19 ? applyNum.substring(applyNum.length() - 4,applyNum.length())  : "");
        this.applyDate = applyDate;
        this.status = status;
        this.remark = remark;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOldCard() {
        return oldCard;
    }

    public void setOldCard(String oldCard) {
        this.oldCard = oldCard;
    }

    public String getApplyCard() {
        return applyCard;
    }

    public void setApplyCard(String applyCard) {
        this.applyCard = applyCard;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public BankCardStatus getStatus() {
        return status;
    }

    public void setStatus(BankCardStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
