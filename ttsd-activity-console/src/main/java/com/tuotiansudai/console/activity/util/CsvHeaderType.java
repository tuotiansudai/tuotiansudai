package com.tuotiansudai.console.activity.util;


public enum CsvHeaderType {
    UserDrawTimeList("用户手机号,姓名,可用抽奖机会,已用抽奖机会","抽奖机会统计"),
    UserPrizeList("中奖时间,获奖用户手机号,姓名,奖品","用户奖品统计");

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
