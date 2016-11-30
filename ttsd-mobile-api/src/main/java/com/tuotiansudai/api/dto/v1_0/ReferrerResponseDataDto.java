package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ReferrerRelationView;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class ReferrerResponseDataDto {

    @ApiModelProperty(value = "被推荐人", example = "wangtuotian")
    private String userId;

    @ApiModelProperty(value = "被推荐人层级", example = "1")
    private String level;

    @ApiModelProperty(value = "被推荐时间", example = "2016-11-25 14:44:01")
    private String time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ReferrerResponseDataDto(){

    }
    public ReferrerResponseDataDto(ReferrerRelationView input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setUserId(input.getLoginName());
        this.setLevel("" + input.getLevel());
        this.setTime(simpleDateFormat.format(input.getRegisterTime()));
    }
}
