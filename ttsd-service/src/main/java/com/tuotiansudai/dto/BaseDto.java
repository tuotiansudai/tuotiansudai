package com.tuotiansudai.dto;

public class BaseDto{

    private boolean success = true;

    private BaseDataDto data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BaseDataDto getData() {
        return data;
    }

    public void setData(BaseDataDto data) {
        this.data = data;
    }
}
