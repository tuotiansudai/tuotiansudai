package com.esoft.jdp2p.borrower.model;

// default package

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.User;

/**
 * BorrowerInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "borrower_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerInfo implements java.io.Serializable {

	// Fields

	private String userId;
	private User user;
	private String creditRating;
	private Double creditLimit;
	private String riskLevel;
	private Double riskFactor;
	private BorrowerAdditionalInfo borrowerAdditionalInfo;
	private BorrowerAuthentication borrowerAuthentication;
	private BorrowerPersonalInfo borrowerPersonalInfo;

	// Constructors

	/** default constructor */
	public BorrowerInfo() {
	}

	/** minimal constructor */
	public BorrowerInfo(User user) {
		this.userId = user.getId();
		this.user = user;
	}

	@Column(name = "credit_limit", precision = 22, scale = 0)
	public Double getCreditLimit() {
		return this.creditLimit;
	}

	@Column(name = "credit_rating", length = 100)
	public String getCreditRating() {
		return this.creditRating;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
	public BorrowerAdditionalInfo getBorrowerAdditionalInfo() {
		return this.borrowerAdditionalInfo;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
	public BorrowerAuthentication getBorrowerAuthentication() {
		return this.borrowerAuthentication;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
	public BorrowerPersonalInfo getBorrowerPersonalInfo() {
		return this.borrowerPersonalInfo;
	}

	@Column(name = "risk_factor", precision = 22, scale = 0)
	public Double getRiskFactor() {
		return this.riskFactor;
	}

	@Column(name = "risk_level", length = 100)
	public String getRiskLevel() {
		return this.riskLevel;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
	public User getUser() {
		return this.user;
	}

	// Property accessors
	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public void setCreditRating(String creditRating) {
		this.creditRating = creditRating;
	}

	public void setBorrowerAdditionalInfo(
			BorrowerAdditionalInfo borrowerAdditionalInfo) {
		this.borrowerAdditionalInfo = borrowerAdditionalInfo;
	}

	public void setBorrowerAuthentication(
			BorrowerAuthentication borrowerAuthentication) {
		this.borrowerAuthentication = borrowerAuthentication;
	}

	public void setRiskFactor(Double riskFactor) {
		this.riskFactor = riskFactor;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public void setUser(User user) {
		if (user != null) {
			this.user = user;
			this.userId = user.getId();
		}
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setBorrowerPersonalInfo(
			BorrowerPersonalInfo borrowerPersonalInfo) {
		this.borrowerPersonalInfo = borrowerPersonalInfo;
	}

}