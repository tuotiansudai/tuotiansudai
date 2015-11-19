package com.esoft.jdp2p.loan.model;

// default package

import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.coupon.model.UserCoupon;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Recharge entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "recharge")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Recharge implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	// 优惠券
	private UserCoupon coupon;
	private Date time;
	// 到账金额
	private Double actualMoney;
	/**
	 * 充值方式(不可为空)
	 */
	private String rechargeWay;
	// 手续费
	private Double fee;
	private Date successTime;
	private String status;
	// 是否为管理员充值
	private boolean isRechargedByAdmin;

	private String remark;

	private String source;

	private String channel;

	// Constructors

	/** default constructor */
	public Recharge() {
	}

	/** minimal constructor */
	public Recharge(String id, User user, Timestamp time, Double actualMoney,
			String rechargeWay, Double fee, boolean isRechargedByAdmin) {
		this.id = id;
		this.user = user;
		this.time = time;
		this.actualMoney = actualMoney;
		this.rechargeWay = rechargeWay;
		this.fee = fee;
		this.isRechargedByAdmin = isRechargedByAdmin;
	}

	// Property accessors
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
	@JoinColumn(name = "coupon")
	public UserCoupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(UserCoupon coupon) {
		this.coupon = coupon;
	}

	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	@Column(name = "success_time", length = 19)
	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	@Column(name = "status", nullable = false, length = 100)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "actual_money", nullable = false, precision = 22, scale = 0)
	public Double getActualMoney() {
		return this.actualMoney;
	}

	public void setActualMoney(Double actualMoney) {
		this.actualMoney = actualMoney;
	}

	@Column(name = "recharge_way", length = 32)
	public String getRechargeWay() {
		return this.rechargeWay;
	}

	public void setRechargeWay(String rechargeWay) {
		this.rechargeWay = rechargeWay;
	}

	@Column(name = "fee", nullable = false, precision = 22, scale = 0)
	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Column(name = "is_recharged_by_admin", nullable = false, columnDefinition = "BOOLEAN")
	public boolean getIsRechargedByAdmin() {
		return this.isRechargedByAdmin;
	}

	public void setIsRechargedByAdmin(boolean isRechargedByAdmin) {
		this.isRechargedByAdmin = isRechargedByAdmin;
	}

	@Column(name = "remark", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "source", length = 10)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "channel", length = 32)
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}