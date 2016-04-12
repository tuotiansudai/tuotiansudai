package com.esoft.archer.system.model;

import java.sql.Timestamp;
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
 * AccessStatistics entity. 
 * 访问统计表
 */
@Entity
@Table(name = "access_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AccessStatistics implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private Timestamp accessTime;
	private String accessUrl;
	private String visitorIp;
	/**
	 * 该访问的响应时间
	 */
	private Double responseTime;

	// Constructors

	/** default constructor */
	public AccessStatistics() {
	}

	/** minimal constructor */
	public AccessStatistics(String id, Timestamp accessTime, String accessUrl) {
		this.id = id;
		this.accessTime = accessTime;
		this.accessUrl = accessUrl;
	}

	/** full constructor */
	public AccessStatistics(String id, User user, Timestamp accessTime,
			String accessUrl, String visitorIp, Double responseTime) {
		this.id = id;
		this.user = user;
		this.accessTime = accessTime;
		this.accessUrl = accessUrl;
		this.visitorIp = visitorIp;
		this.responseTime = responseTime;
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
	@JoinColumn(name = "uid")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "access_time", nullable = false, length = 19)
	public Timestamp getAccessTime() {
		return this.accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	@Column(name = "access_url", nullable = false, length = 500)
	public String getAccessUrl() {
		return this.accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	@Column(name = "visitor_ip", length = 64)
	public String getVisitorIp() {
		return this.visitorIp;
	}

	public void setVisitorIp(String visitorIp) {
		this.visitorIp = visitorIp;
	}

	@Column(name = "response_time", precision = 22, scale = 0)
	public Double getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}

}