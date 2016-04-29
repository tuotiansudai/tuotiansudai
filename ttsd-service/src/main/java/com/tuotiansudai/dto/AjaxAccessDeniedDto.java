package com.tuotiansudai.dto;

public class AjaxAccessDeniedDto extends BaseDataDto {

    private String directUrl = "/login";

    private String refererUrl;

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public String getRefererUrl() {
        return refererUrl;
    }

    public void setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
    }
}
