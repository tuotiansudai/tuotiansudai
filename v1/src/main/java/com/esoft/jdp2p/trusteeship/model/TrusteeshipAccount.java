package com.esoft.jdp2p.trusteeship.model;

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
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 资金托管账户
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-31 下午3:44:51
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-31 wangzhi 1.0
 */
@Entity
@Table(name = "trusteeship_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class TrusteeshipAccount {
	private String id;
	private User user;
	/**
	 * 托管方
	 */
	private String trusteeship;
	/**
	 * 用户在托管方的编号
	 */
	private String accountId;
	/**
	 * 在托管方的开户时间
	 */
	private Date createTime;
	private String status;

	@Column(name = "account_id", length = 500)
	public String getAccountId() {
		return accountId;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "status", length = 500, nullable = false)
	public String getStatus() {
		return status;
	}

	@Column(name = "trusteeship", length = 500, nullable = false)
	public String getTrusteeship() {
		return trusteeship;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTrusteeship(String trusteeship) {
		this.trusteeship = trusteeship;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
