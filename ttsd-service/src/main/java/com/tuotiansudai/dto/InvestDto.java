package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

public class InvestDto extends ProjectTransferDto {

    private Source source = Source.WEB;

    private String channel = null;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
