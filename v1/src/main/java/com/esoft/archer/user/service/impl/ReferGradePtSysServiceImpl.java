package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.service.ReferGradePtSysService;
import com.esoft.archer.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("referGradePtSysServiceImpl")
public class ReferGradePtSysServiceImpl implements ReferGradePtSysService {
	public static final String INVESTOR = "INVESTOR";
	public static final String ROLE_MERCHANDISER = "ROLE_MERCHANDISER";
	@Resource
	private HibernateTemplate ht;
	@Resource
	private UserService userService;



	/**
	 * 根据层级查找,判断新增用户层级是否存在
	 */
	@Override
	public boolean isExistInvestGrade(Integer grade)  {
		boolean isExistGradeFlag = false;
		List<ReferGradeProfitSys>  users = ht.find(" from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.grade=? and referGradeProfitSys.gradeRole='INVESTOR' ",grade);
		if(CollectionUtils.isNotEmpty(users)){
			isExistGradeFlag = true;
		}
		return  isExistGradeFlag;
	}
	/**
	 * 根据层级查找,判断新增业务员层级是否存在
	 */
	@Override
	public boolean isExistMerchandiserGrade(Integer grade)  {
		boolean isExistGradeFlag = false;
		List<ReferGradeProfitSys>  users = ht.find(" from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.grade=? and referGradeProfitSys.gradeRole='ROLE_MERCHANDISER' ",grade);
		if(CollectionUtils.isNotEmpty(users)){
			isExistGradeFlag = true;
		}
		return  isExistGradeFlag;
	}



	/**
	 *	获取新业务员的系统最高层级
	 */
	@Override
	public Integer getAddHighestMerchandiserGrade(){

		return getMerchandiserMaxGrade() + 1;

	}

	/**
	 *	获取新增用户的系统最高层级
	 */
	@Override
	public Integer getAddHighestInvestGrade(){

		return getInvestMaxGrade() + 1;

	}

	/**
	 *	根据用户角色获取系统对应已经存在的最高层级
	 *	如果推荐人角色为投资人，那么指定推荐人的最高层级不能超过系统配置的投资人角色最高层级
	 *	如果推荐人角色为业务员，那么指定推荐人的最高层级不能超过系统配置的业务员角色最高层级
	 *  如果推荐人是投资人和业务员,按照角色为业务员查找角色最高层级
	 */
	@Override
	public Integer getMaxGradeByRole(String referrerId){
		Integer maxGrade = null;
		boolean hasMerchandiser = userService.hasRole(referrerId, ROLE_MERCHANDISER);//是否业务员
		boolean hasInvest = false;//是否投资人

		if (hasMerchandiser){
			maxGrade = this.getMerchandiserMaxGrade();
		}else{
			hasInvest = userService.hasRole(referrerId, INVESTOR);
			if (hasInvest){
				maxGrade = this.getInvestMaxGrade();
			}
		}

		return maxGrade;
	};
	/**
	 *	获取系统已经存在的用户最高层级
	 */
	@Override
	public Integer getInvestMaxGrade(){
		Integer maxGrade = null;

		String hql  = " select max(referGradeProfitSys.grade) from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.gradeRole='INVESTOR' ";

		maxGrade = (Integer)ht.find(hql).get(0) ;

		return maxGrade;
	};
	/**
	 *	获取系统已经存在的业务员最高层级
	 */
	@Override
	public Integer getMerchandiserMaxGrade(){
		Integer maxGrade = null;

		String hql  = " select max(referGradeProfitSys.grade) from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.gradeRole='ROLE_MERCHANDISER' ";

		maxGrade = (Integer)ht.find(hql).get(0) ;

		return maxGrade;
	}



}