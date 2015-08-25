package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigInteger;

public class TitleDto {
    private BigInteger id;
    /***标题类型base:基础标题，new:新增标题***/
    private String type;
    /***标题名称***/
    @NotEmpty
    private String title;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
