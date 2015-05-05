package com.esoft.jdp2p.message.model;

// default package

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
 * 发送提醒信息的方式（短信、站内信、邮件等）
 */
@Entity
@Table(name = "user_message_way")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserMessageWay implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	/**
	 * 状态（开、关）
	 */
	private String status;
	private String description;

	/** default constructor */
	public UserMessageWay() {
	}

	// Constructors

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}