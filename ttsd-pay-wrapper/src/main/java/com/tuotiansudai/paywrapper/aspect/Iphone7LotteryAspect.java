package com.tuotiansudai.paywrapper.aspect;


import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class Iphone7LotteryAspect {
    private static Logger logger = Logger.getLogger(Iphone7LotteryAspect.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IPhone7InvestLotteryMapper iPhone7InvestLotteryMapper;

    @Autowired
    private IPhone7LotteryConfigMapper iPhone7LotteryConfigMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.startTime}\")}")
    private Date activityIphone7StartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.endTime}\")}")
    private Date activityIphone7EndTime;

    private static final String redisKey = "web:iphone7:lottery:number";

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        logger.info("after returning invest,iphone7 aspect starting...");
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        this.getLotteryNumber(investModel);
        logger.info("after returning invest, iphone7 aspect completed");
    }

    @Transactional
    public void getLotteryNumber(InvestModel investModel){
        String lotteryNumber = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        List<String> lotteryNumberList = redisWrapperClient.lrange(redisKey, 0, -1);
        for(String existsLotteryNumber : lotteryNumberList){
            lotteryNumber = existsLotteryNumber.equals(lotteryNumber) ? String.valueOf((int)((Math.random() * 9 + 1) * 100000)) : lotteryNumber;
        }

        IPhone7LotteryConfigModel iphone7LotteryConfigModel = iPhone7LotteryConfigMapper.findByLotteryNumber(String.valueOf(lotteryNumber));
        IPhone7InvestLotteryModel iPhone7InvestLotteryModel = iPhone7InvestLotteryMapper.findByLotteryNumber(String.valueOf(lotteryNumber));
        lotteryNumber = (iphone7LotteryConfigModel == null && iPhone7InvestLotteryModel == null) ? lotteryNumber : String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        IPhone7InvestLotteryModel model = new IPhone7InvestLotteryModel(investModel.getId(), investModel.getLoginName(), investModel.getAmount(), String.valueOf(lotteryNumber));
        iPhone7InvestLotteryMapper.create(model);
        redisWrapperClient.lpush(redisKey, lotteryNumber);
        logger.debug(MessageFormat.format("invest success: investId_{0},amount_{1},lotteryNumber_{2}",investModel.getId(), investModel.getAmount(), lotteryNumber));

        long totalAmount = investMapper.sumInvestAmountRanking(activityIphone7StartTime, activityIphone7EndTime);
        logger.debug(MessageFormat.format("currentTotalInvestAmount: {0} ", totalAmount));

        List<IPhone7LotteryConfigModel> iphone7LotteryConfigModelList = iPhone7LotteryConfigMapper.findAllApproved();
        iphone7LotteryConfigModelList.stream()
                .filter(iPhone7LotteryConfigModel -> totalAmount >= iphone7LotteryConfigModel.getInvestAmount() * 1000000)
                .forEach(iPhone7LotteryConfigModel -> {
                    logger.debug(MessageFormat.format("lottery start ...... totalAmount:{0}", totalAmount));
                    iPhone7LotteryConfigMapper.effective(iphone7LotteryConfigModel.getId());
                    logger.debug(MessageFormat.format("ConfigMapper has update ......configId:{0}", iphone7LotteryConfigModel.getId()));
                    IPhone7InvestLotteryModel iPhone7InvestLotteryModelWinner = iPhone7InvestLotteryMapper.findByLotteryNumber(iPhone7LotteryConfigModel.getLotteryNumber());
                    if (iPhone7InvestLotteryModelWinner != null) {
                        logger.debug(MessageFormat.format("lottery lotteryNumber:{0}", iphone7LotteryConfigModel.getLotteryNumber()));
                        iPhone7InvestLotteryMapper.updateByLotteryNumber(iphone7LotteryConfigModel.getLotteryNumber());
                    }
                    logger.debug(MessageFormat.format("lottery end ...... totalAmount:{0}", totalAmount));
                });
    }

}
