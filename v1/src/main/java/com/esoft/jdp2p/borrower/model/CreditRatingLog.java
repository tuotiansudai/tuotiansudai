package com.esoft.jdp2p.borrower.model;
// default package

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

import com.esoft.archer.user.model.User;

/**
 * CreditRatingLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "credit_rating_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CreditRatingLog implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 9190232843085140849L;
	private String id;
	private User user;
	private Date time;
	private String operator;
	private String reason;
	private String details;

	// Constructors

	/** default constructor */
	public CreditRatingLog() {
	}

	/** minimal constructor */
	public CreditRatingLog(String id, User user, Date time,
			String operator, String reason) {
		this.id = id;
		this.user = user;
		this.time = time;
		this.operator = operator;
		this.reason = reason;
	}

	/** full constructor */
	public CreditRatingLog(String id, User user, Date time,
			String operator, String reason, String details) {
		this.id = id;
		this.user = user;
		this.time = time;
		this.operator = operator;
		this.reason = reason;
		this.details = details;
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

	@Column(name = "operator", nullable = false, length = 32)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "reason", nullable = false, length = 200)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "details", length = 200)
	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}