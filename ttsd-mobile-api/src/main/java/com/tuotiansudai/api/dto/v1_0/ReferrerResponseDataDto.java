package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ReferrerRelationView;

import java.text.SimpleDateFormat;

public class ReferrerResponseDataDto {
    private String userId;
    private String level;
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
