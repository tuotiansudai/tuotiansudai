package com.tuotiansudai.paywrapper.aspect;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsTransferLoanNotifyDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Aspect
@Component
public class AdvancedRepayAspect {

    private static Logger logger = Logger.getLogger(AdvancedRepayAspect.class);

    @Autowired
    LoanRepayMapper loanRepayMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    TransferApplicationMapper transferApplicationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SmsWrapperClient smsWrapperClient;

    @AfterReturning(value = "execution(* *..AdvanceRepayService.paybackInvest(*))", returning = "returnValue")
    public void afterReturningInvestSuccess(JoinPoint joinPoint, boolean returnValue) {
        long loanRepayId = (Long) joinPoint.getArgs()[0];

        logger.info(MessageFormat.format("[advanced repay {0}] return Value ({1}) aspect is starting...",
                String.valueOf(loanRepayId), String.valueOf(returnValue)));

        if (!returnValue) {
            logger.info(MessageFormat.format("[advanced repay {0}] return Value ({1}) aspect is done",
                    String.valueOf(loanRepayId), String.valueOf(returnValue)));
            return;
        }

        long loanId = loanRepayMapper.findById(loanRepayId).getLoanId();

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findAllTransferringApplicationsByLoanId(loanId);

        for (TransferApplicationModel transferApplicationModel : transferApplicationModels) {
            transferApplicationModel.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferApplicationModel);

            logger.info(MessageFormat.format("Transfer Loan id: {0} is canceled because of advanced repay.", transferApplicationModel.getId()));

            String mobile = userMapper.findByLoginName(transferApplicationModel.getLoginName()).getMobile();
            smsWrapperClient.sendCancelTransferLoanNotify(new SmsTransferLoanNotifyDto(mobile, transferApplicationModel.getName()));
        }

        logger.info(MessageFormat.format("[advanced repay {0}] return Value ({1}) aspect is done",
                String.valueOf(loanRepayId), String.valueOf(returnValue)));
    }
}
