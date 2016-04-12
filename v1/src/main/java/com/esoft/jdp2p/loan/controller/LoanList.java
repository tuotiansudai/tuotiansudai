package com.esoft.jdp2p.loan.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.repay.RepayConstants.RepayUnit;
import com.esoft.jdp2p.statistics.controller.LoanStatistics;

/**
 * Description: 借款查询相关 Copyright: Copyright (c)2013 Company: jdp2p
 * 
 * @author: yinjunlu
 * @version: 1.0 Create at: 2014-1-10 上午9:31:08
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanList extends EntityQuery<Loan> implements Serializable {

	private static final String lazyModelCountHql = "select count(distinct loan) from Loan loan left join loan.loanAttrs attr ";
	private static final String lazyModelHql = "select distinct loan from Loan loan left join loan.loanAttrs attr";

	private Double loanMoney;
	private String loanPurpose;
	private String riskLevel;
	private Double indexLoanRate;
	// 还款时间
	private Integer minDeadline;
	private Integer maxDeadline;

	// 实际借款金额
	private Double minMoney;
	private Double maxMoney;

	// 借款金额
	private Double minLoanMoney;
	private Double maxLoanMoney;
	// 利率
	private Double minRate;
	private Double maxRate;

	private Date searchCommitMinTime;
	private Date searchCommitMaxTime;

	@Resource
	private LoanService loanService;

	public LoanList() {
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);
		final String[] RESTRICTIONS = { "loan.id like #{loanList.example.id}",
				"loan.repayType like #{loanList.example.repayType}",
				"loan.status like #{loanList.example.status}",
				"loan.status <> 'test'",
				"loan.name like #{loanList.example.name}",
				"loan.rate >=#{loanList.minRate}",
				"loan.rate <=#{loanList.maxRate}",
				"loan.status like #{loanList.example.status}",
				"loan.riskLevel like #{loanList.example.riskLevel}",
				"loan.type like #{loanList.example.type}",
				"loan.user.id = #{loanList.example.user.id}",
				"loan.user.username like #{loanList.example.user.username}",
				"loan.businessType like #{loanList.example.businessType}",
//				"(loan.deadline >= #{loanList.minDeadline} and loan.repayPeriod = 'month' or loan.deadline >= #{loanList.minDeadline2*30} and loan.repayPeriod = 'day')",
//				"(loan.deadline <= #{loanList.maxDeadline} and loan.repayPeriod = 'month' or loan.deadline <= #{loanList.maxDeadline2*30} and loan.repayPeriod = 'day')",
				"loan.money >= #{loanList.minMoney}",
				"loan.money <= #{loanList.maxMoney}",
				"loan.loanMoney >= #{loanList.minLoanMoney}",
				"loan.loanMoney <= #{loanList.maxLoanMoney}",
				"loan.commitTime >= #{loanList.searchCommitMinTime}",
				"loan.commitTime <= #{loanList.searchCommitMaxTime}",
				"loan.loanPurpose like #{loanList.example.loanPurpose}"
		/* "url like #{loanList.example.url}" */};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		Loan loan = new Loan();
		loan.setUser(new User());
		setExample(loan);
	}
	
	/**
	 * 筛选借款期限
	 * 
	 * @param minDeadline
	 * @param maxDeadline
	 */
	public void setMinAndMaxDeadline(Integer minDeadline, Integer maxDeadline) {
		setMinDeadline(minDeadline);
		setMaxDeadline(maxDeadline);
	}

	public Double getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(Double loanMoney) {
		this.loanMoney = loanMoney;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public Double getIndexLoanRate() {
		return indexLoanRate;
	}

	public void setIndexLoanRate(Double indexLoanRate) {
		this.indexLoanRate = indexLoanRate;
	}

	public Integer getMinDeadline() {
		return minDeadline;
	}
	//FIXME: 解决参数个数问题，即可用统一方法解决。
	private String minDeadlineQuery;
	public void setMinDeadline(Integer minDeadline) {
		if(minDeadline != null){
			this.removeRestriction(minDeadlineQuery);
			minDeadlineQuery = "(loan.deadline >= ("+ minDeadline+" * loan.type.repayTimePeriod) and loan.type.repayTimeUnit = '"+RepayUnit.MONTH+"' or loan.deadline >= ("+(minDeadline*30)+" * loan.type.repayTimePeriod) and loan.type.repayTimeUnit = '"+RepayUnit.DAY+"')";
			this.addRestriction(minDeadlineQuery);
		}
		this.minDeadline = minDeadline;
	}

	public Integer getMaxDeadline() {
		return maxDeadline;
	}
	private String maxDeadlineQuery;
	public void setMaxDeadline(Integer maxDeadline) {
		if(minDeadline != null){
			this.removeRestriction(maxDeadlineQuery);
			this.maxDeadlineQuery = "(loan.deadline <= ("+maxDeadline+" * loan.type.repayTimePeriod) and loan.type.repayTimeUnit = '"+RepayUnit.MONTH+"' or loan.deadline <= ("+(maxDeadline*30)+" * loan.type.repayTimePeriod) and loan.type.repayTimeUnit = '"+RepayUnit.DAY+"')";
			this.addRestriction(maxDeadlineQuery);
		}
		this.maxDeadline = maxDeadline;
	}

	public Double getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(Double minMoney) {
		this.minMoney = minMoney;
	}

	public Double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Date getSearchCommitMinTime() {
		return searchCommitMinTime;
	}

	public void setSearchCommitMinTime(Date searchCommitMinTime) {
		this.searchCommitMinTime = searchCommitMinTime;
	}

	public Date getSearchCommitMaxTime() {
		return searchCommitMaxTime;
	}

	public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
		this.searchCommitMaxTime = searchCommitMaxTime;
	}

	public Double getMinRate() {
		return minRate;
	}

	public void setMinRate(Double minRate) {
		this.minRate = minRate;
	}

	public Double getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(Double maxRate) {
		this.maxRate = maxRate;
	}

	/**
	 * 查询，金额范围
	 * 
	 * @param min
	 *            最新金额
	 * @param max
	 *            最大金额
	 */
	public void setMinAndMaxMoney(Double min, Double max) {
		setMinMoney(min);
		setMaxMoney(max);
	}

	public void setMinAndMaxRate(Double min, Double max) {
		setMinRate(min);
		setMaxRate(max);
	}
	
	public void setMinAndMaxLoanMoney(Double min, Double max) {
		setMinLoanMoney(min);
		setMaxLoanMoney(max);
	}
	
	public Double getMinLoanMoney() {
		return minLoanMoney;
	}

	public void setMinLoanMoney(Double minLoanMoney) {
		this.minLoanMoney = minLoanMoney;
	}

	public Double getMaxLoanMoney() {
		return maxLoanMoney;
	}

	public void setMaxLoanMoney(Double maxLoanMoney) {
		this.maxLoanMoney = maxLoanMoney;
	}

	/**
	 * 计算所有的企业融资金额
	 * 
	 * @deprecated
	 * @see LoanStatistics.getSumLoanMoney
	 * @return
	 */
	@Deprecated
	public double getAllLoansMoney() {
		String hql = "select sum(loan.money) from Loan loan "
				+ "where loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ?";
		List<Object> oos = getHt().find(
				hql,
				new String[] { LoanConstants.LoanStatus.RAISING,
						LoanConstants.LoanStatus.RECHECK,
						LoanConstants.LoanStatus.REPAYING,
						LoanConstants.LoanStatus.OVERDUE,
						LoanConstants.LoanStatus.COMPLETE,
						LoanConstants.LoanStatus.BAD_DEBT });
		if (oos.get(0) == null) {
			return 0D;
		}
		return (Double) oos.get(0);
	}

	
}
