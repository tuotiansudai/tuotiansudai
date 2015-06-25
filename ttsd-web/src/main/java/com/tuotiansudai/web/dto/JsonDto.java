package com.tuotiansudai.web.dto;

import java.lang.Object;

/**
 * Created by Administrator on 2015/6/24.
 */
public class JsonDto {

    private String status;

    private Object model;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
