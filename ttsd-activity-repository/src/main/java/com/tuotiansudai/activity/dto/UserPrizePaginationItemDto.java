package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class UserPrizePaginationItemDto {

    private long id;

    private String prize;

    private String userName;

    private String mobile;

    private String investAmount;

    private Date createdTime;

    public UserPrizePaginationItemDto(UserTravelPrizeModel model) {
        this.id = model.getId();
        this.prize = model.getPrize();
        this.userName = model.getUserName();
        this.mobile = model.getMobile();
        this.investAmount = AmountConverter.convertCentToString(model.getInvestAmount());
        this.createdTime = model.getCreatedTime();
    }

    public UserPrizePaginationItemDto(UserLuxuryPrizeModel model) {
        this.id = model.getId();
        this.prize = model.getPrize();
        this.userName = model.getUserName();
        this.mobile = model.getMobile();
        this.investAmount = AmountConverter.convertCentToString(model.getInvestAmount());
        this.createdTime = model.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public String getPrize() {
        return prize;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
