package com.tuotiansudai.membership.repository.model;

import java.util.Arrays;

public enum MembershipPrivilegePriceType {
    _30("30天",30,2500),
    _180("180天",180,12000),
    _360("360天",360,18000);

    private final String name;
    private final int duration;
    private final long price;

    MembershipPrivilegePriceType(String name,int duration,long price){
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public long getPrice() {
        return price;
    }

    public static MembershipPrivilegePriceType getPriceTypeByDuration(int duration){
        return Arrays.stream(MembershipPrivilegePriceType.values())
                .filter(membershipPrivilegePriceType->membershipPrivilegePriceType.getDuration()==duration)
                .findFirst().orElse(null);
    }
}
