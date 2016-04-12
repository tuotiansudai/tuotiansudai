package com.esoft.jdp2p.message.model;

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
 * 用户设置的信息发送节点、方式
 */
@Entity
@Table(name = "user_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserMessage implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private UserMessageTemplate userMessageTemplate;
//	private String status;

	// Constructors

	/** default constructor */
	public UserMessage() {
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}
	
//	@Column(name = "status", nullable = false, length = 50)
//	public String getStatus() {
//		return status;
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "message_template", nullable = false)
	public UserMessageTemplate getUserMessageTemplate() {
		return userMessageTemplate;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public void setStatus(String status) {
//		this.status = status;
//	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserMessageTemplate(UserMessageTemplate userMessageTemplate) {
		this.userMessageTemplate = userMessageTemplate;
	}

}