package com.esoft.umpay.recharge.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.archer.user.controller.RechargeHome;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.umpay.recharge.service.impl.UmPayRechargeOteration;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class UmpayRechargeHome extends RechargeHome {

	@Resource
	UmPayRechargeOteration umPayRechargeOteration;

	@Resource
	HibernateTemplate ht;

	private boolean isOpenFastPayment;

	public boolean getIsOpenFastPayment() {
		return isOpenFastPayment;
	}

	public void setIsOpenFastPayment(boolean isOpenFastPayment) {
		this.isOpenFastPayment = isOpenFastPayment;
	}

	// 商户充值
	public void adminRecharge() {

	}

	/**
	 * 个人充值
	 */
	public void recharge() {
		try {
			umPayRechargeOteration.createOperation(this.getInstance(),
					FacesContext.getCurrentInstance(),isOpenFastPayment);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean  isOpenedQuickPay(String userId){

		String hql = "from BankCard where user.id =? and isOpenFastPayment =? and status=?";

		List<BankCard> bankCard= ht.find(hql, new Object[]{userId, true,"passed"});

		return bankCard != null&& bankCard.size() > 0;


	}


}
