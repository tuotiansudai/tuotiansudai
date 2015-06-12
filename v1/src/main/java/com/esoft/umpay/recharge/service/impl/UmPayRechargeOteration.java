package com.esoft.umpay.recharge.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserConstants.RechargeStatus;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.RechargeService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;

/**
 * Description : 充值操作
 * 
 * @author zt
 * @data 2015-3-9下午7:46:43
 */
@Service("umPayRechargeOteration")
public class UmPayRechargeOteration extends UmPayOperationServiceAbs<Recharge> {

	@Resource
	RechargeService rechargeService;

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	ConfigService configService;

	@Logger
	static Log log;

	/**
	 * 发送请求
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Recharge recharge,
			FacesContext facesContext) throws IOException {
		TrusteeshipAccount ta = getTrusteeshipAccount(recharge.getUser()
				.getId());
		// 保存一个充值订单
		String id = rechargeService.createRechargeOrder(recharge);
		log.debug(id);
		recharge = ht.get(Recharge.class, recharge.getId());
		User user = ht.get(User.class, recharge.getUser().getId());
		recharge.setIsRechargedByAdmin(false);
		recharge.setTime(new Date());
		recharge.setUser(user);
		recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
		// 获取拼装map
		Map<String, String> sendMap = UmPaySignUtil
				.getSendMapDate(UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		// 同步地址
		sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
				+ UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		// 异步地址
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		// 视图类型
		// map.put("sourceV",sourceV);
		sendMap.put("order_id", recharge.getId());
		sendMap.put("mer_date",
				DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		/**
		 * <option value="B2CDEBITBANK">借记卡网银</option> <option
		 * value="B2BBANK">企业网银</option> <option
		 * value="DEBITCARD">借记卡快捷</option>
		 */
		// 充值方式
		sendMap.put("pay_type", "B2CDEBITBANK");
		// 资金账户托管平台的用户号
		sendMap.put("user_id", ta.getId());
		// 资金账户托管平台的账户号
		// map.put("account_id",account_id);
		double actualMoney = (double) recharge.getActualMoney();
		// 充值的时候必须发送的请求是整数倍 而且单位是分
		int monery = (int) actualMoney * 100;
		// 充值金额
		sendMap.put("amount", String.valueOf(monery));
		// 发卡银行编号
		sendMap.put("gate_id", recharge.getRechargeWay());
		// 用户IP地址
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		sendMap.put("user_ip", FacesUtil.getRequestIp(request));
		// 保存操作记录
		TrusteeshipOperation to = null;
		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			// 保存操作记录
			to = createTrusteeshipOperation(recharge.getId(), reqData.getUrl(),
					recharge.getId(),
					UmPayConstants.OperationType.MER_RECHARGE_PERSON,
					GsonUtil.fromMap2Json(reqData.getField()));
			// 发送请求
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			e.printStackTrace();
		}
		return to;
	}

	/**
	 * WEB通知
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		Map<String, String> paramMap = null;
		try {
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.info("recharge-web-data:" + paramMap);
			// 状态码
			String ret_code = paramMap.get("ret_code");
			// 订单编号
			String order_id = paramMap.get("order_id");

			// 操作记录
			TrusteeshipOperation to = trusteeshipOperationBO.get(
					UmPayConstants.OperationType.MER_RECHARGE_PERSON, order_id,
					UmPayConstants.OperationType.UMPAY);
			// 充值成功
			if ("0000".equals(ret_code)) {
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				to.setResponseTime(new Date());
				to.setResponseData(paramMap.toString());
				ht.update(to);
			} else {
				// 充值失败
				Recharge recharge = ht.get(Recharge.class,
						paramMap.get("order_id"));
				recharge.setStatus(RechargeStatus.FAIL);
				ht.update(recharge);
				to.setStatus(TrusteeshipConstants.Status.REFUSED);
				to.setResponseData(paramMap.toString());
				ht.update(to);
				// 错误原因
				throw new TrusteeshipReturnException("错误代码:" + ret_code
						+ "-错误信息:" + paramMap.get("ret_msg"));
			}
		} catch (VerifyException e) {
			log.debug(e);
			throw new TrusteeshipReturnException("验签失败!");
		}

	}

	/**
	 * S2S通知
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		Map<String, String> paramMap = null;
		try {
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.info("recharge-s2s-data:" + paramMap);
			if (null != paramMap) {
				// 状态码
				String ret_code = paramMap.get("ret_code");
				// 订单编号
				String order_id = paramMap.get("order_id");

				// 查找对应的充值记录
				TrusteeshipOperation to = trusteeshipOperationBO.get(
						UmPayConstants.OperationType.MER_RECHARGE_PERSON,
						paramMap.get("order_id"),
						UmPayConstants.OperationType.UMPAY);
				String operator = to.getOperator();
				// 设置响应时间
				to.setResponseTime(new Date());
				to.setResponseData(paramMap.toString());

				if ("0000".equals(ret_code)) {
					// 充值成功
					rechargeService.rechargePaySuccess(order_id);
					// 设置用户操作状态,是否充值成功,充值成功修改操作状态
					to.setStatus(TrusteeshipConstants.Status.PASSED);
					to.setResponseData(paramMap.toString());
					// 更新记录
					ht.update(to);
				} else {
					// 标记充值失败。
					Recharge recharge = ht.get(Recharge.class, operator);
					recharge.setStatus(RechargeStatus.FAIL);
					ht.update(recharge);
					to.setStatus(TrusteeshipConstants.Status.REFUSED);
					to.setResponseData(paramMap.toString());
					ht.update(to);
				}
				// 不管充值成功还是失败,上面只要通过了就证明验签没问题,至于充值失败也要返回给umpay数据
				try {
					response.setCharacterEncoding("utf-8");
					// 返回数据
					String responseData = getResponseData(order_id, ret_code);
					response.getWriter().print(responseData);
					FacesUtil.getCurrentInstance().responseComplete();
				} catch (IOException e) {
					log.debug(e);
					e.getStackTrace();
				}
			}
		} catch (VerifyException e) {
			log.debug(e);
			throw new RuntimeException("验签失败!");
		}

	}

}
