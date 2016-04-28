package com.esoft.jdp2p.bankcode.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "bank_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BankCode implements java.io.Serializable {

	private static final long serialVersionUID = -6203572245481040371L;

	private String id;
	private String name;
	private String icon;
	/**
	 * 该银行卡在第三方支付中的编码
	 */
	private String payCode;
	private String type;
	private String status;
	private int seqNum;

	@Column(name = "icon", length = 1000)
	public String getIcon() {
		return icon;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "name", length = 200)
	public String getName() {
		return name;
	}

	@Column(name = "pay_code", length = 200)
	public String getPayCode() {
		return payCode;
	}

	@Column(name = "seq_num")
	public int getSeqNum() {
		return seqNum;
	}

	@Column(name = "status", length = 200)
	public String getStatus() {
		return status;
	}

	@Column(name = "type", length = 200)
	public String getType() {
		return type;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

}
