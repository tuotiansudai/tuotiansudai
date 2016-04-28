package com.esoft.jdp2p.risk.model;

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

/**
 * 风险准备金(发起借款时候扣除的)
 * @author Administrator
 *
 */
@Entity
@Table(name = "risk_preparedness")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class RiskPreparedness {

	private String id;
	private String rate;
	private String description;
	private RiskRank riskRank;
	
	
	
	public RiskPreparedness() {
		
	}

	public RiskPreparedness(String id, String rate, String description,
			RiskRank riskRank) {
		this.id = id;
		this.rate = rate;
		this.description = description;
		this.riskRank = riskRank;
	}

	@Id
	@Column(name = "id" , nullable = false , unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "rate")
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "risk_rank")
	public RiskRank getRiskRank() {
		return riskRank;
	}

	public void setRiskRank(RiskRank riskRank) {
		this.riskRank = riskRank;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
