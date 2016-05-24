package com.esoft.umpay.trusteeship.controller;

import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingAgreementOperation;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingBankCardOperation;
import com.esoft.umpay.bankcard.service.impl.UmPayReplaceBankCardOperation;
import com.esoft.umpay.invest.service.impl.UmPayInvestOeration;
import com.esoft.umpay.loan.service.impl.UmPayCancelLoanOperation;
import com.esoft.umpay.loan.service.impl.UmPayLoaingOperation;
import com.esoft.umpay.recharge.service.impl.UmPayRechargeOteration;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.esoft.umpay.repay.service.impl.UmPayOverdueRepayOperation;
import com.esoft.umpay.repay.service.impl.UmpayAdvanceRepayOperation;
import com.esoft.umpay.sysrecharge.service.impl.UmPaySystemRechargeOteration;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.esoft.umpay.withdraw.service.impl.UmPayWithdrawOperation;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Scope(ScopeType.REQUEST)
public class TrusteeshipHome {

	@Resource
	UmPayBindingBankCardOperation umPayBindingBankCardOperation;

	@Resource
	UmPayReplaceBankCardOperation umPayReplaceBankCardOperation;

	@Resource
	UmPayRechargeOteration umPayRechargeOteration;

	@Resource
	UmPayInvestOeration umPayInvestOeration;

	@Resource
	UmPayLoaingOperation umPayLoaingOperation;

	@Resource
	UmPayNormalRepayOperation umPayNormalRepayOperation;

	@Resource
	UmpayAdvanceRepayOperation umpayAdvanceRepayOperation;

	@Resource
	UmPayCancelLoanOperation umPayCancelLoanOperation;

	@Resource
	UmPayWithdrawOperation umPayWithdrawOperation;

	@Resource
	UmPayOverdueRepayOperation umPayOverdueRepayOperation;

	@Resource
	UmPayBindingAgreementOperation umPayBindingAgreementOperation;

	@Resource
	UmPaySystemRechargeOteration umPaySystemRechargeOteration;

	@Logger
	static Log log;

	private String operationType;

	public String handleWebReturn() {
		if (log.isDebugEnabled()) {
			log.debug("POST call back: " + this.operationType);
		}
		if (UmPayConstants.OperationType.PTP_MER_BIND_CARD
				.equals(this.operationType)) {
			return this.bindinBankCradWeb(false);
		} else if (UmPayConstants.OperationType.MER_RECHARGE_PERSON
				.equals(this.operationType)) {
			return this.rechargePersonWeb(false);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST
				.equals(this.operationType)) {
			return this.investWeb(false);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY
				.equals(this.operationType)) {
			return this.normalRepayWeb();
		} else if (UmPayConstants.OperationType.CUST_WITHDRAWALS
				.equals(this.operationType)) {
			return this.custWithdrawWeb(false);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY
				.equals(this.operationType)) {
			return this.advanceRepay();
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY
				.equals(this.operationType)) {
			return this.overdueRepay();
		} else if (UmPayConstants.OperationType.PTP_MER_REPLACE_CARD.equals(this.operationType)) {
			return this.replaceBankCardWeb(false);
		} else if (UmPayConstants.OperationType.PTP_MER_BIND_AGREEMENT.equals(this.operationType)) {
			return this.bindingAgreementWeb(false);
		} else if(UmPayConstants.OperationType.TRANSFER_ASYN.equals(this.operationType)){
			return this.transferAsynWeb();
		}
		return "404";
	}

	/**
	 * 移动端联动优势的回调
	 * 只有六种情况：绑卡 换卡 投资 充值 签约 取现
	 * @return
	 */
	public String handleMobReturn() {
		if (log.isDebugEnabled()) {
			log.debug("POST call back: " + this.operationType);
		}
		if (UmPayConstants.OperationType.PTP_MER_BIND_CARD
				.equals(this.operationType)) {
			return this.bindinBankCradWeb(true);
		} else if (UmPayConstants.OperationType.MER_RECHARGE_PERSON
				.equals(this.operationType)) {
			return this.rechargePersonWeb(true);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST
				.equals(this.operationType)) {
			return this.investWeb(true);
		} else if (UmPayConstants.OperationType.CUST_WITHDRAWALS
				.equals(this.operationType)) {
			return this.custWithdrawWeb(true);
		} else if (UmPayConstants.OperationType.PTP_MER_REPLACE_CARD.equals(this.operationType)) {
			return this.replaceBankCardWeb(true);
		} else if (UmPayConstants.OperationType.PTP_MER_BIND_AGREEMENT.equals(this.operationType)) {
			return this.bindingAgreementWeb(true);
		}
		return "404";
	}

	public void handleS2SWebReturn() {
		if (log.isDebugEnabled()) {
			log.debug("S2S call back: " + this.operationType);
		}
		if (UmPayConstants.OperationType.PTP_MER_BIND_CARD
				.equals(this.operationType)) {
			this.S2S(umPayBindingBankCardOperation);
		} else if (UmPayConstants.OperationType.MER_RECHARGE_PERSON
				.equals(this.operationType)) {
			this.S2S(umPayRechargeOteration);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST
				.equals(this.operationType)) {
			this.S2S(umPayInvestOeration);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER
				.equals(this.operationType)) {
			this.S2S(umPayLoaingOperation);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY
				.equals(this.operationType)) {
			this.S2S(umPayNormalRepayOperation);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_FAIL_BY_MANAGER
				.equals(this.operationType)) {
			this.S2S(umPayCancelLoanOperation);
		} else if (UmPayConstants.OperationType.CUST_WITHDRAWALS
				.equals(this.operationType)) {
			this.S2S(umPayWithdrawOperation);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY
				.equals(this.operationType)) {
			this.S2S(umpayAdvanceRepayOperation);
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY
				.equals(this.operationType)) {
			this.S2S(umPayOverdueRepayOperation);
		} else if (UmPayConstants.OperationType.PTP_MER_REPLACE_CARD.equals(this.operationType)) {
			this.S2S(umPayReplaceBankCardOperation);
		} else if (UmPayConstants.OperationType.PTP_MER_BIND_AGREEMENT.equals(this.operationType)) {
			this.S2S(umPayBindingAgreementOperation);
		} else if (UmPayConstants.OperationType.TRANSFER_ASYN.equals(this.operationType)) {
			this.S2S(umPaySystemRechargeOteration);
		}
	}

	/**
	 * web通知-绑定银行卡
	 * 
	 * @return
	 */
	public String bindinBankCradWeb(boolean onMobile) {
		try {
			umPayBindingBankCardOperation
					.receiveOperationPostCallback(FacesUtil
							.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("您需要绑定的银行卡信息已经提交至联动优势,请等待审核!");
			FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
			if(onMobile){
				return "/mobile/templates/bindcard.xhtml";
			}else {
				return "pretty:bankCardList";
			}
		} catch (TrusteeshipReturnException e) {
			log.debug(e);
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("绑卡失败: " + e.getMessage());
		}
		if(onMobile){
			return "/mobile/templates/bindcard.xhtml";
		}else {
			return null;
		}
	}

	public String replaceBankCardWeb(boolean onMobile) {
		try {
			this.umPayReplaceBankCardOperation.receiveOperationPostCallback(FacesUtil.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("您需要更换的银行卡信息已经提交至联动优势,请等待审核!");
			FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
			if(onMobile){
				return "/mobile/templates/changecard.xhtml";
			}else {
				return "pretty:bankCardList";
			}
		} catch (TrusteeshipReturnException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("换卡失败: " + e.getMessage());
		}
		if(onMobile){
			return "/mobile/templates/changecard.xhtml";
		}else {
			return null;
		}
	}

	public String bindingAgreementWeb(boolean onMobile) {
		try {
			umPayBindingAgreementOperation.receiveOperationPostCallback(FacesUtil.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("签约协议成功");
		} catch (TrusteeshipReturnException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("签约失败: " + e.getMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("签约失败: " + e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		if(onMobile){
			return "/mobile/templates/sign.xhtml";
		}else {
			return "pretty:bankCardList";
		}
	}

	/**
	 * WEB通知-个人账户充值
	 */
	public String rechargePersonWeb(boolean onMobile) {
		try {
			umPayRechargeOteration.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("充值成功");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("充值失败: " + e.getMessage());
			log.debug(e);
			e.printStackTrace();
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		if(onMobile){
			return "/mobile/templates/recharge.xhtml";
		}else {
			return "pretty:userCenter";
		}
	}

	/**
	 * WEB-投标
	 */
	public String investWeb(boolean onMobile) {
		try {
			umPayInvestOeration.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("投资成功");
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("投资失败: " + e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		if(onMobile){
			return "/mobile/templates/invest.xhtml";
		}else {
			return "pretty:userCenter";
		}
	}

	/**
	 * web-正常还款
	 */
	public String normalRepayWeb() {
		try {
			umPayNormalRepayOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.addInfoMessage("还款成功！");
		} catch (TrusteeshipReturnException e) {
			log.debug(e);
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:userCenter";
	}

	/**
	 * web-提前还款
	 */
	public String advanceRepay() {
		try {
			umpayAdvanceRepayOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.addInfoMessage("还款成功！");
		} catch (TrusteeshipReturnException e) {
			log.debug(e);
			e.printStackTrace();
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return "pretty:userCenter";
	}

	/**
	 * web-逾期还款
	 * 
	 * @return
	 */
	public String overdueRepay() {
		try {
			umPayOverdueRepayOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
		} catch (TrusteeshipReturnException e) {
			log.debug(e);
			e.printStackTrace();
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return "pretty:userCenter";
	}

	/**
	 * 提现
	 */
	public String custWithdrawWeb(boolean onMobile) {
		try {
			umPayWithdrawOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("银行已经接受您的请求!");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("提现失败: " + e.getLocalizedMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		if(onMobile){
			return "/mobile/templates/withdraw.xhtml";
		}else {
			return "pretty:userCenter";
		}
	}

	private String transferAsynWeb() {
		try {
			umPaySystemRechargeOteration.receiveOperationPostCallback(FacesUtil.getHttpServletRequest());
			FacesUtil.setRequestAttribute("callback_status","success");
			FacesUtil.addInfoMessage("银行已经接受您的请求!");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.setRequestAttribute("callback_status","fail");
			FacesUtil.addErrorMessage("平台充值失败: " + e.getLocalizedMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "/admin/fund/systemBillEdit.htm";
	}

	/**
	 * S2S处理
	 * 
	 * @param operation
	 */
	@SuppressWarnings("rawtypes")
	private void S2S(UmPayOperationServiceAbs operation) {
		operation.receiveOperationS2SCallback(
				FacesUtil.getHttpServletRequest(),
				FacesUtil.getHttpServletResponse());
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

}
