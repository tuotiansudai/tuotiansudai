package com.tuotiansudai.repository.model;

/**
 * Created by tuotian on 15/8/17.
 */
public class Title {
    private String id;
    /***标题类型base:基础标题，new:新增标题***/
    private String type;
    /***标题名称***/
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
