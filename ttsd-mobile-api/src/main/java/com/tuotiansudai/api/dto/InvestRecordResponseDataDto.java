package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.utils.AmountUtil;

import java.text.SimpleDateFormat;

public class InvestRecordResponseDataDto {
    private String userName;
    private String investTime;
    private String investMoney;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public InvestRecordResponseDataDto(){

    }
    public InvestRecordResponseDataDto(InvestModel input){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setUserName(CommonUtils.encryptUserName(input.getLoginName()));
        this.setInvestMoney(AmountUtil.convertCentToString(input.getAmount()));
        this.setInvestTime(simpleDateFormat.format(input.getCreatedTime()));
    }
}
