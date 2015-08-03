package com.esoft.umpay.repay.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.esoft.archer.user.model.*;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.loan.service.impl.UmPayLoanStatusService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * Description :正常还款
 * 
 * @author zt
 * @data 2015-3-14下午2:39:43
 */
@Service("umPayNormalRepayOperation")
public class UmPayNormalRepayOperation extends
		UmPayOperationServiceAbs<LoanRepay> {

	public static final String NOT_BIND_CARD = "未在联动优势绑定借记卡,交易失败";
	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UmPayLoanStatusService umpayLoanStatusService;

	@Resource
	UmPayLoanMoneyService umPayLoanMoneyService;

	@Resource
	UserBillBO userBillBO;

	@Resource
	RepayService repayService;

	@Resource
	LoanService loanService;

	@Logger
	Log log;

	@Resource
	SystemBillService systemBillService;

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void recommendedIncome(Loan loan) throws IOException,ReqDataException, RetDataException{
		//找到该笔借款的投资明细
		List<Invest> investList = ht.find(
				"from Invest i where i.loan.id=? and i.status not in (?,?)",
				new String[] { loan.getId(), InvestConstants.InvestStatus.CANCEL, InvestConstants.InvestStatus.UNFINISHED });
		for (Invest invest : investList) {
			List<ReferrerRelation> referrerRelationList = ht.find("from ReferrerRelation t where t.userId = ?", new String[]{invest.getUser().getId()});
			for(ReferrerRelation referrerRelation : referrerRelationList){
				List<Role> userRoleList = referrerRelation.getReferrer().getRoles();
				List<String> list = Lists.transform(userRoleList, new Function<Role, String>() {
					@Override
					public String apply(Role role) {
						return role.getId();
					}
				});
				String roleId = "";
				if(list.contains("ROLE_MERCHANDISER")){
					roleId = "ROLE_MERCHANDISER";
				}else if(list.contains("INVESTOR") && !list.contains("ROLE_MERCHANDISER")){
					roleId = "INVESTOR";
				}else{
					roleId = "MEMBER";
				}
				double bonus = calculateBonus(invest, referrerRelation, loan, roleId);
				String orderId = invest.getId() + System.currentTimeMillis();
				String particAccType = UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON;
				String transAction = UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT;
				String particUserId = getTrusteeshipAccount(referrerRelation.getReferrerId())!=null?getTrusteeshipAccount(referrerRelation.getReferrerId()).getId():"";
				Date nowdate = new Date();
				String status = InvestUserReferrer.FAIL;
				String msg = "";
				if(list.contains("INVESTOR") && !list.contains("ROLE_MERCHANDISER") && particUserId!="" && bonus > 0.00){
					//调用联动优势接口
					String returnMsg = umPayLoanMoneyService.giveMoney2ParticUserId(orderId, bonus,particAccType,transAction,particUserId);
					if(returnMsg.split("\\|")[0].equals("0000")){
						status = InvestUserReferrer.SUCCESS;
					}else{
						msg = returnMsg.split("\\|")[1];
					}
				}
				if(bonus == -1){
					continue;
				}
				if (!roleId.equals("INVESTOR") && !roleId.equals("ROLE_MERCHANDISER")){
					msg = NOT_BIND_CARD;
				}
				insertIntoInvestUserReferrer(invest, bonus, referrerRelation, list, orderId, nowdate, status);
				if(list.contains("ROLE_MERCHANDISER")){
					continue;
				}
				insertIntoUserBill(invest, bonus, referrerRelation, particUserId, nowdate, status, msg);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void recommendedIncomeReward(Loan loan) throws IOException,ReqDataException, RetDataException{
		//找到该笔借款的投资明细
		List<Invest> investList = ht.find(
				"from Invest i where i.loan.id=? and i.status not in (?,?)",
				new String[] { loan.getId(), InvestConstants.InvestStatus.CANCEL, InvestConstants.InvestStatus.UNFINISHED });
		for (Invest invest : investList) {
			List<ReferrerRelation> referrerRelationList = ht.find("from ReferrerRelation t where t.userId = ?", new String[]{invest.getUser().getId()});
			for(ReferrerRelation referrerRelation : referrerRelationList){
				List<Role> userRoleList = referrerRelation.getReferrer().getRoles();
				List<String> list = Lists.transform(userRoleList, new Function<Role, String>() {
					@Override
					public String apply(Role role) {
						return role.getId();
					}
				});
				String roleId = "";
				if(list.contains("ROLE_MERCHANDISER")){
					roleId = "ROLE_MERCHANDISER";
				}else if(list.contains("INVESTOR") && !list.contains("ROLE_MERCHANDISER")){
					roleId = "INVESTOR";
				}else{
					roleId = "MEMBER";
				}
				double bonus = calculateBonusReward(invest, referrerRelation, loan, roleId);
				String orderId = invest.getId() + System.currentTimeMillis();
				String particAccType = UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON;
				String transAction = UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT;
				String particUserId = getTrusteeshipAccount(referrerRelation.getReferrerId())!=null?getTrusteeshipAccount(referrerRelation.getReferrerId()).getId():"";
				Date nowdate = new Date();
				String status = InvestUserReferrer.FAIL;
				String msg = "";
				if(list.contains("INVESTOR") && !list.contains("ROLE_MERCHANDISER") && particUserId!="" && bonus > 0.00){
					//调用联动优势接口
					String returnMsg = umPayLoanMoneyService.giveMoney2ParticUserId(orderId, bonus,particAccType,transAction,particUserId);
					if(returnMsg.split("\\|")[0].equals("0000")){
						status = InvestUserReferrer.SUCCESS;
					}else{
						msg = returnMsg.split("\\|")[1];
					}
				}
				if(bonus == -1){
					continue;
				}
				if (!roleId.equals("INVESTOR") && !roleId.equals("ROLE_MERCHANDISER")){
					msg = NOT_BIND_CARD;
				}
				insertIntoInvestUserReferrer(invest, bonus, referrerRelation, list, orderId, nowdate, status);
				if(list.contains("ROLE_MERCHANDISER")){
					continue;
				}
				insertIntoUserBill(invest, bonus, referrerRelation, particUserId, nowdate, status, msg);
			}
		}
	}

	private double calculateBonus(Invest invest, ReferrerRelation referrerRelation,Loan loan,String roleId) {
		int percentage = 100;
		int maxDigital = 2;
		List<ReferGradeProfitUser> referGradeProfitUserList = ht.find("from ReferGradeProfitUser t where t.referrer = ? and t.grade = ?",
                new Object[]{referrerRelation.getReferrer(),referrerRelation.getLevel()});
		if (referGradeProfitUserList.size() > 0){
			return ArithUtil.div(ArithUtil.mul(invest.getMoney(), referGradeProfitUserList.get(0).getProfitRate(), maxDigital), percentage, maxDigital);
        }else {
			if(!roleId.equals("INVESTOR") && !roleId.equals("ROLE_MERCHANDISER")){
				List<ReferGradeProfitSys> referGradeProfitSysListEx = ht.find("from ReferGradeProfitSys t where t.grade = ? and t.gradeRole = ?", new Object[]{referrerRelation.getLevel(),"INVESTOR"});
				if (referGradeProfitSysListEx.size() > 0){
					return ArithUtil.div(ArithUtil.mul(invest.getMoney(), referGradeProfitSysListEx.get(0).getProfitRate(), maxDigital), percentage, maxDigital);
				}else{
					return -1;
				}
			}
            List<ReferGradeProfitSys> referGradeProfitSysList = ht.find("from ReferGradeProfitSys t where t.grade = ? and t.gradeRole = ?", new Object[]{referrerRelation.getLevel(),roleId});
			if (referGradeProfitSysList.size() > 0){
				return ArithUtil.div(ArithUtil.mul(invest.getMoney(), referGradeProfitSysList.get(0).getProfitRate(), maxDigital), percentage, maxDigital);
			}else {
				return -1;
			}
        }
	}

	private double calculateBonusReward(Invest invest, ReferrerRelation referrerRelation,Loan loan,String roleId) {
		int percentage = 100;
		int maxDigital = 2;
		List<ReferGradeProfitUser> referGradeProfitUserList = ht.find("from ReferGradeProfitUser t where t.referrer = ? and t.grade = ?",
				new Object[]{referrerRelation.getReferrer(),referrerRelation.getLevel()});
		if(!roleId.equals("INVESTOR") && !roleId.equals("ROLE_MERCHANDISER")){
			List<ReferGradeProfitSys> referGradeProfitSysListEx = ht.find("from ReferGradeProfitSys t where t.grade = ? and t.gradeRole = ?", new Object[]{referrerRelation.getLevel(),"INVESTOR"});
			if (referGradeProfitSysListEx.size() > 0){
				return ArithUtil.div(ArithUtil.mul(invest.getMoney(), 0.2, maxDigital), percentage, maxDigital);
			}else{
				return -1;
			}
		}
		List<ReferGradeProfitSys> referGradeProfitSysList = ht.find("from ReferGradeProfitSys t where t.grade = ? and t.gradeRole = ?", new Object[]{referrerRelation.getLevel(),roleId});
		if (referGradeProfitSysList.size() > 0){
			if (referrerRelation.getLevel()==1 && roleId.equals("ROLE_MERCHANDISER")) {
				return ArithUtil.div(ArithUtil.mul(invest.getMoney(), 1, maxDigital), percentage, maxDigital);
			} else {
				return ArithUtil.div(ArithUtil.mul(invest.getMoney(), 0.2, maxDigital), percentage, maxDigital);
			}
		}else {
			return -1;
		}
	}

	public void insertIntoUserBill(Invest invest, double bonus, ReferrerRelation referrerRelation, String particUserId, Date nowdate, String status, String msg) {
		//插入交易流水
		//获取当前余额
		double balance = userBillBO.getBalance(referrerRelation.getReferrerId());
		//获取当前冻结
		double frozenMoney = userBillBO.getFrozenMoney(referrerRelation.getReferrerId());
		UserBill ub = new UserBill();
		if (!status.equals("success")) {
			ub.setBalance(balance);
		}else {
			ub.setBalance(ArithUtil.add(balance, bonus));
		}
		ub.setFrozenMoney(frozenMoney);
		ub.setTime(nowdate);
		ub.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		ub.setType("ti_balance");
		ub.setTypeInfo("referrer_reward");
		ub.setMoney(bonus);
		ub.setSeqNum((userBillBO.getLastestBill(referrerRelation.getReferrerId())!=null?userBillBO.getLastestBill(referrerRelation.getReferrerId()).getSeqNum():0) + 1);
		ub.setUser(referrerRelation.getReferrer());
		String detail = "";
		if((!particUserId.equals("") && status.equals("success")) || bonus == 0.00){
            detail = "收到分红,来自"+invest.getUser().getUsername()+"的投资";
        }else if(particUserId.equals("")){
            detail = NOT_BIND_CARD;
        }else{
            detail = msg;
        }
		ub.setDetail(detail);
		ht.save(ub);
	}

	private void insertIntoInvestUserReferrer(Invest invest, double bonus, ReferrerRelation referrerRelation, List<String> list, String orderId, Date nowdate, String status) {
		//插入数据库
		InvestUserReferrer iur = new InvestUserReferrer();
		iur.setId(orderId);
		iur.setBonus(bonus);
		iur.setInvest(invest);
		iur.setTime(nowdate);
		iur.setReferrer(referrerRelation.getReferrer());
		iur.setStatus(status);
		if(list.contains("ROLE_MERCHANDISER")){
            iur.setRoleName("ROLE_MERCHANDISER");
        }else{
            iur.setRoleName("INVESTOR");
        }
		ht.save(iur);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(LoanRepay lr,
			FacesContext facesContext) throws IOException,ReqDataException, RetDataException {
		ht.update(lr);
		// 所有待还金额
		Double allRepayMoney = ArithUtil.add(lr.getCorpus(),
				lr.getDefaultInterest(), lr.getFee(), lr.getInterest());
		try {
			// 冻结还款金额
			/*
			 * userBillBO.freezeMoney(lr.getLoan().getUser().getId(),allRepayMoney
			 * , OperatorInfo.NORMAL_REPAY, "冻结还款金额，还款编号："+ lr.getId());
			 */
			DecimalFormat currentNumberFormat = new DecimalFormat("#");
			Map<String, String> sendMap = UmPaySignUtil
					.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
			sendMap.put(
					"ret_url",
					UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY);
			sendMap.put(
					"notify_url",
					UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
							+ UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY);
			// 时间戳,取前18位即为:还款编号
			String order_id = lr.getId() + System.currentTimeMillis();
			sendMap.put("order_id", order_id);
			sendMap.put("mer_date",
					DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
			sendMap.put("project_id", lr.getLoan().getId());
			sendMap.put("serv_type",
					UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY);
			sendMap.put("trans_action",
					UmPayConstants.TransferProjectStatus.TRANS_ACTION_IN);
			sendMap.put("partic_type",
					UmPayConstants.TransferProjectStatus.PARTIC_TYPE_LOANER);
			sendMap.put("partic_acc_type",
					UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
			sendMap.put("partic_user_id",
					getTrusteeshipAccount(lr.getLoan().getUser().getId())
							.getId());// 有点恶心
			sendMap.put("amount",
					currentNumberFormat.format(allRepayMoney * 100));
			ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
			TrusteeshipOperation to = createTrusteeshipOperation(
					order_id,
					reqData.getUrl(),
					lr.getId(),
					UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY,
					GsonUtil.fromMap2Json(reqData.getField()));
			sendOperation(to, facesContext);
		} catch (ReqDataException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 前台回调
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException,IOException {
		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("正常还款返回信息:" + paramMap);
			String ret_code = paramMap.get("ret_code");
			String order_id = paramMap.get("order_id");
			TrusteeshipOperation to = trusteeshipOperationBO
					.get(UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_NORMAL_REPAY,
							order_id, UmPayConstants.OperationType.UMPAY);
			to.setResponseData(paramMap.toString());
			to.setResponseTime(new Date());
			if ("0000".equals(ret_code)) {
				String repayId = to.getOperator();
				LoanRepay lr = ht.get(LoanRepay.class, repayId,
						LockMode.UPGRADE);
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				if (lr.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
					lr.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
				}
				if (lr.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
					/*
					 * userBillBO.unfreezeMoney(lr.getLoan().getUser().getId(),
					 * ArithUtil.add(lr.getCorpus(),
					 * lr.getDefaultInterest(),lr.getFee(), lr.getInterest()),
					 * OperatorInfo.NORMAL_REPAY, "资金托管方还款，解冻还款金额，还款编号：" +
					 * lr.getId());
					 */
					// 正常还款
					LoanRepay repay = ht.get(LoanRepay.class, repayId,
							LockMode.UPGRADE);
					repay.setStatus(RepayStatus.REPAYING);
					repayService.normalRepay(repay);
					// 调用接口将标的里面的钱转给投资人
					loanMoney2InvestPerson(repay);
					// 如果还款完成调用 调用接口改变标的状态为结束
					if (loanService.isCompleted(repay.getLoan().getId())) {
						/* updateLoanStatus2Complete(repay.getLoan()); */
						umpayLoanStatusService
								.updateLoanStatusOperation(
										repay.getLoan(),
										UmPayConstants.UpdateProjectStatus.PROJECT_STATE_FINISH,
										false);
					}
				}
			} else {
				fail(to);
			}
			ht.update(to);
		} catch (VerifyException e) {
			e.printStackTrace();
		} catch (InsufficientBalance e) {
			e.printStackTrace();
		} catch (NormalRepayException e) {
			e.printStackTrace();
		} catch (ReqDataException e) {
			e.printStackTrace();
		} catch (RetDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 还款失败
	 * 
	 * @param to
	 */
	@SuppressWarnings("deprecation")
	@Transactional(rollbackFor = Exception.class)
	public void fail(TrusteeshipOperation to) {
		LoanRepay lr = ht.get(LoanRepay.class, to.getOperator(),
				LockMode.UPGRADE);
		// 修改状态
		lr.setStatus(RepayStatus.REPAYING);
		// 解冻金额
		/*
		 * try {
		 * userBillBO.unfreezeMoney(lr.getLoan().getUser().getId(),ArithUtil
		 * .add(lr.getCorpus(), lr.getDefaultInterest(),lr.getFee(),
		 * lr.getInterest()),OperatorInfo.NORMAL_REPAY,"资金托管方还款失败，解冻还款金额，还款编号："
		 * + lr.getId()); } catch (InsufficientBalance e) { log.error(e); throw
		 * new RuntimeException(e); }
		 */
		to.setStatus(TrusteeshipConstants.Status.REFUSED);
		ht.update(lr);
		ht.update(to);
	}

	/**
	 * 调用标的转账: 将loan里面的钱转给投资人 , 但是不包括fee手续费
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void loanMoney2InvestPerson(LoanRepay lr) {
		DecimalFormat currentNumberFormat = new DecimalFormat("#");
		LoanRepay loanRepay = ht.get(LoanRepay.class, lr.getId());
		// 借款人给系统的手续费
		Double fee = loanRepay.getFee();
		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
						new Object[] { loanRepay.getLoan().getId(),
								loanRepay.getPeriod() });
		try {
			// 从loan里面把相应的钱都转给相应的投资人
			for (InvestRepay investRepay : irs) {
				Invest invest = investRepay.getInvest();
				Map<String, String> sendMap = UmPaySignUtil
						.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
				log.debug("将第三方标的的钱转至投资人:" + invest.getUser().getId());
				sendMap.put("ret_url",
						UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL);
				sendMap.put("notify_url",
						UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL);
				sendMap.put("order_id", investRepay.getId());
				sendMap.put("mer_date",
						DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
				sendMap.put("project_id", invest.getLoan().getId());
				sendMap.put(
						"serv_type",
						UmPayConstants.TransferProjectStatus.SERV_TYPE_REPAY_BACK);
				sendMap.put("trans_action",
						UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
				sendMap.put(
						"partic_type",
						UmPayConstants.TransferProjectStatus.PARTIC_TYPE_INVESTOR);
				sendMap.put(
						"partic_acc_type",
						UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
				sendMap.put("partic_user_id",
						getTrusteeshipAccount(invest.getUser().getId()).getId());
				// 应该转从标的转给投资人的钱 = 投资的本金 + 罚息 + 应得的利息( = 利息 - 投资人给系统的手续费)
				Double allRepayMoney = ArithUtil.add(investRepay.getCorpus(),
						investRepay.getDefaultInterest(),
						investRepay.getInterest() - investRepay.getFee());
				sendMap.put("amount",
						currentNumberFormat.format(allRepayMoney * 100));
				ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
				TrusteeshipOperation to = createTrusteeshipOperation(
						investRepay.getId(), reqData.getUrl(),
						investRepay.getId(),
						UmPayConstants.OperationType.PROJECT_TRANSFER,
						reqData.getPlain());
				String responseBodyAsString = HttpClientUtil
						.getResponseBodyAsString(to.getRequestUrl());
				Map<String, String> resData = Plat2Mer_v40
						.getResData(responseBodyAsString);
				to.setResponseData(resData.toString());
				to.setResponseTime(new Date());
				if ("0000".equals(resData.get("ret_code"))) {
					log.debug("第三方从标的转给" + invest.getUser().getId() + "成功!");
					to.setStatus(TrusteeshipConstants.Status.PASSED);
					fee += investRepay.getFee();
				} else {
					to.setStatus(TrusteeshipConstants.Status.REFUSED);
				}
				ht.update(to);
			}
			// 每次还款都需要将手续费给系统(商户)
			/* loanMoney2Mer(loanRepay,fee); */
			log.info("还款给平台收费:" + fee);
			umPayLoanMoneyService.loanMoney2Mer(lr.getId(), fee, lr.getLoan()
					.getId());
		} catch (ReqDataException e) {
			log.error("还款将第三方标的钱转至投资人账户下:加密失败" + e);
		} catch (RetDataException e) {
			log.error("还款将第三方标的钱转至投资人账户下:解密失败" + e);
			e.printStackTrace();
		}

	}

	/**
	 * 后台通知会通知多次
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {

		try {
			Map<String, String> paramMap = UmPaySignUtil
					.getMapDataByRequest(request);
			log.debug("正常还款后台通知回调:" + paramMap);
			receiveOperationPostCallback(request);
			String responseData = getResponseData(paramMap.get("order_id"),
					"0000");
			log.debug("正常还款通知对方:" + responseData);
			response.getWriter().print(responseData);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		} catch (VerifyException e) {
			log.error("正常还款验签失败" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将标的状态变为'完成' //FIXME 这里可以直接调用标的更新接口里面的修改状态为'完成',可以更新多次将标的更新到最后
	 * 
	 * @throws ReqDataException
	 * @throws RetDataException
	 * @SuppressWarnings("unchecked")
	 * @Transactional(rollbackFor = Exception.class) public void
	 *                            updateLoanStatus2Complete(Loan loan) throws
	 *                            ReqDataException, RetDataException{
	 *                            Map<String, String> sendMap =
	 *                            UmPaySignUtil.getSendMapDate
	 *                            (UmPayConstants.OperationType
	 *                            .MER_UPDATE_PROJECT);
	 *                            sendMap.put("project_id",loan.getId());
	 *                            sendMap.put("change_type",UmPayConstants.
	 *                            UpdateProjectStatus
	 *                            .CHANGE_TYPE_UPDATE_PRIJECT);
	 *                            sendMap.put("project_state"
	 *                            ,UmPayConstants.UpdateProjectStatus
	 *                            .PROJECT_STATE_FINISH); ReqData reqData =
	 *                            Mer2Plat_v40.makeReqDataByGet(sendMap);
	 *                            log.debug
	 *                            ("更新umpay标的为完成-发送数据: "+reqData.toString());
	 *                            TrusteeshipOperation to =
	 *                            createTrusteeshipOperation(loan.getId(),
	 *                            reqData.getUrl(), loan.getId(),
	 *                            UmPayConstants.
	 *                            OperationType.MER_UPDATE_PROJECT
	 *                            ,reqData.getPlain()); //创建一个get直连 String
	 *                            responseBodyAsString =
	 *                            HttpClientUtil.getResponseBodyAsString
	 *                            (to.getRequestUrl()); //获取返回参数
	 *                            Map<String,String> resData =
	 *                            Plat2Mer_v40.getResData(responseBodyAsString);
	 *                            log
	 *                            .debug("更新umpay标的为完成-接收数据: "+resData.toString
	 *                            ()); to.setResponseData(resData.toString());
	 *                            to.setResponseTime(new Date());
	 *                            if("0000".equals(resData.get("ret_code"))){
	 *                            log.debug("更新umpay标的为完成:成功");
	 *                            to.setStatus(TrusteeshipConstants
	 *                            .Status.PASSED); }else{
	 *                            log.debug("更新umpay标的为失败:"
	 *                            +resData.get("ret_msg"));
	 *                            to.setStatus(TrusteeshipConstants
	 *                            .Status.REFUSED); } ht.update(to); }
	 */

	/**
	 * 调用标的转账 将loan里面的钱转账至系统商户(收取手续费 = 借款人fee+投资人fee)
	 * 
	 * @throws ReqDataException
	 * @throws RetDataException
	 * @SuppressWarnings("unchecked")
	 * @Transactional(rollbackFor = Exception.class) public void
	 *                            loanMoney2Mer(LoanRepay lr , Double allFee)
	 *                            throws ReqDataException, RetDataException{
	 *                            DecimalFormat currentNumberFormat = new
	 *                            DecimalFormat("#"); Map<String, String>
	 *                            sendMap =
	 *                            UmPaySignUtil.getSendMapDate(UmPayConstants
	 *                            .OperationType.PROJECT_TRANSFER);
	 *                            sendMap.put("ret_url"
	 *                            ,UmPayConstants.ResponseWebUrl
	 *                            .PRE_RESPONSE_URL);
	 *                            sendMap.put("notify_url",UmPayConstants
	 *                            .ResponseS2SUrl.PRE_RESPONSE_URL);
	 *                            sendMap.put("order_id",lr.getId());
	 *                            sendMap.put
	 *                            ("mer_date",DateUtil.DateToString(new Date(),
	 *                            DateStyle.YYYYMMDD));
	 *                            sendMap.put("project_id",
	 *                            lr.getLoan().getId());
	 *                            sendMap.put("serv_type",
	 *                            UmPayConstants.TransferProjectStatus
	 *                            .SERV_TYPE_PLATFORM_FEE);
	 *                            sendMap.put("trans_action"
	 *                            ,UmPayConstants.TransferProjectStatus
	 *                            .TRANS_ACTION_OUT);
	 *                            sendMap.put("partic_type",UmPayConstants
	 *                            .TransferProjectStatus.PARTIC_TYPE_P2P);
	 *                            sendMap.put("partic_acc_type",UmPayConstants.
	 *                            TransferProjectStatus.PARTIC_ACC_TYPE_MER);
	 *                            sendMap
	 *                            .put("partic_user_id",UmPayConstants.Config
	 *                            .MER_CODE);//这里是商户号
	 *                            sendMap.put("amount",currentNumberFormat
	 *                            .format(allFee)); ReqData reqData =
	 *                            Mer2Plat_v40.makeReqDataByGet(sendMap);
	 *                            log.debug
	 *                            ("从第三方的loan里面给系统手续费-发送数据: "+reqData.toString
	 *                            ()); TrusteeshipOperation to =
	 *                            createTrusteeshipOperation(lr.getId(),
	 *                            reqData.getUrl(), lr.getId(),
	 *                            UmPayConstants.OperationType
	 *                            .PROJECT_TRANSFER,reqData.getPlain()); String
	 *                            responseBodyAsString =
	 *                            HttpClientUtil.getResponseBodyAsString
	 *                            (to.getRequestUrl()); Map<String,String>
	 *                            resData =
	 *                            Plat2Mer_v40.getResData(responseBodyAsString);
	 *                            log.debug("从第三方的loan里面给系统手续费-接收数据: "+resData.
	 *                            toString()); String ret_code =
	 *                            resData.get("ret_code");
	 *                            if(!"0000".equals(ret_code)){ //成功将钱划给商户
	 *                            log.debug("从第三方loan划账给系统手续费-划账成功!");
	 *                            to.setStatus
	 *                            (TrusteeshipConstants.Status.PASSED); }else{
	 *                            log
	 *                            .error("从第三方loan里面给系统手续费-划账失败-原因: "+resData.
	 *                            get("ret_msg"));
	 *                            to.setStatus(TrusteeshipConstants
	 *                            .Status.REFUSED); }
	 *                            to.setResponseData(resData.toString());
	 *                            to.setResponseTime(new Date()); ht.update(to);
	 *                            }
	 */
}
