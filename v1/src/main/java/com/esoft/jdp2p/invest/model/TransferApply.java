package com.esoft.jdp2p.invest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.invest.service.TransferService;

/**
 * 债权转让申请
 * 
 * @author Administrator
 * 
 */
/**
 * @author Administrator
 * 
 */
@Entity
@Table(name = "transfer_apply")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TransferApply implements java.io.Serializable {

	// Fields
	private String id;
	/**
	 * 该债权来自哪笔投资
	 */
	private Invest invest;

	/**
	 * 债权转让申请时间
	 */
	private Date applyTime;

	/**
	 * 债权转让申请到期时间
	 */
	private Date deadline;

	/**
	 * 转让的本金
	 */
	private double corpus;

	/** 折让金 */
	private double premium;

	private String status;

	private List<Invest> invests = new ArrayList<Invest>(0);

	@Column(name = "apply_time")
	public Date getApplyTime() {
		return applyTime;
	}

	@Column(name = "corpus")
	public double getCorpus() {
		return corpus;
	}

	@Column(name = "deadline")
	public Date getDeadline() {
		return deadline;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invest_id")
	public Invest getInvest() {
		return invest;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@Column(name = "premium")
	public double getPremium() {
		return premium;
	}

	@OneToMany(mappedBy = "transferApply")
	public List<Invest> getInvests() {
		return invests;
	}

	public void setInvests(List<Invest> invests) {
		this.invests = invests;
	}

	public void setPremium(double premium) {
		this.premium = premium;
	}

	public void setCorpus(double corpus) {
		this.corpus = corpus;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInvest(Invest invest) {
		this.invest = invest;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 债权价格
	 * 
	 * @return
	 */
	@Transient
	public double getWorth() {
		if (this.getInvest() != null) {
			TransferService transferService = (TransferService) SpringBeanUtil
					.getBeanByName("transferService");
			return transferService.calculateWorth(this.getInvest().getId(),
					this.getCorpus());
		}
		throw new RuntimeException("unexpected invocation");
	}

	/**
	 * 转出价格
	 * 
	 * @return
	 */
	@Transient
	public double getPrice() {
		return ArithUtil.sub(getWorth(), getPremium());
	}

	/**
	 * 剩余价值
	 * 
	 * @return
	 */
	@Transient
	public double getRemainWorth() {
		return ArithUtil.round(getRemainCorpus() / getCorpus() * getWorth(), 2);
	}

	/**
	 * 剩余本金
	 * 
	 * @return
	 */
	@Transient
	public double getRemainCorpus() {
		if (StringUtils.isNotEmpty(this.getId())) {
			TransferService transferService = (TransferService) SpringBeanUtil
					.getBeanByName("transferService");
			return transferService.calculateRemainCorpus(this.getId());
		}
		throw new RuntimeException("unexpected invocation");
	}

}