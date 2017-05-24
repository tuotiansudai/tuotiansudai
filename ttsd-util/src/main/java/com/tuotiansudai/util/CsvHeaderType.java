package com.tuotiansudai.util;


public enum CsvHeaderType {

    ConsoleUserFundsCsvHeader("时间,序号,用户名,业务员,姓名,手机号,费用类型,操作类型,金额,余额,冻结金额", "后台用户资金查询导出CSV"),
    ConsoleReferrerManageCsvHeader("项目名称,期数,投资人,投资人姓名,投资金额,投资时间,来源,推荐人,推荐人姓名,推荐人是否业务员,推荐层级,推荐奖励,奖励状态,奖励时间", "后台推荐人管理导出CSV"),
    ConsoleUsers("用户名,是否绑卡,真实姓名,手机号,电子邮件,推荐人,推荐人是否为业务员,来源,渠道,注册时间,开通自动投标,角色,状态,生日,省,市", "后台用户管理"),
    ConsoleRecharge("编号,时间,用户名,业务员,姓名,手机号,充值金额,充值渠道,快捷充值,充值状态,充值来源,渠道", "后台充值记录"),
    ConsoleWithdraw("编号,申请时间,初审时间,复核时间,用户名,业务员,姓名,手机号,提现金额,手续费,银行卡,状态,来源", "后台提现记录"),
    ConsoleInvests("项目编号,项目名称,期数,投资人,业务员,姓名,手机号,生日,省,市,推荐人,业务员,姓名,手机号,渠道,来源,投资时间,自动投标,投资金额,使用优惠(使用优惠信息／实际返款),阶梯加息优惠(阶梯加息利率/实际返款),投资状态", "后台用户投资记录"),
    ConsoleBalance("用户名,是否是业务员,姓名,手机号,生日,省,市,最后交易时间,账户余额", "用户余额查询"),
    BIInvestViscosity("用户名,真实姓名,电话,是否业务员,推荐人,推荐人姓名,推荐人是否业务员,投资总金额(元),投资标的数,上次投资时间", "用户续投情况"),
    BIInvestCountViscosity("用户名,真实姓名,电话,是否业务员,推荐人,推荐人姓名,推荐人是否业务员,投资总金额(元),投资次数,上次投资时间", "用户续投情况"),
    ExchangeCodeCsv("名称,创建日期,体验券金额,加息券利率,红包金额,有效期限,数量,使用条件,可投资标的,是否与其他优惠券共用,兑换码", "优惠券兑换码明细"),
    Feedback("编号,用户名,联系方式,反馈渠道,反馈类型,内容,时间,是否处理", "意见反馈"),
    InvestAchievementHeader("项目名称,项目状态,借款期限,项目金额,拓天标王,拓荒先锋,一锤定音,满标日期,首投用时,满标用时", "投资称号管理"),
    FinanceReportHeader("项目编号, 项目名称, 计息模式, 借款人, 他项人, 收益率, 周期, 标的金额(元), 起标日期, 投资时间, 放款时间, 投资人,投资人姓名, 推荐人, 投资金额(元), 计息天数, 回款时间, 期限, 实际收益(元), 服务费(元), 实际回款(元), 推荐奖励(元), 使用优惠(使用优惠信息), 使用优惠(实际返款), 阶梯加息优惠(阶梯加息利率), 阶梯加息优惠(实际返款)", "债券财务报表"),
    CouponHeader("名称,金额(元),总投资金额(元),活动期限,有效天数,预计发放数量(张),已发放(张),已使用(张),发放对象,可投标的,使用条件,应发放总收益(元),已发放收益(元)", "体验券管理"),
    InterestCouponsHeader("名称,利率(%),总投资金额(元),活动期限,有效天数,发放对象,预计发放数量(张),实际发放张数(张),已使用(张),可投标的,使用条件,应发放总收益(元),已发放收益(元)", "加息券管理"),
    RedEnvelopesHeader("名称, 金额(元), 总投资金额(元), 活动期限, 有效天数, 已发放(张), 已使用(张), 发放对象, 可投标的, 起投金额, 应发放总收益(元), 已发放收益(元), 是否共用", "现金红包管理"),
    BirthdayCouponsHeader("名称, 翻倍倍数, 总投资金额(元), 活动期限, 发放对象, 可投标的, 已使用(张), 应发放总收益(元), 已发放收益(元), 推送提醒", "生日月活动管理"),
    PointPrizeHeader("奖品, 中奖人数, 状态", "财豆奖品管理"),
    UserPointHeader("用户名,真实姓名, 手机号, 可用财豆,累计财豆", "用户财豆管理"),
    CouponExchangeHeader("名称,券额(元),利率,总数量,已兑换数量,所需财豆,有效期限,有效天数,可投标的,使用条件", "优惠券兑换管理"),
    SystemBillHeader("时间,费用类型,操作类型,金额(元),费用详情", "系统账户查询"),
    TransferListHeader("编号,原始项目,转让人,原债权本金(元),转让价格(元),剩余期限,转让状态, 转让发起时间,承接人,来源,转让手续费(元)", "所有的转让项目"),
    LoanRepayHeader("项目编号,项目名称,还款人,预计还款日期,实际还款日期,当前日期,应还本金(元),应还利息(元),应还总数(元),实际还款总额(元),还款状态", "所有的转让项目"),
    BookingLoanHeader("投资人姓名,投资人手机号,预约渠道,预约时间,预约项目,预约投资金额(元),通知时间,状态", "预约投资情况"),
    ConsoleLoanList("编号,项目名称,借款期限,借款人,代理人,借款金额(元),年化/活动(利率),阶梯加息,项目状态,发起时间", "所有的借款"),
    AccountBalance("用户名,姓名,手机号,地区,最后交易时间,账户余额", "用户余额"),
    ProductOrderList("用户名,实际花费积分,兑换时间,兑换数量,姓名,手机号码,收货地址,状态,发货时间", "商品订单导出"),
    AutumnActivityList("家庭名称,投资金额(元),时间,奖品,家庭成员,投资金额(元)","中秋活动列表导出"),
    Iphone7LotteryStatHeader("用户手机号,姓名,活动期限投资金额(元),投资码个数","IPhone7抽奖活动导出"),
    LotteryPrizeHeader("姓名,用户手机号,用户名,中奖时间,奖品", "抽奖记录导出"),
    NotWorkHeader("编号, 姓名, 用户名, 手机号, 活动期限内投资金额, 获得奖品, 推荐用户注册数, 推荐用户实名认证数, 推荐用户累计投资金额", "今天不上班活动导出"),
    AnnualHeader("姓名, 用户名, 手机号, 活动期限内投资金额, 获得奖品", "元旦活动导出"),
    HeadlinesTodayHeader("手机号, 参与时间, 姓名, 是否投资,", "今日头条拉新抽奖活动导出"),
    CouponDetailsHeader("用户名, 使用时间, 投资金额, 投资年化金额, 项目期限, 项目编号, 项目名称, 投资金额汇总, 投资年化金额汇总", "生日月活动管理详情"),
    UserMicroModelHeader("姓名, 手机号, 注册渠道, 注册时间, 注册后未投资天数（天）, 是否投资, 累计投资金额（元）, 投资笔数, 笔均投资金额（元）, 投资标的数, 标的平均投资金额（元）, 转化周期（天）, 首二间隔（天）, 首三间隔（天）, 最后一次投资时间, 待收回款（元）, 最后一次登录时间, 最后一次登录时间距今（天）, 最后一次登录来源", "用户微模型"),
    WomanDayHeader("手机号, 姓名, 获取花瓣数量, 投资花瓣数量, 签到花瓣数量, 邀请花瓣数量, 获得礼盒", "妇女节活动管理详情"),
    UserRemainHeader("用户名, 手机号, 真实姓名, 注册时间, 是否使用新手体验金, 使用体验金时间, 投资总次数, 投资总金额, 首次投资时间, 首次投资金额, 二次投资时间, 二次投资金额", "用户留存管理"),
    QuestionsHeader("问题标题, 问题链接", "问答问题列表"),
    MothersDayHeader("用户名, 姓名, 手机号, 活动期内投资金额, 获得奖励, 获得体验金", "母亲节详情导出"),
    DragonBoatHeader("姓名, 手机号, 活动期内累计投资金额, PK阵营, 参与PK金额, 邀请新用户获得体验金, PK获得体验金, 邀请老用户领取数量, 邀请新用户注册数量", "端午节详情导出");

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
