package com.esoft.archer.user.schedule;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;

/**
 * 解锁用户
 * 
 * @author Administrator
 * 
 */

@Component
public class EnableUserJob implements Job {

	public static final String USER_ID = "user_id";

	@Logger
	Log log;

	@Resource
	private UserService userService;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String userId = context.getJobDetail().getJobDataMap().getString(USER_ID);
		try {
			userService.enableUser(userId);
		} catch (UserNotFoundException e) {
			log.info(e);
		}
	}

}
