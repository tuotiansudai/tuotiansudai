package com.esoft.archer.user.service;

import com.esoft.archer.user.model.UserPoint;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:用户积分service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wangzhi 1.0
 */
public interface UserPointService {

	/**
	 * 增加积分
	 * 
	 * @param userId
	 *            用户id
	 * @param point
	 *            金额
	 * @param type
	 *            积分类型
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * 
	 */
	public void add(String userId, int point, String type,
			String operatorInfo, String operatorDetail);

	/**
	 * 减少积分
	 * 
	 * @param userId
	 *            用户id
	 * @param point
	 *            金额
	 * @param type
	 *            积分类型
	 * @param operatorInfo
	 *            操作信息
	 * @param operatorDetail
	 *            操作详情
	 * @throws InsufficientBalance
	 *             积分不足
	 */
	public void minus(String userId, int point, String type,
			String operatorInfo, String operatorDetail)
			throws InsufficientBalance;

	/**
	 * 获取用户积分对象
	 * 
	 * @param userId
	 *            用户id
	 * @param type
	 *            积分类型
	 */
	public UserPoint getPointByUserId(String userId, String type);
	
	/**
	 * 获取用户积分数值
	 * 
	 * @param userId
	 *            用户id
	 * @param type
	 *            积分类型
	 */
	public int getPointsByUserId(String userId, String type);

}
