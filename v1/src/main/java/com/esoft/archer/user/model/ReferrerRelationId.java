package com.esoft.archer.user.model;

public class ReferrerRelationId implements java.io.Serializable {

    private String referrerId;

    private String userId;

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferrerRelationId that = (ReferrerRelationId) o;

        if (referrerId != null ? !referrerId.equals(that.referrerId) : that.referrerId != null) return false;
        return !(userId != null ? !userId.equals(that.userId) : that.userId != null);

    }

    @Override
    public int hashCode() {
        int result = referrerId != null ? referrerId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
