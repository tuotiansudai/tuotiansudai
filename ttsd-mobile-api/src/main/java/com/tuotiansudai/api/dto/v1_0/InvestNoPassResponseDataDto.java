package com.tuotiansudai.api.dto.v1_0;

public class InvestNoPassResponseDataDto extends BaseResponseDataDto{

    public InvestNoPassResponseDataDto(){}

    public InvestNoPassResponseDataDto(String url){this.url = url;}

    private String url;

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url; }

}
