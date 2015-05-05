package com.esoft.umpay.recharge.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.archer.user.controller.RechargeHome;
import com.esoft.umpay.recharge.service.impl.UmPayRechargeOteration;

public class UmpayRechargeHome extends RechargeHome {

	@Resource
	UmPayRechargeOteration umPayRechargeOteration;

	// 商户充值
	public void adminRecharge() {

	}

	/**
	 * 个人充值
	 */
	public void recharge() {
		try {
			umPayRechargeOteration.createOperation(this.getInstance(),
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
