package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import io.swagger.annotations.ApiModelProperty;

public class PointDrawResultResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "抽奖奖品位置", example = "1")
    private long seq;

    @ApiModelProperty(value = "奖品名称", example = "10元红包")
    private String prizeName;

    @ApiModelProperty(value = "奖品类型", example = "CONCRETE-真实奖品，VIRTUAL：虚拟奖品, POINT:积分")
    private String prizeType;

    public PointDrawResultResponseDto(DrawLotteryResultDto drawLotteryResultDto) {
        if (drawLotteryResultDto.getReturnCode() == 0) {
            this.seq = this.getSeq(drawLotteryResultDto.getPrize());
            this.prizeName = drawLotteryResultDto.getPrizeValue();
            this.prizeType = this.getPrizeTypeBylotteryPrize(drawLotteryResultDto.getPrize());
        }
    }

    private long getSeq(String lotteryPrize) {

        switch (lotteryPrize) {
            case "POINT_SHOP_RED_ENVELOPE_10":
                return 1;
            case "POINT_SHOP_POINT_500":
                return 2;
            case "POINT_SHOP_JD_10":
                return 3;
            case "POINT_SHOP_INTEREST_COUPON_2":
                return 4;
            case "POINT_SHOP_RED_ENVELOPE_50":
                return 5;
            case "POINT_SHOP_POINT_3000":
                return 6;
            case "POINT_SHOP_PHONE_CHARGE_10":
                return 7;
            case "POINT_SHOP_INTEREST_COUPON_5":
                return 8;
        }
        return 1;
    }

    private String getPrizeTypeBylotteryPrize(String lotteryPrize) {
        switch (lotteryPrize) {
            case "POINT_SHOP_RED_ENVELOPE_10":
            case "POINT_SHOP_INTEREST_COUPON_2":
            case "POINT_SHOP_RED_ENVELOPE_50":
            case "POINT_SHOP_INTEREST_COUPON_5":
                return "VIRTUAL";
            case "POINT_SHOP_POINT_500":
            case "POINT_SHOP_POINT_3000":
                return "POINT";
            case "POINT_SHOP_JD_100":
            case "POINT_SHOP_PHONE_CHARGE_10":
                return "CONCRETE";
        }
        return "";
    }


    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
}
