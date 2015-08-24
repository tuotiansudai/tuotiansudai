package com.tuotiansudai.repository.model;

import java.math.BigInteger;

/**
 * Created by tuotian on 15/8/17.
 */
public class TitleModel {
    private BigInteger id;
    /***标题类型base:基础标题，new:新增标题***/
    private String type;
    /***标题名称***/
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
