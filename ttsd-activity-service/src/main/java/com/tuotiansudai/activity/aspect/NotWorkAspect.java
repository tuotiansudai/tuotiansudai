package com.tuotiansudai.activity.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.NotWorkService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
public class NotWorkAspect {

    final private static Logger logger = Logger.getLogger(NotWorkAspect.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    NotWorkService notWorkService;

    @Autowired
    LoanDetailsMapper loanDetailsMapper;

    @Autowired
    InvestMapper investMapper;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "加薪专享";

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterInvest(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        if (null == investModel) {
            logger.error("not work activity invest query null.");
            return;
        }
        if (investModel.getLoanId() == 1) {
            //体验标不参与活动
            return;
        }
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
        if (null == loanDetailsModel) {
            logger.error("not work activity loanDetails query null.");
            return;
        }

        String loginName = investModel.getLoginName();
        long amount = investModel.getAmount();

        try {
            if (loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)) {
                notWorkService.userInvest(loginName, amount);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0} don`t increase invest amount.", loginName, e));
        }

        try {
            UserModel userModel = userMapper.findByLoginName(loginName);
            if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
                notWorkService.recommendedInvest(loginName, amount);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}`s referrer don`t increase recommend invest amount.", loginName, e));
        }
    }
}
