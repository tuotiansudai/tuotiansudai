package com.esoft.jdp2p.invest.model;

import com.esoft.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invest_userReferrer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class InvestUserReferrer implements java.io.Serializable{

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    private String id;
    private Invest invest;
    private Date time;
    private Double bonus;
    private User referrer;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id",nullable = false)
    public User getReferrer() {
        return referrer;
    }

    public void setReferrer(User referrer) {
        this.referrer = referrer;
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
