package com.esoft.jdp2p.trusteeship.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 资金托管，关联操作<br/>
 * 类似于开通账户、充值之类
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
@Table(name = "trusteeship_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TrusteeshipOperation {
	private String id;
	/**
	 * 操作类型（开户、投标等等）
	 */
	private String type;
	/**
	 * 操作的唯一标识（与回调的唯一标识一致，用于查询某一条操作记录）
	 */
	private String markId;
	/**
	 * 操作者（如果是开户，此字段为userId；如果为充值，此字段为rechargeId）
	 */
	private String operator;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 请求时间
	 */
	private Date requestTime;
	/**
	 * 请求地址
	 */
	private String requestUrl;
	private String requestData;
	/**
	 * 返回时间
	 */
	private Date responseTime;
	private String responseData;
	/**
	 * 托管方
	 */
	private String trusteeship;
	
	/**
	 * 发送时候的编码
	 */
	private String charset;

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "mark_id", nullable = false, length = 50)
	public String getMarkId() {
		return markId;
	}

	@Column(name = "operator", length = 500)
	public String getOperator() {
		return operator;
	}

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "request_data", columnDefinition = "CLOB")
	public String getRequestData() {
		return requestData;
	}

	@Column(name = "request_time")
	public Date getRequestTime() {
		return requestTime;
	}

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "response_data", columnDefinition = "CLOB")
	public String getResponseData() {
		return responseData;
	}

	@Column(name = "response_time")
	public Date getResponseTime() {
		return responseTime;
	}

	@Column(name = "status", length = 100)
	public String getStatus() {
		return status;
	}

	@Column(name = "trusteeship", length = 200)
	public String getTrusteeship() {
		return trusteeship;
	}

	@Column(name = "type", length = 200)
	public String getType() {
		return type;
	}

	@Column(name = "request_url", length = 1024)
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMarkId(String markId) {
		this.markId = markId;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTrusteeship(String trusteeship) {
		this.trusteeship = trusteeship;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "charset", length=20)
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	
}
