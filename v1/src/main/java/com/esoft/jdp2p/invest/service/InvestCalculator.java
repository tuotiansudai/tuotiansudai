package com.esoft.jdp2p.invest.service;

public interface InvestCalculator {
	/**
	 * 计算投资的预计收益
	 * 
	 * @param investMoney
	 *            投资金额
	 * @param loanId
	 *            借款编号
	 * @return
	 */
	public Double calculateAnticipatedInterest(double investMoney, String loanId);

}
