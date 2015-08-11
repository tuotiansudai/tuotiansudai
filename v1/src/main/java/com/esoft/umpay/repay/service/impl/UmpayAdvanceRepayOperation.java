package com.esoft.umpay.repay.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.umpay.loan.service.impl.UmPayLoanStatusService;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.invest.InvestConstants.TransferStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.invest.service.TransferService;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * Description : 提前还款
 * 
 * @author zt
 * @data step:转入标的,分账,更改状态
 */
@Service("umpayAdvanceRepayOperation")
public class UmpayAdvanceRepayOperation extends UmPayOperationServiceAbs<Loan> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UmPayLoanMoneyService umPayLoanMoneyService;

	@Resource
	UserBillBO userBillBO;

	@Resource
	RepayService repayService;

	@Resource
	FeeConfigBO feeConfigBO;

	@Resource
	TransferService transferService;

	@Resource
	UmPayLoanStatusService umpayLoanStatusService;

	@Resource
	LoanService loanService;

	@Logger
	Log log;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {
		ht.evict(loan);
		loan = ht.get(Loan.class, loan.getId(), LockMode.UPGRADE);
		// 查询当期还款是否已还清
		List<LoanRepay> repays = loan.getLoanRepays();
		// 剩余所有本金
		double sumCorpus = 0D;
		// 还款手续费总额
		double feeAll = 0D;
		for (LoanRepay repay : repays) {
			if (repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)
					|| repay.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
				// 在还款期，而且没还款
				if (repayService.isInRepayPeriod(repay.getRepayDay())) {
					// 有未完成的当期还款。
					throw new UmPayOperationException("当期还款未完成");
				} else {
					sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
					feeAll = ArithUtil.add(feeAll, repay.getFee());
				}
			} else if (repay.getStatus().equals(
					LoanConstants.RepayStatus.BAD_DEBT)
					|| repay.getStatus().equals(
							LoanConstants.RepayStatus.OVERDUE)) {
				// 还款中存在逾期或者坏账
				throw new UmPayOperationException("还款中存在逾期或者坏账");
			}
		}
		// 取消投资下面的所有正在转让的债权
		String hql = "from TransferApply ta where ta.invest.loan.id=? and ta.status=?";
		List<TransferApply> tas = ht.find(hql, new String[] { loan.getId(),
				TransferStatus.WAITCONFIRM });
		if (tas.size() > 0) {
			// 有购买了等待第三方确认的债权，所以不能还款。
			throw new UmPayOperationException("有等待第三方确认的债权转让，不能还款！");
		}
		tas = ht.find(hql, new String[] { loan.getId(),
				TransferStatus.TRANSFERING });
		for (TransferApply ta : tas) {
			transferService.cancel(ta.getId());
		}
		// 给投资人的罚金
		double feeToInvestor = feeConfigBO.getFee(
				FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null, null,
				sumCorpus);
		// 给系统的罚金
		double feeToSystem = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_SYSTEM,
				FeeType.PENALTY, null, null, sumCorpus);
		// 待还总额 (给投资人总利息 + 给系统总额 + 剩余总本金 +还款手续费总和)
		double sumPay = ArithUtil.add(feeToInvestor, feeToSystem, sumCorpus,
				feeAll);
		ht.merge(loan);

		try {
			// 拼装数据
			DecimalFormat currentNumberFormat = new DecimalFormat("#");
			Map<String, String> sendMap = UmPaySignUtil
					.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
			sendMap.put(
					"ret_url",
					UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY);
			sendMap.put(
					"notify_url",
					UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY);
			// 编号为:loanId + 时间戳
			String order_id = loan.getId() + System.currentTimeMillis();
			sendMap.put("order_id", order_id);
			sendMap.put("mer_date",
					DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
			sendMap.put("project_id", loan.getId());
			sendMap.put("serv_type",
					UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY);
			sendMap.put("trans_action",
					UmPayConstants.TransferProjectStatus.TRANS_ACTION_IN);
			sendMap.put("partic_type",
					UmPayConstants.TransferProjectStatus.PARTIC_TYPE_LOANER);
			sendMap.put("partic_acc_type",
					UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
			sendMap.put("partic_user_id",
					getTrusteeshipAccount(loan.getUser().getId()).getId());
			sendMap.put("amount", currentNumberFormat.format(sumPay * 100)); // 还款的总额,先全部打入标的里面,
																				// 然后进行分账
			ReqData reqData;
			reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			TrusteeshipOperation to = createTrusteeshipOperation(
					order_id,
					reqData.getUrl(),
					loan.getId(),
					UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 前台回调 如果成功 则开始系统调用接口进行分账,将投资人所还的资金转入相应的账户里面
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		Map<String, String> paramMap;
		try {
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.debug("提前还款返回信息:" + paramMap);
			String ret_code = paramMap.get("ret_code");
			String order_id = paramMap.get("order_id");
			TrusteeshipOperation to = trusteeshipOperationBO
					.get(UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_ADVANCE_REPAY,
							order_id, UmPayConstants.OperationType.UMPAY);
			to.setResponseData(paramMap.toString());
			to.setResponseTime(new Date());
			Loan loan = ht.get(Loan.class, to.getOperator(), LockMode.UPGRADE);
			if ("0000".equals(ret_code)
					&& TrusteeshipConstants.Status.SENDED
							.equals(to.getStatus())) {
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				if (loan.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
					loan.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
				}
				if (loan.getStatus().equals(LoanStatus.WAIT_REPAY_VERIFY)) { // 如果是等待还款确认
					// 调动标的转账执行分账操作
					// 将钱转入投资人 + 将钱转入系统收费
					loanMoney2Invest(loan);
					/* 这个操作只能等待第三方分账 成功然后调用 */
					repayService.advanceRepay(loan.getId());
					if (loanService.isCompleted(loan.getId())) {
						/* updateLoanStatus2Complete(repay.getLoan()); */
						umpayLoanStatusService.updateLoanStatusOperation(loan, UmPayConstants.UpdateProjectStatus.PROJECT_STATE_FINISH, false);
					}
				}
			} else {
				if (loan.getStatus().equals(LoanStatus.WAIT_REPAY_VERIFY)) {
					to.setStatus(TrusteeshipConstants.Status.REFUSED);
					loan.setStatus(LoanStatus.REPAYING);
					ht.update(loan);
				}
			}
			ht.update(to);
		} catch (VerifyException e) {
			e.printStackTrace();
		} catch (AdvancedRepayException e) {
			throw new UmPayOperationException("提前还款异常");
		} catch (InsufficientBalance e) {
			throw new UmPayOperationException("余额不足");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("提前还款后台通知回调:" + paramMap);
			receiveOperationPostCallback(request);
			String responseData = getResponseData(paramMap.get("order_id"),
					"0000");
			log.debug("提前还款通知对方:" + responseData);
			response.getWriter().print(responseData);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		} catch (VerifyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Transactional(rollbackFor = Exception.class)
	public void loanMoney2Invest(Loan loan) {
		ht.evict(loan);
		loan = ht.get(Loan.class, loan.getId(), LockMode.UPGRADE);
		// 查询当期还款是否已还清
		List<LoanRepay> repays = loan.getLoanRepays();
		// 剩余所有本金
		double sumCorpus = 0D;
		// 还款手续费总额 = 借款人总给系统+投资人总给系统
		double feeAll = 0D;
		for (LoanRepay repay : repays) {
			if (repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)
					|| repay.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
				// 在还款期，而且没还款
				if (repayService.isInRepayPeriod(repay.getRepayDay())) {
					// 有未完成的当期还款。
					throw new UmPayOperationException("当期还款未完成");
				} else {
					sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
					feeAll = ArithUtil.add(feeAll, repay.getFee()); // 借款人给系统
				}
			} else if (repay.getStatus().equals(
					LoanConstants.RepayStatus.BAD_DEBT)
					|| repay.getStatus().equals(
							LoanConstants.RepayStatus.OVERDUE)) {
				// 还款中存在逾期或者坏账
				throw new UmPayOperationException("还款中存在逾期或者坏账");
			}
		}
		// 给投资人的罚金
		double feeToInvestor = feeConfigBO.getFee(
				FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null, null,
				sumCorpus);
		// 给系统的罚金
		double feeToSystem = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_SYSTEM,
				FeeType.PENALTY, null, null, sumCorpus);

		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		// 获取所有的投资人还款信息
		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.status=?",
						new Object[] { loan.getId(),
								LoanConstants.RepayStatus.REPAYING });
		// 遍历投资列表
		try {
			double feeToInvestorTemp = feeToInvestor;
			for (int i = 0; i < irs.size(); i++) {
				InvestRepay investRepay = irs.get(i);
				if (investRepay.getCorpus() != 0
						&& investRepay.getCorpus() != null) {
					// 罚金
					double ircashFineToInvestor = 0D;
					double irRepayMoney = 0D;
					if (i == irs.size() - 1) {
						ircashFineToInvestor = feeToInvestorTemp;
					} else {
						// 单个投资人罚金 = 投资的本金 / 剩余所有本金 * 给系统罚金的比例
						ircashFineToInvestor = ArithUtil.round(
								investRepay.getCorpus() / sumCorpus
										* feeToInvestor, 2);
					}
					// 给投资人罚金总和 临时 = 给投资人罚金总和 - 单个投资人罚金
					feeToInvestorTemp = ArithUtil.sub(feeToInvestorTemp,
							ircashFineToInvestor);
					// 给投资人的钱 = + 单个投资人罚金 + 本金
					irRepayMoney = ArithUtil.add(investRepay.getCorpus(),
							ircashFineToInvestor);

					Invest invest = investRepay.getInvest();
					Map<String, String> sendMap = UmPaySignUtil
							.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
					log.debug("将第三方标的的钱转至投资人:" + invest.getUser().getId());
					sendMap.put("ret_url",
							UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL);
					sendMap.put("notify_url",
							UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL);
					sendMap.put("order_id", investRepay.getId());
					sendMap.put("mer_date", DateUtil.DateToString(new Date(),
							DateStyle.YYYYMMDD));
					sendMap.put("project_id", invest.getLoan().getId());
					sendMap.put(
							"serv_type",
							UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY_BACK);
					sendMap.put(
							"trans_action",
							UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
					sendMap.put(
							"partic_type",
							UmPayConstants.TransferProjectStatus.PARTIC_TYPE_INVESTOR);
					sendMap.put(
							"partic_acc_type",
							UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
					sendMap.put("partic_user_id",
							getTrusteeshipAccount(invest.getUser().getId())
									.getId());
					// 应该转从标的转给投资人的钱 = 投资的本金 + 罚息 + 应得的利息( = 利息 - 投资人给系统的手续费)
					// 这里取消掉了,直接 罚金 + 本金
					// Double allRepayMoney =
					// ArithUtil.add(investRepay.getCorpus(),investRepay.getDefaultInterest(),
					// investRepay.getInterest()-investRepay.getFee() );
					sendMap.put("amount",
							currentNumberFormat.format(irRepayMoney * 100));
					ReqData reqData;
					reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
					TrusteeshipOperation to = createTrusteeshipOperation(
							investRepay.getId(), reqData.getUrl(),
							investRepay.getId(),
							UmPayConstants.OperationType.PROJECT_TRANSFER,
							reqData.getPlain());
					String responseBodyAsString = HttpClientUtil
							.getResponseBodyAsString(to.getRequestUrl());
					Map<String, String> resData = Plat2Mer_v40
							.getResData(responseBodyAsString);
					to.setResponseData(resData.toString());
					to.setResponseTime(new Date());
					if ("0000".equals(resData.get("ret_code"))) {
						log.debug("第三方从标的转给" + invest.getUser().getId() + "成功!");
						to.setStatus(TrusteeshipConstants.Status.PASSED);
						/* feeAll = ArithUtil.add(feeAll, investRepay.getFee()); */// 投资人给系统的钱
					} else {
						to.setStatus(TrusteeshipConstants.Status.REFUSED);
					}
					ht.update(to);
				}
			}
			log.info("提前还款给平台收费:" + feeAll);
			umPayLoanMoneyService.loanMoney2Mer(
					loan.getId() + System.currentTimeMillis(),
					ArithUtil.add(feeAll, feeToSystem), loan.getId());
		} catch (ReqDataException e) {
			log.error("提前还款将第三方标的钱转至投资人账户下:加密失败" + e);
		} catch (RetDataException e) {
			log.error("提前还款将第三方标的钱转至投资人账户下:解密失败" + e);
			e.printStackTrace();
		}
	}

}
