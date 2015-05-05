package com.esoft.archer.user.service;

import com.esoft.archer.user.exception.MinPointLimitCannotMattchSeqNumException;
import com.esoft.archer.user.exception.SeqNumAlreadyExistException;
import com.esoft.archer.user.exception.UserExistInLevelException;
import com.esoft.archer.user.model.LevelForUser;

/**
 * Description: 等级service<br/>
 * Copyright: Copyright (c)2013<br/>
 * Company:jdp2p<br/>
 * 
 * @author gongph
 * @version: 1.0 Create at: 2014-06-20 下午5:36:30
 * 
 * 
 */
public interface LevelService {

	/**
	 * 新增或修改等级
	 * 
	 * @param levelForUser
	 *            等级对象
	 * @throws SeqNumAlreadyExistException
	 *             等级序号已存在
	 * @throws MinPointLimitCannotMattchSeqNumException
	 *             等级积分下限的顺序，与等级序号的顺序，不相匹配
	 */
	public void saveOrUpdate(LevelForUser levelForUser)
			throws SeqNumAlreadyExistException,
			MinPointLimitCannotMattchSeqNumException;

	/**
	 * 删除
	 * 
	 * @param levelForUserId
	 * @throws UserExistInLevelException
	 *             有用户处于该等级，不能删除
	 */
	public void delete(String levelForUserId) throws UserExistInLevelException;
}
