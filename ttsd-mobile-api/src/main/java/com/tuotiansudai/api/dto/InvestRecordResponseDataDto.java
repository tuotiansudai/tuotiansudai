package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;
import java.util.List;

public class InvestRecordResponseDataDto {
    private String userName;
    private String investTime;
    private String investMoney;

    private List<InvestAchievement> achievements;

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

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<InvestAchievement> achievements) {
        this.achievements = achievements;
    }

    public InvestRecordResponseDataDto(){
    }

    public InvestRecordResponseDataDto(InvestModel input){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setUserName(input.getLoginName());
        this.setInvestMoney(AmountConverter.convertCentToString(input.getAmount()));
        this.setInvestTime(simpleDateFormat.format(input.getTradingTime() == null ? input.getCreatedTime() : input.getTradingTime()));
        this.setAchievements(input.getAchievements());
    }
}
