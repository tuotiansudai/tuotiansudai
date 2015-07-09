package com.esoft.umpay.bankcard.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.esoft.jdp2p.user.service.RechargeService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;

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
	@Logger
	Log log;

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(BankCard bankCard,
			FacesContext facesContext) throws IOException {
		TrusteeshipAccount ta = getTrusteeshipAccount(bankCard.getUser()
				.getId());
		// 判断是否是换卡操作
		User us = bankCard.getUser();
		String hql = "from BankCard where user.id =? and status =?";
		List<BankCard> userWillBindingBankCard = ht.find(hql, new String[] { us.getId(), "delete_for_replace" });
		Map<String, String> sendMap;
		if (null == userWillBindingBankCard && userWillBindingBankCard.size() > 0) {
			sendMap = UmPaySignUtil.getSendMapDate("mer_replace_card");
		} else {
			sendMap = UmPaySignUtil
					.getSendMapDate(UmPayConstants.OperationType.MER_BIND_CARD);
		}
		// 同步地址
		sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
				+ UmPayConstants.OperationType.MER_BIND_CARD);
		// 后台地址
		sendMap.put("notify_url",
				UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
						+ UmPayConstants.OperationType.MER_BIND_CARD);
		// 因为返回通知的时候不知道是绑定什么卡,哪张卡,这里用绑卡的ID加上时间戳,保证不重复情况加回调的时候去掉时间戳的结尾
		String order_id = System.currentTimeMillis() + bankCard.getCardNo();
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
		sendMap.put("is_open_fastPayment", "0");
		TrusteeshipOperation to = null;
		try {
			// 加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			log.debug("绑卡发送数据:" + reqData);
			// 保存操作记录
			to = createTrusteeshipOperation(order_id, reqData.getUrl(),
					bankCard.getUser().getId(),
					UmPayConstants.OperationType.MER_BIND_CARD,
					GsonUtil.fromMap2Json(reqData.getField()));
			// 发送请求
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			e.getStackTrace();
		}
		return to;
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
					UmPayConstants.OperationType.MER_BIND_CARD, order_id, ta
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
			e.printStackTrace();
			throw new TrusteeshipReturnException("验签失败");
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
			if (null != paramMap) {
				String ret_code = paramMap.get("ret_code");
				String order_id = paramMap.get("order_id");
				if ("0000".equals(ret_code)) {
					TrusteeshipAccount ta = ht.get(TrusteeshipAccount.class,
							paramMap.get("user_id"));
					User user = ta.getUser();
					ht.evict(user);
					if(StringUtils.isNotEmpty(order_id)){
						// 拿到用户未绑定的卡ID(截取13位到最后,因为银行卡长度是不固定的)
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
									.getId() + "绑定"
									+ userWillBindingBankCard.get(0).getCardNo() + "成功!"));
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
					e.printStackTrace();
				}
			}
		} catch (VerifyException e) {
			e.printStackTrace();
		}
	}
}
