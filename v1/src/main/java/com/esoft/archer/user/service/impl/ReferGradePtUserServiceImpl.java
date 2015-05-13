package com.esoft.archer.user.service.impl;


import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.ReferGradePtUserService;
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
	public User getUserById(String userId) throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		return user;
	}
}
