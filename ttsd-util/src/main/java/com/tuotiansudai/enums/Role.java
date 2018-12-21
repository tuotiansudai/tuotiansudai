package com.tuotiansudai.enums;

public enum Role {
    USER("注册用户"),
    INVESTOR("出借人"),
    LOANER("借款人"),
    SD_STAFF("速贷业务员"),
    ZC_STAFF("资产业务员"),
    NOT_STAFF_RECOMMEND("自然用户"), //用户上溯邀请人为非业务员角色，或该用户没有邀请人，则该用户为自然用户
    SD_STAFF_RECOMMEND("速贷系用户"), //用户上溯邀请人的终点为速贷业务员时，该用户为速贷系
    ZC_STAFF_RECOMMEND("资产系用户"), //用户上溯邀请人的终点为资产业务员时，该用户为资产系
    OPERATOR("运营专员"),
    OPERATOR_ADMIN("运营管理员"),
    RISK_CONTROL_STAFF("风控专员"),
    CUSTOMER_SERVICE("客服"),
    AGENT("代理商"),
    ADMIN("管理员"),
    EDITOR("编辑"),
    DATA("数据专员"),
    ASK_ADMIN("问答管理员"),
    PAYROLL_ADMIN("工资代发管理员"),
    COMPANY_STAFF("企业员工"), // 代发工资的企业员工用户
    FINANCE_ADMIN("财务审核"); // 代发工资一级审核

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}