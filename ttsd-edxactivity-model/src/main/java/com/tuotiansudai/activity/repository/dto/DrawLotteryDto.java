package com.tuotiansudai.activity.repository.dto;

import com.tuotiansudai.activity.repository.model.TianDouPrize;
import com.tuotiansudai.dto.BaseDataDto;

public class DrawLotteryDto extends BaseDataDto {

    private int returnCode;

    private TianDouPrize tianDouPrize;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public TianDouPrize getTianDouPrize() {
        return tianDouPrize;
    }

    public void setTianDouPrize(TianDouPrize tianDouPrize) {
        this.tianDouPrize = tianDouPrize;
    }
}
