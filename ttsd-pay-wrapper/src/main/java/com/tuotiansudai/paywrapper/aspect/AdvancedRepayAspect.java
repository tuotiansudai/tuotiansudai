package com.tuotiansudai.paywrapper.aspect;

import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.smswrapper.service.SmsService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    SmsService smsService;

    @After(value = "execution(* *..AdvanceRepayService.paybackInvest(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        long loanRepayId = (Long) joinPoint.getArgs()[0];
        long loanId = loanRepayMapper.findById(loanRepayId).getLoanId();

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findAllTransferringApplicationsByLoanId(loanId);

        for (TransferApplicationModel transferApplicationModel : transferApplicationModels) {
            transferApplicationModel.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferApplicationModel);
            String mobile = userMapper.findByLoginName(transferApplicationModel.getLoginName()).getMobile();
            smsService.cancelTransferLoan(mobile, transferApplicationModel.getName());
        }
    }
}
