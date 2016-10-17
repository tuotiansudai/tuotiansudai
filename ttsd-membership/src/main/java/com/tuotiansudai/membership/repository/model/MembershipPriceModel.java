package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;

public class MembershipPriceModel implements Serializable {

    private int level;

    private int duration;

    private long price;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
