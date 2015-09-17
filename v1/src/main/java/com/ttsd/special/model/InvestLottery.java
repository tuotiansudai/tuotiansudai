package com.ttsd.special.model;

import com.esoft.archer.user.model.User;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.model.Invest;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invest_lottery")
public class InvestLottery implements java.io.Serializable {

    // Fields

    private long id;
    private User user;
    private Invest invest;
    private InvestLotteryType type;
    private Date createdTime;
    private InvestLotteryPrizeType prizeType;
    private Long amount;
    private Date awardTime;
    private Boolean valid;
    private String cashPrize;
    private Date grantedTime;
    private Boolean granted;

    public InvestLottery() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
    public InvestLotteryType getType() {
        return this.type;
    }

    public void setType(InvestLotteryType type) {
        this.type = type;
    }

    @Column(name = "created_time", nullable = false, length = 19)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "prize_type", nullable = false)
    @Enumerated (EnumType.STRING)
    public InvestLotteryPrizeType getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(InvestLotteryPrizeType prizeType) {
        this.prizeType = prizeType;
    }


    @Column(name = "amount")
    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Column(name = "award_time")
    public Date getAwardTime() {
        return this.awardTime;
    }

    public void setAwardTime(Date awardTime) {
        this.awardTime = awardTime;
    }

    @Column(name = "is_valid",columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public Boolean getValid() {
        return this.valid;
    }

    public void setValid(Boolean valid) {
        this.valid= valid;
    }
    @Column(name = "granted_time")
    public Date getGrantedTime() {
        return grantedTime;
    }

    public void setGrantedTime(Date grantedTime) {
        this.grantedTime = grantedTime;
    }
    @Column(name = "is_granted",columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }
    @Transient
    public String getCashPrize() {
        if (this.cashPrize == null && this.getAmount() != null) {
            return  this.getPrizeType().getDesc()+this.amount/100d ;
        }
        return cashPrize;
    }
}