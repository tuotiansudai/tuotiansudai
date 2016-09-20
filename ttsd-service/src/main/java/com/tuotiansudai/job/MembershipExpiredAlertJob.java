package com.tuotiansudai.job;

import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipService;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MembershipExpiredAlertJob implements Job {

    @Autowired
    private UserMembershipService userMembershipService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<UserMembershipModel> tomorrowExpiredUserMembership = userMembershipService.getExpiredUserMembership(new DateTime().plusDays(1).toDate());

    }
}
