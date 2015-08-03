package com.esoft.umpay.repay.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.commons.logging.Log;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.controller.RepayHome;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.esoft.umpay.repay.service.impl.UmPayOverdueRepayOperation;
import com.esoft.umpay.repay.service.impl.UmpayAdvanceRepayOperation;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;

public class UmPayRepayHome extends RepayHome {

	@Resource
	UmPayNormalRepayOperation umPayNormalRepayOperation;

	@Resource
	UmpayAdvanceRepayOperation umpayAdvanceRepayOperation;

	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	UmPayOverdueRepayOperation umPayOverdueRepayOperation;

	@Resource
	UserBillService userBillService;

	@Logger
	Log log;

	/**
	 * 正常还款
	 */
	@Override
	public void normalRepay(String repayId) {
		LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
		// 判断是否可还款
		try {
			checkNormalRepay(lr);
			umPayNormalRepayOperation.createOperation(lr,
					FacesContext.getCurrentInstance());
		} catch (NormalRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new RuntimeException("unexpected invocation", e);
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (ReqDataException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (RetDataException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * 检查是否可还款
	 * 
	 * @param repay
	 * @throws InsufficientBalance
	 * @throws NormalRepayException
	 */
	private void checkNormalRepay(LoanRepay repay) throws InsufficientBalance,
			NormalRepayException {
		if (!repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
			// 该还款不处于正常还款状态。
			throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
		}
		double repayAllMoney = ArithUtil
				.add(repay.getCorpus(), repay.getDefaultInterest(),
						repay.getFee(), repay.getInterest());
		double balance = userBillService.getBalance(repay.getLoan().getUser()
				.getId());
		if (balance < repayAllMoney) {
			throw new InsufficientBalance("balance:" + balance
					+ "  repayAllMoney:" + repayAllMoney);
		}
	}

	/**
	 * 提前还款
	 */
	@Override
	public void advanceRepay(String loanId) {
		Loan loan = getBaseService().get(Loan.class, loanId);
		try {
			umpayAdvanceRepayOperation.createOperation(loan,
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			return;
		}
	}

	/**
	 * 逾期还款
	 */
	@Override
	public void overdueRepay(String repayId) {
		// 用户可用余额
		double balance = userBillService.getBalance(loginUserInfo
				.getLoginUserId());
		LoanRepay repay = getBaseService().get(LoanRepay.class, repayId);

		if (balance < ArithUtil.add(repay.getCorpus(), repay.getInterest(),
				repay.getFee(), repay.getDefaultInterest())) {
			FacesUtil.addErrorMessage("您的账户余额不足，请先进行充值然后再还款。");
			return;
		}
		LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
		if (lr.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)
				|| lr.getStatus().equals(LoanConstants.RepayStatus.BAD_DEBT)
				|| lr.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
			try {
				umPayOverdueRepayOperation.createOperation(lr,
						FacesContext.getCurrentInstance());
			} catch (IOException e) {
				log.error(e.getLocalizedMessage(), e);
				throw new RuntimeException("unexpected invocation", e);
			} catch (UmPayOperationException e) {
				FacesUtil.addErrorMessage(e.getMessage());
			} catch (ReqDataException e) {
				FacesUtil.addErrorMessage(e.getMessage());
			} catch (RetDataException e) {
				FacesUtil.addErrorMessage(e.getMessage());
			}
		} else {
			FacesUtil.addErrorMessage("还款不处于逾期还款状态");
		}
	}

	@Override
	public Class<Repay> getEntityClass() {
		return Repay.class;
	}

}
