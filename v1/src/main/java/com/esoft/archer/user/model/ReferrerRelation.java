package com.esoft.archer.user.model;


import javax.persistence.*;

@Entity
@Table(name = "referrer_relation")
@IdClass(ReferrerRelationId.class)
public class ReferrerRelation implements java.io.Serializable {
    @Id
    @Column(name = "referrer_id")
    private String referrerId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "level")
    private Integer level;

    @OneToOne
    @JoinColumn(name = "referrer_id", insertable = false, updatable = false)
    private User referrer;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public User getReferrer() {
        return referrer;
    }

    public void setReferrer(User referrer) {
        this.referrer = referrer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReferrerRelation(){

    }
    public ReferrerRelation(String referrerId,String userId){
        this.referrerId = referrerId;
        this.userId = userId;
    }

}

