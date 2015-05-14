package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.service.ReferGradePtSysService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("referGradePtSysServiceImpl")
public class ReferGradePtSysServiceImpl implements ReferGradePtSysService {
	@Resource
	private HibernateTemplate ht;

	/**
	 * 根据层级查找
	 */
	@Override
	public boolean isExistGrade(Integer grade)  {
		boolean isExistGradeFlag = false;
		List<ReferGradeProfitSys>  users = ht.find(" from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.grade=? ",grade);
		if(CollectionUtils.isNotEmpty(users)){
			isExistGradeFlag = true;
		}
		return  isExistGradeFlag;
	}
}