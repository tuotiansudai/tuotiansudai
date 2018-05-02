package com.tuotiansudai.api.dto.v3_0;


import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ItemDto implements Serializable {

    @ApiModelProperty(value = "标签", example = "性别")
    private String label;

    @ApiModelProperty(value = "值", example = "男")
    private String value;

    public ItemDto() {
    }

    public ItemDto(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Strings.isNullOrEmpty(value) ? "--" : value;
    }
}
