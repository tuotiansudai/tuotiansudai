package com.tuotiansudai.dto;

public class GivenMembershipDto extends BaseDataDto {
    private String description;
    private String url;

    public GivenMembershipDto() {
    }

    public GivenMembershipDto(String description, String url) {
        this.description = description;
        this.url = url;
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
}

