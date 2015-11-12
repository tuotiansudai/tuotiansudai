package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

public class InvestDto extends ProjectTransferDto {

    private Source source = Source.WEB;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
