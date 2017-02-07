package com.tuotiansudai.membership.repository.model;

public enum MembershipDiscount {
    V2("V2会员9.5折", 0.95, 2),
    V3("V3会员9.2折", 0.92, 3),
    V4("V4会员8.8折", 0.88, 4),
    V5("V5会员8.5折", 0.85, 5);

    private  String description;

    private double discount;

    private int level;

    MembershipDiscount(String description, double discount, int level) {
        this.description = description;
        this.discount = discount;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel(){return level;}

    public double getDiscount() {
        return discount;
    }


    public static String getMembershipDescriptionByLevel(int level){
        for(MembershipDiscount membershipDiscount: MembershipDiscount.values()){
            if(membershipDiscount.getLevel() == level){
                return membershipDiscount.getDescription();
            }
        }
        return "";
    }

    public static double getMembershipDiscountByLevel(int level){
        for(MembershipDiscount membershipDiscount: MembershipDiscount.values()){
            if(membershipDiscount.getLevel() == level){
                return membershipDiscount.getDiscount();
            }
        }
        return 1.0;
    }
}
