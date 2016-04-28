package com.esoft.jdp2p.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.risk.service.SystemBillService;

@Service
public class RechargeBO {
	
	@Resource
	HibernateTemplate ht;

	@Resource
	private UserBillService ibs;

	@Resource
	private SystemBillService sbs;
	
	/**
	 * 充值支付成功
	 * @param recharge
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void rechargeSuccess(Recharge recharge) {
		recharge.setStatus(UserConstants.RechargeStatus.SUCCESS);
		recharge.setSuccessTime(new Date());
		ht.merge(recharge);
		// 往InvestorBill中插入值并计算余额
		ibs.transferIntoBalance(recharge.getUser().getId(),
				recharge.getActualMoney(), OperatorInfo.RECHARGE_SUCCESS,
				"充值编号：" + recharge.getId());
		sbs.transferInto(
				recharge.getFee(),
				OperatorInfo.RECHARGE_SUCCESS,
				"充值手续费, 用户ID：" + recharge.getUser().getId() + "充值ID"
						+ recharge.getId());
	}
}
