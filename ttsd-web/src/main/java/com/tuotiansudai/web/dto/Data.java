package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class Data {

    private boolean exist;

    public boolean getIsExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
