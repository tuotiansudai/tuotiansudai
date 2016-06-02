package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;

public class MembershipModel implements Serializable {

    private long id;
    private int level;
    private long experience;
    private double fee;

    public MembershipModel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
