package com.tuotiansudai.api.dto.v1_0;

public class OperationDataLatestSixMonthResponseDataDto extends BaseResponseDataDto {
    private String name;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
