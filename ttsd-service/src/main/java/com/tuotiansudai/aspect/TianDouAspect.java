package com.tuotiansudai.aspect;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.impl.RankingActivityServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class TianDouAspect {

    private static Logger logger = Logger.getLogger(TianDouAspect.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @After(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.debug("after returning invest, tianDou assign starting...");
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[1];
        try {
            String loginName = investModel.getLoginName();
            long amount = investModel.getAmount();
            long loanId = investModel.getLoanId();
            LoanModel loanModel = loanMapper.findById(loanId);
            long period = loanModel.getPeriods();
            long tianDouScore = amount / 12 * period;

            String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

            // 用户天豆获取记录。<List>loginName:amount+score+desc+time
            String value = amount + "+" + tianDouScore + "+" + loanId + "+" + time;
            redisWrapperClient.lpush(RankingActivityServiceImpl.TIAN_DOU_INVEST_SCORE_RECORD + loginName, value);
            redisWrapperClient.zincrby(RankingActivityServiceImpl.TIAN_DOU_USER_SCORE_RANK, tianDouScore, loginName);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        logger.debug("after returning invest, tianDou assign completed");
    }
}
