package com.esoft.umpay.recharge.service.impl;

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
import com.ttsd.api.dto.AccessSource;
import com.ttsd.api.dto.BankCardResponseDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.util.CommonUtils;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

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
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Recharge recharge,
			FacesContext facesContext,boolean isOpenFastPayment) throws IOException {
		recharge.setSource(AccessSource.WEB.name());
		Map<String,String> sendMap = assembleSendMap(recharge, isOpenFastPayment, null, false);
		// 保存操作记录
		TrusteeshipOperation trusteeshipOperation = null;
		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			// 保存操作记录
			trusteeshipOperation = createTrusteeshipOperation(recharge.getId(), reqData.getUrl(),
					recharge.getId(),
					UmPayConstants.OperationType.MER_RECHARGE_PERSON,
					GsonUtil.fromMap2Json(reqData.getField()));
			// 发送请求
			sendOperation(trusteeshipOperation, facesContext);
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return trusteeshipOperation;
	}

	/**
	 * @function 组装请求联动优势接口的报文
	 * @param recharge
	 * @param isOpenFastPayment
	 * @param request
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String,String> assembleSendMap(Recharge recharge, boolean isOpenFastPayment, HttpServletRequest request, boolean isMobileRequest){
		TrusteeshipAccount ta = getTrusteeshipAccount(recharge.getUser()
				.getId());
		if(isOpenFastPayment){
			String bankCardNo = rechargeService.getRechangeWay(recharge.getUser().getId());
			if (bankCardNo == null){
				return null;
			}
			recharge.setRechargeWay(rechargeService.getRechangeWay(recharge.getUser()
					.getId()));

		}
		// 保存一个充值订单
		String id = rechargeService.createRechargeOrder(recharge,request);
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
		if(isMobileRequest) {
			// 同步地址
			sendMap.put("ret_url", UmPayConstants.ResponseMobUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		}else{
			// 同步地址
			sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		}
		// 异步地址
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.MER_RECHARGE_PERSON);
		// 视图类型
		// map.put("sourceV",sourceV);
		sendMap.put("order_id", recharge.getId());
		log.debug("order_id="+recharge.getId());
		sendMap.put("mer_date",
				DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		/**
		 * <option value="B2CDEBITBANK">借记卡网银</option> <option
		 * value="B2BBANK">企业网银</option> <option
		 * value="DEBITCARD">借记卡快捷</option>
		 */
		// 充值方式
		if(isOpenFastPayment){
			sendMap.put("pay_type", "DEBITCARD");
		}else{
			sendMap.put("pay_type", "B2CDEBITBANK");
			// 发卡银行编号
			sendMap.put("gate_id", recharge.getRechargeWay());
		}
		// 资金账户托管平台的用户号
		sendMap.put("user_id", ta.getId());
		// 资金账户托管平台的账户号
		double actualMoney = (double) recharge.getActualMoney();
		// 充值的时候必须发送的请求是整数倍 而且单位是分
		int monery = (int) actualMoney * 100;
		// 充值金额
		sendMap.put("amount", String.valueOf(monery));
		// 用户IP地址
		if (request == null){
			request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			sendMap.put("user_ip", FacesUtil.getRequestIp(request));
		}else {
			sendMap.put("user_ip", FacesUtil.getRequestIp(request));
		}

		return sendMap;
	}

	/**
	 * @function 用户通过app端快捷充值
	 * @param recharge 充值所需参数包装类
	 * @return
	 * @throws IOException
	 */
	@Transactional(rollbackFor = Exception.class)
	public BaseResponseDto createOperation(Recharge recharge,HttpServletRequest request) throws IOException {
		BaseResponseDto baseResponseDto = new BaseResponseDto();
		Map<String,String> sendMap = assembleSendMap(recharge, true, request, true);

		if (sendMap == null){
			baseResponseDto.setCode(ReturnMessage.NOT_OPNE_FAST_PAYMENT.getCode());
			baseResponseDto.setMessage(ReturnMessage.NOT_OPNE_FAST_PAYMENT.getMsg());
			return baseResponseDto;
		}
		//配置此项，表示使用H5页面
		sendMap.put("sourceV", UmPayConstants.SourceViewType.SOURCE_V);
		// 同步地址
//		sendMap.put("ret_url", "");
		// 保存操作记录

		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			String requestData = GsonUtil.fromMap2Json(reqData.getField());//请求数据
			// 保存操作记录
			createTrusteeshipOperation(recharge.getId(), reqData.getUrl(),
					recharge.getId(),
					UmPayConstants.OperationType.MER_RECHARGE_PERSON,
					requestData);
			baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
			baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
			BankCardResponseDto bankCardResponseDto = new BankCardResponseDto();
			bankCardResponseDto.setUrl(reqData.getUrl());
			bankCardResponseDto.setRequestData(CommonUtils.mapToFormData(reqData.getField(),true));
			baseResponseDto.setData(bankCardResponseDto);
			return baseResponseDto;
		} catch (UnsupportedEncodingException e){
			log.error(e.getLocalizedMessage(),e);
			baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
			baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
			return baseResponseDto;
		} catch (ReqDataException e) {
			baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
			baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
			log.error(e.getLocalizedMessage(),e);
			return baseResponseDto;
		}
	}


	@Override
	public TrusteeshipOperation createOperation(Recharge recharge, FacesContext facesContext) throws Exception {
		return null;
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
			log.info("recharge-web-data:" + paramMap);
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
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
			log.info("recharge-s2s-data:" + paramMap);
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
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
