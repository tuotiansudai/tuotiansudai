package com.esoft.jdp2p.borrower.service;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.jdp2p.borrower.model.BorrowerAdditionalInfo;
import com.esoft.jdp2p.borrower.model.BorrowerAuthentication;
import com.esoft.jdp2p.borrower.model.BorrowerPersonalInfo;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-14 下午3:30:28  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-14      wangzhi      1.0          
 */
public interface BorrowerService {

	/**
	 * 初始化BorrowerPersonalInfo，借款人普通信息
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public abstract BorrowerPersonalInfo initBorrowerPersonalInfo(String userId)
			throws UserNotFoundException;

	/**
	 * 保存BorrowerPersonalInfo，借款人普通信息
	 * @param bpi
	 */
	public abstract void saveOrUpdateBorrowerPersonalInfo(
			BorrowerPersonalInfo bpi);

	/**
	 * 审核 BorrowerPersonalInfo，借款人普通信息
	 * @param bpiId 普通信息编号
	 * @param isPassed 是否通过
	 * @param msg 审核信息
	 * @param verifiedUserId 审核用户编号
	 */
	public abstract void verifyBorrowerPersonalInfo(String bpiId,
			boolean isPassed, String msg, String verifiedUserId);

	/**
	 * 初始化BorrowerAdditionalInfo，借款人工作财务信息
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public abstract BorrowerAdditionalInfo initBorrowerAdditionalInfo(
			String userId) throws UserNotFoundException;

	/**
	 * 保存BorrowerAdditionalInfo，借款人工作财务信息
	 * @param bpi
	 */
	public abstract void saveOrUpdateBorrowerAdditionalInfo(
			BorrowerAdditionalInfo bai);

	/**
	 * 审核 BorrowerAdditionalInfo，借款人工作财务信息
	 * @param bpiId 工作财务信息编号
	 * @param isPassed 是否通过
	 * @param msg 审核信息
	 * @param verifiedUserId 审核用户编号
	 */
	public abstract void verifyBorrowerAdditionalInfo(String baiId,
			boolean isPassed, String msg, String verifiedUserId);

	/**
	 * 初始化BorrowerAuthentication，借款人材料信息
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public abstract BorrowerAuthentication initBorrowerAuthentication(
			String userId) throws UserNotFoundException;

	/**
	 * 保存BorrowerAuthentication，借款人材料信息
	 * @param bp 
	 */
	public abstract void saveOrUpdateBorrowerAuthentication(
			BorrowerAuthentication ba);

	/**
	 * 审核 BorrowerAuthentication，借款人材料信息
	 * @param bpId 材料信息编号
	 * @param isPassed 是否通过
	 * @param msg 审核信息
	 * @param verifiedUserId 审核用户编号
	 */
	public abstract void verifyBorrowerAuthentication(String baId,
			boolean isPassed, String msg, String verifiedUserId);

}