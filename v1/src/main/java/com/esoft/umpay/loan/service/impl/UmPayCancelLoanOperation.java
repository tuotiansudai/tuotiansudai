package com.esoft.umpay.loan.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.query.service.impl.UmPayQueryTransferOperation;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;


/**
 * Description : 流标操作 
 * @author zt
 * @data 2015-3-13下午3:43:20
 */
@Service("umPayCancelLoanOperation")
public class UmPayCancelLoanOperation extends UmPayOperationServiceAbs<Loan>{

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	
	@Resource
	UmPayQueryTransferOperation umPayQueryTransferOperation;
	
	
	@Resource
	UmPayLoanStatusService umPayLoanStatusService;
	
	@Logger
	Log log;

	@Resource
	LoanService loanService;
	
	@Resource
	UserBillBO ubs;
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,FacesContext facesContext) throws IOException {
		loan = ht.get(Loan.class, loan.getId());
		ht.lock(loan, LockMode.UPGRADE);
		loan.setStatus(LoanConstants.LoanStatus.CANCEL);
		loan.setCancelTime(new Date());
		List<Invest> invests = loan.getInvests();
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		try {
			log.info("开始流标编号:"+loan.getId());
			for (Invest inv :invests) {
				if (!inv.getStatus().equalsIgnoreCase(InvestConstants.InvestStatus.BID_SUCCESS)) {
					continue;
				}
				//查询这个这笔投资是否成功,成功则跳过
				//String transferStatus = umPayQueryTransferOperation.handleSendedOperation("02"+inv.getId(),"03",inv.getTime());
				//if(!"2".equals(transferStatus)){	//流标失败了
					//组装参数
					Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
				    sendMap.put("ret_url",UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL+UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_FAIL_BY_MANAGER);
				    sendMap.put("notify_url",UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL+UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_FAIL_BY_MANAGER);
				    //订单编号(01+investId)
				    String order_id = "01"+inv.getId();
				    sendMap.put("order_id",order_id);
				    sendMap.put("mer_date",DateUtil.DateToString(new Date(),DateStyle.YYYYMMDD));
				    sendMap.put("project_id",loan.getId());
				    sendMap.put("serv_type",UmPayConstants.TransferProjectStatus.SERV_TYPE_FAIL_BY_MANAGER);	//流标
				    sendMap.put("trans_action",UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);			//标的转出
				    sendMap.put("partic_type",UmPayConstants.TransferProjectStatus.PARTIC_TYPE_INVESTOR);		//投资者
				    sendMap.put("partic_acc_type",UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);	//个人
				    //联动用户ID,这里要根据invest来取  要的是每个投资人的id而不是借款人的,这里是针对投资人不是借款人
				    sendMap.put("partic_user_id",getTrusteeshipAccount(inv.getUser().getId()).getId());
				    sendMap.put("amount",currentNumberFormat.format(inv.getInvestMoney()*100));	//单位:分
					ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
					log.debug("流标-用户:"+inv.getUser().getId()+"-订单ID:"+"01"+inv.getId()+"-发送数据: "+reqData);
					TrusteeshipOperation to = createTrusteeshipOperation(order_id, reqData.getUrl(), inv.getId(), UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_FAIL_BY_MANAGER, reqData.getPlain());
					String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
					log.info("流标返回数据:"+responseBodyAsString);
					Map<String,String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
					log.debug("流标返款-用户:"+inv.getUser().getId()+"-接收数据: "+resData);
					to.setResponseTime(new Date());
					to.setResponseData(resData.toString());
					if("0000".equals(resData.get("ret_code"))){
						//操作流标,解冻投资金额
						ubs.unfreezeMoney(inv.getUser().getId(), inv.getMoney(), OperatorInfo.CANCEL_LOAN, "借款" + loan.getId()+ "流标，解冻投资金额"+inv.getMoney());
						//更改投资状态
						inv.setStatus(InvestConstants.InvestStatus.CANCEL);
						ht.update(inv);
						to.setStatus(TrusteeshipConstants.Status.PASSED);
					}else{
						to.setStatus(TrusteeshipConstants.Status.REFUSED);
						//执行失败是否抛出异常阻止继续流标操作
						//throw new UmPayOperationException("流标失败:给用户"+inv.getUser().getId()+"放款失败,请联系第三方操作");
					}
					ht.update(to);
			}
			//将第三方状态修改为结束,不知道可以可以跨状态所以调用了几次
			umPayLoanStatusService.updateLoanStatusOperation(loan, UmPayConstants.UpdateProjectStatus.PROJECT_STATE_FINISH,false );
		} catch (ReqDataException e) {
			throw new UmPayOperationException("流标失败:加密失败!");
		} catch (RetDataException e) {
			throw new UmPayOperationException("流标失败:解密失败!");
		} catch (InsufficientBalance e) {
			throw new UmPayOperationException("流标失败:解冻用户余额不足!");
		}
		
		//改变借款状态,上面如果有失败的怎么办?还要不要继续操作流标
		try {
			ubs.unfreezeMoney(loan.getUser().getId(), loan.getDeposit(),OperatorInfo.CANCEL_LOAN
					,"借款" + loan.getId() + "流标，解冻保证金"+loan.getDeposit());
		} catch (InsufficientBalance e) {
			throw new UmPayOperationException("流标失败:解冻借款保障金,余额不足!");
		}
		return null;
	}

	@Override
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		
	}

	/**
	 * 流标有S2S通知 
	 */
	@Override
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		
		
	}

}
