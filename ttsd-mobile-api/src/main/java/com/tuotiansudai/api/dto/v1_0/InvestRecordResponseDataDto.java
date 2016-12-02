package com.tuotiansudai.api.dto.v1_0;

import com.google.common.primitives.Ints;
import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InvestRecordResponseDataDto {

    @ApiModelProperty(value = "用户名", example = "王拓天")
    private String userName;

    @ApiModelProperty(value = "投资时间", example = "2016-11-24 12:22:12")
    private String investTime;

    @ApiModelProperty(value = "投资金额", example = "100")
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
        List<InvestAchievement> investAchievements = input.getAchievements();
        Collections.sort(investAchievements, new Comparator<InvestAchievement>() {
            @Override
            public int compare(InvestAchievement investAchievement1, InvestAchievement investAchievement2) {
                return Ints.compare(investAchievement1.getPriority(), investAchievement2.getPriority());
            }
        });
        this.setAchievements(investAchievements);
    }

}
