package com.esoft.umpay.loan.service.impl;

import java.io.IOException;
import java.util.Map;

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
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.trusteeship.UmPayConstants.UpdateProjectStatus;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;

/**
 * 
 * Description : 通过审核-开标操作
 * @author zt
 * @data 2015-3-9下午9:52:12
 */
@Service("umPayPassLoanApplyOperation")
public class UmPayPassLoanApplyOperation extends UmPayOperationServiceAbs<Loan>{

	
	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	LoanService loanService;

	@Resource
	HibernateTemplate ht;

	@Logger
	Log log;

	@Resource
	UmPayLoanStatusService umPayLoanStatusService;
	
	/**
	 * 审核标的
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {
		try {
			//开标
			loanService.passApply(loan);
			//将标的更新成:开标
			Map<String, String> resData = umPayLoanStatusService.updateLoanStatusOperation(loan, UpdateProjectStatus.PROJECT_STATE_PASSED, false);
			if(!"0000".equals(resData.get("ret_code"))){
				throw new UmPayOperationException("错误信息:"+resData.get("ret_msg"));
			}
			//将标的更新成:投标
			Map<String, String> resDate2 = umPayLoanStatusService.updateLoanStatusOperation(loan, UpdateProjectStatus.PROJECT_STATE_RAISING, false);
			if(!"0000".equals(resDate2.get("ret_code"))){
				throw new UmPayOperationException("错误信息:"+resData.get("ret_msg"));
			}
		} catch (InvalidExpectTimeException e) {
			throw new UmPayOperationException("项目执行日期不合法");
		} catch (InsufficientBalance e) {
			throw new UmPayOperationException("余额不足");
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
