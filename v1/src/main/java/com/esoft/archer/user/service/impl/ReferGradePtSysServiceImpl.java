package com.esoft.archer.user.service.impl;


import com.esoft.archer.user.exception.UserFoundException;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.service.ReferGradePtSysService;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("referGradePtSysServiceImpl")
public class ReferGradePtSysServiceImpl implements ReferGradePtSysService {
	@Resource
	private HibernateTemplate ht;

	/**
	 * 根据层级查找
	 */
	@Override
	public void getByGrade(Integer grade) throws UserFoundException {
		List<ReferGradeProfitSys>  users = ht.find(" from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.grade=? ",grade);
		if (users !=null && users.size() > 0) {
			throw new UserFoundException("grade:" + grade);
		}
	}
}
