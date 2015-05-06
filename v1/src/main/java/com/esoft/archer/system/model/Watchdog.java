package com.esoft.archer.system.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Watchdog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "watchdog")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Watchdog implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private String url;
	private String reallyUrl;
	private String ip;
	private Date time;

	// Constructors

	/** default constructor */
	public Watchdog() {
	}

	/** full constructor */
	public Watchdog(String userId, String url, String reallyUrl, String ip,
			Date time) {
		this.userId = userId;
		this.url = url;
		this.reallyUrl = reallyUrl;
		this.ip = ip;
		this.time = time;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Lob 
	@Column(name = "url", nullable = false, columnDefinition="CLOB")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Lob 
	@Column(name = "really_url", nullable = false, columnDefinition="CLOB")
	public String getReallyUrl() {
		return this.reallyUrl;
	}

	public void setReallyUrl(String reallyUrl) {
		this.reallyUrl = reallyUrl;
	}

	@Column(name = "ip", nullable = false, length = 20)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}