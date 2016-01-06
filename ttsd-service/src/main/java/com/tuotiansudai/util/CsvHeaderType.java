package com.tuotiansudai.util;


public enum CsvHeaderType {

    ConsoleUserFundsCsvHeader("时间,序号,用户名,业务员,姓名,手机号,费用类型,操作类型,金额,余额,冻结金额","后台用户资金查询导出CSV"),
    ConsoleReferrerManageCsvHeader("项目名称,期数,投资人,投资人姓名,投资金额,投资时间,来源,推荐人,推荐人姓名,推荐人是否业务员,推荐层级,推荐奖励,奖励状态,奖励时间","后台推荐人管理导出CSV"),
    ConsoleUsers("用户名,是否绑卡,真实姓名,手机号,电子邮件,推荐人,推荐人是否为业务员,来源,渠道,注册时间,角色,状态","后台用户管理"),
    ConsoleRecharge("编号,时间,用户名,业务员,姓名,手机号,充值金额,手续费,充值渠道,管理员充值,充值状态,充值来源,渠道","后台充值记录"),
    ConsoleWithdraw("编号,申请时间,初审时间,复核时间,用户名,业务员,姓名,手机号,提现金额,手续费,银行卡,状态,来源","后台提现记录"),
    ConsoleInvests("项目编号,项目名称,期数,投资人,业务员,姓名,手机号,推荐人,业务员,姓名,手机号,渠道,来源,投资时间,自动投标,投资金额,投资状态","后台用户投资记录"),
    ConsoleBalance("用户名,是否是业务员,姓名,手机号,地区,账户余额","用户余额查询");

    private String header;

    private String description;

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    CsvHeaderType(String header, String description) {
        this.header = header;
        this.description = description;
    }

}
