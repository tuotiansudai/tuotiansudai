package com.esoft.umpay.query.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;


/**
 * Description :用户状态查询 
 * @author zt
 * @data 2015-3-12下午2:33:30
 */
@Service("umPayQueryUserOperation")
public class UmPayQueryUserOperation{

	@Resource
	HibernateTemplate ht;
	
	@Logger
	static Log log;
	
	
	/**
	 * 用户查询
	 * @param userId
	 */
	@SuppressWarnings("unchecked")
	public void handleSendedOperation(String userId) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		TrusteeshipAccount ta = null;
		List<TrusteeshipAccount> taList = ht.find( "from TrusteeshipAccount t where t.user.id=?",new String[]{userId});
		if (null != taList && taList.size() > 0) {
			ta = taList.get(0);
			try {
		    	Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.USER_SEARCH);
			    sendMap.put("user_id",ta.getId());
			    sendMap.put("is_find_account","01");//是否查询余额
			    sendMap.put("is_select_agreement","1");//是否查询授权协议
				ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
				log.info("查询umpay用户数据:"+reqData.toString());
				//创建一个get直连
				String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
				log.info("返回数据:"+responseBodyAsString);
				//获取返回参数
				Map<String,String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
				log.info("解析数据:"+resData.toString());
				String ret_code = resData.get("ret_code");//返回码
				if("0000".equals(ret_code)){
					String account_status = resData.get("account_state");
					if("1".equals(account_status)){//账户状态1-正常 2-挂失 3-冻结 4-锁定 9-销户
						account_status = "正常";
					}else if("2".equals(account_status)){
						account_status = "挂失";
					}else if("3".equals(account_status)){
						account_status = "冻结";
					}else if("4".equals(account_status)){
						account_status = "锁定";
					}else if("9".equals(account_status)){
						account_status = "销户";
					}
					String identity_type = resData.get("identity_type");
					if("1".equals(identity_type)){
						identity_type = "身份证";
					}
					request.setAttribute("balance", Double.parseDouble(resData.get("balance"))/100);//余额
					request.setAttribute("ret_code", ret_code);
					request.setAttribute("identity_type",identity_type);//证件类型
					request.setAttribute("ret_msg", resData.get("ret_msg"));//返回信息
					request.setAttribute("plat_user_id", resData.get("plat_user_id"));//资金账户托管平台用户号
					request.setAttribute("account_id", resData.get("account_id"));//资金账户托管平台的账户号
					request.setAttribute("cust_name", resData.get("cust_name"));//姓名
					request.setAttribute("identity_code", resData.get("identity_code"));//证件号
					request.setAttribute("contact_mobile", resData.get("contact_mobile"));//手机号
					request.setAttribute("mail_addr", resData.get("mail_addr"));//邮箱
					request.setAttribute("account_state",account_status );//账户状态
					request.setAttribute("card_id", resData.get("card_id"));//提现银行卡
					request.setAttribute("gate_id", resData.get("gate_id"));//发卡银行编号
					request.setAttribute("user_bind_agreement_list", resData.get("user_bind_agreement_list"));//用户签约约的协议列表信息
				}else{
					request.setAttribute("ret_code", "状态不合法:"+ret_code);
				}
			} catch (ReqDataException e) {
				e.printStackTrace();
				request.setAttribute("result", "加密失败");
			} catch (RetDataException e) {
				e.printStackTrace();
				request.setAttribute("result", "获取数据失败");
			}
		}else{
			request.setAttribute("result", "未找到该用户信息");
		}
	    
		
		
	}

}
