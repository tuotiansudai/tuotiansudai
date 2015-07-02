package com.tuotiansudai.smswrapper.dto;

public class ResultDto {

    private boolean success;

    private ResultDataDto data;

    public boolean isSuccess() {
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
