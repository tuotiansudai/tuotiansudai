package com.esoft.archer.user.service;

import com.esoft.archer.user.exception.UserFoundException;
import com.esoft.archer.user.model.User;

public interface ReferGradePtSysService {


	/**
	 * 根据推荐层级查找
	 *
	 * @param grade
	 *            层级
	 * @return
	 * @throws UserFoundException
	 */
	public void getByGrade(Integer grade) throws UserFoundException;
}
