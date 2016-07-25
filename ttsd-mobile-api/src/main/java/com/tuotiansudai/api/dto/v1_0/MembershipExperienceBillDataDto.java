package com.tuotiansudai.api.dto.v1_0;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;

import java.util.Date;

public class MembershipExperienceBillDataDto {
    private long experience;
    private String desc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdTime;

    public MembershipExperienceBillDataDto(MembershipExperienceBillModel model) {
        this.experience = model.getExperience();
        this.desc = model.getDescription();
        this.createdTime = model.getCreatedTime();
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
