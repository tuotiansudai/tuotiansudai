package com.esoft.umpay.loan.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.UmPayConstants.UpdateProjectStatus;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : 修改第三方标的状态
 * @author zt
 * @data 2015-3-24上午9:23:49
 */
@Service("umpayLoanStatusService")
public class UmPayLoanStatusService{

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	HibernateTemplate ht;
	
	
	@Logger
	Log log;

	/**
	 * 创建标的
	 * @param loan
	 * @return
	 * @throws ReqDataException
	 * @throws RetDataException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public Map< String , String > createLoan(Loan loan) throws ReqDataException, RetDataException{
		if(null==loan.getExpectTime()) loan.setExpectTime(new Date());
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.MER_BIND_PROJECT);
		sendMap.put("project_id",loan.getId());
		sendMap.put("project_name",loan.getId());
		sendMap.put("project_amount",String.valueOf(currentNumberFormat.format(loan.getLoanMoney().doubleValue() * 100)));
		sendMap.put("project_expire_date",DateUtil.DateToString(loan.getExpectTime(), DateStyle.YYYYMMDD));
		sendMap.put("loan_user_id",getTrusteeshipAccount(loan.getUser().getId()).getId());
    	ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
		log.debug("创建标的-发送数据: "+reqData);
		TrusteeshipOperation to = createTrusteeshipOperation(loan.getId(), reqData.getUrl(), loan.getId(), UmPayConstants.OperationType.MER_BIND_PROJECT,reqData.getPlain());
		String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
		Map<String,String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
		log.debug("创建标的-接收数据: "+resData.toString());
		to.setResponseData(resData.toString());
		to.setResponseTime(new Date());
		if("0000".equals(resData.get("ret_code"))){
			//获取标的在第三方状态
			String project_state = resData.get("project_state");
			//开标成功
			if("92".equals(project_state)){
				to.setStatus(TrusteeshipConstants.Status.PASSED);
			}else{
				throw new UmPayOperationException("建标失败,第三方状态:"+project_state);
			}
		}else{
			to.setStatus(TrusteeshipConstants.Status.REFUSED);
			throw new UmPayOperationException("建标失败,原因:"+resData.get("ret_msg"));
		}
		return resData;
	}
	
	
	
	/**
	 * 更新标的状态
	 * @param loan
	 * @param loanStatus 要更新成的状态
	 * @param flag		 其实这里不必要这个flag可以用status控制
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public Map<String  ,String > updateLoanStatusOperation(Loan loan , String loanStatus , boolean flag){
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.MER_UPDATE_PROJECT);
		sendMap.put("project_id",loan.getId());
	    sendMap.put("change_type",UmPayConstants.UpdateProjectStatus.CHANGE_TYPE_UPDATE_PRIJECT);
	    sendMap.put("project_state",loanStatus);
	    if(flag){
	    	sendMap.put("project_name",loan.getId());
		    sendMap.put("project_amount",currentNumberFormat.format(loan.getMoney()*100));
		    sendMap.put("project_expire_date",DateUtil.DateToString(loan.getExpectTime(), DateStyle.YYYYMMDD));
		    sendMap.put("loan_user_id",getTrusteeshipAccount(loan.getUser().getId()).getId());
	    }
		Map<String , String> resData = new HashMap<>();
		try {
			ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
			log.debug("更新标的状态为:"+loanStatus+"->发送:"+reqData.toString());
			TrusteeshipOperation to = createTrusteeshipOperation(loan.getId(), reqData.getUrl(), loan.getId(), loanStatus, reqData.getPlain());
			String responseBody = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());

			resData = Plat2Mer_v40.getResData(responseBody);
			log.debug("更新标的状态为:" + loanStatus + "->接收:" + resData.toString());
			to.setResponseData(resData.toString());
			to.setResponseTime(new Date());
			if("0000".equals(resData.get("ret_code"))){
				to.setStatus(TrusteeshipConstants.Status.PASSED);
			}else{
				to.setStatus(TrusteeshipConstants.Status.REFUSED);
			}
			trusteeshipOperationBO.save(to);
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new UmPayOperationException("更新标的状态失败:参数加密失败!");
		} catch (RetDataException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new UmPayOperationException("更新标的状态失败:参数解密失败!");
		}
		return resData;
	}
	
	
	
	
	
	/**
	 * 获取账号
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipAccount getTrusteeshipAccount(String userId){
		TrusteeshipAccount ta = null;
		List<TrusteeshipAccount> taList = ht.find( "from TrusteeshipAccount t where t.user.id=?",new String[]{userId});
		if (null != taList && taList.size() > 0) {
			ta = taList.get(0);
		}
		return ta;
	}
	
	
	

	/**
	 * 保存发送操作记录
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
		to.setTrusteeship(UmPayConstants.OperationType.UMPAY);
		to.setRequestTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.SENDED);
		trusteeshipOperationBO.save(to);
		return to;
	}
	
	
	
	/**
	 * 如果失败,直接调用这个方法更改第三方标的的类型,纠正过来即可
	 * @param args
	 * @throws ReqDataException
	 * @throws RetDataException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ReqDataException, RetDataException {
		Loan loan = new Loan();
		loan.setId("20150311000026");
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.MER_UPDATE_PROJECT);
		sendMap.put("project_id",loan.getId());
	    sendMap.put("change_type",UmPayConstants.UpdateProjectStatus.CHANGE_TYPE_UPDATE_PRIJECT);
	    sendMap.put("project_state",UpdateProjectStatus.PROJECT_STATE_RAISING);
	    ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
	    System.out.println("发送参数:"+reqData.toString());
	    String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
		Map<String,String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
		System.out.println("接受参数:"+resData.toString());
		System.out.println(resData.toString());
		System.out.println(resData.get("ret_msg"));
	}
}
