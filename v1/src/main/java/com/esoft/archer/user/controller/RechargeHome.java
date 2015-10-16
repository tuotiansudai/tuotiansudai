package com.esoft.archer.user.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.pay.service.PayService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.user.service.RechargeService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class RechargeHome extends EntityHome<Recharge> {

	@Logger
	static Log log;
	@Resource
	HibernateTemplate ht;
	@Resource
	private LoginUserInfo loginUserInfo;

	@Resource
	private RechargeService rechargeService;

	@Override
	protected Recharge createInstance() {
		Recharge recharge = new Recharge();
		recharge.setFee(0D);
		recharge.setUser(new User(loginUserInfo.getLoginUserId()));
		return recharge;
	}

	public void calculateFee() {
		double fee = rechargeService.calculateFee(this.getInstance()
				.getActualMoney());
		this.getInstance().setFee(fee);
	}
	public BigDecimal formatActualMoney(Double actualMoney,Double fee){
		actualMoney = actualMoney == null?0.00D:actualMoney;
		fee = fee == null?0.00D:fee;
		BigDecimal actualMoneyTemp = new BigDecimal(actualMoney);
		BigDecimal feeTemp = new BigDecimal(fee);
		return  actualMoneyTemp.add(feeTemp);
	}

	public String offlineRecharge() {
		rechargeService.createOfflineRechargeOrder(getInstance());
		FacesUtil.addInfoMessage("您的线下充值记录已经提交，请等待管理员审核。请勿重复提交！");
		return "pretty:userCenter";
	}

	/**
	 * 充值
	 */
	public void recharge() {
		if (StringUtils.isEmpty(this.getInstance().getRechargeWay())) {
			FacesUtil.addErrorMessage("请选择充值方式！");
			return;
		}
		// 前台表单_blank提交，判空使用js验证，用于防止跳转到支付页面被拦截
		String url = rechargeService.createRechargeOrder(getInstance(), null);
		FacesUtil.sendRedirect(url);
	}

	/** 接收充值回调 */
	public String receiveRechargeReturnWeb() {
		if (log.isInfoEnabled()) {
			log.info("recharge return web:"
					+ HttpClientUtil.requestParametersToString(FacesUtil
							.getHttpServletRequest()));
		}
		PayService payService = (PayService) SpringBeanUtil
				.getBeanByName(getInstance().getRechargeWay() + "PayService");
		payService.receiveReturnWeb(FacesUtil.getHttpServletRequest());
		return "pretty:userCenter";
	}

	public void receiveRechargeReturnS2S() {
		if (log.isInfoEnabled()) {
			log.info("recharge return s2s:"
					+ HttpClientUtil.requestParametersToString(FacesUtil
							.getHttpServletRequest()));
		}
		PayService payService = (PayService) SpringBeanUtil
				.getBeanByName(getInstance().getRechargeWay() + "PayService");
		payService.receiveReturnS2S(FacesUtil.getHttpServletRequest(),
				FacesUtil.getHttpServletResponse());
		FacesUtil.getCurrentInstance().responseComplete();
	}

	/** 去往充值支付方 */
	public void toRecharge() {
		Recharge recharge = getInstance();
		if (recharge == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("payWay:" + getPayWay(recharge) + ", bankNo:"
					+ getBankNo(recharge));
		}
		PayService payService = (PayService) SpringBeanUtil
				.getBeanByName(getPayWay(recharge) + "PayService");
		payService.recharge(FacesUtil.getCurrentInstance(), recharge,
				getBankNo(recharge));
	}

	/**
	 * 管理员充值
	 * 
	 * @param rechargeId
	 */
	public void rechargeByAdmin(String rechargeId) {
		rechargeService.rechargeByAdmin(rechargeId);
		FacesUtil.addInfoMessage("充值成功，相应款项已经充入到用户账户中");
	}

	/**
	 * 获取支付方式
	 * 
	 * @param recharge
	 * @return
	 */
	private static String getPayWay(Recharge recharge) {
		return recharge.getRechargeWay().split("_")[0];
	}

	/**
	 * 获取充值银行号码
	 * 
	 * @param recharge
	 * @return
	 */
	private static String getBankNo(Recharge recharge) {
		String[] strs = recharge.getRechargeWay().split("_");
		if (strs.length > 1) {
			return strs[1];
		}
		return null;
	}

}
