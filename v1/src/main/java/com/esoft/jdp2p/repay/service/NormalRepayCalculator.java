package com.esoft.jdp2p.repay.service;

import java.util.Date;
import java.util.List;

import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.model.RepayCustomPeriod;

/**
 * Filename: RepayManage.java Description: 正常还款数据 计算接口 Company: jdp2p
 * 
 * @author: wangzhi
 * @version: 1.0
 * @since 2.0
 */
public interface NormalRepayCalculator {

	// ////////////////////////正常还款--开始////////////////////////////////////////////////

	/**
	 * 得出正常还款的还款数据
	 * 
	 * @param money
	 *            金额
	 * @param rate
	 *            年利率
	 * @param deadline
	 *            总期数
	 * @param repayTimeUnit
	 *            还款账单（天、月），还款周期的最小单位
	 * @param repayTimePeriod
	 *            还款周期 （两天，三月之类）
	 * @param interestBeginTime
	 *            开始计息时间
	 * @param interestType
	 *            计息方式（按天、按月）
	 * @param customPeriods
	 *            完全自定义还款总期数、每期还款时间、每期还款本金所占全部本金的百分比
	 * @return 还款数据
	 */
	// public List<LoanRepay> generateLoanRepays(double money, double rate,
	// Integer deadline, String repayTimeUnit, Integer repayTimePeriod,
	// Date interestBeginTime, String interestType, String interestBeginPoint,
	// List<RepayCustomPeriod> customPeriods);

	/**
	 * 得出正常还款的还款数据
	 * 
	 * @param investMoney
	 *            投资金额
	 * @param investTime
	 *            投资时间
	 * @param rate
	 *            年利率
	 * @param deadline
	 *            总期数
	 * @param repayTimeUnit
	 *            还款账单（天、月），还款周期的最小单位
	 * @param repayTimePeriod
	 *            还款周期 （两天，三月之类）
	 * @param interestBeginTime
	 *            开始计息时间
	 * @param interestType
	 *            计息方式（按天、按月）
	 * @param interestBeginPoint
	 *            计息节点（即投即生息，放款后生息 之类）
	 * @param customPeriods
	 *            完全自定义还款总期数、每期还款时间、每期还款本金所占全部本金的百分比
	 * @return 还款数据
	 */
	public List<Repay> generateRepays(double investMoney, Date investTime,
			double rate, Integer deadline, String repayTimeUnit,
			Integer repayTimePeriod, Date interestBeginTime,
			String interestType, String interestBeginPoint,
			List<RepayCustomPeriod> customPeriods);

	// ////////////////////////正常还款--结束////////////////////////////////////////////////

}
