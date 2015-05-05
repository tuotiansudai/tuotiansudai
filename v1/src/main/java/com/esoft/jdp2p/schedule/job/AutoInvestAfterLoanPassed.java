package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.invest.service.AutoInvestService;
import com.esoft.jdp2p.invest.service.impl.AutoInvestServiceImpl;
import com.esoft.jdp2p.loan.service.LoanService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 借款审核通过以后，开启当前借款的自动投标
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-8 下午4:14:15
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-8 wangzhi 1.0
 */
@Component
public class AutoInvestAfterLoanPassed implements Job {

	public static final String LOAN_ID = "aialp_loan_id";

	@Resource
	private AutoInvestService autoInvestService;

	Log log = LogFactory.getLog(AutoInvestAfterLoanPassed.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String loanId = context.getJobDetail().getJobDataMap()
					.getString(LOAN_ID);
			if (log.isDebugEnabled()) {
				log.debug("autoInvestJob, loanId: " + loanId);
			}
			autoInvestService.autoInvest(loanId);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
		}
	}
}
