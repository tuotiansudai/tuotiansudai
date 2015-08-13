package com.tuotiansudai.client.dto;

public class ResultDto {

    private boolean success = true;

    private ResultDataDto data;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResultDataDto getData() {
        return data;
    }

    public void setData(ResultDataDto data) {
        this.data = data;
    }
}
