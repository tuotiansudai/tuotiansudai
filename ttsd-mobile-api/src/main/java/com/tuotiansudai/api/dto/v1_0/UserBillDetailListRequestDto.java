package com.tuotiansudai.api.dto.v1_0;

public class UserBillDetailListRequestDto extends BaseParamDto{
    private String userId;
    private UserBillCategory userBillCategory;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBillCategory getUserBillCategory() {
        return userBillCategory;
    }

    public void setUserBillCategory(UserBillCategory userBillCategory) {
        this.userBillCategory = userBillCategory;
    }
}
