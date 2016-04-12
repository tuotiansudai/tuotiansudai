package com.esoft.jdp2p.repay.service;

import java.util.Date;

import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.exception.OverdueRepayException;
import com.esoft.jdp2p.repay.model.LoanRepay;

/**
 * Filename: RepayManage.java Description: 还款管理接口 Copyright: Copyright (c)2013
 * Company: jdp2p
 * 
 * @author: bizhibo
 * @version: 1.0 Create at: 2014-1-4 上午11:42:20
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 bizhibo 1.0 1.0 Version
 */
public interface RepayService {

	/**
	 * 通过借款交易的id生成还款信息
	 * 
	 * @param loanId
	 *            借款交易的id
	 */
	public void generateRepay(String loanId);

	/**
	 * 正常还款
	 * 
	 * @param repayId
	 *            还款编号
	 * @throws InsufficientBalance
	 * @throws NormalRepayException
	 */
	public void normalRepay(String repayId) throws InsufficientBalance,
			NormalRepayException;

	/**
	 * 正常还款
	 * 
	 * @param loanRepay
	 *            还款
	 * @throws InsufficientBalance
	 * @throws NormalRepayException
	 */
	public void normalRepay(LoanRepay loanRepay) throws InsufficientBalance,
			NormalRepayException;

	/**
	 * 提前还款
	 * 
	 * @param loanId
	 *            借款id
	 * @param amount
	 *            提前还款金额
	 * @throws AdvancedRepayException
	 *             不符合提前还款条件
	 * 
	 * @throws InsufficientBalance
	 *             余额不足
	 * 
	 */
	public void advanceRepay(String loanId) throws InsufficientBalance,
			AdvancedRepayException;

	/**
	 * 逾期还款
	 * 
	 * @param repayId
	 *            还款id
	 * @throws InsufficientBalance
	 *             余额不足
	 * @throws OverdueRepayException
	 */
	public void overdueRepay(String repayId) throws InsufficientBalance,
			OverdueRepayException;

	/**
	 * 管理员进行逾期还款
	 * 
	 * @param repayId
	 *            还款id
	 * @param adminUserId
	 *            管理员用户id
	 */
	public void overdueRepayByAdmin(String repayId, String adminUserId);

	/**
	 * 还款到期，自动扣款，否则项目状态为逾期，还款和借款也变为逾期，锁定用户账号；如果还款已经逾期了，那么每天都计算该还款的逾期费用，
	 * 加入到还款的本金和利息中。逾期超过一年，则变为坏账。
	 */
	public void autoRepay();

	/**
	 * 判断是否在还款期
	 * 
	 * @param repayDate
	 *            还款日
	 */
	public boolean isInRepayPeriod(Date repayDate);

	/**
	 * 还款提醒，n天以内到还款日的还款，或者逾期的。给还款人发短信
	 */
	public void repayAlert();

	/**
	 * 检查所有项目，是否有逾期，如果逾期，则做相应处理
	 */
	public void checkLoanOverdue();
}
