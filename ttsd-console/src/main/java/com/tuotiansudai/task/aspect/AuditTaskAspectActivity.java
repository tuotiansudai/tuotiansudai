package com.tuotiansudai.task.aspect;


import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationType;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditTaskAspectActivity {


    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AccountService accountService;

    @Autowired
    ActivityMapper activityMapper;

    static Logger logger = Logger.getLogger(AuditTaskAspectLoan.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.service.ActivityService.createEditRecheckActivity(..))", returning = "returnValue")
    public void afterCreateEditRecheckActivity(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after create edit recheck activity aspect.");
        try {
            ActivityDto activityDto = (ActivityDto) joinPoint.getArgs()[0];
            ActivityStatus activityStatus = (ActivityStatus) joinPoint.getArgs()[1];
            String loginName = (String) joinPoint.getArgs()[2];
            String ip = (String) joinPoint.getArgs()[3];

            String realName = accountService.getRealName(loginName);

            ActivityModel activityModelExist = activityMapper.findById(activityDto.getActivityId());
            String description;

            String creator = activityModelExist.getCreatedBy();
            String creatorRealName = accountService.getRealName(creator);

            switch (activityStatus) {
                case TO_APPROVE:
                    if (activityDto.getActivityId() == null) {
                        description = realName + " 创建了活动［" + activityDto.getTitle() + "］。";
                    } else {
                        description = realName + " 编辑了活动［" + activityDto.getTitle() + "］。";
                    }
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
                    break;
                case REJECTION:
                    description = realName + " 驳回了 " + creatorRealName + " 创建的活动［" + activityDto.getTitle() + "］。";
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
                case APPROVED:
                    description = realName + " 审核通过了 " + creatorRealName + " 创建的活动［" + activityDto.getTitle() + "］。";
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
            }
        } catch (Exception e) {
            logger.error("after create edit recheck activity aspect fail ", e);
        }
    }
}
