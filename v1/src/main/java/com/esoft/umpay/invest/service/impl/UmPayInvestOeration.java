package com.esoft.umpay.invest.service.impl;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.coupon.CouponConstants;
import com.esoft.jdp2p.coupon.exception.ExceedDeadlineException;
import com.esoft.jdp2p.coupon.exception.UnreachedMoneyLimitException;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.exception.ExceedMoneyNeedRaised;
import com.esoft.jdp2p.invest.exception.IllegalLoanStatusException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.UmPayConstants.TransferProjectStatus;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.ttsd.api.dto.InvestResponseDataDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.util.CommonUtils;
import com.ttsd.special.services.ConferenceSaleService;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;


/**
 * Description : 投标操作
 *
 * @author zt
 * @data 2015-3-11下午1:58:36
 */
@Service("umPayInvestOeration")
public class UmPayInvestOeration extends UmPayOperationServiceAbs<Invest> {


    @Resource
    TrusteeshipOperationBO trusteeshipOperationBO;

    @Resource
    InvestService investService;

    @Resource
    UserBillBO ubs;

    @Resource
    LoanCalculator loanCalculator;

    @Resource
    UserBillBO userBillBO;

    @Resource
    LoanService loanService;

    @Resource
    HibernateTemplate ht;

    @Autowired
    ConferenceSaleService conferenceSaleService;

    @Logger
    Log log;


    /**
     * 投资
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrusteeshipOperation createOperation(Invest invest,
                                                FacesContext facesContext) throws IOException {
        try {
            /*investService.create(invest);*/
            //获取invest对象,保存至operation里面然后,等回调成功的时候讲对象取出来然后再进行保存,避免资金冻结情况的发生
            invest = investUnFreeze(invest);
            Map<String, String> sendMap = assembleSendMap(invest, false);
            ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
            // 保存操作记录(将对象直接转换成XML保存至operator里面,投资成功后取出)
            /*String xml = new XStream().toXML(invest);
            JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//放置自包含
			JSONObject jso = JSONObject.fromObject(invest ,jsonConfig);
			String jsos = jso.toString();
			log.debug("长度:"+jsos.length());
			log.debug(jsos);*/
            TrusteeshipOperation to = createTrusteeshipOperation(invest.getId(), reqData.getUrl(), invest.getId(), UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST, GsonUtil.fromMap2Json(reqData.getField()));
            // 发送请求
            sendOperation(to, facesContext);
            return to;
        } catch (ReqDataException e) {
            throw new UmPayOperationException("请求第三方加密失败！");
        } catch (InsufficientBalance e1) {
            throw new UmPayOperationException("账户余额不足，请充值！");
        } catch (ExceedMoneyNeedRaised e1) {
            throw new UmPayOperationException("投资金额不能大于尚未募集的金额！");
        } catch (ExceedDeadlineException e1) {
            throw new UmPayOperationException("优惠券已过期！");
        } catch (UnreachedMoneyLimitException e1) {
            throw new UmPayOperationException("投资金额未到达优惠券使用条件！");
        } catch (IllegalLoanStatusException e1) {
            throw new UmPayOperationException("当前借款不可投资！");
        } catch (NoMatchingObjectsException e) {
            throw new UmPayOperationException("投资失败！");
        }

    }

    /**
     * 投资
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public InvestResponseDataDto createOperation(Invest invest) {
        InvestResponseDataDto investResponseDataDto = null;
        try {
            investResponseDataDto = new InvestResponseDataDto();
            invest = investUnFreeze(invest);

            Map<String, String> sendMap = assembleSendMap(invest, true);

            sendMap.put("sourceV", UmPayConstants.SourceViewType.SOURCE_V);
            //sendMap.put("ret_url", "");
            ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
            String requestData = CommonUtils.mapToFormData(reqData.getField(), true);

            createTrusteeshipOperation(invest.getId(), reqData.getUrl(), invest.getId(), UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST, GsonUtil.fromMap2Json(reqData.getField()));
            investResponseDataDto.setUrl(reqData.getUrl());
            investResponseDataDto.setRequestData(requestData);
            return investResponseDataDto;
        }catch (UnsupportedEncodingException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new UmPayOperationException(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
        }catch (ReqDataException e) {
            log.error(e.getLocalizedMessage(),e);
            throw new UmPayOperationException(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
        } catch (InsufficientBalance e1) {
            log.error(e1.getLocalizedMessage(),e1);
            throw new UmPayOperationException(ReturnMessage.INSUFFICIENT_BALANCE.getCode());
        } catch (ExceedMoneyNeedRaised e1) {
            log.error(e1.getLocalizedMessage(),e1);
            throw new UmPayOperationException(ReturnMessage.EXCEED_MONEY_NEED_RAISED.getCode());
        } catch (ExceedDeadlineException e1) {
            log.error(e1.getLocalizedMessage(),e1);
            throw new UmPayOperationException(ReturnMessage.EXCEED_DEAD_LINE_EXCEPTION.getCode());
        } catch (UnreachedMoneyLimitException e1) {
            log.error(e1.getLocalizedMessage(),e1);
            throw new UmPayOperationException(ReturnMessage.UNREACHED_MONEY_LIMIT_EXCETPTION.getCode());
        } catch (IllegalLoanStatusException e1) {
            log.error(e1.getLocalizedMessage(),e1);
            throw new UmPayOperationException(ReturnMessage.LLLEGAL_LOAN_STATUS_EXCEPTION.getCode());
        } catch (NoMatchingObjectsException e) {
            log.error(e.getLocalizedMessage(),e);
            throw new UmPayOperationException(ReturnMessage.NO_MATCHING_OBJECTS_EXCEPTION.getCode());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> assembleSendMap(Invest invest, boolean isMobileRequest) {
        DecimalFormat currentNumberFormat = new DecimalFormat("#");
        Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
        if(isMobileRequest) {
            sendMap.put("ret_url", UmPayConstants.ResponseMobUrl.PRE_RESPONSE_URL + UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST);
        }else {
            sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL + UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST);
        }
        sendMap.put("notify_url", UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL + UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST);
        sendMap.put("order_id", invest.getId());
        sendMap.put("mer_date", DateUtil.DateToString(invest.getTime(), DateStyle.YYYYMMDD));
        sendMap.put("project_id", invest.getLoan().getId());
        sendMap.put("serv_type", TransferProjectStatus.SERV_TYPE_INVEST);
        sendMap.put("trans_action", TransferProjectStatus.TRANS_ACTION_IN);
        sendMap.put("partic_type", TransferProjectStatus.PARTIC_TYPE_INVESTOR);
        sendMap.put("partic_acc_type", TransferProjectStatus.PARTIC_ACC_TYPE_PERSON);
        sendMap.put("partic_user_id", getTrusteeshipAccount(invest.getUser().getId()).getId());
        sendMap.put("amount", currentNumberFormat.format(invest.getInvestMoney() * 100));

        return sendMap;
    }


    /**
     * 处理前台通知的投标
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOperationPostCallback(ServletRequest request)
            throws TrusteeshipReturnException {
        Map<String, String> paramMap = null;
        try {
            paramMap = UmPaySignUtil.getMapDataByRequest(request);
            log.debug("投资前台验签通过数据:" + paramMap);
            String ret_code = paramMap.get("ret_code");
            String order_id = paramMap.get("order_id");
            TrusteeshipOperation to = trusteeshipOperationBO.get(UmPayConstants.ResponseUrlType.PROJECT_TRANSFER_INVEST, order_id, order_id, UmPayConstants.OperationType.UMPAY);
            if ("0000".equals(ret_code)) {
                to.setResponseData(paramMap.toString());
                to.setResponseTime(new Date());
                //处理投资成功修改状态
                Invest invest = InvestSuccess(to);
                ht.update(invest);
            } else {
                fail(to);
                log.error("投资失败:" + paramMap.toString());
                throw new UmPayOperationException("投资失败:错误信息:" + paramMap.get("ret_msg"));
            }
            ht.update(to);
        } catch (VerifyException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new UmPayOperationException("接收信息时出错!");
        } catch (InsufficientBalance e) {
            log.error(e.getLocalizedMessage(), e);
            throw new UmPayOperationException("余额不足!");
        } catch (NoMatchingObjectsException e) {
            log.error(e.getLocalizedMessage(), e);
		} catch (DuplicateKeyException e) {
			log.error(e.getLocalizedMessage(),e);
			throw new TrusteeshipReturnException("duplication");
		}
	}

	/**
	 * 处理后台通知的投标
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			Map<String,String> paramMap = UmPaySignUtil.getMapDataByRequest(request);
			log.debug("投资后台验签通过数据为:"+paramMap.toString());
			receiveOperationPostCallback(request);
			String responseData = getResponseData(paramMap.get("order_id"), "0000");
			log.debug("通知对方:"+responseData);
			response.getWriter().print(responseData);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		} catch (VerifyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}




	/**
	 * 在未收到第三方通知的情况下,不冻结投资
	 * 当等到收到第三方成功通知的时候直接将用户的钱划走
	 * @param invest
	 * @throws IllegalLoanStatusException
	 * @throws NoMatchingObjectsException
	 * @throws ExceedMoneyNeedRaised
	 * @throws InsufficientBalance
	 * @throws ExceedDeadlineException
	 * @throws UnreachedMoneyLimitException
	 */
	@SuppressWarnings("deprecation")
	public Invest investUnFreeze(Invest invest) throws IllegalLoanStatusException, NoMatchingObjectsException, ExceedMoneyNeedRaised, InsufficientBalance, ExceedDeadlineException, UnreachedMoneyLimitException{
		String loanId = invest.getLoan().getId();
		invest.setInvestMoney(invest.getMoney());
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		if (!loan.getStatus().equals(LoanStatus.RAISING)) {
			throw new IllegalLoanStatusException(loan.getStatus());
		}
		//尚未募集的金额
		double remainMoney = loanCalculator.calculateMoneyNeedRaised(loan.getId());
		if (invest.getMoney() > remainMoney) {
			throw new ExceedMoneyNeedRaised();
		}
		// 判断是否有代金券；判断能否用代金券
		if (invest.getUserCoupon() != null) {
			//代金券不是未使用状态，抛出异常
			if (!invest.getUserCoupon().getStatus()
					.equals(CouponConstants.UserCouponStatus.UNUSED)) {
				throw new ExceedDeadlineException();
			}
			// 判断代金券是否达到使用条件
			if (invest.getMoney() < invest.getUserCoupon().getCoupon()
					.getLowerLimitMoney()) {
				throw new UnreachedMoneyLimitException();
			}
			// 用户填写认购的钱数以后，判断余额，如果余额不够，不能提交，弹出新页面让用户充值
			// investMoney>代金券金额+余额
			if (invest.getMoney() > invest.getUserCoupon().getCoupon()
					.getMoney()
					+ ubs.getBalance(invest.getUser().getId())) {
				throw new InsufficientBalance();
			}
		}
		//是否余额不足
		if (invest.getMoney() > ubs.getBalance(invest.getUser().getId())) {
			throw new InsufficientBalance();
		}
		invest.setStatus(InvestConstants.InvestStatus.WAIT_AFFIRM);
		invest.setRate(loan.getRate());
		invest.setTime(new Date());
		// 投资成功以后，判断项目是否有尚未认购的金额，如果没有，则更改项目状态。
		invest.setId(investService.generateId(invest.getLoan().getId()));
		if (invest.getTransferApply() == null || StringUtils.isEmpty(invest.getTransferApply().getId())) {
			invest.setTransferApply(null);
		}

		invest.setLoan(ht.get(Loan.class, invest.getLoan().getId()));
		invest.setUser(ht.get(User.class, invest.getUser().getId()));
		ht.save(invest);

		return invest;
	}


    /**
     * 处理投资成功
     *
     * @throws NoMatchingObjectsException
     * @throws InsufficientBalance
     */
    @SuppressWarnings("deprecation")
    public Invest InvestSuccess(TrusteeshipOperation to) throws NoMatchingObjectsException, InsufficientBalance {
        Invest invest = ht.get(Invest.class, to.getOperator(), LockMode.UPGRADE);
        //获取loan
        Loan loan = ht.get(Loan.class, invest.getLoan().getId(), LockMode.UPGRADE);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM) || invest.getStatus().equals(InvestConstants.InvestStatus.CANCEL)) {
            invest.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
            to.setStatus(TrusteeshipConstants.Status.PASSED);
            //判断是否募集完成
            loanService.dealRaiseComplete(loan.getId());
            ubs.freezeMoney(invest.getUser().getId(), invest.getMoney(), OperatorInfo.INVEST_SUCCESS,
                    "投资成功：冻结金额。借款ID:" + loan.getId() + "  投资id:" + invest.getId());

            // 如果满足会销活动条件则发放奖励
            conferenceSaleService.processIfInActivityForInvest(invest);
        }
        return invest;
    }

    /**
     * 处理投资失败
     *
     * @param to
     */
    @SuppressWarnings("deprecation")
    @Transactional(rollbackFor = Exception.class)
    public void fail(TrusteeshipOperation to) {
        Invest invest = ht.get(Invest.class, to.getMarkId(), LockMode.UPGRADE);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM) || invest.getStatus().equals(InvestConstants.InvestStatus.CANCEL)) {
            invest.setStatus(InvestConstants.InvestStatus.CANCEL);
            ht.update(invest);
            to.setStatus(TrusteeshipConstants.Status.REFUSED);
            ht.update(to);
        }
    }


    /**
     * 暂时处理投资失败的那个标的状态

     @Transactional(rollbackFor = Exception.class)
     public void receiveOperationPostCallback(ServletRequest request)
     throws TrusteeshipReturnException {
     String order_id = request.getParameter("order_id");
     Invest invest = ht.get(Invest.class, order_id);
     if(invest.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS) || invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)){
     invest.setStatus(InvestConstants.InvestStatus.CANCEL);
     ht.update(invest);
     }
     // 改项目状态，项目如果是等待复核的状态，改为募集中
     if (invest.getLoan().getStatus()
     .equals(LoanConstants.LoanStatus.RECHECK) && invest.getLoan().getExpectTime().after(new Date())) {
     invest.getLoan().setStatus(LoanConstants.LoanStatus.RAISING);
     ht.update(invest.getLoan());
     }
     // 解冻投资金额
     try {
     ubs.unfreezeMoney(invest.getUser().getId(),
     invest.getMoney(), OperatorInfo.CANCEL_INVEST,
     "投资：" + invest.getId() + "失败，解冻投资金额, 借款ID："
     + invest.getLoan().getId());
     } catch (InsufficientBalance e) {
     log.debug(e);
     throw new UmPayOperationException("账户余额不足！");
     }
     }
     */
}
