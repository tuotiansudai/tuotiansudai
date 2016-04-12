package com.esoft.jdp2p.message.model;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 发送提醒信息的节点（成功发起借款，有人投标等）
 */
@Entity
@Table(name = "user_message_node")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserMessageNode implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	/**
	 * 状态（开、关）
	 */
	private String status;
	private String description;

	// Constructors

	/** default constructor */
	public UserMessageNode() {
	}

	@Column(name = "description", length = 200)
	public String getDescription() {
		return this.description;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
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