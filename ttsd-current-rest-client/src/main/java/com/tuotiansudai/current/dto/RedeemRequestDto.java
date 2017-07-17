package com.tuotiansudai.current.dto;

import com.tuotiansudai.repository.model.Source;

public class RedeemRequestDto {
    private long amount;
    private Source source;

    public RedeemRequestDto(long amount, Source source) {
        this.amount = amount;
        this.source = source;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
