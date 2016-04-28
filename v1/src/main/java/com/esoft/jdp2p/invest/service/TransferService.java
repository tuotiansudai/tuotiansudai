package com.esoft.jdp2p.invest.service;

import org.hibernate.ObjectNotFoundException;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.jdp2p.invest.exception.ExceedInvestTransferMoney;
import com.esoft.jdp2p.invest.exception.InvestTransferException;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

/**
 * Filename: InvestService.java <br/>
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:债权转让service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wangzhi 1.0
 */
public interface TransferService {
	/**
	 * 申请债权转让
	 * 
	 * @param investId
	 *            被转让的投资
	 * @param money
	 *            转让的本金
	 * @param transferMoney
	 *            债权转让价格
	 */
	public void applyInvestTransfer(TransferApply ta)throws ExceedInvestTransferMoney;

	/**
	 * 债权转让成功
	 * 
	 * @param investTransferId
	 *            被转让的债权
	 * @param userId
	 *            债权购买者
	 * @param transferMoney
	 *            转让价格
	 * @throws InsufficientBalance 余额不足
	 */
	public void transfer(String transferApplyId, String userId, double transferMoney) throws InsufficientBalance, ExceedInvestTransferMoney;


	/**
	 * 计算债权转让完成百分比
	 * @author wangxiao 3-27
	 * @param loanId
	 * @return
	 * @throws NoMatchingObjectsException 
	 */
	public double calculateInvestTransferCompletedRate(String transferApplyId) throws NoMatchingObjectsException;

	/**
	 * 计算未转出的本金
	 * @author wx 4-9
	 * @param investTransferId
	 * @return
	 */
	public double calculateRemainCorpus(String transferApplyId);
	/**
	 * 判断当前投资是否可转让
	 * @author wangxiao
	 * @param investId
	 * @return
	 * @throws ObjectNotFoundException
	 */
	public boolean canTransfer(String investId) throws InvestTransferException;
	
	/**
	 * 计算投资中某本金的当前价格
	 * @param investId
	 * @param corpus
	 * @return
	 */
	public double calculateWorth(String investId, double corpus);
	
	/**
	 * 债权转让到期
	 * 
	 * @param investTransferId
	 *            债权转让id
	 */
	public void dealOverExpectTime(String investTransferId);
	
	
	/**
	 * 取消债权转让
	 * 
	 * @param investTransferId
	 *            债权转让id
	 */
	public void cancel(String investTransferId);
	
	/**
	 * 计算债权转让剩余时间
	 * @param transferApplyId
	 * @return
	 * @throws NoMatchingObjectsException 
	 */
	public String calculateRemainTime(String transferApplyId) throws NoMatchingObjectsException;
	
	/**
	 * 计算债权转让手续费
	 * @param ta 债权转让对象
	 * @return
	 * @throws NoMatchingObjectsException 
	 */
	public double calculateFee(TransferApply ta);
}
