package com.esoft.jdp2p.risk.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.esoft.archer.user.model.User;

/**
 * 系统金额记录
 * 主要是记录充值提现记录，查看系统账户的余额。
 */
@Entity
@Table(name = "system_money_log")
public class SystemMoneyLog implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private Date time;
	private String type;
	private String reason;
	private String detail;
	private Double money;
//	private Double balance;
	private String fromAccount;
	private Long seqNum;
	private String toAccount;
	private String description;

	// Constructors

	/** default constructor */
	public SystemMoneyLog() {
	}

	/** full constructor */
	public SystemMoneyLog(String id, User user, Timestamp time, String reason,
			Double money, String fromAccount, String toAccount) {
		this.id = id;
		this.user = user;
		this.time = time;
		this.reason = reason;
		this.money = money;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	@Column(name = "seq_num", nullable = false)
	public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
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
	@JoinColumn(name = "operator")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "detail", length = 200)
	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	

//	@Column(name = "balance", nullable = false, precision = 22, scale = 0)
//	public Double getBalance() {
//		return this.balance;
//	}
//
//	public void setBalance(Double balance) {
//		this.balance = balance;
//	}

	@Column(name = "type", nullable=false, length = 200)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "reason", nullable = false, length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "money", nullable = false, precision = 22, scale = 0)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "from_account", length = 32)
	public String getFromAccount() {
		return this.fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	@Column(name = "to_account", length = 32)
	public String getToAccount() {
		return this.toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}