package com.esoft.jdp2p.message.aop;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.message.MessageConstants.UserMessageNodeId;
import com.esoft.jdp2p.message.service.impl.MessageBO;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 消息动作监听器，在需要发送消息的节点进行监听
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-7 下午4:58:10
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-7 wangzhi 1.0
 */
@Component
@Aspect
public class MessageMonitor {

	@Resource
	private MessageBO messageBO;
	
	@Resource
	private HibernateTemplate ht;
	
	@Logger
	Log log;

	/**
	 * 充值成功
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(argNames = "recharge", value = "execution(public void com.esoft.jdp2p.user.service.impl.RechargeBO.rechargeSuccess(..)) && args(recharge)")
	public void rechargeSuccess(Recharge recharge) {
		if (log.isDebugEnabled()) {
			log.debug("MessageMonitor:rechargeSuccess, rechargeId:"+recharge.getId());
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", recharge.getUser().getUsername());
		params.put("time", DateUtil.DateToString(recharge.getSuccessTime(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put("money", recharge.getActualMoney().toString());
		params.put("rechargeId", recharge.getId());
		messageBO.sendMsg(recharge.getUser(), UserMessageNodeId.RECHARGE_SUCCESS, params);
	}

	/**
	 * 借款审核通过
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(argNames = "loan", value = "execution(public void com.esoft.jdp2p.loan.service.LoanService.passApply(..)) " +
			"&& args(loan)")
	public void passApply(Loan loan) {
		if (log.isDebugEnabled()) {
			log.debug("MessageMonitor:passApply, 借款审核通过，借款编号："+loan.getId());
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("loanName", loan.getName());
		params.put("time", DateUtil.DateToString(loan.getVerifyTime(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		
		messageBO.sendMsg(loan.getUser(), UserMessageNodeId.LOAN_VERIFY_SUCCESS, params);
	}
	
//	/**
//	 * 投资成功，给投资用户发短信
//	 * @param invest
//	 */
//	@AfterReturning(argNames = "invest", value = "execution(public void com.esoft.jdp2p.invest.service.InvestService.create(..)) && args(invest)")
//	public void sendInvestMessage(Invest invest) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("username", invest.getUser().getId());
//		params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
//		
//		User user = ht.get(User.class, invest.getUser().getId());
//		
//		messageBO.sendMsg(user, MessageConstants.UserMessageNodeId.INVEST, params);
//	}


}




