package com.tuotiansudai.dto.ranking;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.repository.TianDouPrize;

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
