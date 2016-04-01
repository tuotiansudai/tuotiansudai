package com.tuotiansudai.dto.ranking;

import com.tuotiansudai.repository.TianDouPrize;

public class UserTianDouRecordDto implements Comparable<UserTianDouRecordDto>{

    private String loginName;

    private String type; // "DRAW", "INVEST"

    private long amount;

    private long score;

    private TianDouPrize prize;

    private String time;

    private String desc;

    public UserTianDouRecordDto(String loginName, String type, TianDouPrize prize){
        this.loginName = loginName;
        this.type = type;
        this.prize = prize;
    }

    public UserTianDouRecordDto(String loginName, String type, TianDouPrize prize, String time){
        this.loginName = loginName;
        this.type = type;
        this.prize = prize;
        this.time = time;
    }

    public UserTianDouRecordDto(String loginName, String type, long amount, long score, String desc, String time){
        this.loginName = loginName;
        this.type = type;
        this.amount = amount;
        this.score = score;
        this.desc = desc;
        this.time = time;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public TianDouPrize getPrize() {
        return prize;
    }

    public void setPrize(TianDouPrize prize) {
        this.prize = prize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int compareTo(UserTianDouRecordDto dto){
        return this.time.compareTo(dto.getTime());
    }
}
