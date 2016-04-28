package com.esoft.umpay.loan.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.exception.BorrowedMoneyTooLittle;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.LoanOutSuccessfulNotificationJob;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.logging.Log;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description : 放款操作 
 * @author zt
 * @data 2015-3-12下午5:39:59
 */
@Service("umPayLoaingOperation")
public class UmPayLoaingOperation extends UmPayOperationServiceAbs<Loan> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UmPayLoanMoneyService umPayLoanMoneyService;

	@Resource
	LoanService loanService;

	@Resource
	UmPayLoanStatusService umPayLoanStatusService;

	@Resource
	UmPayNormalRepayOperation umPayNormalRepayOperation;

	@Logger
	Log log;

	@Resource
	StdScheduler scheduler;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {
		loan = ht.get(Loan.class, loan.getId());
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
	    sendMap.put("ret_url",UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL+UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER);
	    sendMap.put("notify_url",UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL+UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER);
	    //订单ID(01+loanId),为了方便在后台查询
	    String order_id = "01"+loan.getId();
	    sendMap.put("order_id",order_id);
	    sendMap.put("mer_date",DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
	    sendMap.put("project_id",loan.getId());
	    sendMap.put("serv_type",UmPayConstants.TransferProjectStatus.SERV_TYPE_GIVE_MONEY_TO_BORROWER);
	    sendMap.put("trans_action",UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
	    sendMap.put("partic_type",UmPayConstants.TransferProjectStatus.PARTIC_TYPE_LOANER);
	    sendMap.put("partic_acc_type",UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
	    sendMap.put("partic_user_id",getTrusteeshipAccount(loan.getUser().getId()).getId());
	    //实际借到的钱
	    List<Invest> invests = Lists.newArrayList(Collections2.filter(loan.getInvests(), 
	    		new Predicate<Invest>() { 
	    			public boolean apply(Invest invest) {
						return invest.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS);
					}
				}));
	    Double allInvestMoney = 0D;
	    for(Invest inv : invests){
	    	allInvestMoney = ArithUtil.add(allInvestMoney, inv.getInvestMoney());
	    }
	    //放款时收取借款手续费 手续费
	    Double loanGuranteeFee = loan.getLoanGuranteeFee();
	    // 放款的金额  = 实际借到的钱 -  放款时收取借款手续费 手续费
	    Double LoaingMoney = allInvestMoney - loanGuranteeFee;
	    sendMap.put("amount",currentNumberFormat.format(LoaingMoney*100));
	    Map<String,String> resData;
	    try {
	    	//加密参数
			ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
			log.debug("放款-发送-数据: "+reqData);
			TrusteeshipOperation to = createTrusteeshipOperation(order_id, reqData.getUrl(), loan.getId(), UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER,reqData.getPlain());
			//创建一个get直连
			String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
			//获取返回参数
			resData = Plat2Mer_v40.getResData(responseBodyAsString);
			log.debug("放款-返回-数据: "+resData.toString());
			to.setResponseData(resData.toString());
			to.setResponseTime(new Date());
			if("0000".equals(resData.get("ret_code"))){
				to.setStatus(TrusteeshipConstants.Status.CANCEL);
				ht.update(to);
				if (loan.getStatus().equals(LoanConstants.LoanStatus.RECHECK)) {
						loanService.giveMoneyToBorrower(loan.getId());
				}
				//收取平台借款手续费
				/*LoanMoney2Mer("02"+loan.getId() , loanGuranteeFee , loan.getId());*/
				umPayLoanMoneyService.loanMoney2Mer("02"+loan.getId(), loanGuranteeFee, loan.getId());
				//更新标的状态为还款中,对于投标中的不能改变几个参数已经做了处理
				umPayLoanStatusService.updateLoanStatusOperation(loan, UmPayConstants.UpdateProjectStatus.PROJECT_STATE_REPAYING, false);
				//把下面这句耗时比较长的逻辑放到NotificationJob里了
				//umPayNormalRepayOperation.recommendedIncome(loan);
				this.addLoanOutSuccessfulNotificationJob(loan);
			}else{
				to.setStatus(TrusteeshipConstants.Status.REFUSED);
				ht.update(to);
				throw new UmPayOperationException("放款失败;:状态异常:"+resData.get("ret_code")+"信息:"+resData.get("ret_msg"));
			}
		} catch (ReqDataException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new UmPayOperationException("放款失败:参数加密失败!");
		} catch (RetDataException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new UmPayOperationException("放款失败:参数解密失败!");
		} catch (BorrowedMoneyTooLittle e) {
			log.debug(e.getMessage());
			throw new UmPayOperationException("放款失败:募集到的资金太少");
		}
		return null;
	}

	/**
	 * 对于放款操作,只有后台通知
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			Map<String,String> paramMap = UmPaySignUtil.getMapDataByRequest(request);

			// 
			String ret_code = paramMap.get("ret_code");
			//订单ID(01+loanId)
			String order_id = paramMap.get("order_id");

			log.debug(MessageFormat.format("放款S2S验签通过: ret_code={0}, order_id={1}", ret_code, order_id));
			TrusteeshipOperation to = trusteeshipOperationBO.get(UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER, order_id, UmPayConstants.OperationType.UMPAY);
			//获取操作记录
			if(to != null && "0000".equals(ret_code)){	//处理成功
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				ht.update(to);
				String loanId = to.getOperator();
				Loan loan = ht.get(Loan.class, loanId);
				ht.evict(loan);
				loan = ht.get(Loan.class, loanId);
				if(LoanConstants.LoanStatus.RECHECK.equals(loan.getStatus())){
					try {
						loanService.giveMoneyToBorrower(loanId);
						log.debug("标的"+loanId+"放款成功");
					} catch (BorrowedMoneyTooLittle e) {
						log.debug("标的"+loanId+"放款失败");
						log.debug(e.getMessage());
						e.printStackTrace();
					}
				}
				String responseData = getResponseData(order_id, ret_code);
				response.getWriter().print(responseData);
				FacesUtil.getCurrentInstance().responseComplete();
			}
		} catch (VerifyException e) {
			log.error("放款验签失败"+e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {

	}

	private void addLoanOutSuccessfulNotificationJob(Loan loan) {
		Date now = new Date();
		Date threeMinutesLater = DateUtil.addMinute(now, 3);
		JobDetail jobDetail = JobBuilder
				.newJob(LoanOutSuccessfulNotificationJob.class)
				.withIdentity(loan.getId(), ScheduleConstants.JobGroup.LOAN_OUT_NOTIFICATION)
				.build();
		jobDetail.getJobDataMap().put(LoanOutSuccessfulNotificationJob.LOAN_ID, loan.getId());
		SimpleTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(loan.getId(), ScheduleConstants.TriggerGroup.LOAN_OUT_NOTIFICATION)
				.forJob(jobDetail)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.startAt(threeMinutesLater).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			log.debug("add make loan notify job,loan_id = " + loan.getId());
		} catch (SchedulerException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("添加[标的放款通知]调度成功，项目编号[" + loan.getId() + "]");
		}
	}
	

	/**
	 * 平台收费
	 * @param orderId 流水号 
	 * @param money	金额
	 * @param projectId	项目ID
	 * @throws ReqDataException 
	 * @throws RetDataException 

	@SuppressWarnings("unchecked")
	public void LoanMoney2Mer(String orderId , Double fee ,String projectId) throws ReqDataException, RetDataException{
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		//标的转账接口
		Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
		sendMap.put("ret_url",UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL);
	    sendMap.put("notify_url",UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL);
	    sendMap.put("order_id",orderId);
	    sendMap.put("mer_date",DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
	    sendMap.put("project_id",projectId);
	    sendMap.put("serv_type",UmPayConstants.TransferProjectStatus.SERV_TYPE_PLATFORM_FEE);
	    sendMap.put("trans_action",UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
	    sendMap.put("partic_type",UmPayConstants.TransferProjectStatus.PARTIC_TYPE_P2P);
	    sendMap.put("partic_acc_type",UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_MER);
	    sendMap.put("partic_user_id",UmPayConstants.Config.MER_CODE);//这里是商户号
	    sendMap.put("amount",currentNumberFormat.format(fee));
	    ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
	    log.debug("从第三方的loan里面给系统手续费-发送数据: "+reqData.toString());
	    TrusteeshipOperation to = createTrusteeshipOperation(orderId, reqData.getUrl(), projectId, UmPayConstants.OperationType.PROJECT_TRANSFER,reqData.getPlain());
	    String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
	    Map<String,String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
	    log.debug("从第三方的loan里面给系统手续费-接收数据: "+resData.toString());
	    String ret_code = resData.get("ret_code");
	    if(!"0000".equals(ret_code)){	//成功将钱划给商户
	    	log.debug("从第三方loan划账给系统手续费-划账成功!");
	    	to.setStatus(TrusteeshipConstants.Status.PASSED);
	    }else{
	    	log.error("从第三方loan里面给系统手续费-划账失败-原因: "+resData.get("ret_msg"));
	    	to.setStatus(TrusteeshipConstants.Status.REFUSED);
	    }
	    to.setResponseData(resData.toString());
	    to.setResponseTime(new Date());
	    ht.update(to);
	}
		 */

}
