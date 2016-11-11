package com.tuotiansudai.message.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.message.dto.MessageCompleteDto;
import com.tuotiansudai.message.service.MessageService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
public class MessageCreateAspect {

    private final static String LOAN_MESSAGE_REDIS_KEY = "web:loan:loanMessageMap";

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

    @Pointcut("execution(* *..LoanCreateService.startRaising(..))")
    public void startRaisingLoanPointcut() {
    }

    @Pointcut("execution(void com.tuotiansudai.service.AnnounceService.create(..))")
    public void createAnnouncePointcut() {
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "startRaisingLoanPointcut()")
    public void afterStartRaisingLoan(JoinPoint joinPoint) {
        long loanId = (long) joinPoint.getArgs()[0];
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
    @AfterReturning(value = "createLoanPointcut() || updateLoanPointcut()")
    public void afterCreateOrUpdateLoan(JoinPoint joinPoint) {
        LoanCreateRequestDto loanCreateRequestDto = (LoanCreateRequestDto) joinPoint.getArgs()[0];
        if (null == loanCreateRequestDto || Strings.isNullOrEmpty(loanCreateRequestDto.getLoanMessage().getLoanMessageTitle().trim()) ||
                Strings.isNullOrEmpty(loanCreateRequestDto.getLoanMessage().getLoanMessageContent().trim())) {
            return;
        }
        try {
            MessageCompleteDto messageCompleteDto = MessageCompleteDto.createFromLoanCreateRequestDto(loanCreateRequestDto);

            long messageId = messageService.createAndEditManualMessage(messageCompleteDto, 0);
            redisWrapperClient.hset(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId()), String.valueOf(messageId));
            logger.info(MessageFormat.format("[Message Event Aspect] after create/update loan pointcut finished. loanId:{0}", loanCreateRequestDto.getLoan().getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after create/update loan pointcut is fail. loanId:{0}", loanCreateRequestDto.getLoan().getId()), e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "createAnnouncePointcut()")
    public void afterCreateAnnounce(JoinPoint joinPoint) {
        AnnounceDto announceDto = (AnnounceDto) joinPoint.getArgs()[0];
        String createdBy = (String) joinPoint.getArgs()[1];

        try {
            MessageCompleteDto messageCompleteDto = MessageCompleteDto.createFromAnnounceDto(announceDto, createdBy);
            long messageId = messageService.createAndEditManualMessage(messageCompleteDto, 0);
            messageService.approveMessage(messageId, createdBy);
            logger.info(MessageFormat.format("[Message Event Aspect] after create announce pointcut finished. announceId:{0}", announceDto.getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after create announce pointcut is fail. announceId:{0}", announceDto.getId()), e);
        }
    }
}
