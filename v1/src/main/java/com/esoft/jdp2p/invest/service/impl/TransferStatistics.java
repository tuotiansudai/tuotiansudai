package com.esoft.jdp2p.invest.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

/**
 * 债权转让统计
 * 
 * @author Administrator
 * 
 */
@Component
public class TransferStatistics {

	@Resource
	HibernateTemplate ht;

	/**
	 * 根据类型计算本金总额
	 * 
	 * @param type
	 * @return
	 */
	public double getAllMoneyByType(String type) {
		String hql = "select sum(ta.corpus) from TransferApply ta "
				+ "where ta.status =?";
		List<Object> oos = ht.find(hql, type);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * 根据类型计算数量
	 * 
	 * @param type
	 * @return
	 */
	public long getCountByType(String type) {
		String hql = "select count(ta) from TransferApply ta "
				+ "where ta.status =?";
		List<Object> oos = ht.find(hql, type);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}
	
	/**
	 * 计算已转出的本金
	 * 
	 * @return
	 */
	public double getAllMoneyByOut() {
		String hql = "select sum(i.investMoney) from Invest i "
				+ "where i.transferApply is not null";
		List<Object> oos = ht.find(hql);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * 计算转出的数量
	 * 
	 * @return
	 */
	public long getCountOut() {
		String hql = "select count(i) from Invest i "
				+ "where i.transferApply is not null";
		List<Object> oos = ht.find(hql);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}


	/**
	 * 根据类型和转出用户计算本金总额
	 * 
	 * @return
	 */
	public double getAllMoneyByTypeUser(String type, String userId) {
		String hql = "select sum(ta.corpus) from TransferApply ta "
				+ "where ta.status =? and ta.invest.user.id =?";
		List<Object> oos = ht.find(hql, new String[] { type, userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * 根据类型和转出用户计算数量
	 * 
	 * @return
	 */
	public long getCountByTypeUser(String type, String userId) {
		String hql = "select count(ta) from TransferApply ta "
				+ "where ta.status =? and ta.invest.user.id =?";
		List<Object> oos = ht.find(hql, new String[] { type, userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}

	/**
	 * 计算某用户已转出的本金
	 * 
	 * @return
	 */
	public double getAllMoneyByUserOut(String userId) {
		String hql = "select sum(i.investMoney) from Invest i "
				+ "where i.transferApply.invest.user.id =?";
		List<Object> oos = ht.find(hql, userId);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * 计算某用户转出的数量
	 * 
	 * @return
	 */
	public long getCountByUserOut(String userId) {
		String hql = "select count(i) from Invest i "
				+ "where i.transferApply.invest.user.id =?";
		List<Object> oos = ht.find(hql, userId);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}

	/**
	 * 计算某用户转入本金
	 * 
	 * @return
	 */
	public double getAllMoneyByUserIn(String userId) {
		String hql = "select sum(i.investMoney) from Invest i "
				+ "where i.user.id=? and i.transferApply is not null";
		List<Object> oos = ht.find(hql, userId);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * 计算某用户转入的数量
	 * 
	 * @return
	 */
	public long getCountByUserIn(String userId) {
		String hql = "select count(i) from Invest i "
				+ "where i.user.id=? and i.transferApply is not null";
		List<Object> oos = ht.find(hql, userId);
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}

}
