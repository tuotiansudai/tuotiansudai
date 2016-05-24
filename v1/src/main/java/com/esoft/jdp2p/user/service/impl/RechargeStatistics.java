package com.esoft.jdp2p.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.user.UserConstants.RechargeStatus;

@Service
public class RechargeStatistics {

	@Resource
	HibernateTemplate ht;

	/**
	 * 累计充值
	 */
	public double getPaidRechargeMoney(String userId) {
		String hql = "select sum(recharge.actualMoney) from Recharge recharge "
				+ "where recharge.status =? and recharge.user.id=?";
		List<Object> oos = ht.find(hql, new String[] { RechargeStatus.SUCCESS,
				userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}
}
