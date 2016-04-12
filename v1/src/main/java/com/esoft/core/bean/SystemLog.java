package com.esoft.core.bean;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * SystemLog entity. 
 * 系统日志，利用Log4j直接写入数据库
 */
@Entity
@Table(name = "system_log")
public class SystemLog implements java.io.Serializable {

	// Fields

	private String id;
	private Timestamp logDate;
	private String logLevel;
	private String ip;
	private String info;

	// Constructors

	/** default constructor */
	public SystemLog() {
	}

	/** minimal constructor */
	public SystemLog(String id) {
		this.id = id;
	}

	/** full constructor */
	public SystemLog(String id, Timestamp logDate, String logLevel, String ip,
			String info) {
		this.id = id;
		this.logDate = logDate;
		this.logLevel = logLevel;
		this.ip = ip;
		this.info = info;
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

	@Column(name = "log_date", length = 19)
	public Timestamp getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Timestamp logDate) {
		this.logDate = logDate;
	}

	@Column(name = "log_level", length = 20)
	public String getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	@Column(name = "ip", length = 64)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Lob 
	@Column(name = "info", columnDefinition="CLOB")
	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}