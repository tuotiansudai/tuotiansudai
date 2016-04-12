package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.jdp2p.repay.service.RepayService;

/**
 * 还款提醒
 * @author Administrator
 *
 */
@Component
public class RepayAlert implements Job{
	
	@Resource
	private RepayService repayService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		repayService.repayAlert();
	}
}
