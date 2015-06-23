package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class UserInteractiveDto {

    @JsonView(UserJsonView.User.class)
    private int id;

    @JsonView(UserJsonView.User.class)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
