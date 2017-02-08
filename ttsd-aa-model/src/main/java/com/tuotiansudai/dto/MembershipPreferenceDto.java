package com.tuotiansudai.dto;

public class MembershipPreferenceDto extends BaseDataDto {
    private int level;
    private int rate;
    private String amount;
    private boolean valid;
    private boolean membershipPrivilege;

    public MembershipPreferenceDto(boolean status) {
        super(status);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isMembershipPrivilege() {
        return membershipPrivilege;
    }

    public void setMembershipPrivilege(boolean membershipPrivilege) {
        this.membershipPrivilege = membershipPrivilege;
    }
}
