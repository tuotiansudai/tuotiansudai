package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户等级历史
 */
@Entity
@Table(name = "user_level_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserLevelHistory implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private LevelForUser levelForUser;
	/**
	 * 获得该等级的时间
	 */
	private Date grantTime;
	/**
	 * 有效期(秒)
	 */
	private int validityPeriod;
	/**
	 * 到期时间
	 */
	private Date expirationTime;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration_time")
	public Date getExpirationTime() {
		return expirationTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "grant_time")
	public Date getGrantTime() {
		return grantTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level")
	public LevelForUser getLevelForUser() {
		return levelForUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	@Column(name = "validity_period")
	public int getValidityPeriod() {
		return validityPeriod;
	}

	@Lob
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setGrantTime(Date grantTime) {
		this.grantTime = grantTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLevelForUser(LevelForUser levelForUser) {
		this.levelForUser = levelForUser;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

}