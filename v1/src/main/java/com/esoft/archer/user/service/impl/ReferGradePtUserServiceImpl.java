package com.esoft.archer.user.service.impl;


import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.ReferGradePtUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("referGradePtUserService")
public class ReferGradePtUserServiceImpl implements ReferGradePtUserService {
	@Resource
	private HibernateTemplate ht;

	/**
	 * 根据用户id查找用户
	 */
	@Override
	public boolean isExistUser(String userId)  {
		boolean isExistUserFlag = false;
		User user = ht.get(User.class, userId);
		if (user != null) {
			isExistUserFlag = true;
		}
		return isExistUserFlag;
	}

	@Override
	public boolean isExistReferrerGrade(String referrer, Integer grade) {
		boolean isExistReferrerGrade = false;
		String hql  = " select count(referGradeProfitUser) from ReferGradeProfitUser referGradeProfitUser where referGradeProfitUser.referrer.id = ? and referGradeProfitUser.grade = ? ";
		Object oj = ht.find(hql, referrer, grade).get(0);

		Long countResult = 0L;
		if (oj != null){
			countResult = (Long)oj;
		}

		if (countResult > 0L){
			isExistReferrerGrade = true;
		}

		return isExistReferrerGrade;
	}
}
