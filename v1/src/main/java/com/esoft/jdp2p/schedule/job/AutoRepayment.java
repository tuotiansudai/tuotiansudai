package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.jdp2p.repay.service.RepayService;

/**
 * 还款到期，自动扣款，自动还款，顺便如果账户余额不足，那么项目变为逾期。
 * @author Administrator
 *
 */
@Component
public class AutoRepayment implements Job{
	
	@Resource
	private RepayService repayService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		repayService.autoRepay();
	}
}
