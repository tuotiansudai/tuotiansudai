package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "attention_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AttentionUser {

	private String id;
	private User user;
	private User attentionUser;
	private Date attentionTime;

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "attention_time")
	public Date getAttentionTime() {
		return attentionTime;
	}

	public void setAttentionTime(Date attentionTime) {
		this.attentionTime = attentionTime;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attention_user_id")
	public User getAttentionUser() {
		return attentionUser;
	}

	public void setAttentionUser(User attentionUser) {
		this.attentionUser = attentionUser;
	}

}
