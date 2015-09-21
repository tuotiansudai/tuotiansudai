package com.esoft.jdp2p.schedule.job;

import com.ttsd.special.model.ReceiveStatus;
import com.ttsd.special.services.InvestLotteryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrantCashPrizeJob implements Job {

    static Log log = LogFactory.getLog(GrantCashPrizeJob.class);

    public static final String LOTTERY_ID = "lotteryId";

    public static final String BONUS = "bonus";

    public static final String USERID = "userId";

    @Autowired
    private InvestLotteryService investLotteryService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String lotteryId = jobExecutionContext.getJobDetail().getJobDataMap().getString(LOTTERY_ID);
        String userId = jobExecutionContext.getJobDetail().getJobDataMap().getString(USERID);
        String bonus = jobExecutionContext.getJobDetail().getJobDataMap().getString(BONUS);
        log.info("winningPersonIncome----begin");
        boolean isSuccess = investLotteryService.winningPersonIncome("" + lotteryId,bonus,userId);
        log.info("winningPersonIncome----end");
        if(isSuccess){
            investLotteryService.updateInvestLotteryGranted(Long.parseLong(lotteryId), ReceiveStatus.RECEIVED);
        }else{
            investLotteryService.updateInvestLotteryGranted(Long.parseLong(lotteryId), ReceiveStatus.FAILED);
            log.error("investLottery : " + lotteryId + ", userId : " + userId + " grant prize failed !");
        }
    }
}
