package com.esoft.archer.testcase;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.CheckLoanOverExpectTime;


public class QuartzTest  {
		private static SchedulerFactory sf = new StdSchedulerFactory(); 
		private static String JOB_GROUP_NAME = "ddlib";
		private static String TRIGGER_GROUP_NAME = "ddlibTrigger";
	
	
	  /**添加一个定时任务，使用默认的任务组名，触发器名，触发器组名*/
	   public static void addJob(String jobName,Job job,String cronExpression)
	                               throws SchedulerException, ParseException{
		   addJob(jobName,null,jobName,null,job,cronExpression);
	   }
	  
	   /** 
	    * 添加一个定时任务
	    * @param jobName 任务名
	    * @param jobGroupName 任务组名
	    * @param triggerName 触发器名
	    * @param triggerGroupName 触发器组名
	    * @param job     任务
	    * @param cronExpression    时间设置，参考quartz说明文档
	    */
	   public static void addJob(String jobName,String jobGroupName,
	                             String triggerName,String triggerGroupName,Job job,String cronExpression)
	                               throws SchedulerException, ParseException{
		   if(StringUtils.isBlank(jobGroupName)){
			   jobGroupName = JOB_GROUP_NAME;
		   }
		   if(StringUtils.isBlank(triggerGroupName)){
			   triggerGroupName = TRIGGER_GROUP_NAME;
		   }
		   Scheduler sched = sf.getScheduler();
		   JobDetail jobDetail = JobBuilder
					.newJob(job.getClass())
					.withIdentity(
							jobName,
							jobGroupName)
					.build();
		   
		   CronTrigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("auto_repayment", "auto_repayment")
					.forJob(jobDetail)
					.withSchedule(
							CronScheduleBuilder.cronSchedule("5/5 * * * * ? *"))
					.build();
	       sched.scheduleJob(jobDetail,trigger);
	       //启动
//	       if(!sched.isShutdown()){
//	    	   sched.start();
//	       }
	   }
	  
	   /** 
//	    * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
//	    */
//	   public static void modifyJobTime(String jobName,String cronExpression)
//	                                  throws SchedulerException, ParseException{
//		   modifyJobTime(jobName, null, cronExpression);
//	   }
//	  
//	   /**
//	    * 修改一个任务的触发时间
//	    */
//	   public static void modifyJobTime(String triggerName,String triggerGroupName,
//	                                    String cronExpression)throws SchedulerException, ParseException{
//		   if(StringUtils.isBlank(triggerGroupName)){
//			   triggerGroupName = TRIGGER_GROUP_NAME;
//		   }
//	       Scheduler sched = sf.getScheduler();
//	       Trigger trigger = sched.getTrigger(triggerName,triggerGroupName);
//	       if(trigger != null){
//	           CronTrigger ct = (CronTrigger)trigger;
//	           //修改时间
//	           ct.setCronExpression(cronExpression);
//	           //重启触发器
//	           sched.resumeTrigger(triggerName,triggerGroupName);
//	       }
//	   }
//	  
//	   /**移除一个任务和触发器(使用默认的任务组名，触发器名，触发器组名)*/
//	   public static void removeJob(String jobName,String triggerName)
//	                               throws SchedulerException{
//		   removeJob(jobName, null, triggerName, null);
//	   }
//	  
//	   /**移除一个任务和触发器 */
//	   public static void removeJob(String jobName,String jobGroupName,
//	                                String triggerName,String triggerGroupName)
//	                               throws SchedulerException{
//		   if(StringUtils.isBlank(jobGroupName)){
//			   jobGroupName = JOB_GROUP_NAME;
//		   }
//		   if(StringUtils.isBlank(triggerGroupName)){
//			   triggerGroupName = TRIGGER_GROUP_NAME;
//		   }
//	       Scheduler sched = sf.getScheduler();
//	       sched.pauseTrigger(triggerName,triggerGroupName);//停止触发器
//	       sched.unscheduleJob(triggerName,triggerGroupName);//移除触发器
//	       sched.deleteJob(jobName,jobGroupName);//删除任务
//	   }
	
	public static void main(String[] args) throws SchedulerException, ParseException {
		addJob("test", new QuartzJobTest(), "*/5 * * * * ?");
//		addJob("zht", new TestJob(), "*/10 * * * * ?");
//		removeJob("myJob","myJobGroup", "myTrigger","myTriggerGroup");
//		removeJob("test","test");
//		removeJob("zht","zht");
	}
	@Resource
	private StdScheduler scheduler;

	public void test() {
//		JobDetail jobDetail = JobBuilder
//				.newJob(AutoRepayment.class)
//				.withIdentity("auto_repayment",
//						ScheduleConstants.JobGroup.AUTO_REPAYMENT).build();// 任务名，任务组，任务执行类
		try {
			// SimpleTrigger trigger = TriggerBuilder
			// .newTrigger()
			// .withIdentity(
			// "auto_repayment",
			// "auto_repayment")
			// .forJob(jobDetail)
			// .withSchedule(SimpleScheduleBuilder.simpleSchedule())
			// .startAt(DateUtil.addSecond(new Date(), 500000)).build();

//			CronTrigger trigger = TriggerBuilder
//					.newTrigger()
//					.withIdentity("auto_repayment", "auto_repayment")
//					.forJob(jobDetail)
//					.withSchedule(
//							CronScheduleBuilder.cronSchedule("5/5 * * * * ? *"))
//					.build();
//			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.deleteJob(JobKey.jobKey("auto_repayment", "auto_repayment"));

			// CronTrigger trigger = (CronTrigger) scheduler.getTrigger(
			// "auto_repayment",
			// ScheduleConstants.TriggerGroup.AUTO_REPAYMENT);
			// if (trigger != null) {
			// System.out.println(trigger.getNextFireTime());
			// // 修改时间
			// trigger.setCronExpression("0 * * * * ? *");
			// // 重启触发器
			// scheduler.resumeTrigger("auto_repayment",
			// ScheduleConstants.TriggerGroup.AUTO_REPAYMENT);
			// // scheduler.start();
			// }

			// SimpleTrigger trigger = (SimpleTrigger) scheduler
			// .getTrigger(TriggerKey
			// .triggerKey(
			// "auto_repayment",
			// "auto_repayment"));
			// if (trigger != null) {
			// // 修改时间
			// Trigger newTrigger = trigger.getTriggerBuilder()
			// .withSchedule(SimpleScheduleBuilder.simpleSchedule())
			// .startAt(DateUtil.addSecond(new Date(), 5)).build();
			// // 重启触发器
			// scheduler.rescheduleJob(trigger.getKey(), newTrigger);
			// }
		} catch (SchedulerException e) {
			e.printStackTrace();
			// } catch (ParseException e) {
			// e.printStackTrace();
		}
		// try {
		// SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(
		// getInstance().getId(),
		// ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME);
		// if (trigger != null) {
		// // 修改时间
		// trigger.setStartTime(getInstance().getExpectTime());
		// // 重启触发器
		// scheduler
		// .resumeTrigger(
		// getInstance().getId(),
		// ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME);
		// }
		// } catch (SchedulerException e) {
		// e.printStackTrace();
		// }
	}
}
