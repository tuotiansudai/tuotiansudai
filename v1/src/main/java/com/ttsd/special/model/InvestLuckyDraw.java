package com.ttsd.special.model;

import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.invest.model.Invest;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "special_invest_lucky_draw")
public class InvestLuckyDraw implements java.io.Serializable {

    // Fields

    private String id;
    private User user;
    private Invest invest;
    private InvestLuckyDrawType type;
    private Date createdTime;
    private InvestLuckyDrawPrizeLevel prizeLevel;
    private Double amount;
    private Date awardTime;
    private boolean isValid;

    public InvestLuckyDraw() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_id")
    public Invest getInvest() {
        return this.invest;
    }

    public void setInvest(Invest invest) {
        this.invest = invest;
    }

    @Column(name = "type", nullable = false, length = 16)
    @Enumerated (EnumType.STRING)
    public InvestLuckyDrawType getType() {
        return this.type;
    }

    public void setType(InvestLuckyDrawType type) {
        this.type = type;
    }

    @Column(name = "created_time", nullable = false, length = 19)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "prize_level", nullable = false, length = 100)
    @Enumerated (EnumType.STRING)
    public InvestLuckyDrawPrizeLevel getPrizeLevel() {
        return prizeLevel;
    }

    public void setPrizeLevel(InvestLuckyDrawPrizeLevel prizeLevel) {
        this.prizeLevel = prizeLevel;
    }


    @Column(name = "amount", nullable = false, precision = 22, scale = 0)
    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "award_time", length = 19)
    public Date getAwardTime() {
        return this.awardTime;
    }

    public void setAwardTime(Date awardTime) {
        this.awardTime = awardTime;
    }

    @Column(name = "is_valid",columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean IsValid() {
        return this.isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid= isValid;
    }
}