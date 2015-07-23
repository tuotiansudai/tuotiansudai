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

	@Logger
	static Log log;

	private String operationType;

	public String handleWebReturn() {
		if (log.isDebugEnabled()) {
			log.debug("POST call back: " + this.operationType);
		}
		if (UmPayConstants.OperationType.MER_BIND_CARD
				.equals(this.operationType)) {
			return this.bindinBankCradWeb();
		} else if (UmPayConstants.OperationType.MER_RECHARGE_PERSON
				.equals(this.operationType)) {
			return this.rechargePersonWeb();
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST
				.equals(this.operationType)) {
			return this.investWeb();
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY
				.equals(this.operationType)) {
			return this.normalRepayWeb();
		} else if (UmPayConstants.OperationType.CUST_WITHDRAWALS
				.equals(this.operationType)) {
			return this.custWithdrawWeb();
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY
				.equals(this.operationType)) {
			return this.advanceRepay();
		} else if (UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY
				.equals(this.operationType)) {
			return this.overdueRepay();
		} else if (UmPayConstants.OperationType.MER_REPLACE_CARD.equals(this.operationType)) {
			return this.replaceBankCardWeb();
		} else if (UmPayConstants.OperationType.MER_BIND_AGREEMENT.equals(this.operationType)) {
			return this.bindingAgreementWeb();
		}
		return "404";
	}

	public void handleS2SWebReturn() {
		if (log.isDebugEnabled()) {
			log.debug("S2S call back: " + this.operationType);
		}
		if (UmPayConstants.OperationType.MER_BIND_CARD
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
		} else if (UmPayConstants.OperationType.MER_REPLACE_CARD.equals(this.operationType)) {
			this.S2S(umPayReplaceBankCardOperation);
		} else if (UmPayConstants.OperationType.MER_BIND_AGREEMENT.equals(this.operationType)) {
			this.S2S(umPayBindingAgreementOperation);
		}
	}

	/**
	 * web通知-绑定银行卡
	 * 
	 * @return
	 */
	public String bindinBankCradWeb() {
		try {
			umPayBindingBankCardOperation
					.receiveOperationPostCallback(FacesUtil
							.getHttpServletRequest());
			FacesUtil.addInfoMessage("您需要绑定的银行卡信息已经提交至联动优势,请等待审核!");
			return "pretty:withdraw";
		} catch (TrusteeshipReturnException e) {
			log.debug(e);
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public String replaceBankCardWeb() {
		try {
			this.umPayReplaceBankCardOperation.receiveOperationPostCallback(FacesUtil.getHttpServletRequest());
			FacesUtil.addInfoMessage("您需要更换的银行卡信息已经提交至联动优势,请等待审核!");
			return "pretty:withdraw";
		} catch (TrusteeshipReturnException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return  null;
	}

	public String bindingAgreementWeb() {
		try {
			umPayBindingAgreementOperation.receiveOperationPostCallback(FacesUtil.getHttpServletRequest());
			FacesUtil.addInfoMessage("签约协议成功");
		} catch (TrusteeshipReturnException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.addErrorMessage(e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:bankCardList";
	}

	/**
	 * WEB通知-个人账户充值
	 */
	public String rechargePersonWeb() {
		try {
			umPayRechargeOteration.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.addInfoMessage("充值成功");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.addErrorMessage("充值失败：" + e.getMessage());
			log.debug(e);
			e.printStackTrace();
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:userCenter";
	}

	/**
	 * WEB-投标
	 */
	public String investWeb() {
		try {
			umPayInvestOeration.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
			FacesUtil.addInfoMessage(e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:userCenter";
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
	public String custWithdrawWeb() {
		try {
			umPayWithdrawOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.addInfoMessage("银行已经接受您的请求!");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.addInfoMessage("提现操作失败！");
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:userCenter";
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
