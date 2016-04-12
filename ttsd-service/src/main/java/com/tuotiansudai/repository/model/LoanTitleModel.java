package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanTitleModel implements Serializable {
    private long id;
    /***
     * 标题类型
     ***/
    private LoanTitleType type;
    /***
     * 标题名称
     ***/
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LoanTitleType getType() {
        return type;
    }

    public void setType(LoanTitleType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
