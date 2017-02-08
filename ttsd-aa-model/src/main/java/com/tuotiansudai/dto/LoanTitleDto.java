package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanTitleType;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanTitleDto {
    private long id;
    /***
     * 标题类型
     ***/
    private LoanTitleType type;
    /***
     * 标题名称
     ***/
    @NotEmpty
    private String title;

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
}
