package com.esoft.jdp2p.coupon.model;

// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 优惠券 Coupon entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Coupon implements java.io.Serializable {

	// Fields

	/**
	 * 规则：使用节点+类型+金额+使用下限
	 */
	private String id;
	/**
	 * 优惠券名称
	 */
	private String name;
	/**
	 * 代金券类型
	 */
	private String type;

	/**
	 * 状态（是否可用等等），为此优惠券整体的状态
	 */
	private String status;
	/**
	 * 金额
	 */
	private Double money;

	/**
	 * 使用下限（多少钱以上才可以使用该优惠券）
	 */
	private Double lowerLimitMoney;
	/**
	 * 有效期
	 */
	private Integer periodOfValidity;

	// Constructors

	/** default constructor */
	public Coupon() {
	}

	@Column(name = "periodOfValidity")
	public Integer getPeriodOfValidity() {
		return periodOfValidity;
	}

	public void setPeriodOfValidity(Integer periodOfValidity) {
		this.periodOfValidity = periodOfValidity;
	}

	@Column(name = "lower_limit_money", precision = 22, scale = 0)
	public Double getLowerLimitMoney() {
		return lowerLimitMoney;
	}

	public void setLowerLimitMoney(Double lowerLimitMoney) {
		this.lowerLimitMoney = lowerLimitMoney;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "money", precision = 22, scale = 0)
	public Double getMoney() {
		return this.money;
	}

	@Column(name = "name", length = 200)
	public String getName() {
		return this.name;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return this.status;
	}

	@Column(name = "type", nullable = false, length = 200)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}