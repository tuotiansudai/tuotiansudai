package com.tuotiansudai.console.bi.repository.model;

import java.io.Serializable;

public class KeyValueModel implements Serializable{

    private String name;
    private String value;
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public KeyValueModel() {
    }

    public KeyValueModel(String name, String value, String group) {
        this.name = name;
        this.value = value;
        this.group = group;
    }
}
