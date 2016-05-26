package com.tuotiansudai.aspect;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.impl.RankingActivityServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class TianDouAspect {

    private static Logger logger = Logger.getLogger(TianDouAspect.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    @After(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after returning invest, tianDou assign starting...");
        DateTime activityStartTime = new DateTime(2016, 4, 1, 0, 0, 0);
        DateTime now = new DateTime();
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];

        if (now.isBefore(activityStartTime)) {
            logger.info("ranking activity not started. Start time is: 2016-04-01 00:00:00. loginName:" + investModel.getLoginName()
                    + ", loanId:" + investModel.getLoanId() + ", amount:" + investModel.getAmount());
        } else {
            String loginName = investModel.getLoginName();
            if (judgeUserRoleExist(loginName, Role.LOANER)) {
                logger.info(loginName + " is a loaner, won't add tiandou for him.");
                return;
            } else {
                long amount = investModel.getAmount();
                long loanId = investModel.getLoanId();
                LoanModel loanModel = loanMapper.findById(loanId);
                long period = loanModel.getPeriods();
                long tianDouScore = new BigDecimal((double) amount * period / 1200).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

                String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HH:mm:ss");

                // 用户天豆获取记录。<List>loginName:amount+score+desc+time
                String value = amount + "+" + tianDouScore + "+" + loanId + "+" + time;
                redisWrapperClient.lpush(RankingActivityServiceImpl.TIAN_DOU_INVEST_SCORE_RECORD + loginName, value);
                redisWrapperClient.zincrby(RankingActivityServiceImpl.TIAN_DOU_USER_SCORE_RANK, tianDouScore, loginName);
            }
            logger.debug("after returning invest, tianDou assign completed");
        }
    }

    private boolean judgeUserRoleExist(String loginName, Role role) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        for (UserRoleModel userRoleModel : userRoleModels) {
            if (userRoleModel.getRole() == role) {
                return true;
            }
        }
        return false;
    }

}
