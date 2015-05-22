package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.jdp2p.repay.service.RepayService;

/**
 * 检查项目逾期
 * @author Administrator
 *
 */
@Component
public class LoanOverdueCheck implements Job{
	
	@Resource
	private RepayService repayService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		repayService.checkLoanOverdue();
	}
}
