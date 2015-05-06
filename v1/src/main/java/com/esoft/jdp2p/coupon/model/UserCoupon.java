package com.esoft.jdp2p.coupon.model;

// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.loan.model.Recharge;

/**
 * 用户持有优惠券 Coupon entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserCoupon implements java.io.Serializable {

	// Fields

	private String id;
	/**
	 * 持有用户
	 */
	private User user;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 生成时间
	 */
	private Date generateTime;
	/**
	 * 使用时间
	 */
	private Date usedTime;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 优惠券
	 */
	private Coupon coupon;
	/**
	 * 有效期
	 */
	private Date deadline;

	private Set<Recharge> recharges = new HashSet<Recharge>(0);

	// Constructors

	/** default constructor */
	public UserCoupon() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon", nullable = false)
	public Coupon getCoupon() {
		return coupon;
	}

	@Column(name = "deadline")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDeadline() {
		return this.deadline;
	}

	@Lob 
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {
		return description;
	}

	@Column(name = "generate_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getGenerateTime() {
		return this.generateTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "coupon")
	public Set<Recharge> getRecharges() {
		return this.recharges;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return this.status;
	}

	@Column(name = "used_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUsedTime() {
		return this.usedTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return this.user;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRecharges(Set<Recharge> recharges) {
		this.recharges = recharges;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public void setUser(User user) {
		this.user = user;
	}

}