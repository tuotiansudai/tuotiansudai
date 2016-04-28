package com.esoft.umpay.loan.service.impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.exception.InvalidExpectTimeException;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;



/**
 * 
 * Description : 创建标的 
 * @author zt
 * @data 2015-3-10下午4:11:42
 */

@Service("umPayCreateLoanOperation")
public class UmPayCreateLoanOperation extends UmPayOperationServiceAbs<Loan> {

	
	@Resource
	LoanService loanService;
	
	@Resource
	HibernateTemplate ht;

	@Resource
	UmPayLoanStatusService umPayLoanStatusService;

	@Logger
	Log log;

	
	/**
	 * 发送请求-直连
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {
		try {
		loanService.createLoanByAdmin(loan);
		//将代码抽出来, 但是这里需要优化
		umPayLoanStatusService.createLoan(loan);
		} catch (ReqDataException e) {
			log.debug("发标创建请求数据失败!标号:"+loan.getId());
			throw new UmPayOperationException("发标创建请求数据失败!标号:"+loan.getId());
		} catch (RetDataException e) {
			log.debug("发标解析请求回调数据失败!标号"+loan.getId());
			throw new UmPayOperationException("发标解析请求回调数据失败!标号"+loan.getId());
		} catch (InvalidExpectTimeException e1) {
			throw new UmPayOperationException("预计执行时间必须在当前时间之后");
		} catch (InsufficientBalance e1) {
			throw new UmPayOperationException("余额不足，无法支付借款保证金");
		}
		return null;
	}

	
	
	@Override
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
	}

	@Override
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
	}

}
