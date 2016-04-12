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

/**
 * Description :标的状态查询 
 * @author zt
 * @data 2015-3-12下午2:33:03
 */
@Service("umPayQueryLoanStatusOperation")
public class UmPayQueryLoanStatusOperation {
	
	
	@Resource
	HibernateTemplate ht;

	@Logger
	static Log log;
	@SuppressWarnings("unchecked")
	public void handleSendedOperation(String orderId) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Map<String,String> resData = null;
		try {
			Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_ACCOUNT_SEARC);
		    sendMap.put("project_id",orderId);
		    ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
			log.info("查询umpay标的数据:"+reqData.toString());
			//创建一个get直连
			String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
			log.info("返回数据:"+responseBodyAsString);
			//获取返回参数
			resData = Plat2Mer_v40.getResData(responseBodyAsString);
			log.info("解析数据:"+resData.toString());
			String ret_code = resData.get("ret_code");
			if("0000".equals(ret_code)){
				request.setAttribute("project_id", resData.get("project_id"));//商户端标的号
				request.setAttribute("project_account_id", resData.get("project_account_id"));//标的账户号
				request.setAttribute("project_account_state", getProjectAccountState(resData.get("project_account_state")));//标的账户状态
				request.setAttribute("project_state", getProjectState(resData.get("project_state")));//标的状态
				request.setAttribute("balance", Double.parseDouble(resData.get("balance"))/100);//金额
			}
		} catch (ReqDataException e) {
			e.printStackTrace();
			request.setAttribute("result", "加密失败");
		} catch (RetDataException e) {
			e.printStackTrace();
			request.setAttribute("result", "获取数据失败");
		}
		request.setAttribute("ret_code", resData.get("ret_code"));
		request.setAttribute("ret_msg", resData.get("ret_msg"));
	}
	
	
	
	
	
	
	
	
	
	
	
	public String getProjectAccountState(String projectAccountState){
		if("01".equals(projectAccountState)){
			projectAccountState = "正常";
		}else if("02".equals(projectAccountState)){
			projectAccountState = "冻结";
		}
		return projectAccountState;
	}
	
	public String getProjectState(String projectState){
		if("-1".equals(projectState)){
			projectState = "取消";
		}else if("90".equals(projectState)){
			projectState = "初始";
		}else if("91".equals(projectState)){
			projectState = "建标中";
		}else if("92".equals(projectState)){
			projectState = "建标成功";
		}else if("92".equals(projectState)){
			projectState = "建标失败";
		}else if("94".equals(projectState)){
			projectState = "标的锁定";
		}else if("0".equals(projectState)){
			projectState = "开标";
		}else if("1".equals(projectState)){
			projectState = "投标中";
		}else if("2".equals(projectState)){
			projectState = "还款中";
		}else if("3".equals(projectState)){
			projectState = "已还款";
		}else if("4".equals(projectState)){
			projectState = "结束";
		}else if("5".equals(projectState)){
			projectState = "满标";	//UMPAY不支持
		}else if("6".equals(projectState)){
			projectState = "流标";	//UMPAY不支持
		}
		return projectState;
	}
}
