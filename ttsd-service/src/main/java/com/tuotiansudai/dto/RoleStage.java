package com.tuotiansudai.dto;

public enum RoleStage {

    ALL("全部"),
    USER("注册用户"),
    INVESTOR("出借人"),
    LOANER("借款人"),
    STAFF("业务员"),
    CUSTOMER_SERVICE("客服"),
    AGENT("渠道用户"),
    NATURAL_USER("自然用户"),
    RECOMMENDATION("业务员的一级推荐"),
    ADMIN("管理员"),
    OTHERS("其他用户");

    private String description;

    RoleStage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

