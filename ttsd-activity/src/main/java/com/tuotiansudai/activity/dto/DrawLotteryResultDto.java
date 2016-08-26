package com.tuotiansudai.activity.dto;


import com.tuotiansudai.dto.BaseDataDto;

public class DrawLotteryResultDto extends BaseDataDto {

    private int returnCode;

    private String prize;

    private String prizeType;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
}
