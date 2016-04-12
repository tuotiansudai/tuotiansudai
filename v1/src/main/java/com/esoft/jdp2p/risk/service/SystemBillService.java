package com.esoft.jdp2p.risk.service;

import com.esoft.jdp2p.loan.exception.InsufficientBalance;

/**
 * 系统收益账户service
 * @author Administrator
 *
 */
public interface SystemBillService {

	/**
	 * 获取最新一条数据
	 * @return
	 */
//	public SystemBill getLastestBill();
	
	/**
	 * 获取账户余额
	 * @return
	 */
	public double getBalance();

	/**
	 * 转出
	 * @param money 金额
	 * @param operatorType 操作类型
	 * @param operatorDetail 操作详情
	 * 
	 * @throws InsufficientBalance 余额不足
	 */
	public void transferOut(double money, String reason, String detail) throws InsufficientBalance;
	
	/**
	 * 转入.
	 * @param money 金额
	 * @param operatorType 操作类型
	 * @param operatorDetail 操作详情
	 */
	public void transferInto(double money, String reason,
			String detail);
	
}
