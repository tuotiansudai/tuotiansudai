package com.tuotiansudai.message.aspect;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.util.DistrictUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Aspect
@Component
public class MessageCreateAspect {

    private final static String LOAN_MESSAGE_REDIS_KEY = "web:loan:loanMessageMap";

    private final static String LOAN_ID_KEY = "LOAN_ID";

    private static Logger logger = Logger.getLogger(MessageCreateAspect.class);

    @Autowired
    MessageService messageService;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    @Pointcut("execution(* *..LoanCreateService.createLoan(..))")
    public void createLoanPointcut() {
    }

    @Pointcut("execution(* *..LoanCreateService.updateLoan(..))")
    public void updateLoanPointcut() {
    }

    @Pointcut("execution(* *..FundraisingStartJob.execute(..))")
    public void startRaisingLoanPointcut() {
    }

    @Pointcut("execution(void com.tuotiansudai.service.AnnounceService.create(..))")
    public void createAnnouncePointcut() {
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "startRaisingLoanPointcut()", returning = "returnValue")
    public void afterStartRaisingLoan(JoinPoint joinPoint, boolean returnValue) {
        JobExecutionContext context = (JobExecutionContext) joinPoint.getArgs()[0];
        long loanId = Long.parseLong(String.valueOf(context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY)));
        try {
            String messageId = redisWrapperClient.hget(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanId));
            if (!Strings.isNullOrEmpty(messageId)) {
                messageService.approveMessage(Long.valueOf(messageId), "sidneygao");
                redisWrapperClient.hdel(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanId));
            }
            logger.info(MessageFormat.format("[Message Event Aspect] after start raising loan pointcut finished. loanId:{0}", loanId));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after start raising loan pointcut is fail. loanId:{0}", loanId), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "createLoanPointcut() || updateLoanPointcut()", returning = "returnValue")
    public void afterCreateOrUpdateLoan(JoinPoint joinPoint, boolean returnValue) {
        LoanCreateRequestDto loanCreateRequestDto = (LoanCreateRequestDto) joinPoint.getArgs()[0];
        if (null == loanCreateRequestDto || Strings.isNullOrEmpty(loanCreateRequestDto.getLoanMessageTitle().trim()) ||
                Strings.isNullOrEmpty(loanCreateRequestDto.getLoanMessageContent().trim())) {
            return;
        }
        try {
            MessageCreateDto messageCreateDto = new MessageCreateDto();

            messageCreateDto.setTitle(loanCreateRequestDto.getLoanMessageTitle());
            messageCreateDto.setTemplate(loanCreateRequestDto.getLoanMessageContent());
            messageCreateDto.setTemplateTxt(loanCreateRequestDto.getLoanMessageContent());
            messageCreateDto.setType(MessageType.MANUAL);
            messageCreateDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
            messageCreateDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
            messageCreateDto.setManualMessageType(ManualMessageType.NOTIFY);
            messageCreateDto.setWebUrl(MessageFormat.format("/loan/{0}", loanCreateRequestDto.getLoan().getId()));
            messageCreateDto.setAppUrl(AppUrl.NOTIFY);
            messageCreateDto.setJpush(true);
            messageCreateDto.setPushType(PushType.IMPORTANT_EVENT);
            messageCreateDto.setPushSource(PushSource.ALL);
            messageCreateDto.setPushDistricts(DistrictUtil.getAllCodes());
            messageCreateDto.setStatus(MessageStatus.APPROVED);
            messageCreateDto.setReadCount(0);
            messageCreateDto.setActivatedBy(null);
            messageCreateDto.setActivatedTime(null);
            messageCreateDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
            messageCreateDto.setUpdatedBy(loanCreateRequestDto.getLoan().getCreatedBy());
            messageCreateDto.setUpdatedTime(new Date());
            messageCreateDto.setCreatedBy(loanCreateRequestDto.getLoan().getCreatedBy());
            messageCreateDto.setCreatedTime(new Date());

            long messageId = messageService.createAndEditManualMessage(messageCreateDto, 0);
            redisWrapperClient.hset(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId()), String.valueOf(messageId));
            logger.info(MessageFormat.format("[Message Event Aspect] after create/update loan pointcut finished. loanId:{0}", loanCreateRequestDto.getLoan().getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after create/update loan pointcut is fail. loanId:{0}", loanCreateRequestDto.getLoan().getId()), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "createAnnouncePointcut()", returning = "returnValue")
    public void afterCreateAnnounce(JoinPoint joinPoint, boolean returnValue) {
        AnnounceDto announceDto = (AnnounceDto) joinPoint.getArgs()[0];
        String createdBy = (String) joinPoint.getArgs()[1];

        try {
            MessageCreateDto messageCreateDto = new MessageCreateDto();

            messageCreateDto.setTitle(announceDto.getTitle());
            messageCreateDto.setTemplate(announceDto.getContent());
            messageCreateDto.setTemplateTxt(announceDto.getContentText());
            messageCreateDto.setType(MessageType.MANUAL);
            messageCreateDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
            messageCreateDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
            messageCreateDto.setManualMessageType(ManualMessageType.NOTIFY);
            messageCreateDto.setWebUrl(MessageFormat.format("/announce/{0}", announceDto.getId()));
            messageCreateDto.setAppUrl(AppUrl.NOTIFY);
            messageCreateDto.setJpush(true);
            messageCreateDto.setPushType(PushType.IMPORTANT_EVENT);
            messageCreateDto.setPushSource(PushSource.ALL);
            messageCreateDto.setPushDistricts(DistrictUtil.getAllCodes());
            messageCreateDto.setStatus(MessageStatus.APPROVED);
            messageCreateDto.setReadCount(0);
            messageCreateDto.setActivatedBy(createdBy);
            messageCreateDto.setActivatedTime(new Date());
            messageCreateDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
            messageCreateDto.setUpdatedBy(createdBy);
            messageCreateDto.setUpdatedTime(new Date());
            messageCreateDto.setCreatedBy(createdBy);
            messageCreateDto.setCreatedTime(new Date());

            long messageId = messageService.createAndEditManualMessage(messageCreateDto, 0);
            messageService.approveMessage(messageId, createdBy);
            logger.info(MessageFormat.format("[Message Event Aspect] after create announce pointcut finished. announceId:{0}", announceDto.getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after create announce pointcut is fail. announceId:{0}", announceDto.getId()), e);
        }
    }
}
