package com.esoft.jdp2p.risk.service;

import com.esoft.jdp2p.risk.model.RiskReserve;

/**
 * 风险准备金service
 * @author Administrator
 *
 */
public interface RiskReserveService {

	/**
	 * 获取某一操作用户的最新一条数据
	 * @return
	 */
	public RiskReserve getLastestBillByUser(String userId);
	
	/**
	 * 获取风险准备金的最新一条数据
	 * @return
	 */
	public RiskReserve getLastestBill();
	
	/**
	 * 获取账户余额
	 * @return
	 */
	public double getBalance();

	
	/**
	 * 转出
	 * @param userId 
	 * @param money 金额
	 * @param operatorType 操作类型
	 * @param operatorDetail 操作详情
	 * 
	 * @return true 成功
	 * @return false 失败
	 */
	public boolean takeOut(String userId, double money, String operatorType, String operatorDetail);
	
	
	/**
	 * 转入
	 * @param userId 
	 * @param money 金额
	 * @param operatorType 操作类型
	 * @param operatorDetail 操作详情
	 * 
	 * @return true 成功
	 * @return false 失败
	 */
	public boolean transferInto(String userId, double money, String operatorType, String operatorDetail);
	
	
	
}
