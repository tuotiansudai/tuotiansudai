package com.esoft.umpay.bankcard.service.impl;

import com.cfca.util.pki.ocsp.Req;
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
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.logging.Log;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 更换银行卡
 * @author zt
 */
@Service("umPayReplaceBankCardOperation")
public class UmPayReplaceBankCardOperation  extends UmPayOperationServiceAbs<BankCard>{

	@Logger
	Log log;

	@Resource
	private TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	private BankCardService bankCardService;

	@Resource
	private RechargeService rechargeService;

	@Resource
	private HibernateTemplate ht;

	@Resource
	private SystemBillService systemBillService;

	@Override
	@SuppressWarnings({ "unchecked", "null" })
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(BankCard bankCard,
			FacesContext facesContext) throws IOException {
		TrusteeshipOperation to = null;
		try{
			String orderId = generateReplaceCardOrderId(bankCard);
			ReqData reqData = buildReqData(bankCard, orderId, false);
			log.debug("换卡发送数据:" + reqData);
			to = createTrusteeshipOperation(orderId, reqData.getUrl(),
					bankCard.getUser().getId(),
					UmPayConstants.OperationType.PTP_MER_REPLACE_CARD,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		}catch (UmPayOperationException e) {
			log.error("联动优势处理失败",e);
			e.getStackTrace();
		}
		return to;
	}

	@Transactional(rollbackFor = Exception.class)
	public ReqData createOperation_mobile(BankCard bankCard) throws ReqDataException{
		// 与桌面端不同，桌面端在UmPayBankCardHome里，保存了此记录，移动端将保存逻辑放到这里了。
		ht.save(bankCard);
        String orderId = generateReplaceCardOrderId(bankCard);
        ReqData reqData = buildReqData(bankCard, orderId, true);
        log.debug("换卡发送数据:" + reqData);
        createTrusteeshipOperation(orderId, reqData.getUrl(),
                bankCard.getUser().getId(),
                UmPayConstants.OperationType.PTP_MER_REPLACE_CARD,
                GsonUtil.fromMap2Json(reqData.getField()));
        return reqData;
	}

	private String generateReplaceCardOrderId(BankCard bankCard) {
		return System.currentTimeMillis() + bankCard.getCardNo();
	}

	private ReqData buildReqData(BankCard bankCard, String orderId, boolean isMobileRequest){
		TrusteeshipAccount trusteeshipAccount = getTrusteeshipAccount(bankCard.getUser()
				.getId());
		if(trusteeshipAccount==null){
			throw new UmPayOperationException("该用户没有实名认证");
		}

		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PTP_MER_REPLACE_CARD);

		if(isMobileRequest) {
			sendMap.put("ret_url", UmPayConstants.ResponseMobUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.PTP_MER_REPLACE_CARD);
			//配置此项，表示使用H5页面
			sendMap.put("sourceV", UmPayConstants.SourceViewType.SOURCE_V);
		}else{
			sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
					+ UmPayConstants.OperationType.PTP_MER_REPLACE_CARD);
		}
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL

						+ UmPayConstants.OperationType.PTP_MER_REPLACE_CARD);
		sendMap.put("order_id", orderId);
		sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		sendMap.put("user_id", trusteeshipAccount.getId());
		sendMap.put("card_id", bankCard.getCardNo());
		sendMap.put("account_name", bankCard.getUser().getRealname());
		sendMap.put("identity_type", "IDENTITY_CARD");
		sendMap.put("identity_code", bankCard.getUser().getIdCard());

		try {
			return Mer2Plat_v40.makeReqDataByPost(sendMap);
		} catch (ReqDataException e) {
			throw new UmPayOperationException("加密失败",e);

		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		Map<String, String> paramMap = null;
		try {
			paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.debug("换卡-前台-通知:" + paramMap.toString());
			TrusteeshipAccount trusteeshipAccount = ht.get(TrusteeshipAccount.class,
					paramMap.get("user_id"));
			String order_id = paramMap.get("order_id");
			TrusteeshipOperation to = trusteeshipOperationBO.get(
					UmPayConstants.OperationType.PTP_MER_REPLACE_CARD, order_id, trusteeshipAccount
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
		} catch (VerifyException e){
			e.printStackTrace();
			throw new TrusteeshipReturnException("验签失败");
		} catch (DuplicateKeyException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new TrusteeshipReturnException("duplication");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("换卡-后台-通知:" + paramMap.toString());
			if (null != paramMap && paramMap.get("service").equals(UmPayConstants.OperationType.MER_BIND_CARD_NOTIFY)) {
				String ret_code = paramMap.get("ret_code");
				String order_id = paramMap.get("order_id");
				if ("0000".equals(ret_code)) {
					TrusteeshipAccount trusteeshipAccount = ht.get(TrusteeshipAccount.class,
							paramMap.get("user_id"));
					User user = trusteeshipAccount.getUser();
					ht.evict(user);
					String bankCardId = order_id.substring(13,
							order_id.length());
					if (!this.bankCardService.isCardNoBinding(bankCardId)) {
						String hql = "from BankCard where user.id =? and status =? and cardNo =? and isOpenFastPayment=false";
						String hqlPassed = "from BankCard where user.id =? and status =? and isOpenFastPayment=false";
						List<BankCard> userAlreadyBindingBankCard = ht
								.find(hqlPassed, new String[]{user.getId(), "passed"});
						List<BankCard> userWillBindingBankCard = ht
								.find(hql, new String[]{user.getId(), "uncheck",
										bankCardId});
						if (null != userWillBindingBankCard) {
							for (BankCard bankCard : userWillBindingBankCard){
								bankCard.setStatus("passed");
								bankCard.setBankNo(paramMap.get("gate_id"));
								bankCard.setBank(this.rechargeService.getBankNameByNo(paramMap.get("gate_id")));
								ht.update(bankCard);
							}
							if (paramMap.get("gate_id").equals("CMB")){
								String detailTemplate = "用户{0}更换{1}银行卡";
								try {
									this.systemBillService.transferOut(0.01,"replace_card", MessageFormat.format(detailTemplate,
											userWillBindingBankCard.get(0).getUser().getId(),
											this.rechargeService.getBankNameByNo(paramMap.get("gate_id"))));
								} catch (InsufficientBalance insufficientBalance) {
									log.error(insufficientBalance);
								}
							}
							if (userAlreadyBindingBankCard != null) {
								for (BankCard bankCardPassed : userAlreadyBindingBankCard) {
									bankCardPassed.setStatus("remove");
									ht.update(bankCardPassed);
								}
							}
							log.debug(("用户:"
									+ userWillBindingBankCard.get(0).getUser()
									.getId() + "换卡"
									+ userWillBindingBankCard.get(0).getCardNo() + "成功!"));
						}
					} else {
						log.debug(bankCardId+"已经被绑定！！！！");
					}
				}
				try {
					response.setCharacterEncoding("utf-8");
					// 返回数据
					String responseData = getResponseData(order_id, ret_code);
					response.getWriter().print(responseData);
					FacesUtil.getCurrentInstance().responseComplete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (VerifyException e) {
			e.printStackTrace();
		}
	}

}
