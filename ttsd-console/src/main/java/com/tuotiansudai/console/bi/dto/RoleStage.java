package com.tuotiansudai.console.bi.dto;

public enum RoleStage {

    ALL("全部"),
    USER("注册用户"),
    INVESTOR("出借人"),
    LOANER("代理人"),
    SD_STAFF("速贷业务员"),
    ZC_STAFF("资产业务员"),
    NOT_STAFF_RECOMMEND("自然用户"), //用户上溯邀请人为非业务员角色，或该用户没有邀请人，则该用户为自然用户
    SD_STAFF_RECOMMEND("速贷系用户"), //用户上溯邀请人的终点为速贷业务员时，该用户为速贷系
    ZC_STAFF_RECOMMEND("资产系用户"), //用户上溯邀请人的终点为资产业务员时，该用户为资产系
    CUSTOMER_SERVICE("客服"),
    AGENT("渠道用户"),
    SD_STAFF_RECOMMENDATION("速贷业务员的一级推荐"),
    ZC_STAFF_RECOMMENDATION("资产业务员的一级推荐"),
    ADMIN("管理员"),
    COMPANY_STAFF("企业员工"), // 代发工资的企业员工用户
    FINANCE_ADMIN("财务管理员"), // 代发工资一级审核
    OTHERS("其他用户");

    private String description;

    RoleStage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

