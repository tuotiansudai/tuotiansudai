package com.esoft.umpay.bankcard.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.RechargeService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.ttsd.api.dto.BankCardResponseDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.util.CommonUtils;
import com.ttsd.special.services.ConferenceSaleService;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 绑定银行卡
 *
 * @author zt
 */
@Service("umPayBindingBankCardOperation")
public class UmPayBindingBankCardOperation extends
		UmPayOperationServiceAbs<BankCard> {

	@Resource
	private HibernateTemplate ht;
	@Resource
	private TrusteeshipOperationBO trusteeshipOperationBO;
	@Resource
	private RechargeService rechargeService;
	@Resource
	private BankCardService bankCardService;
	@Resource
	private SystemBillService systemBillService;
	@Autowired
	private ConferenceSaleService conferenceSaleService;
	@Logger
	Log log;

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(BankCard bankCard,
			FacesContext facesContext) throws IOException {
		// 因为返回通知的时候不知道是绑定什么卡,哪张卡,这里用绑卡的ID加上时间戳,保证不重复情况加回调的时候去掉时间戳的结尾
		String order_id = System.currentTimeMillis() + bankCard.getCardNo();
		Map<String, String> sendMap = assembleSendMap(bankCard,order_id,false);
		TrusteeshipOperation to = null;
		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			log.debug("绑卡发送数据:" + reqData);
			// 保存操作记录
			to = createTrusteeshipOperation(order_id, reqData.getUrl(),
					bankCard.getUser().getId(),
					UmPayConstants.OperationType.PTP_MER_BIND_CARD,
					GsonUtil.fromMap2Json(reqData.getField()));
			// 发送请求
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return to;
	}

	/**
	 * @function 组装调用联动优势的请求报文
	 * @param bankCard
	 * @param order_id
	 * @return
	 */
	public Map<String,String> assembleSendMap(BankCard bankCard, String order_id, boolean isMobileRequest){

		TrusteeshipAccount ta = getTrusteeshipAccount(bankCard.getUser()
				.getId());
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PTP_MER_BIND_CARD);
		if(isMobileRequest) {
			// 同步地址
			sendMap.put("ret_url", UmPayConstants.ResponseMobUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.PTP_MER_BIND_CARD);
		}else{
			// 同步地址
			sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.PTP_MER_BIND_CARD);
		}
		// 后台地址
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.PTP_MER_BIND_CARD);
		// 流水号(时间戳)
		sendMap.put("order_id", order_id);
		// 时间
		sendMap.put("mer_date",
				DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		// 联动优势开设用户id
		sendMap.put("user_id", ta.getId());
		// 银行卡号
		sendMap.put("card_id", bankCard.getCardNo());
		// 开卡户名
		sendMap.put("account_name", bankCard.getUser().getRealname());
		// 证件类型
		sendMap.put("identity_type", "IDENTITY_CARD");
		// 身份证证件号码(记得前台判断没有开户不能够绑卡)
		sendMap.put("identity_code", bankCard.getUser().getIdCard());
		// 银联号
		/* map.put("cnaps_code",""); */
		// 开卡地区
		/* map.put("account_area",""); */
		// 开户行
		/* map.put("card_branch_name",""); */
		// 快捷协议标志
		sendMap.put("is_open_fastPayment", bankCard.getIsOpenFastPayment()?"1":"0");
		return sendMap;
	}

	/**
	 * @function 用户通过app端绑定银行卡
	 * @param bankCard
	 * @return
	 * @throws IOException
	 */
	@Transactional(rollbackFor = Exception.class)
	public BaseResponseDto createOperation(BankCard bankCard) throws IOException {
		// 因为返回通知的时候不知道是绑定什么卡,哪张卡,这里用绑卡的ID加上时间戳,保证不重复情况加回调的时候去掉时间戳的结尾
		String order_id = System.currentTimeMillis() + bankCard.getCardNo();
		Map<String, String> sendMap = assembleSendMap(bankCard,order_id,true);
		//配置此项，表示使用H5页面
		sendMap.put("sourceV", UmPayConstants.SourceViewType.SOURCE_V);
		// 同步地址
//		sendMap.put("ret_url", "");
		BaseResponseDto baseResponseDto = new BaseResponseDto();
		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			String requestData = GsonUtil.fromMap2Json(reqData.getField());
			log.debug("绑卡发送数据:" + reqData);
			// 保存操作记录
			createTrusteeshipOperation(order_id, reqData.getUrl(),
					bankCard.getUser().getId(),
					UmPayConstants.OperationType.PTP_MER_BIND_CARD,
					requestData);
			baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
			baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
			BankCardResponseDto bankCardResponseDto = new BankCardResponseDto();
			bankCardResponseDto.setUrl(reqData.getUrl());
			bankCardResponseDto.setRequestData(CommonUtils.mapToFormData(reqData.getField(),true));
			baseResponseDto.setData(bankCardResponseDto);
		} catch (UnsupportedEncodingException e){
			log.error(e.getLocalizedMessage(),e);
			baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
			baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(),e);
			baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
			baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
		}
		return baseResponseDto;
	}


	/**
	 * 绑卡-WEB通知
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		Map<String, String> paramMap = null;
		try {
			// 解密
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.debug("绑卡-前台-通知:" + paramMap.toString());
			TrusteeshipAccount ta = ht.get(TrusteeshipAccount.class,
					paramMap.get("user_id"));
			// 这里是uuid+时间戳组成
			String order_id = paramMap.get("order_id");
			// 操作记录
			TrusteeshipOperation to = trusteeshipOperationBO.get(
					UmPayConstants.OperationType.PTP_MER_BIND_CARD, order_id, ta
							.getUser().getId(),
					UmPayConstants.OperationType.UMPAY);
			String ret_code = paramMap.get("ret_code");
			if ("0000".equals(ret_code)) {
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				to.setResponseTime(new Date());
				to.setResponseData(paramMap.toString());
				ht.update(to);
			} else {
				to.setStatus(TrusteeshipConstants.Status.REFUSED);
				to.setResponseTime(new Date());
				to.setResponseData(paramMap.toString());
				ht.update(to);
				// 真实错误原因
				throw new RuntimeException(new TrusteeshipReturnException(
						ret_code + ":" + paramMap.get("ret_msg")));
			}
		} catch (VerifyException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new TrusteeshipReturnException("验签失败");
		} catch (DuplicateKeyException e) {
			log.error(e.getLocalizedMessage(),e);
			throw new TrusteeshipReturnException("duplication");
		}
	}

	/**
	 * S2S通知
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			// 解密
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("绑卡-后台-通知:" + paramMap.toString());
			if (null != paramMap && paramMap.get("service").equals(UmPayConstants.OperationType.MER_BIND_CARD_NOTIFY)) {
				String ret_code = paramMap.get("ret_code");
				String order_id = paramMap.get("order_id");
				if (order_id == null){
					order_id = "";
				}
				if ("0000".equals(ret_code)) {
					TrusteeshipAccount ta = ht.get(TrusteeshipAccount.class,
							paramMap.get("user_id"));
					User user = ta.getUser();
					ht.evict(user);
					if(StringUtils.isNotEmpty(order_id)) {
						// 拿到用户未绑定的卡ID(截取13位到最后,因为银行卡长度是不固定的)
						String bankCardId = order_id.substring(13,
								order_id.length());
						if (!this.bankCardService.isCardNoBinding(bankCardId)) {
							String hql = "";
							if (StringUtils.isNotEmpty(paramMap.get("user_bind_agreement_list"))) {
								hql = "from BankCard where user.id =? and status =? and cardNo =? and isOpenFastPayment = true";
							} else {
								hql = "from BankCard where user.id =? and status =? and cardNo =? and isOpenFastPayment = false";
							}
							List<BankCard> userWillBindingBankCard = ht
									.find(hql, new String[]{user.getId(), "uncheck",
											bankCardId});
							if (null != userWillBindingBankCard) {
								for (int i = 0; i < userWillBindingBankCard.size(); i++) {
									BankCard bankCard = new BankCard();
									bankCard = userWillBindingBankCard.get(i);
									bankCard.setStatus("passed");
									bankCard.setBankNo(paramMap.get("gate_id"));
									bankCard.setBank(this.rechargeService.getBankNameByNo(paramMap.get("gate_id")));
									ht.update(bankCard);
								}
								if (paramMap.get("gate_id").equals("CMB")) {
									String detailTemplate = "用户{0}绑定{1}银行卡";
									try {
										this.systemBillService.transferOut(0.01, "binding_card", MessageFormat.format(detailTemplate,
												userWillBindingBankCard.get(0).getUser().getId(),
												this.rechargeService.getBankNameByNo(paramMap.get("gate_id"))));
									} catch (InsufficientBalance insufficientBalance) {
										log.error(insufficientBalance);
									}
								}

								// 如果满足会销活动条件则发放奖励
								conferenceSaleService.processIfInActivityForBindCard(bankCardId, user.getId());
							}
							if (userWillBindingBankCard.size() > 0){
								log.debug(("用户:"
										+ userWillBindingBankCard.get(0).getUser()
										.getId() + "绑定"
										+ userWillBindingBankCard.get(0).getCardNo() + "成功!"));
							}else {
								log.debug("********************************************bind card fail***********************************************");
							}

						} else {
							log.debug(bankCardId + "已经被绑定！！！！");
						}
					}
				}

				try {
					response.setCharacterEncoding("utf-8");
					// 返回数据
					String responseData = getResponseData(order_id, ret_code);
					response.getWriter().print(responseData);
					FacesUtil.getCurrentInstance().responseComplete();
				} catch (IOException e) {
					log.error(e.getLocalizedMessage(), e);
				}
			}
		} catch (VerifyException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}
