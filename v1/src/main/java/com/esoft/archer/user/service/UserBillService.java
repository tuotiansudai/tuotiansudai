package com.esoft.archer.user.service;

import com.esoft.jdp2p.loan.exception.InsufficientBalance;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:用户账户service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wangzhi 1.0
 */
public interface UserBillService {

	/**
	 * 转入到余额，例如借款成功时候。
	 * 
	 * @param userId 用户id
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * 
	 */
	public void transferIntoBalance(String userId, double money,
			String operatorInfo, String operatorDetail);

	/**
	 * 从余额中转出
	 * 
	 * @param userId 用户id
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * @throws InsufficientBalance 余额不足
	 */
	public void transferOutFromBalance(String userId, double money,
			String operatorInfo, String operatorDetail) throws InsufficientBalance;
	
	/**
	 * 从冻结金额中转出
	 * 
	 * @param userId 用户id
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * @throws InsufficientBalance 余额不足
	 */
	public void transferOutFromFrozen(String userId, double money,
			String operatorInfo, String operatorDetail) throws InsufficientBalance;

	/**
	 * 冻结金额.
	 * 
	 * @param userId
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * @throws InsufficientBalance 余额不足
	 */
	public void freezeMoney(String userId, double money,
			String operatorInfo, String operatorDetail) throws InsufficientBalance;

	/**
	 * 解冻金额.
	 * 
	 * @param userId 用户id
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * 
	 * @throws InsufficientBalance 余额不足
	 */
	public void unfreezeMoney(String userId, double money,
			String operatorInfo, String operatorDetail) throws InsufficientBalance;

	/**
	 * 获取用户账户余额
	 * @param userId 用户id
	 * @return 余额
	 */
	public double getBalance(String userId);
	
	/**
	 * 获取用户账户冻结金额
	 * @param userId 用户id
	 * @return 余额
	 */
	public double getFrozenMoney(String userId);
}
