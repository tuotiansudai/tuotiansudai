package com.esoft.umpay.withdraw.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.util.ArithUtil;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.WithdrawCashService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;

/**
 * Description :提现操作
 * 
 * @author zt
 * @data 2015-3-23下午2:20:38
 */
@Service("umPayWithdrawOperation")
public class UmPayWithdrawOperation extends
		UmPayOperationServiceAbs<WithdrawCash> {

	@Resource
	WithdrawCashService withdrawCashService;

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	ConfigService configService;

	@Logger
	static Log log;

	@Resource
	UserBillBO ub;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(WithdrawCash withdrawCash,
			FacesContext facesContext) throws IOException {
		TrusteeshipOperation to;
		try {
			ReqData reqData = buildReqData(withdrawCash, false);
			to = createTrusteeshipOperation(withdrawCash.getId(), reqData.getUrl(),
					withdrawCash.getId(), UmPayConstants.OperationType.CUST_WITHDRAWALS,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new UmPayOperationException("提现失败！");
		}
		return to;
	}

	@Transactional(rollbackFor = Exception.class)
	public ReqData createOperation_mobile(WithdrawCash withdrawCash) throws ReqDataException {
		ReqData reqData;
        reqData = buildReqData(withdrawCash, true);
        createTrusteeshipOperation(withdrawCash.getId(), reqData.getUrl(),
                withdrawCash.getId(), UmPayConstants.OperationType.CUST_WITHDRAWALS,
                GsonUtil.fromMap2Json(reqData.getField()));
		return reqData;
	}

	private ReqData buildReqData(WithdrawCash withdrawCash, boolean isMobileRequest) throws ReqDataException {
		// 得到一个提现订单
		WithdrawCash wc = ht.get(WithdrawCash.class, withdrawCash.getId());
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		Map<String, String> sendMap = UmPaySignUtil
				.getSendMapDate(UmPayConstants.OperationType.CUST_WITHDRAWALS);
		if(isMobileRequest){
			sendMap.put("ret_url", UmPayConstants.ResponseMobUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.CUST_WITHDRAWALS);
			//配置此项，表示使用H5页面
			sendMap.put("sourceV", UmPayConstants.SourceViewType.SOURCE_V);
		} else {
			sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.CUST_WITHDRAWALS);
		}
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.CUST_WITHDRAWALS);
		sendMap.put("order_id", wc.getId());
		sendMap.put("mer_date",
				DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		sendMap.put("user_id", getTrusteeshipAccount(wc.getUser().getId())
				.getId());
		// 金额
		sendMap.put("amount", currentNumberFormat.format(wc.getMoney() * 100));
		// FIXME UMPAY==P2P平台收取手续费UMPAY暂时不支持
		return Mer2Plat_v40.makeReqDataByPost(sendMap);
	}

	/**
	 * WEB提现
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(noRollbackFor = UmPayOperationException.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		WithdrawCash wc = null;
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("前台提现验签通过：" + paramMap);
			String ret_code = paramMap.get("ret_code");
			String order_id = paramMap.get("order_id");
			String ret_msg = paramMap.get("ret_msg");
			TrusteeshipOperation to = trusteeshipOperationBO.get(
					UmPayConstants.OperationType.CUST_WITHDRAWALS, order_id,
					UmPayConstants.OperationType.UMPAY);
			order_id = to.getOperator();
			ht.evict(to);
			to = ht.get(TrusteeshipOperation.class, to.getId(),
					LockMode.UPGRADE);
			to.setResponseTime(new Date());
			to.setResponseData(paramMap.toString());
			// 处理成功
			wc = ht.get(WithdrawCash.class, order_id, LockMode.UPGRADE);

			if (TrusteeshipConstants.Status.SENDED.equals(to.getStatus())) {
				wc.setVerifyTime(new Date());
				wc.setVerifyMessage(ret_msg);
				if ("0000".equals(ret_code)) {
					// 提现订单编号
					// withdrawCashService.passWithdrawCashRecheck(wc);
					to.setStatus(TrusteeshipConstants.Status.PASSED);
					ht.merge(to);
					// 冻结提现金额和提现手续费
					String operationDetailTemplate = "申请提现，冻结提现金额{0}元（含{1}元手续费），提现编号: {2}";
					String operationDetail = MessageFormat.format(operationDetailTemplate,
							wc.getMoney() + wc.getFee(),
							wc.getFee(),
							wc.getId());
					ub.freezeMoney(wc.getUser().getId(),
							wc.getMoney() + wc.getFee(),
							OperatorInfo.APPLY_WITHDRAW,
							operationDetail);
					wc.setStatus(UserConstants.WithdrawStatus.RECHECK);
				} else {
					// withdrawCashService.refuseWithdrawCashApply(wc);
					to.setStatus(TrusteeshipConstants.Status.REFUSED);
					ht.merge(to);
					wc.setStatus(UserConstants.WithdrawStatus.VERIFY_FAIL);
					throw new UmPayOperationException("提现失败:" + ret_msg);
				}
			}
		} catch (InsufficientBalance e) {
			log.debug("用户 " + wc.getUser() + "账户余额不足! ");
			throw new RuntimeException(e);
		} catch (VerifyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * S2S提现
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		WithdrawCash wc = null;
		try {
			Map<String, String> paramMap = UmPaySignUtil.getMapDataByRequest(request);
			receiveOperationPostCallback(request);
			String order_id = paramMap.get("order_id");
			wc = ht.get(WithdrawCash.class, order_id, LockMode.UPGRADE);
			wc.setRecheckMessage(paramMap.containsKey("ret_msg") ? paramMap.get("ret_msg") : "");
			if ("0000".equals(paramMap.get("ret_code"))) {
				// 处理提现成功
				withdrawCashService.passWithdrawCashRecheck(wc);
			} else {
				String operationDetailTemplate = "提现申请未通过，解冻提现金额{0}元（含{1}元手续费）， 提现编号:{2}";
				ub.unfreezeMoney(wc.getUser().getId(),
						wc.getMoney() + wc.getFee(),
						OperatorInfo.APPLY_WITHDRAW,
						MessageFormat.format(operationDetailTemplate, wc.getMoney() + wc.getFee(), wc.getFee(), wc.getId()));
				// 处理提现失败
				withdrawCashService.refuseWithdrawCashApply(wc);
			}
			String responseData = getResponseData(order_id, "0000");
			log.debug("通知对方:" + responseData);
			response.getWriter().print(responseData);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (InsufficientBalance e) {
			log.debug("用户 " + wc.getUser() + "账户余额不足! ");
			throw new RuntimeException(e);
		} catch (TrusteeshipReturnException e) {
			log.error("提现后台通知错误：" + e);
		} catch (VerifyException e) {
			log.error("提现后台通知验签错误：" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
