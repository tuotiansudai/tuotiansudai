package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class Data {

    @JsonView(DataJsonView.Data.class)
    private boolean exist;

    public boolean getExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
