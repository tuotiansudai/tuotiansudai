package com.esoft.jdp2p.user.service;

import com.esoft.archer.user.model.UserBill;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;

import java.util.List;

/**
 * Filename:AtmFeesService.java Description:提现接口 Copyright: Copyright(c)2013
 * Company:jdp2p
 * 
 * @author:gongph version:1.0 Create at: 2014-1-4 下午04:33:57
 */
public interface WithdrawCashService {

	/**
	 * 计算提现费用
	 * 
	 * @param amount
	 *            提现金额
	 * @return double 提现费用
	 */
	public double calculateFee(double amount);

	/**
	 * 通过提现申请
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void passWithdrawCashApply(WithdrawCash withdrawCash);
	
	/**
	 * 通过提现复核
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void passWithdrawCashRecheck(WithdrawCash withdrawCash);

	/**
	 * 拒绝提现申请
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void refuseWithdrawCashApply(WithdrawCash withdrawCash);

	/**
	 * 申请提现
	 * 
	 * @param withdrawCash
	 * @throws InsufficientBalance 
	 */
	public void applyWithdrawCash(WithdrawCash withdraw) throws InsufficientBalance;
	
	/**
	 * 管理员提现
	 * @throws InsufficientBalance 
	 */
	public void withdrawByAdmin(UserBill ub) throws InsufficientBalance;
	/**
	 * 生成ID
	 */
	public String generateId();

	/**
	 * 查询用户的提现记录
	 * @param userId
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<WithdrawCash> queryUserWithdrawLogs(String userId, int offset, int limit);

	/**
	 * 查询用户的提现记录数
	 * @param userId
	 * @return
	 */
	public int queryUserWithdrawLogsCount(String userId);

}
