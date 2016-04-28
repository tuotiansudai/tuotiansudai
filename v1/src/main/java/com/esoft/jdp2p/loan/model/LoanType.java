package com.esoft.jdp2p.loan.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "loan_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LoanType {

	private String id;
	private String name;
	private String description;
	
	/**
	 * 还款类型（等额本息 之类）
	 */
	private String repayType;
	/**
	 * 还款账单（按天还、按月还）
	 */
	private String repayTimeUnit;
	/**
	 * 还款周期（几天、几月）
	 */
	private Integer repayTimePeriod;
	/**
	 * 计息方式
	 */
	private String interestType;

	/**
	 * 计息节点
	 */
	private String interestPoint;

	public LoanType() {
	}

	public LoanType(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return description;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "interest_point", length = 200)
	public String getInterestPoint() {
		return interestPoint;
	}

	@Column(name = "interest_type", length = 200)
	public String getInterestType() {
		return interestType;
	}

	@Column(name = "name", length = 32)
	public String getName() {
		return name;
	}

	@Column(name = "repay_time_period")
	public Integer getRepayTimePeriod() {
		return repayTimePeriod;
	}

	@Column(name = "repay_time_unit", length = 200)
	public String getRepayTimeUnit() {
		return repayTimeUnit;
	}
	
	@Column(name = "repay_type", length = 200)
	public String getRepayType() {
		return repayType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInterestPoint(String interestPoint) {
		this.interestPoint = interestPoint;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRepayTimePeriod(Integer repayTimePeriod) {
		this.repayTimePeriod = repayTimePeriod;
	}

	public void setRepayTimeUnit(String repayTimeUnit) {
		this.repayTimeUnit = repayTimeUnit;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

}
