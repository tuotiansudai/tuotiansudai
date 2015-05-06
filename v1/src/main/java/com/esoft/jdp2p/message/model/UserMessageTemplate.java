package com.esoft.jdp2p.message.model;

import javax.persistence.CascadeType;
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
 * 发送提醒信息的模板
 */
@Entity
@Table(name = "user_message_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserMessageTemplate implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private UserMessageNode userMessageNode;
	private UserMessageWay userMessageWay;
	/**
	 * 可选，关闭，必选
	 */
	private String status;
	private String template;
	private String description;

	// Constructors

	/** default constructor */
	public UserMessageTemplate() {
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	@Column(name = "template", nullable = false, length = 100000)
	public String getTemplate() {
		return template;
	}

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "message_node", nullable = false)
	public UserMessageNode getUserMessageNode() {
		return this.userMessageNode;
	}

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "message_way")
	public UserMessageWay getUserMessageWay() {
		return this.userMessageWay;
	}

	@Column(name = "name", length = 200)
	public String getName() {
		return name;
	}
	

	@Column(name = "status",nullable=false, length = 50)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setUserMessageNode(UserMessageNode userMessageNode) {
		this.userMessageNode = userMessageNode;
	}

	public void setUserMessageWay(UserMessageWay userMessageWay) {
		this.userMessageWay = userMessageWay;
	}

}