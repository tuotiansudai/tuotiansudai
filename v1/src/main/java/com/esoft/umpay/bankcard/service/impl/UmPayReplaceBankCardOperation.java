package com.esoft.umpay.bankcard.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
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
import java.io.IOException;
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
	private HibernateTemplate ht;
	
	@Override
	@SuppressWarnings({ "unchecked", "null" })
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(BankCard bankCard,
			FacesContext facesContext) throws IOException {
		TrusteeshipAccount trusteeshipAccount = getTrusteeshipAccount(bankCard.getUser()
				.getId());
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.MER_REPLACE_CARD);
		sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
				+ UmPayConstants.OperationType.MER_REPLACE_CARD);
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.MER_REPLACE_CARD);
		String order_id = System.currentTimeMillis() + bankCard.getCardNo();
		sendMap.put("order_id", order_id);
		sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		sendMap.put("user_id", trusteeshipAccount.getId());
		sendMap.put("card_id", bankCard.getCardNo());
		sendMap.put("account_name", bankCard.getUser().getRealname());
		sendMap.put("identity_type", "IDENTITY_CARD");
		sendMap.put("identity_code", bankCard.getUser().getIdCard());
		TrusteeshipOperation to = null;
		try{
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			log.debug("换卡发送数据:" + reqData);
			to = createTrusteeshipOperation(order_id, reqData.getUrl(),
					bankCard.getUser().getId(),
					UmPayConstants.OperationType.MER_REPLACE_CARD,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		}catch (ReqDataException e) {
			e.getStackTrace();
		}
		return to;
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
					UmPayConstants.OperationType.MER_REPLACE_CARD, order_id, trusteeshipAccount
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
			if (null != paramMap) {
				String ret_code = paramMap.get("ret_code");
				String order_id = paramMap.get("order_id");
				if ("0000".equals(ret_code)) {
					TrusteeshipAccount trusteeshipAccount = ht.get(TrusteeshipAccount.class,
							paramMap.get("user_id"));
					User user = trusteeshipAccount.getUser();
					ht.evict(user);
					String bankCardId = order_id.substring(13,
							order_id.length());
					String hql = "from BankCard where user.id =? and status =? and cardNo =?";
					List<BankCard> userWillBindingBankCard = ht
							.find(hql, new String[] { user.getId(), "uncheck",
									bankCardId });
					if (null != userWillBindingBankCard) {
						BankCard bankCard = userWillBindingBankCard.get(0);
						bankCard.setStatus("passed");
						ht.update(bankCard);
						log.debug(("用户:"
								+ userWillBindingBankCard.get(0).getUser()
								.getId() + "换卡"
								+ userWillBindingBankCard.get(0).getCardNo() + "成功!"));
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
