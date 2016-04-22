package com.tuotiansudai.coupon.repository.model;

import java.io.Serializable;
import java.util.List;

public class CouponUserGroupModel implements Serializable{

    private long id;

    private long couponId;

    private UserGroup userGroup;

    private List<String> userGroupItems;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public List<String> getUserGroupItems() {
        return userGroupItems;
    }

    public void setUserGroupItems(List<String> userGroupItems) {
        this.userGroupItems = userGroupItems;
    }

}
