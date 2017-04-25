package com.tuotiansudai.api.dto.v3_0;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class DisclosureDto implements Serializable {

    @ApiModelProperty(value = "披露标题", example = "借款人基本信息")
    private String title;

    @ApiModelProperty(value = "披露信息详情", example = "list")
    private List<ItemDto> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }
}
