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
	/**
	 *	获取新增系统最高层级
	 */
	@Override
	public Integer getAddHighestGrade(){

		Integer addHighestGrade = null;

		String hql  = " select count(referGradeProfitSys) from ReferGradeProfitSys referGradeProfitSys  ";

		addHighestGrade = ((Long)ht.find(hql).get(0)).intValue() + 1;


		return addHighestGrade;

	}
	/**
	 *	获取系统已经存在的最高层级
	 */
	@Override
	public Integer getMaxGrade(){
		Integer maxGrade = null;

		String hql  = " select max(referGradeProfitSys.grade) from ReferGradeProfitSys referGradeProfitSys  ";

		maxGrade = (Integer)ht.find(hql).get(0) ;

		return maxGrade;
	};



}