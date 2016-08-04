package com.tuotiansudai.api.dto.v1_0;

public class RepayCalendarRequestDto extends BaseParamDto{
    private String year;
    private String month;
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
