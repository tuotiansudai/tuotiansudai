package com.esoft.archer.user.service;

import com.esoft.archer.user.model.UserPoint;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:用户等级service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wangzhi 1.0
 */
public interface UserLevelService {

	/**
	 * 改变等级
	 * 
	 * @param userId
	 *            用户id
	 * @param levelId
	 *            等级id
	 * @param validityPeriod
	 *            有效期（秒）
	 * @param description
	 *            描述
	 * 
	 */
	public void change(String userId, String levelId, int validityPeriod,
			String description);

	/**
	 * 刷新用户等级，主要用户用户升级积分发生变化后
	 * 
	 * @param userId
	 *            被刷新的用户编号
	 * 
	 */
	public void refreshUserLevel(String userId);

}
