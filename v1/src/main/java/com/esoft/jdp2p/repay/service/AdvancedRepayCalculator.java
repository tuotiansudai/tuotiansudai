package com.esoft.jdp2p.repay.service;

import java.util.Date;
import java.util.List;

import com.esoft.jdp2p.repay.model.Repay;

/**
 * Filename: RepayManage.java Description: 提前还款数据 计算接口 Company: jdp2p
 * 
 * @author: wangzhi
 * @version: 1.0
 * @since 2.0
 */
public interface AdvancedRepayCalculator {

	// //////////////////////提前还款--开始////////////////////////////////////////////////
	/**
	 * 计算提前还款所需还的本金
	 * 
	 * @param loanId
	 *            提前还款的借款
	 * @return
	 */
	public Double getAdvanceRepayCorpus(String loanId);

	/**
	 * 计算提前还款的手续费
	 * 
	 * @param loanId
	 *            提前还款的借款
	 * @return
	 */
	public Double calculateAdvanceRepayFee(String loanId);

	// /////////////////////////提前还款--结束/////////////////////////////////////////////

}
