package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 借款者账户
 */
@Entity
@Table(name = "user_bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserBill implements java.io.Serializable {

	// Fields

	private String id;
	private Long seqNum;
	private User user;
	private Date time;
	private String type;
	private String typeInfo;
	private Double money;
	private String detail;
	private Double balance;
	/**
	 * 冻结金额
	 */
	private Double frozenMoney;
	private String operator;

	public UserBill() {
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

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "type", nullable = false, length = 100)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "type_info", nullable = false, length = 200)
	public String getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	@Column(name = "money", nullable = false, precision = 22, scale = 0)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "detail", length = 200)
	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Column(name = "balance", precision = 22, scale = 0)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "frozen_money")
	public Double getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(Double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	@Column(name = "seq_num", nullable = false)
	public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	@Column(name = "operator")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}