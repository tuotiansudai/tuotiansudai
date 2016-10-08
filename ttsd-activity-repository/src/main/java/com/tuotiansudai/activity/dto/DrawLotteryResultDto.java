package com.tuotiansudai.activity.dto;


import com.tuotiansudai.dto.BaseDataDto;

public class DrawLotteryResultDto extends BaseDataDto {

    public DrawLotteryResultDto(int returnCode,String prize,String prizeType,String prizeValue){
        this.returnCode = returnCode;
        this.prize = prize;
        this.prizeType = prizeType;
        this.prizeValue = prizeValue;
    }

    public DrawLotteryResultDto(int returnCode){
        this.returnCode = returnCode;
    }

    private int returnCode;

    private String prize;

    private String prizeType;

    private String prizeValue;

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

    public String getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }
}
