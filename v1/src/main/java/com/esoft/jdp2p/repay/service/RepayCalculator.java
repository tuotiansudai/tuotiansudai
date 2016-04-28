package com.esoft.jdp2p.repay.service;

import com.esoft.jdp2p.repay.model.AdvanceRepay;

public interface RepayCalculator {
	/**
	 * 计算预计利息
	 * 
	 * @param investMoney
	 *            投资金额
	 * @param loanId
	 *            借款编号
	 * @return
	 */
	public Double calculateAnticipatedInterest(double money, String loanId);

	/**
	 * 计算逾期还款的费用（逾期利息+罚息）
	 * 
	 * @param loanId
	 *            借款交易id
	 * @return 费用金额
	 */
	public double calculateOverdueRepayFee(String loanId);

	/**
	 * 计算提前还款
	 */
	public AdvanceRepay calculateAdvanceRepay(String loanId);

}
