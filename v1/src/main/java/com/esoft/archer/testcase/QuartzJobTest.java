package com.esoft.archer.testcase;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.jdp2p.invest.service.AutoInvestService;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.service.RepayService;

/**
 * 还款到期，自动扣款，自动还款，顺便如果账户余额不足，那么项目变为逾期。
 * @author Administrator
 *
 */
@Component
public class QuartzJobTest implements Job{
	
	@Resource
	private TestService testService;
	
	@Resource
	private HibernateTemplate ht;
	
	@Resource
	private AutoInvestService autoInvestService;
	
	@Override
//	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
//		testService.test();
		autoInvestService.autoInvest("123");
		System.out.println(123);
	}
}
