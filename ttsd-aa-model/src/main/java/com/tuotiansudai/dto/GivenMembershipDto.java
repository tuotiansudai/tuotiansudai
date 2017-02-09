package com.tuotiansudai.dto;

public class GivenMembershipDto extends BaseDataDto {
    private String description;
    private String url;
    private String btnName;

    public GivenMembershipDto() {
    }

    public GivenMembershipDto(String description, String url,String btnName) {
        this.description = description;
        this.url = url;
        this.btnName = btnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBtnName() { return btnName; }

    public void setBtnName(String btnName) { this.btnName = btnName; }
}

