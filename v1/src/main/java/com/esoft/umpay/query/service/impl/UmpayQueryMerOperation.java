package com.esoft.umpay.query.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

@Service("umpayQueryMerOperation")
public class UmpayQueryMerOperation {
	
	@Resource
	HibernateTemplate ht;

	@Logger
	static Log log;
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void handleSendedOperation(String orderId) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Map<String,String> resData = null;
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PTP_MER_QUERY);
		sendMap.put("query_mer_id",orderId);
	    sendMap.put("account_type","01");
	    ReqData reqData;
	    try {
			reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
		    String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
			resData = Plat2Mer_v40.getResData(responseBodyAsString);
			String ret_code = resData.get("ret_code");
			if("0000".equals(ret_code)){
				request.setAttribute("query_mer_id", resData.get("query_mer_id"));
				request.setAttribute("balance",  Double.parseDouble(resData.get("balance"))/100);
				request.setAttribute("account_type", resData.get("account_type"));
				request.setAttribute("account_state", resData.get("account_state"));
			}else{
				
			}
			request.setAttribute("ret_code", ret_code);
			request.setAttribute("ret_msg", resData.get("ret_msg"));
		} catch (RetDataException e) {
			e.printStackTrace();
		} catch (ReqDataException e) {
			e.printStackTrace();
		}
		 
	}
	
	
}
