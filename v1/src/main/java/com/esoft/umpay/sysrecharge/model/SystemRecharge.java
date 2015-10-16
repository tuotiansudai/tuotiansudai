package com.esoft.umpay.sysrecharge.model;

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
@Table(name = "system_recharge")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class SystemRecharge implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private Date time;
	private Double money;
	private Date successTime;
	private String status;
	private String remark;

	// Constructors

	/** default constructor */
	public SystemRecharge() {
	}

	/** minimal constructor */
	public SystemRecharge(String id, User user, Timestamp time, Double money) {
		this.id = id;
		this.user = user;
		this.time = time;
		this.money = money;
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

	@Column(name = "money", nullable = false, precision = 22, scale = 0)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "remark", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}