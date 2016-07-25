package com.tuotiansudai.util;


public enum CsvHeaderType {

    ConsoleUserFundsCsvHeader("时间,序号,用户名,业务员,姓名,手机号,费用类型,操作类型,金额,余额,冻结金额","后台用户资金查询导出CSV"),
    ConsoleReferrerManageCsvHeader("项目名称,期数,投资人,投资人姓名,投资金额,投资时间,来源,推荐人,推荐人姓名,推荐人是否业务员,推荐层级,推荐奖励,奖励状态,奖励时间","后台推荐人管理导出CSV"),
    ConsoleUsers("用户名,是否绑卡,真实姓名,手机号,电子邮件,推荐人,推荐人是否为业务员,来源,渠道,注册时间,开通自动投标,角色,状态,生日,省,市","后台用户管理"),
    ConsoleRecharge("编号,时间,用户名,业务员,姓名,手机号,充值金额,手续费,充值渠道,快捷充值,充值状态,充值来源,渠道","后台充值记录"),
    ConsoleWithdraw("编号,申请时间,初审时间,复核时间,用户名,业务员,姓名,手机号,提现金额,手续费,银行卡,状态,来源","后台提现记录"),
    ConsoleInvests("项目编号,项目名称,期数,投资人,业务员,姓名,手机号,生日,省,市,推荐人,业务员,姓名,手机号,渠道,来源,投资时间,自动投标,投资金额,投资状态","后台用户投资记录"),
    ConsoleBalance("用户名,是否是业务员,姓名,手机号,生日,省,市,最后交易时间,账户余额","用户余额查询"),
    BIInvestViscosity("用户名,真实姓名,电话,是否业务员,推荐人,推荐人姓名,推荐人是否业务员,投资总金额(元),投资次数,上次投资时间","用户续投情况"),
    ExchangeCodeCsv("名称,创建日期,体验券金额,加息券利率,红包金额,有效期限,数量,使用条件,可投资标的,是否与其他优惠券共用,兑换码", "优惠券兑换码明细"),
    Feedback("编号,用户名,联系方式,反馈渠道,反馈类型,内容,时间,是否处理", "意见反馈"),
    InvestAchievementHeader("项目名称,项目状态,借款期限,项目金额,拓天标王,拓荒先锋,一锤定音,满标日期,首投用时,满标用时", "投资称号管理"),
    FinanceReportHeader("项目编号, 项目名称, 计息模式, 借款人, 他项人, 收益率, 周期, 标的金额(元), 起标日期, 投资时间, 放款时间, 投资人,投资人姓名, 推荐人, 投资金额(元), 计息天数, 回款时间, 期限, 实际收益(元), 服务费(元), 实际回款(元), 推荐奖励(元)", "债券财务报表"),
    BookingLoanHeader("投资人姓名,投资人手机号,预约渠道,预约时间,预约项目,预约投资金额(元),通知时间,状态","预约投资情况");
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
