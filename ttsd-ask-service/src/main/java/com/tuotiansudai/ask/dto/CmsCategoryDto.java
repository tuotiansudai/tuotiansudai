package com.tuotiansudai.ask.dto;

import java.io.Serializable;

public class CmsCategoryDto implements Serializable{

    private String parent;
    private String name;
    private String slug;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
