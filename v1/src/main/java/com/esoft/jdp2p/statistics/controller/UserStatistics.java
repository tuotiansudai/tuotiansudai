package com.esoft.jdp2p.statistics.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.DateUtil;

/**
 * 会员统计
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class UserStatistics {
	@Resource
	HibernateTemplate ht;
	/**
	 * 平台总用户数
	 * @return
	 */
	public long getUserCount(){
		String hql = "select count(user) from User user";
		List<Object> oos = ht.find(hql);
		Object o = oos.get(0);
		if (o == null) {
			return 0L;
		}
		return (Long) o;
	}
	
	/**
	 * 获取今日注册会员数
	 * @return
	 */
	public long getTodayRegisterUserCount(){
		String hql = "select count(user) from User user where user.registerTime >= ?";
		List<Object> oos = ht.find(hql, DateUtil.getZero(new Date()));
		Object o = oos.get(0);
		if (o == null) {
			return 0L;
		}
		return (Long) o;
	}
}
