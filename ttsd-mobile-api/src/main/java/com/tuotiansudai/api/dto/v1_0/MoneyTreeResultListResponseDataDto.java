package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class MoneyTreeResultListResponseDataDto extends BaseResponseDataDto{

    private List<MoneyTreeResultResponseDataDto> prizeList;

    public List<MoneyTreeResultResponseDataDto> getPrizeList() {
        return prizeList;
    }

    public void setPrizeList(List<MoneyTreeResultResponseDataDto> prizeList) {
        this.prizeList = prizeList;
    }
}
