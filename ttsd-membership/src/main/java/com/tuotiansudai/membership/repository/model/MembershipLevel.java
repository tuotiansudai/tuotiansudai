package com.tuotiansudai.membership.repository.model;

public enum MembershipLevel {
    V0("普通", "4999", 0),
    V1("青铜", "5000", 1),
    V2("白银", "300000", 2),
    V3("黄金", "1500000", 3),
    V4("铂金", "5000000", 4),
    V5("钻石", "500W以上", 5);

    private  String levelName;

    private String levelValue;

    private int level;

    MembershipLevel(String levelName, String levelValue, int level) {
        this.levelName = levelName;
        this.levelValue = levelValue;
        this.level = level;
    }

    public String getLevelName() {return levelName;}

    public String getNextLevelName(){return "sss";}

    public String getLevelValue() { return levelValue;}

    public int getLevel(){return level;}

    public int getNextLevel(){return level>=5?5:(level + 1);}

}
