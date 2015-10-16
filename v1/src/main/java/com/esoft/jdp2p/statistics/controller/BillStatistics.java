package com.esoft.jdp2p.statistics.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.annotations.ScopeType;

/**
 * 账户（单）统计
 * 
 * @author Administrator
 * 
 */
@Component
public class BillStatistics {

	@Resource
	private UserBillService ubs;

	/**
	 * 获取用户账户余额
	 * 
	 * @return
	 */
	public double getBalanceByUserId(String userId) {
		return ubs.getBalance(userId);
	}

	/**
	 * 获取用户账户冻结金额
	 * 
	 * @param userId
	 * @return
	 */
	public double getFrozenMoneyByUserId(String userId) {
		return ubs.getFrozenMoney(userId);
	}

}
