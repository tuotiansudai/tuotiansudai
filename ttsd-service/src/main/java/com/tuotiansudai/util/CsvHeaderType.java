package com.tuotiansudai.util;


public enum CsvHeaderType {

    ConsoleUserFundsCsvHeader("时间,序号,用户名,费用类型,操作类型,金额,余额,冻结金额","后台用户资金查询导出CSV"),
    ConsoleReferrerManageCsvHeader("项目名称,期数,投资人,投资人姓名,投资金额,投资时间,推荐人,推荐人姓名,推荐人是否业务员,推荐层级,推荐奖励,奖励状态,奖励时间","后台推荐人管理导出CSV");

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
