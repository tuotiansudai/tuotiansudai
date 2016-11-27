package com.tuotiansudai.message.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
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
public class MessageLoanAspect {

    private final static String LOAN_MESSAGE_REDIS_KEY = "web:loan:loanMessageMap";

    private static Logger logger = Logger.getLogger(MessageLoanAspect.class);

    @Autowired
    MessageService messageService;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    @Pointcut("execution(* *..LoanCreateService.startRaising(..))")
    public void startRaisingLoanPointcut() {
    }

    @SuppressWarnings(value = "unchecked")
    @AfterReturning(value = "startRaisingLoanPointcut()")
    public void afterStartRaisingLoan(JoinPoint joinPoint) {
        long loanId = (long) joinPoint.getArgs()[0];
        try {
            String messageId = redisWrapperClient.hget(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanId));
            if (!Strings.isNullOrEmpty(messageId)) {
                messageService.approveMessage(Long.valueOf(messageId), "sidneygao");
            }
            logger.info(MessageFormat.format("[Message Event Aspect] after start raising loan pointcut finished. loanId:{0}", loanId));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Message Event Aspect] after start raising loan pointcut is fail. loanId:{0}", loanId), e);
        }
    }
}
