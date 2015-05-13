package com.esoft.archer.user.service;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;

public interface ReferGradePtUserService {


	/**
	 * 根据用户编号查找用户
	 *
	 * @param userId
	 *            用户编号
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUserById(String userId) throws UserNotFoundException;
}
