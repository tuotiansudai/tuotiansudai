package com.tuotiansudai.enums;

public enum ExperienceBillBusinessType {
    REGISTER("新手注册", "新手注册成功，获得体验金：{0}元, 注册时间：{1}"),
    INVEST_LOAN("投资体验金项目", "您投资了拓天体验金项目，投资体验金金额：{0}元, 投资时间：{1}"),
    NEWMAN_TYRANT("新贵富豪争霸赛", "恭喜您在新贵富豪争霸赛活动中获得体验金：{0}元体验金，获奖时间：{1}"),
    MONEY_TREE("摇钱树活动", "恭喜您在摇钱树活动中摇中了：{0}元体验金，摇奖时间：{1}"),
    RISK_ESTIMATE("投资偏好测评奖励", "首次投资偏好测评，奖励{0}体验金，评测时间：{1}"),
    MOTHERS_DAY("母亲节活动", "恭喜您在母亲节活动中抽中了：{0}元体验金，摇奖时间：{1}"),
    DIVIDE_MONEY("瓜分体验金活动奖励", "恭喜您在瓜分体验金活动中获得体验金：{0}元体验金，获奖时间：{1}"),
    DRAGON_BOAT_INVITE_NEW_USER("端午节活动邀请好友", "恭喜您在端午节活动中邀请新用户，得到了{0}元体验金奖励，获奖时间：{1}"),
    DRAGON_BOAT_ZONGZI_PK("端午节活动粽子PK", "恭喜您在端午节粽子PK活动中，得到了{0}元体验金奖励，获奖时间：{1}"),
    CELEBRATION_LUCK_DRAW("周年庆幸运大抽奖", "恭喜您在周年庆幸运大抽奖活动中抽中了：{0}元体验金奖励，获奖时间：{1}"),
    CELEBRATION_SINGLE_ECONOMICAL("周年庆单笔聚划算", "恭喜您在周年庆单笔聚划算活动中获得了{0}元体验金奖励，获奖时间：{1}");

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
