package com.esoft.archer.user.model;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "july_activity_reward")
public class JulyActivityReward {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "user_Id")
    private User user;

    @OneToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @Column(name = "certified_reward")
    private boolean certifiedReward;

    @Column(name = "certified_reward_time")
    private Date certifiedRewardTime;

    @Column(name = "referrer_certified_reward")
    private boolean referrerCertifiedReward;

    @Column(name = "referrer_certified_reward_time")
    private Date referrerCertifiedRewardTime;

    @Column(name = "first_recharge_reward")
    private boolean firstRechargeReward;

    @Column(name = "first_recharge_reward_time")
    private Date firstRechargeRewardTime;

    @Column(name = "referrer_first_recharge_reward")
    private boolean referrerFirstRechargeReward;

    @Column(name = "referrer_first_recharge_reward_time")
    private Date referrerFirstRechargeRewardTime;

    @Column(name = "referrer_first_invest_reward")
    private boolean referrerFirstInvestReward;

    @Column(name = "referrer_first_invest_reward_time")
    private Date referrerFirstInvestRewardTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getReferrer() {
        return referrer;
    }

    public void setReferrer(User referrer) {
        this.referrer = referrer;
    }

    public boolean getCertifiedReward() {
        return certifiedReward;
    }

    public void setCertifiedReward(boolean certifiedReward) {
        this.certifiedReward = certifiedReward;
    }

    public Date getCertifiedRewardTime() {
        return certifiedRewardTime;
    }

    public void setCertifiedRewardTime(Date certifiedRewardTime) {
        this.certifiedRewardTime = certifiedRewardTime;
    }

    public boolean getReferrerCertifiedReward() {
        return referrerCertifiedReward;
    }

    public void setReferrerCertifiedReward(boolean referrerCertifiedReward) {
        this.referrerCertifiedReward = referrerCertifiedReward;
    }

    public Date getReferrerCertifiedRewardTime() {
        return referrerCertifiedRewardTime;
    }

    public void setReferrerCertifiedRewardTime(Date referrerCertifiedRewardTime) {
        this.referrerCertifiedRewardTime = referrerCertifiedRewardTime;
    }

    public boolean getFirstRechargeReward() {
        return firstRechargeReward;
    }

    public void setFirstRechargeReward(boolean firstRechargeReward) {
        this.firstRechargeReward = firstRechargeReward;
    }

    public Date getFirstRechargeRewardTime() {
        return firstRechargeRewardTime;
    }

    public void setFirstRechargeRewardTime(Date firstRechargeRewardTime) {
        this.firstRechargeRewardTime = firstRechargeRewardTime;
    }

    public boolean getReferrerFirstRechargeReward() {
        return referrerFirstRechargeReward;
    }

    public void setReferrerFirstRechargeReward(boolean referrerFirstRechargeReward) {
        this.referrerFirstRechargeReward = referrerFirstRechargeReward;
    }

    public Date getReferrerFirstRechargeRewardTime() {
        return referrerFirstRechargeRewardTime;
    }

    public void setReferrerFirstRechargeRewardTime(Date referrerFirstRechargeRewardTime) {
        this.referrerFirstRechargeRewardTime = referrerFirstRechargeRewardTime;
    }

    public boolean getReferrerFirstInvestReward() {
        return referrerFirstInvestReward;
    }

    public void setReferrerFirstInvestReward(boolean referrerFirstInvestReward) {
        this.referrerFirstInvestReward = referrerFirstInvestReward;
    }

    public Date getReferrerFirstInvestRewardTime() {
        return referrerFirstInvestRewardTime;
    }

    public void setReferrerFirstInvestRewardTime(Date referrerFirstInvestRewardTime) {
        this.referrerFirstInvestRewardTime = referrerFirstInvestRewardTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JulyActivityReward that = (JulyActivityReward) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "JulyActivityReward{" +
                "id=" + id +
                ", user=" + user +
                ", referrer=" + referrer +
                ", certifiedReward=" + certifiedReward +
                ", certifiedRewardTime=" + certifiedRewardTime +
                ", referrerCertifiedReward=" + referrerCertifiedReward +
                ", referrerCertifiedRewardTime=" + referrerCertifiedRewardTime +
                ", firstRechargeReward=" + firstRechargeReward +
                ", firstRechargeRewardTime=" + firstRechargeRewardTime +
                ", referrerFirstRechargeReward=" + referrerFirstRechargeReward +
                ", referrerFirstRechargeRewardTime=" + referrerFirstRechargeRewardTime +
                ", referrerFirstInvestReward=" + referrerFirstInvestReward +
                ", referrerFirstInvestRewardTime=" + referrerFirstInvestRewardTime +
                '}';
    }
}
