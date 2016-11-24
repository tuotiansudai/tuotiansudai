package com.tuotiansudai.api.dto.v1_0;

public class OperationDataInvestByProductTypeResponseDataDto extends BaseResponseDataDto {
    private String name;
    private long amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
