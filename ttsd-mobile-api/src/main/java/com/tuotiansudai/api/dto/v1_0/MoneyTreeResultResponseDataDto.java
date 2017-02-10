package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class MoneyTreeResultResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "体验金金额", example = "50元")
    private String prizeName;

    @ApiModelProperty(value = "摇奖者手机号", example = "13888888888")
    private String prizeMobile;

    @ApiModelProperty(value = "摇奖时间", example = "2016-12-28 12:23:32")
    private Date prizeTime;

    public MoneyTreeResultResponseDataDto(UserLotteryPrizeView userLotteryPrizeView){
        this.prizeName = userLotteryPrizeView.getPrizeValue();
        this.prizeMobile = userLotteryPrizeView.getMobile();
        this.prizeTime = userLotteryPrizeView.getLotteryTime();
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeMobile() {
        return prizeMobile;
    }

    public void setPrizeMobile(String prizeMobile) {
        this.prizeMobile = prizeMobile;
    }

    public Date getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(Date prizeTime) {
        this.prizeTime = prizeTime;
    }
}
