package com.esoft.jdp2p.schedule.service.impl;

import javax.annotation.Resource;

import com.esoft.jdp2p.schedule.job.*;
import org.apache.commons.logging.Log;
import org.hibernate.ObjectNotFoundException;
import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.schedule.ScheduleConstants;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 项目启动以后，初始化调度
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-10 下午12:52:57
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-10 wangzhi 1.0
 */
@Component
public class InitJobs implements ApplicationListener<ContextRefreshedEvent> {

	@Resource
	StdScheduler scheduler;

	@Logger
	static Log log;

	@Resource
	ConfigService configService;

	/**
	 * 开启哪些调度，能手动控制
	 * 
	 * @throws SchedulerException
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			// root application context 没有parent，他就是老大
			// 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。 初始化所有的调度。

			// 第三方资金托管，主动查询，默认不开启
			String enableRefreshTrusteeship = "0";
			try {
				enableRefreshTrusteeship = configService
						.getConfigValue(ConfigConstants.Schedule.ENABLE_REFRESH_TRUSTEESHIP);
			} catch (ObjectNotFoundException onfe) {
				onfe.printStackTrace();
			}

			// 自动还款+检查项目逾期，默认开启
			// FIXME:自动还款应该默认是关闭的
			String enableAutoRepayment = "1";
			try {
				enableAutoRepayment = configService
						.getConfigValue(ConfigConstants.Schedule.ENABLE_AUTO_REPAYMENT);
			} catch (ObjectNotFoundException onfe) {
				onfe.printStackTrace();
			}
			String enableRepayAlert = "0";
			try {
				enableRepayAlert = configService
						.getConfigValue(ConfigConstants.Schedule.ENABLE_REPAY_ALERT);
			} catch (ObjectNotFoundException onfe) {
				onfe.printStackTrace();
			}
			try {
				if (enableRefreshTrusteeship.equals("1")) {
					if (log.isDebugEnabled()) {
						log.debug("enable refresh trusteeship schdule job");
					}
					// 第三方资金托管，主动查询
					CronTrigger trigger = (CronTrigger) scheduler
							.getTrigger(TriggerKey
									.triggerKey(
											ScheduleConstants.TriggerName.REFRESH_TRUSTEESHIP_OPERATION,
											ScheduleConstants.TriggerGroup.REFRESH_TRUSTEESHIP_OPERATION));
					if (trigger == null) {
						initRefreshTrusteeshipJob();
					} else {
						scheduler.resumeTrigger(trigger.getKey());
					}
				}

				if (enableAutoRepayment.equals("1")) {
					if (log.isDebugEnabled()) {
						log.debug("enable auto repayment schdule job");
					}
					// 到期自动还款，改状态（还款完成、逾期之类）
					CronTrigger trigger = (CronTrigger) scheduler
							.getTrigger(TriggerKey
									.triggerKey(
											ScheduleConstants.TriggerName.AUTO_REPAYMENT,
											ScheduleConstants.TriggerGroup.AUTO_REPAYMENT));
					if (trigger == null) {
						initAutoRepaymentJob();
					} else {
						scheduler.resumeTrigger(trigger.getKey());
					}
				}

				if (enableRepayAlert.equals("1")) {
					// 还款提醒
					CronTrigger trigger = (CronTrigger) scheduler
							.getTrigger(TriggerKey.triggerKey(
									ScheduleConstants.TriggerName.REPAY_ALERT,
									ScheduleConstants.TriggerGroup.REPAY_ALERT));
					if (trigger == null) {
						JobDetail jobDetail = JobBuilder
								.newJob(RepayAlert.class)
								.withIdentity(
										ScheduleConstants.TriggerName.REPAY_ALERT,
										ScheduleConstants.TriggerGroup.REPAY_ALERT)
								.build();

						trigger = TriggerBuilder
								.newTrigger()
								.withIdentity(
										ScheduleConstants.TriggerName.REPAY_ALERT,
										ScheduleConstants.TriggerGroup.REPAY_ALERT)
								.forJob(jobDetail)
								.withSchedule(
								// 每天上午九点
										CronScheduleBuilder
												.cronSchedule("0 0 9 * * ? *"))
								.build();

						scheduler.scheduleJob(jobDetail, trigger);
					} else {
						scheduler.rescheduleJob(trigger.getKey(), trigger);
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("start loan overdue check schdule job");
				}
				// 借款逾期调度
				CronTrigger trigger = (CronTrigger) scheduler
						.getTrigger(TriggerKey
								.triggerKey(
										ScheduleConstants.TriggerName.LOAN_OVERDUE_CHECK,
										ScheduleConstants.TriggerGroup.LOAN_OVERDUE_CHECK));
				if (trigger == null) {
					initLoanOverdueCheckJob();
				} else {
					scheduler.resumeTrigger(trigger.getKey());
				}

				// 借款逾期调度
				CronTrigger activityRewardTrigger = (CronTrigger) scheduler
						.getTrigger(TriggerKey.triggerKey(ScheduleConstants.TriggerName.AUTO_ACTIVITY_REWARD,
								ScheduleConstants.TriggerGroup.AUTO_ACTIVITY_REWARD));
				if (activityRewardTrigger == null) {
					this.initAutoActivityRewardJob();
				} else {
					scheduler.resumeTrigger(activityRewardTrigger.getKey());
				}
			} catch (SchedulerException e1) {
				throw new RuntimeException(e1);
			}

		}
	}

	/**
	 * 检查项目逾期
	 * 
	 * @throws SchedulerException
	 */
	private void initLoanOverdueCheckJob() throws SchedulerException {
		JobDetail jobDetail = JobBuilder
				.newJob(LoanOverdueCheck.class)
				.withIdentity(ScheduleConstants.JobName.LOAN_OVERDUE_CHECK,
						ScheduleConstants.JobGroup.LOAN_OVERDUE_CHECK).build();

		CronTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(ScheduleConstants.TriggerName.LOAN_OVERDUE_CHECK,
						ScheduleConstants.TriggerGroup.LOAN_OVERDUE_CHECK)
				.forJob(jobDetail).withSchedule(
				// 每天1点
						CronScheduleBuilder.cronSchedule("0 0 1 * * ? *"))
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 到期自动还款
	 * 
	 * @throws SchedulerException
	 */
	private void initAutoRepaymentJob() throws SchedulerException {
		JobDetail jobDetail = JobBuilder
				.newJob(AutoRepayment.class)
				.withIdentity(ScheduleConstants.JobName.AUTO_REPAYMENT,
						ScheduleConstants.JobGroup.AUTO_REPAYMENT).build();

		CronTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(ScheduleConstants.TriggerName.AUTO_REPAYMENT,
						ScheduleConstants.TriggerGroup.AUTO_REPAYMENT)
				.forJob(jobDetail).withSchedule(
				// 每天0点1分
						CronScheduleBuilder.cronSchedule("0 1 0 * * ? *"))
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 资金托管主动查询
	 * 
	 * @throws SchedulerException
	 */
	private void initRefreshTrusteeshipJob() throws SchedulerException {
		JobDetail jobDetail2 = JobBuilder
				.newJob(RefreshTrusteeshipOperation.class)
				.withIdentity(
						ScheduleConstants.JobName.REFRESH_TRUSTEESHIP_OPERATION,
						ScheduleConstants.JobGroup.REFRESH_TRUSTEESHIP_OPERATION)
				.build();

		CronTrigger trigger2 = TriggerBuilder
				.newTrigger()
				.withIdentity(
						ScheduleConstants.TriggerName.REFRESH_TRUSTEESHIP_OPERATION,
						ScheduleConstants.TriggerGroup.REFRESH_TRUSTEESHIP_OPERATION)
				.forJob(jobDetail2).withSchedule(
				// 每十分钟执行一次
						CronScheduleBuilder.cronSchedule("0 0/2 * * * ? *"))
				.build();

		scheduler.scheduleJob(jobDetail2, trigger2);
	}

	/**
	 * 检查项目逾期
	 * 
	 * @throws SchedulerException
	 */
	private void initRefreshUserLoanstatusJob() throws SchedulerException {

	}

	/**
	 * 资金托管主动查询
	 *
	 * @throws SchedulerException
	 */
	private void initAutoActivityRewardJob() throws SchedulerException {
		DateTime triggerTime = new DateTime().plusMinutes(10);

		JobDetail jobDetail = JobBuilder.newJob(AutoActivityRewardJob.class)
				.withIdentity(ScheduleConstants.JobName.AUTO_ACTIVITY_REWARD, ScheduleConstants.JobGroup.AUTO_ACTIVITY_REWARD)
				.build();

		SimpleTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(ScheduleConstants.TriggerName.AUTO_ACTIVITY_REWARD, ScheduleConstants.TriggerGroup.AUTO_ACTIVITY_REWARD)
				.forJob(jobDetail)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.startAt(triggerTime.toDate())
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
	}
}
