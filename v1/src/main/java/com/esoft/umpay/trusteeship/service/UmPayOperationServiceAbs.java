package com.esoft.umpay.trusteeship.service;

import com.esoft.core.util.*;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationServiceAbs;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UmPayOperationServiceAbs<T> extends
		TrusteeshipOperationServiceAbs<T> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	/**
	 * 保存发送操作记录
	 * 
	 * @param markId
	 * @param requestUrl
	 * @param operator
	 * @param type
	 * @param content
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createTrusteeshipOperation(String markId,
			String requestUrl, String operator, String type, String content) {
		TrusteeshipOperation to = new TrusteeshipOperation();
		to.setId(IdGenerator.randomUUID());
		to.setMarkId(markId);
		to.setOperator(operator);
		to.setRequestUrl(requestUrl);
		to.setCharset("utf-8");
		to.setRequestData(content);
		to.setType(type);
		to.setTrusteeship("umpay");
		to.setRequestTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.SENDED);
		trusteeshipOperationBO.save(to);
		return to;
	}

	/**
	 * 发送操作
	 * 
	 * @param to
	 * @param facesContext
	 * @throws IOException
	 */
	public void sendOperation(TrusteeshipOperation to, FacesContext facesContext)
			throws IOException {
		String form = HtmlElementUtil.createAutoSubmitForm(
				GsonUtil.fromJson2Map(to.getRequestData()), to.getRequestUrl(),
				to.getCharset());
		super.sendOperation(form, to.getCharset(), facesContext);
	}

	/**
	 * 通知对方S2S
	 * 
	 * @param order_id
	 * @param ret_code
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getResponseData(String order_id, String ret_code) {
		// 封装请求参数列表
		Map map = new HashMap();
		map.put("mer_date",
				DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
		map.put("mer_id", UmPayConstants.Config.MER_CODE);
		map.put("order_id", order_id);
		map.put("ret_code", ret_code);
		map.put("sign_type", UmPayConstants.Config.SIGN_TYPE);
		map.put("version", UmPayConstants.Config.VERSION);
		String merNotifyResData = Mer2Plat_v40.merNotifyResData(map);
		// 构建HTML元素
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<META NAME=\"MobilePayPlatform\" ");
		buffer.append("CONTENT=\"");
		buffer.append(merNotifyResData);
		buffer.append("\"/>");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	/**
	 * 获取联动优势给商户开通账户,如果为空则返回null
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipAccount getTrusteeshipAccount(String userId) {
		TrusteeshipAccount ta = null;
		List<TrusteeshipAccount> taList = ht.find(
				"from TrusteeshipAccount t where t.user.id=?",
				new String[] { userId.trim() });
		if (null != taList && taList.size() > 0) {
			ta = taList.get(0);
		}
		return ta;
	}

	/*
	 * public static void main(String[] args) { String responseData =
	 * getResponseData("1111","11111"); System.out.println(responseData); }
	 */
}
