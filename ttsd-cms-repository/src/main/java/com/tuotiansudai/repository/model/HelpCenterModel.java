package com.tuotiansudai.repository.model;


import com.tuotiansudai.enums.HelpCategory;

import java.io.Serializable;

public class HelpCenterModel implements Serializable{
    private long id;
    private String title;
    private String content;
    private HelpCategory category;
    private boolean hot;

    public HelpCenterModel(){}

    public HelpCenterModel(long id, String title, String content, HelpCategory category, boolean hot) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.hot = hot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HelpCategory getCategory() {
        return category;
    }

    public void setCategory(HelpCategory category) {
        this.category = category;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }
}
