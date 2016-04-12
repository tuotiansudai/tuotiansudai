package com.esoft.jdp2p.repay.model;

// default package

import java.util.Date;

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
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;

/**
 * 还款
 */
public class Repay {

	/**
	 * 当前还款为第几期
	 */
	private Integer period;
	/**
	 * 还款日
	 */
	private Date repayDay;
	/**
	 * 本金
	 */
	private Double corpus;
	/**
	 * 利息
	 */
	private Double interest;

	/**
	 * 罚息（逾期利息+网站逾期罚息）
	 */
	private Double defaultInterest;

	/**
	 * 本期长度
	 */
	private Integer length;

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Date getRepayDay() {
		return repayDay;
	}

	public void setRepayDay(Date repayDay) {
		this.repayDay = repayDay;
	}

	public Double getCorpus() {
		return corpus;
	}

	public void setCorpus(Double corpus) {
		this.corpus = corpus;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Double getDefaultInterest() {
		return defaultInterest;
	}

	public void setDefaultInterest(Double defaultInterest) {
		this.defaultInterest = defaultInterest;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}