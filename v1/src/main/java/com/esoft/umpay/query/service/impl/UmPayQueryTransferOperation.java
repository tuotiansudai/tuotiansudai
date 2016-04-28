package com.esoft.umpay.query.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;


/**
 * Description : 交易查询 
 * @author zt
 * @data 2015-3-12下午2:34:17
 */
@Service("umPayQueryTransferOperation")
public class UmPayQueryTransferOperation {
	
	@Resource
	HibernateTemplate ht;
	
	@Logger
	static Log log;
	
	@SuppressWarnings("unchecked")
	public String  handleSendedOperation(String orderId,String orderType , Date date) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Map<String,String> resData = null;
		try {
			Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.TRANSFER_SEARCH);
		    sendMap.put("order_id",orderId);
		    sendMap.put("mer_date",DateUtil.DateToString(date,DateStyle.YYYYMMDD));
		    sendMap.put("busi_type",orderType);
		    ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
		    log.info("查询umpay交易数据:"+reqData.toString());
			//创建一个get直连
			String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
			log.info("返回数据:"+responseBodyAsString);
			//获取返回参数
			resData = Plat2Mer_v40.getResData(responseBodyAsString);
			log.info("解析数据:"+resData.toString());
			String ret_code = resData.get("ret_code");
			if("0000".equals(ret_code)){
				request.setAttribute("order_id", resData.get("order_id"));//唯一订单号
				request.setAttribute("mer_date", resData.get("mer_date"));///商户订单日期
				request.setAttribute("mer_check_date", resData.get("mer_check_date"));//对账日期
				request.setAttribute("trade_no", resData.get("trade_no"));//交易平台流水号
				request.setAttribute("busi_type", resData.get("busi_type"));//业务类型
				request.setAttribute("amount", Double.parseDouble(resData.get("amount"))/100);//交易金额
				request.setAttribute("orig_amt", Double.parseDouble(resData.get("orig_amt"))/100);//原交易金额
				request.setAttribute("com_amt", Double.parseDouble(resData.get("com_amt"))/100);//手续费
				request.setAttribute("tran_state", resData.get("tran_state"));//交易状态
			}
		} catch (ReqDataException e) {
			request.setAttribute("result", "加密失败");
		} catch (RetDataException e) {
			request.setAttribute("result", "解密失败");
		}
		request.setAttribute("ret_code", resData.get("ret_code"));
		request.setAttribute("ret_msg", resData.get("ret_msg"));
		
		return resData.get("tran_state");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取交易类型
	 * @param busiType
	 * @return
	public Map<String,String> getBusiTypeAndTranState(String busiType , String TranState){
		Map<String,String> map = new HashMap<String,String>();
		if("01".equals(busiType)){
			busiType = "充值";
			if("2".equals(TranState)){
				TranState = "成功";
			}else if("3".equals(TranState)){
				TranState = "成功";
			}else if("4".equals(TranState)){
				TranState = "不明";
			}else if("6".equals(TranState)){
				TranState = "其他";
			}
		}else if("02".equals(busiType)){
			busiType = "提现";
			if("0".equals(TranState)){
				TranState = "初始";
			}else if("1".equals(TranState)){
				TranState = "受理中";
			}else if("2".equals(TranState)){
				TranState = "成功";
			}else if("3".equals(TranState)){
				TranState = "失败";
			}else if("4".equals(TranState)){
				TranState = "不明";
			}else if("5".equals(TranState)){
				TranState = "交易关闭(提现)";
			}else if("6".equals(TranState)){
				TranState = "其他";
			}else if("12".equals(TranState)){
				TranState = "以冻结";
			}else if("13".equals(TranState)){
				TranState = "待解冻";
			}else if("14".equals(TranState)){
				TranState = "待解冻";
			}else if("13".equals(TranState)){
				TranState = "待解冻";
			}
		}else if("03".equals(busiType)){
			busiType = "标的转账";
		}else if("04".equals(busiType)){
			busiType = "转账";
		}
		map.put("busiType", busiType);
		return map;
	}
	 */
	
}
