package com.esoft.archer.common.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.User;

/**
 * 认证信息实体
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "auth_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AuthInfo {
	private String id;

	/**
	 * 生成时间
	 */
	private Date generationTime;
	/**
	 * 截止日期
	 */
	private Date deadline;
	/**
	 * 认证码(不可重复)
	 */
	private String authCode;
	
	/**
	 * 验证目标，记录手机号或者邮箱等
	 */
	private String authTarget;
	
	/**
	 * 验证来源，userId之类
	 */
	private String authSource;
	/**
	 * 认证码类型
	 */
	private String authType;
	
	private String status;

	@Column(name = "auth_code", nullable = false, length = 1000)
	public String getAuthCode() {
		return authCode;
	}
	
	@Column(name = "auth_target", nullable = false, length = 1000)
	public String getAuthTarget() {
		return authTarget;
	}

	@Column(name = "auth_type", nullable = false, length = 200)
	public String getAuthType() {
		return authType;
	}

	@Column(name = "deadline")
	public Date getDeadline() {
		return deadline;
	}

	@Column(name = "generation_time", nullable = false)
	public Date getGenerationTime() {
		return generationTime;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	@Column(name = "status", nullable = false, length = 100)
	public String getStatus() {
		return status;
	}
	
	@Column(name = "auth_source", length = 1000)
	public String getAuthSource() {
		return authSource;
	}

	public void setAuthSource(String authSource) {
		this.authSource = authSource;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public void setAuthTarget(String authTarget) {
		this.authTarget = authTarget;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setGenerationTime(Date generationTime) {
		this.generationTime = generationTime;
	}

	public void setId(String id) {
		this.id = id;
	}

}
