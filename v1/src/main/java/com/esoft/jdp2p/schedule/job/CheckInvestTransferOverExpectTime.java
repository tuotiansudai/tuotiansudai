package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.esoft.jdp2p.invest.service.TransferService;
/**
 * 检查债权转让是否在转让申请有效期内
 * @author wangxiao
 *
 */
public class CheckInvestTransferOverExpectTime implements Job{
	
	public static final String INVEST_TRANSFER_ID = "investTransferId";
	@Resource
	private TransferService transferService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String investTransferId = context.getJobDetail().getJobDataMap().getString(INVEST_TRANSFER_ID);
		transferService.dealOverExpectTime(investTransferId);
		
	}

}
