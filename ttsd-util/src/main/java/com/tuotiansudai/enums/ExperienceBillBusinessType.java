package com.tuotiansudai.enums;

public enum ExperienceBillBusinessType {
    REGISTER("新手注册", "新手注册成功，获得体验金：{0}元, 注册时间：{1}"),
    INVEST_LOAN("投资体验金项目", "您投资了拓天体验金项目，投资体验金金额：{0}元, 投资时间：{1}"),
    NEWMAN_TYRANT("新贵富豪争霸赛", "恭喜您在新贵富豪争霸赛活动中获得体验金：{0}元体验金，获奖时间：{1}"),
    MONEY_TREE("摇钱树活动", "恭喜您在摇钱树活动中摇中了：{0}元体验金，摇奖时间：{1}"),
    RISK_ESTIMATE("投资偏好测评奖励", "首次投资偏好测评，奖励{0}体验金，评测时间：{1}"),
    MOTHERS_TREE("母亲节活动", "恭喜您在母亲节活动中抽中了：{0}元体验金，摇奖时间：{1}");

    private final String description;
    private final String contentTemplate;

    ExperienceBillBusinessType(String description, String contentTemplate) {
        this.description = description;
        this.contentTemplate = contentTemplate;
    }


    public String getDescription() {
        return description;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }
}
