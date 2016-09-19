package com.tuotiansudai.membership.dto;

import com.tuotiansudai.membership.repository.model.MembershipPurchaseModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class MembershipPurchasePaginationItemDto {

    private long id;

    private String mobile;

    private String userName;

    private int level;

    private int duration;

    private String amount;

    private Source source;

    private Date createdTime;

    public MembershipPurchasePaginationItemDto(MembershipPurchaseModel model) {
        this.id = model.getId();
        this.mobile = model.getMobile();
        this.userName = model.getUserName();
        this.level = model.getLevel();
        this.duration = model.getDuration();
        this.amount = AmountConverter.convertCentToString(model.getAmount());
        this.source = model.getSource();
        this.createdTime = model.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserName() {
        return userName;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }

    public String getAmount() {
        return amount;
    }

    public Source getSource() {
        return source;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
