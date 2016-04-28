package com.esoft.jdp2p.schedule.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.loan.service.LoanService;

/**
 * 检查项目是否到预计执行时间
 * @author Administrator=
 *
 */
public class CheckLoanOverExpectTime implements Job{
	
	public static final String LOAN_ID = "loanId";
	
	private LoanService loanService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		loanService = (LoanService) SpringBeanUtil.getBeanByName("loanService");
		String loanId = context.getJobDetail().getJobDataMap().getString(LOAN_ID);
		loanService.dealOverExpectTime(loanId);
	}
}
