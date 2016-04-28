package com.esoft.jdp2p.repay.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.exception.OverdueRepayException;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.service.RepayService;

/**
 * 还款
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class RepayHome extends EntityHome<Repay> {

	@Resource
	RepayService repayService;

	@Resource
	UserBillBO userBillBO;

	/**
	 * 正常还款
	 * 
	 * @return
	 */
	public void normalRepay(String repayId) {
		try {
			repayService.normalRepay(repayId);
			FacesUtil.addInfoMessage("还款成功！");
		} catch (InsufficientBalance e) {
			// 余额不足
			FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
		} catch (NormalRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * 提前还款
	 */
	public void advanceRepay(String loanId) {
		try {
			repayService.advanceRepay(loanId);
			FacesUtil.addInfoMessage("提前还款成功！");
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("余额不足！");
		} catch (AdvancedRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * 逾期还款
	 */
	public void overdueRepay(String repayId) {
		try {
			repayService.overdueRepay(repayId);
			FacesUtil.addInfoMessage("逾期还款成功！");
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("余额不足！");
		} catch (OverdueRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

}
