package com.esoft.umpay.query.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.umpay.query.model.TransferDateil;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;


@Service("umpayQueryTransfeqOperation")
public class UmpayQueryTransfeqOperation {

	
	@Resource
	HibernateTemplate ht;
	
	@Logger
	static Log log;
	
	
	
	@SuppressWarnings("unchecked")
	public List<TransferDateil> handleSendedOperation(Date startDate , Date endDate , String accountType ,String userId ,String pageNum){
		if(null==pageNum||"".equals(pageNum)){
			pageNum = "1";
		}
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		List<TransferDateil> detailList = new ArrayList<TransferDateil>();
		Map<String,String> resData = null;
		try {
			if(null!=accountType&&!"".equals(accountType)){
				Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.TRANSEQ_SEARCH);
			    //sendMap.put("account_id",""); 
				if("01".equals(accountType)){
						List<TrusteeshipAccount> taList = ht.find( "from TrusteeshipAccount t where t.user.id=?",new String[]{userId});
						if(null!=taList&&taList.size()>0){
							sendMap.put("partic_user_id",taList.get(0).getId());
						}else{
							return detailList;
						}
				}else{
					sendMap.put("partic_user_id",userId);
				}
			    sendMap.put("account_type",accountType);	//操作类型(个人01,商户02,或者标的03)
			    sendMap.put("start_date",DateUtil.DateToString(startDate, DateStyle.YYYYMMDD));
			    sendMap.put("end_date",DateUtil.DateToString(endDate, DateStyle.YYYYMMDD));
			    sendMap.put("page_num",pageNum);	//页数
			    ReqData	reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
				String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
				resData = Plat2Mer_v40.getResData(responseBodyAsString);
				log.info(responseBodyAsString);
				String ret_code = resData.get("ret_code");
				if("0000".equals(ret_code)){
					String trans_detail = resData.get("trans_detail");		//交易记录
					if("0".equals(resData.get("total_num"))){				//总记录数
						return detailList;
					}
					System.out.println(trans_detail);
					//切分二级域信息
					String[] detail = trans_detail.split("\\|");		
					for (String dt : detail) {
						JSONObject jo = JSONObject.fromObject("{"+dt+"}");
						TransferDateil TransferDateil = new TransferDateil();
						TransferDateil.setDate(jo.getString("acc_check_date")); 		//交易记账日期
						TransferDateil.setAmount(jo.getDouble("amount")/100);			//交易金额
						TransferDateil.setDcMark(jo.getString("dc_mark"));				//借贷方向
						TransferDateil.setTransState(jo.getString("trans_state"));		//交易状态
						TransferDateil.setTransType(jo.getString("trans_type"));		//交易代码
						TransferDateil.setBalance(jo.getDouble("balance")/100);			//账户余额
						TransferDateil.setOrderId(jo.getString("order_id"));			
						TransferDateil.setTranTime(jo.getString("trans_time"));
						TransferDateil.setAmtType(jo.getString("amt_type"));
						TransferDateil.setOrderDate(jo.getString("order_date"));
						detailList.add(TransferDateil);
					}
					request.setAttribute("pageTotal", trans_detail);
				}
				request.setAttribute("ret_code", resData.get("ret_code"));
				request.setAttribute("ret_msg", resData.get("ret_msg"));
				
			}
		} catch (ReqDataException e) {
			log.debug(e);
		} catch (RetDataException e) {
			log.debug(e);
		}
		return detailList;
	}
}


