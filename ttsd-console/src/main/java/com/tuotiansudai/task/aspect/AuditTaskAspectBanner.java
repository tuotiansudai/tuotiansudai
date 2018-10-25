package com.tuotiansudai.task.aspect;

import com.tuotiansudai.activity.repository.dto.BannerDto;
import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.AuditLogMessage;
import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class AuditTaskAspectBanner {
    @Autowired
    private MQWrapperClient mqWrapperClient;
    @Autowired
    private UserService userService;

    static Logger logger = Logger.getLogger(AuditTaskAspectBanner.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @AfterReturning(value = "execution(* com.tuotiansudai.activity.service.BannerService.create(..))")
    public void afterReturningCreateBanner(JoinPoint joinPoint) {
        logger.info("after create banner aspect.");
        try {
            BannerDto bannerDto = (BannerDto) joinPoint.getArgs()[0];
            String loginName = (String) joinPoint.getArgs()[1];
            String ip = (String) joinPoint.getArgs()[2];

            String description = loginName + "在 " + sdf.format(new Date()) + "创建了" + "名称为:" + bannerDto.getName() + "的banner.";
            String mobile = userService.getMobile(loginName);
            mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(loginName, loginName, OperationType.BANNER, bannerDto.getName(), description, ip, mobile, mobile));
        } catch (Exception e) {
            logger.error("after create banner aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.activity.service.BannerService.updateBanner(..))")
    public void afterReturningUpdateBanner(JoinPoint joinPoint) {
        logger.info("after update banner aspect.");
        try {
            BannerModel bannerModel = (BannerModel) joinPoint.getArgs()[0];
            String loginName = (String) joinPoint.getArgs()[1];
            String ip = (String) joinPoint.getArgs()[2];

            String description = loginName + "在 " + sdf.format(new Date()) + "修改了" + "名称为:" + bannerModel.getName() + "的banner.";
            String mobile = userService.getMobile(loginName);
            mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(loginName, loginName, OperationType.BANNER, bannerModel.getName(), description, ip, mobile, mobile));
        } catch (Exception e) {
            logger.error("after update banner aspect fail ", e);
        }
    }


}
