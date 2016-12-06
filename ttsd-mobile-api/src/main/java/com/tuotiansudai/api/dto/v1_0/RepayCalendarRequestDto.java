package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class RepayCalendarRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "年", example = "2016")
    private String year;

    @ApiModelProperty(value = "月", example = "12")
    private String month;

    @ApiModelProperty(value = "日", example = "11")
    private String date;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
