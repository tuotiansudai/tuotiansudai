package com.esoft.archer.pay.service;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esoft.jdp2p.loan.model.Recharge;

public interface PayService {
	//FIXME:最好能抽取一个pay方法，降低耦合
	
	/**
	 * 充值
	 * @param fc
	 * @param recharge
	 * @param bankCardNo
	 */
	public void recharge(FacesContext fc, Recharge recharge, String bankCardNo);
	
	public void receiveReturnWeb(HttpServletRequest request);
	public void receiveReturnS2S(HttpServletRequest request, HttpServletResponse response);

}
