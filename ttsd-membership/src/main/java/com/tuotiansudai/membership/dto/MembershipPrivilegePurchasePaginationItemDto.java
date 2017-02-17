package com.tuotiansudai.membership.dto;

import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class MembershipPrivilegePurchasePaginationItemDto implements Serializable{

    private long id;

    private String mobile;

    private String userName;

    private MembershipPrivilegePriceType privilegePriceType;

    private String amount;

    private Source source;

    private Date createdTime;

    public MembershipPrivilegePurchasePaginationItemDto(MembershipPrivilegePurchaseModel model) {
        this.id = model.getId();
        this.mobile = model.getMobile();
        this.userName = model.getUserName();
        this.privilegePriceType = model.getPrivilegePriceType();
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

    public String getAmount() {
        return amount;
    }

    public MembershipPrivilegePriceType getPrivilegePriceType() {
        return privilegePriceType;
    }

    public Source getSource() {
        return source;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
