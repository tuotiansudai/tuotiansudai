package com.esoft.jdp2p.risk.model;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 费用规则（费率）
 * 
 * @since 2.0
 */
@Entity
@Table(name = "fee_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class FeeConfig implements java.io.Serializable {

	// Fields

	private String id;

	// 节点
	private String feePoint;
	// 费用类型
	private String feeType;
	// 影响因子(用户等级、vip、项目风险等级之类)
	private String factor;
	// 影响因子值
	private String factorValue;
	// 费
	private Double fee;
	// 费类型（固定值或者费率）
	private String operateMode;
	// 费用上限（固定值）
	private Double feeUpperLimit;
	// 区间上限（小于）
	private Double intervalUpperLimit;
	// 区间下限（大于等于）
	private Double intervalLowerLimit;

	private String description;

	@Column(name = "description", length = 200)
	public String getDescription() {
		return this.description;
	}

	@Column(name = "factor", length = 400)
	public String getFactor() {
		return factor;
	}

	@Column(name = "factor_value", length = 400)
	public String getFactorValue() {
		return factorValue;
	}

	@Column(name = "fee", precision = 22, scale = 10)
	public Double getFee() {
		return this.fee;
	}

	@Column(name = "fee_point", length = 400)
	public String getFeePoint() {
		return feePoint;
	}

	@Column(name = "fee_type", length = 400)
	public String getFeeType() {
		return feeType;
	}

	@Column(name = "fee_upper_limit")
	public Double getFeeUpperLimit() {
		return feeUpperLimit;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "interval_lower_limit")
	public Double getIntervalLowerLimit() {
		return intervalLowerLimit;
	}

	@Column(name = "interval_upper_limit")
	public Double getIntervalUpperLimit() {
		return intervalUpperLimit;
	}
	
	@Column(name = "operate_mode")
	public String getOperateMode() {
		return operateMode;
	}

	public void setOperateMode(String operateMode) {
		this.operateMode = operateMode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	public void setFactorValue(String factorValue) {
		this.factorValue = factorValue;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public void setFeePoint(String feePoint) {
		this.feePoint = feePoint;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public void setFeeUpperLimit(Double feeUpperLimit) {
		this.feeUpperLimit = feeUpperLimit;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIntervalLowerLimit(Double intervalLowerLimit) {
		this.intervalLowerLimit = intervalLowerLimit;
	}

	public void setIntervalUpperLimit(Double intervalUpperLimit) {
		this.intervalUpperLimit = intervalUpperLimit;
	}

}