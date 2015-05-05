package com.esoft.jdp2p.invest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.esoft.archer.user.model.User;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.risk.model.RiskRank;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 自动投标实体
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-8 上午10:43:30
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-8 wangzhi 1.0
 */
@Entity
@Table(name = "auto_invest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AutoInvest implements Serializable{
	private static final long serialVersionUID = -6648140561707796695L;
	private String userId;
	private User user;
	/**
	 * 每次投标金额
	 */
	private Double investMoney;
	/**
	 * 借款的最小利率
	 */
	private Double minRate;
	private Double minRatePercent;// 最小利率，整数。不存入数据库

	/**
	 * 借款的最大利率
	 */
	private Double maxRate;
	private Double maxRatePercent;// 最小利率，整数。不存入数据库
	/**
	 * 借款的最短时间
	 */
	private Integer minDeadline;
	/**
	 * 借款的最长时间
	 */
	private Integer maxDeadline;

	/**
	 * 借款的最小风险等级
	 */
	private RiskRank minRiskRank;
	/**
	 * 借款的最大风险等级
	 */
	private RiskRank maxRiskRank;

	/**
	 * 账户保留余额
	 */
	private Double remainMoney;

	/**
	 * 上次自动投标时间
	 */
	private Date lastAutoInvestTime;

	private Integer seqNum;

	/**
	 * 状态（开启 关闭 ）
	 */
	private String status;

	@Column(name = "last_auto_invest_time")
	public Date getLastAutoInvestTime() {
		return lastAutoInvestTime;
	}

	public void setLastAutoInvestTime(Date lastAutoInvestTime) {
		this.lastAutoInvestTime = lastAutoInvestTime;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "invest_money")
	public Double getInvestMoney() {
		return investMoney;
	}

	@Column(name = "max_dealline")
	public Integer getMaxDeadline() {
		return maxDeadline;
	}

	@Column(name = "max_rate")
	public Double getMaxRate() {
		return maxRate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "max_risk_rank")
	public RiskRank getMaxRiskRank() {
		return maxRiskRank;
	}

	@Column(name = "min_deadline")
	public Integer getMinDeadline() {
		return minDeadline;
	}

	@Column(name = "min_rate")
	public Double getMinRate() {
		return minRate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "min_risk_rank")
	public RiskRank getMinRiskRank() {
		return minRiskRank;
	}

	@Column(name = "remain_money")
	public Double getRemainMoney() {
		return remainMoney;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return seqNum;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
	public User getUser() {
		return this.user;
	}

	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setInvestMoney(Double investMoney) {
		this.investMoney = investMoney;
	}

	public void setMaxDeadline(Integer maxDeadline) {
		this.maxDeadline = maxDeadline;
	}

	public void setMaxRate(Double maxRate) {
		this.maxRate = maxRate;
	}

	public void setMaxRiskRank(RiskRank maxRiskRank) {
		this.maxRiskRank = maxRiskRank;
	}

	public void setMinDeadline(Integer minDeadline) {
		this.minDeadline = minDeadline;
	}

	public void setMinRate(Double minRate) {
		this.minRate = minRate;
	}

	public void setMinRiskRank(RiskRank minRiskRank) {
		this.minRiskRank = minRiskRank;
	}

	public void setRemainMoney(Double remainMoney) {
		this.remainMoney = remainMoney;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public void setUser(User user) {
		if (user != null) {
			this.user = user;
			this.userId = user.getId();
		}
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public Double getMinRatePercent() {
		if (this.minRatePercent == null && this.getMinRate() != null) {
			return ArithUtil.round(this.getMinRate() * 100, 2);
		}
		return minRatePercent;
	}

	public void setMinRatePercent(Double minRatePercent) {
		if (minRatePercent != null) {
			this.minRate = ArithUtil.div(minRatePercent, 100, 4);
		}
		this.minRatePercent = minRatePercent;
	}

	@Transient
	public Double getMaxRatePercent() {
		if (this.maxRatePercent == null && this.getMaxRate() != null) {
			return ArithUtil.round(this.getMaxRate() * 100, 2);
		}
		return maxRatePercent;
	}

	public void setMaxRatePercent(Double maxRatePercent) {
		if (maxRatePercent != null) {
			this.maxRate = ArithUtil.div(maxRatePercent, 100, 4);
		}
		this.maxRatePercent = maxRatePercent;
	}

}
