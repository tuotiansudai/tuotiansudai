package com.esoft.jdp2p.repay.service;

import java.util.Date;
import java.util.List;

import com.esoft.jdp2p.repay.model.Repay;

/**
 * Filename: RepayManage.java Description: 逾期还款数据 计算接口 Company: jdp2p
 * 
 * @author: wangzhi
 * @version: 1.0
 * @since 2.0
 */
public interface OverdueRepayCalculator {

	// ///////////////////////逾期--开始//////////////////////////////////////////////////
	/**
	 * 计算逾期还款的费用（逾期利息+罚息）
	 * 
	 * @param loanId
	 *            借款交易id
	 * @return 费用金额
	 */
	public double calculateOverdueRepayFee(String loanId);

	/**
	 * 计算某期逾期借款需要支付的逾期利息+逾期罚金
	 * 
	 * @param repayment
	 * @return
	 */
	public double calculateOverdueSumByPeriod(String repayId);

	/**
	 * 获取逾期还款，需支付的因投资产生的逾期时期的利息（除掉网站罚息的那部分）
	 * 
	 * @param money
	 *            逾期金额
	 * @param rate
	 *            利率
	 * @param day
	 *            逾期时间
	 * @return
	 */
	public double calculateOverdueRatePay(double money, double rate, int day);

	/**
	 * 获取逾期还款，需支付的罚息
	 * 
	 * @param money
	 *            逾期金额
	 * @param rate
	 *            利率
	 * @param day
	 *            逾期时间
	 * @return
	 */
	public double calculateOverduePenalty(double money, double rate, int day);

	// ///////////////////////逾期--结束//////////////////////////////////////////////////

}
