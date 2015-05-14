package com.esoft.jdp2p.invest.model;

import com.esoft.archer.user.model.ReferrerRelation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/7.
 */
@Entity
@Table(name = "invest_userReferrer")
// @Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class InvestUserReferrer implements java.io.Serializable{
    private String id;
    private Invest invest;
    private Date time;
    private Double bonus;
    private String referrerId;
    private String status;
    private String  roleName;

    @Column(name = "role_name",nullable = false, length = 100)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_id", nullable = false)
    public Invest getInvest() {
        return invest;
    }

    @Column(name = "referrer_id",length = 32)
    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerRelation) {
        this.referrerId = referrerId;
    }

    public void setInvest(Invest invest) {
        this.invest = invest;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Column(name = "bonus", nullable = false, precision = 22, scale = 0)
    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    @Column(name = "status", nullable = false, length = 19)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
