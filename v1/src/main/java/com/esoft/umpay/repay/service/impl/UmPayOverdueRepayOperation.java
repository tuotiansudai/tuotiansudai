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
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.repay.exception.OverdueRepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.loan.service.impl.UmPayLoanStatusService;
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

@Service("umPayOverdueRepayOperation")
public class UmPayOverdueRepayOperation extends
		UmPayOperationServiceAbs<LoanRepay> {

	@Logger
	static Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	LoanService loanService;

	@Resource
	UserBillBO userBillBO;

	@Resource
	UmPayLoanMoneyService umPayLoanMoneyService;

	@Resource
	UmPayLoanStatusService umpayLoanStatusService;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	TransferService transferService;

	@Resource
	RepayService repayService;

	@Resource
	UmPayNormalRepayOperation umPayNormalRepayOperation;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(LoanRepay loanRepay,
			FacesContext facesContext) throws IOException,ReqDataException, RetDataException {
		// FIXME:验证
		ht.update(loanRepay);
		// 所有待还金额 = 所有本金 + 所有罚息(给投资人总和罚息+给系统的罚息) + 投资人给系统手续费 + 所有的利息
		Double allRepayMoney = ArithUtil.add(loanRepay.getCorpus(),
				loanRepay.getDefaultInterest(), loanRepay.getFee(),
				loanRepay.getInterest());
		/*
		 * if
		 * (!loanRepay.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)&&
		 * !loanRepay.getStatus().equals(LoanConstants.RepayStatus.BAD_DEBT)) {
		 * throw new UmPayOperationException("还款不处于逾期还款状态！"); }
		 */
		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
						new Object[] { loanRepay.getLoan().getId(),
								loanRepay.getPeriod() });

		// 取消投资下面的所有正在转让的债权
		String hql = "from TransferApply ta where ta.invest.loan.id=? and ta.status=?";
		List<TransferApply> tas = ht.find(hql, new String[] {
				loanRepay.getLoan().getId(), TransferStatus.WAITCONFIRM });
		if (tas.size() > 0) {
			// 有购买了等待第三方确认的债权，所以不能还款。
			throw new UmPayOperationException("有等待第三方确认的债权转让，不能还款！");
		}
		tas = ht.find(hql, new String[] { loanRepay.getLoan().getId(),
				TransferStatus.TRANSFERING });
		for (TransferApply ta : tas) {
			transferService.cancel(ta.getId());
		}

		// 调用接口->借款人的钱先划入标的里面,等成功信息返回调用分账将标的里面的钱划入每个投资人账户下面
		try {
			DecimalFormat currentNumberFormat = new DecimalFormat("#");
			Map<String, String> sendMap = UmPaySignUtil
					.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
			sendMap.put(
					"ret_url",
					UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY);
			sendMap.put(
					"notify_url",
					UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY);
			// 时间戳,取前18位即为:还款编号
			String order_id = loanRepay.getId() + System.currentTimeMillis();
			sendMap.put("order_id", order_id);
			sendMap.put("mer_date",
					DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
			sendMap.put("project_id", loanRepay.getLoan().getId());
			sendMap.put("serv_type",
					UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY);
			sendMap.put("trans_action",
					UmPayConstants.TransferProjectStatus.TRANS_ACTION_IN);
			sendMap.put("partic_type",
					UmPayConstants.TransferProjectStatus.PARTIC_TYPE_LOANER);
			sendMap.put("partic_acc_type",
					UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
			sendMap.put(
					"partic_user_id",
					getTrusteeshipAccount(loanRepay.getLoan().getUser().getId())
							.getId());// 有点恶心
			sendMap.put("amount",
					currentNumberFormat.format(allRepayMoney * 100));
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			TrusteeshipOperation to = createTrusteeshipOperation(
					order_id,
					reqData.getUrl(),
					loanRepay.getId(),
					UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 逾期前台回调
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException,IOException {
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.info("逾期还款前台：" + paramMap);
			String ret_code = paramMap.get("ret_code");
			String order_id = paramMap.get("order_id");
			TrusteeshipOperation to = trusteeshipOperationBO
					.get(UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_OVERDUE_REPAY,
							order_id, UmPayConstants.OperationType.UMPAY);
			to.setResponseData(paramMap.toString());
			to.setResponseTime(new Date());
			if ("0000".equals(ret_code)) {
				String repayId = to.getOperator();
				LoanRepay lr = ht.get(LoanRepay.class, repayId,
						LockMode.UPGRADE); // 锁定还款信息
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				if (lr.getStatus().equals(RepayStatus.OVERDUE)) {
					lr.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
				}
				if (lr.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
					// 先将标的里面的钱转至投资人(第三方分账操作)
					loanMoney2InvestPerson(lr);
					repayService.overdueRepay(repayId); // 逾期还款
					// 如果还款完成调用 调用接口改变标的状态为结束
					if (loanService.isCompleted(lr.getLoan().getId())) {
						/* updateLoanStatus2Complete(repay.getLoan()); */
						umpayLoanStatusService
								.updateLoanStatusOperation(
										lr.getLoan(),
										UmPayConstants.UpdateProjectStatus.PROJECT_STATE_FINISH,
										false);
					}
				}
			} else {
				fail(to);
			}
		} catch (VerifyException e) {
			throw new UmPayOperationException("验证签名失败！");
		} catch (OverdueRepayException e) {
			throw new UmPayOperationException("逾期还款异常！");
		} catch (InsufficientBalance e) {
			throw new UmPayOperationException("余额不足以支付逾期！");
		}
	}

	/**
	 * 服务器通知
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("逾期还款后台通知回调:" + paramMap);
			receiveOperationPostCallback(request);
			String responseData = getResponseData(paramMap.get("order_id"),
					"0000");
			log.debug("逾期还款通知对方:" + responseData);
			response.getWriter().print(responseData);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		} catch (VerifyException e) {
			log.error("正常还款验签失败" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 调用标的转账: 将loan里面的钱转给投资人 , 但是不包括fee手续费 和给系统罚息
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void loanMoney2InvestPerson(LoanRepay lr) {
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		LoanRepay loanRepay = ht.get(LoanRepay.class, lr.getId());
		// 借款人罚息 = 给投资人罚息总和 + 给系统的罚息
		Double defaultInterest = loanRepay.getDefaultInterest();
		// 借款人给系统的手续费
		Double fee = loanRepay.getFee();
		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
						new Object[] { loanRepay.getLoan().getId(),
								loanRepay.getPeriod() });
		try {
			// 从loan里面把相应的钱都转给相应的投资人
			for (InvestRepay investRepay : irs) {
				Invest invest = investRepay.getInvest();
				Map<String, String> sendMap = UmPaySignUtil
						.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
				log.debug("逾期还款将第三方标的的钱转至投资人:" + invest.getUser().getId());
				sendMap.put("ret_url",
						UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL);
				sendMap.put("notify_url",
						UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL);
				sendMap.put("order_id", investRepay.getId());
				sendMap.put("mer_date",
						DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
				sendMap.put("project_id", invest.getLoan().getId());
				sendMap.put(
						"serv_type",
						UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY_BACK);
				sendMap.put("trans_action",
						UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
				sendMap.put(
						"partic_type",
						UmPayConstants.TransferProjectStatus.PARTIC_TYPE_INVESTOR);
				sendMap.put(
						"partic_acc_type",
						UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
				sendMap.put("partic_user_id",
						getTrusteeshipAccount(invest.getUser().getId()).getId());
				// 给系统罚息 = 给系统罚息总和 - 每个投资人罚息
				defaultInterest = ArithUtil.sub(defaultInterest,
						investRepay.getDefaultInterest());
				// 应该转从标的转给投资人的钱 = 投资的本金 + 罚息 + 应得的利息( = 利息 - 投资人给系统的手续费)
				Double allRepayMoney = ArithUtil.add(investRepay.getCorpus(),
						investRepay.getDefaultInterest(),
						investRepay.getInterest() - investRepay.getFee());
				sendMap.put("amount",
						currentNumberFormat.format(allRepayMoney * 100));
				ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
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
					log.debug("逾期还款第三方从标的转给" + invest.getUser().getId() + "成功!");
					to.setStatus(TrusteeshipConstants.Status.PASSED);
					fee += investRepay.getFee();
				} else {
					to.setStatus(TrusteeshipConstants.Status.REFUSED);
				}
				ht.update(to);
			}
			// 每次还款都需要将手续费给系统(商户) 逾期了 所以要加上给系统的罚息
			log.info("逾期还款给平台收费:FEE" + fee + "借款人给系统罚息：" + defaultInterest);
			umPayLoanMoneyService.loanMoney2Mer(loanRepay.getId(), ArithUtil
					.add(defaultInterest, fee), loanRepay.getLoan().getId());
		} catch (ReqDataException e) {
			log.error("逾期还款将第三方标的钱转至投资人账户下:加密失败" + e);
		} catch (RetDataException e) {
			log.error("逾期还款将第三方标的钱转至投资人账户下:解密失败" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 逾期还款失败
	 * 
	 * @param to
	 */
	@SuppressWarnings("deprecation")
	@Transactional(rollbackFor = Exception.class)
	public void fail(TrusteeshipOperation to) {
		LoanRepay lr = ht
				.get(LoanRepay.class, to.getMarkId(), LockMode.UPGRADE);
		lr.setStatus(RepayStatus.OVERDUE);
		to.setStatus(TrusteeshipConstants.Status.REFUSED);
		ht.update(lr);
		ht.update(to);
	}

}
