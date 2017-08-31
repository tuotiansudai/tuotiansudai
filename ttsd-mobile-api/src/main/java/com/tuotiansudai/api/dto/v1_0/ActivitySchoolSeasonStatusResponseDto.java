package com.tuotiansudai.api.dto.v1_0;

public class ActivitySchoolSeasonStatusResponseDto extends BaseResponseDataDto{

    private final ActivitySchoolSeasonStatus status;
    private final String url;


    public ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus status, String url) {
        this.status = status;
        this.url = url;
    }

    public ActivitySchoolSeasonStatus getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

}
