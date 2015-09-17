package com.ttsd.special.services.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.GrantCashPrizeJob;
import com.google.common.collect.Lists;
import com.ttsd.special.dao.InvestLotteryDao;
import com.ttsd.special.dto.LotteryPrizeResponseDto;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryPrizeType;
import com.ttsd.special.model.InvestLotteryProbabilityType;
import com.ttsd.special.model.InvestLotteryType;
import com.ttsd.special.services.InvestLotteryService;
import org.apache.commons.logging.Log;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/9/9.
 */
@Service
public class InvestLotteryServiceImpl implements InvestLotteryService{
    @Autowired
    private InvestLotteryDao investLotteryDao;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Resource
    StdScheduler scheduler;

    @Logger
    Log log;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIntoInvestLottery(String investId) {
        Invest invest = hibernateTemplate.get(Invest.class,investId);
        Loan loan = invest.getLoan();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<InvestLottery> investLotteryList = hibernateTemplate.find("from InvestLottery t where t.user.id = ? and t.type = ? and t.createdTime like ?",
                new Object[]{invest.getUser().getId(),InvestLotteryType.NORMAL,simpleDateFormat.format(new Date())});
        int total = 0;
        if (invest.getInvestMoney() >= 500 && invest.getInvestMoney() < 1500) {
            total = 1;
        } else if (invest.getInvestMoney() >= 1500) {
            total = 3;
        }
        int nowTotal = investLotteryList.size();
        List<InvestLottery> investLotterys = Lists.newArrayList();
        if (("xs").equals(loan.getLoanActivityType())){
            InvestLottery investLottery = luckyDrawRules(invest.getInvestMoney(), this.getInvestLottery(invest, InvestLotteryType.NOVICE));
            investLotterys.add(investLottery);
        } else if (("pt").equals(loan.getLoanActivityType())) {
            for (int i=0;i < total - nowTotal;i++) {
                InvestLottery investLottery = luckyDrawRules(invest.getInvestMoney(),this.getInvestLottery(invest, InvestLotteryType.NORMAL));
                investLotterys.add(investLottery);
            }
        }
        for (InvestLottery investLottery:investLotterys) {
            hibernateTemplate.save(investLottery);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LotteryPrizeResponseDto getLotteryPrize(InvestLotteryType investLotteryType) {
        LotteryPrizeResponseDto dto = new LotteryPrizeResponseDto();
        List<InvestLottery> investLotteries = investLotteryDao.findInvestLotteryByType(investLotteryType);
        InvestLottery investLottery = null;
        int remainingTimes = 0;
        if(investLotteries.size() > 0){
            investLottery = investLotteries.get(0);
            investLotteries.remove(0);
            remainingTimes = investLotteries.size();
        }
        if (investLottery != null){
            investLottery.setAwardTime(new Date());
            investLottery.setValid(true);
            dto.setRemainingTimes(remainingTimes);
            dto.setInvestLotteryPrizeType(investLottery.getPrizeType());
            if(InvestLotteryPrizeType.G.equals(investLottery.getPrizeType())){
                dto.setPrizeDesc("" + investLottery.getAmount());
            }else{
                dto.setPrizeDesc(investLottery.getPrizeType().getDesc());
            }
            investLotteryDao.updateInvestLottery(investLottery);
            if(InvestLotteryPrizeType.G.equals(investLottery.getPrizeType())){
                this.addGrantCashPrizeJob(investLottery.getId(),investLottery.getUser().getId(),investLottery.getAmount());
            }
        }
        return dto;

    }

    private void addGrantCashPrizeJob(long id,String userId,double amount){
        Date now = new Date();
        Date threeMinutesLater = DateUtil.addMinute(now,1);
        JobDetail jobDetail = JobBuilder
                .newJob(GrantCashPrizeJob.class)
                .withIdentity("cash", ScheduleConstants.JobGroup.AUTO_GRANT_CASH_PRIZE)
                .build();
        jobDetail.getJobDataMap().put(GrantCashPrizeJob.LOAN_ID, "cash");
        SimpleTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("cash", ScheduleConstants.TriggerGroup.AUTO_GRANT_CASH_PRIZE)
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(threeMinutesLater).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.debug("add make grant cash prize job,userId : " + userId +",id :" + id);
        } catch (SchedulerException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug( "id:"+id +"," + userId + ":抽奖现金奖品发放成功.");
        }
    }

    private InvestLottery getInvestLottery(Invest invest,InvestLotteryType investLotteryType ) {
        InvestLottery investLottery = new InvestLottery();
        investLottery.setInvest(invest);
        investLottery.setUser(invest.getUser());
        investLottery.setType(investLotteryType);
        investLottery.setCreatedTime(new Date());
        investLottery.setValid(false);
        return investLottery;
    }

    private InvestLottery luckyDrawRules(double investMoney, InvestLottery investLottery) {
        int random = new Random(System.currentTimeMillis()).nextInt(100);
        int[] lotteryScale = null;
        if (investMoney >= 1 && investMoney < 10) {
            lotteryScale = InvestLotteryProbabilityType.ONE.getIntScale();
        } else if (investMoney >= 10 && investMoney < 100) {
            lotteryScale = InvestLotteryProbabilityType.TEN.getIntScale();
        } else if (investMoney >= 100 && investMoney < 300) {
            lotteryScale = InvestLotteryProbabilityType.ONEHUNDRED.getIntScale();
        } else if (investMoney >= 300 && investMoney < 500) {
            lotteryScale = InvestLotteryProbabilityType.THREEHUNDERD.getIntScale();
        } else if (investMoney >= 500) {
            lotteryScale = InvestLotteryProbabilityType.FIVEHUNDRED.getIntScale();
        }
        int index = this.findArrayIndex(lotteryScale,random);
        switch (index) {
            case 0:
                investLottery.setPrizeType(InvestLotteryPrizeType.G);
                investLottery.setAmount(calculateMoney(investMoney, investLottery));
            case 1:
                investLottery.setPrizeType(InvestLotteryPrizeType.F);
                investLottery.setAmount(0L);
            case 2:
                investLottery.setPrizeType(InvestLotteryPrizeType.E);
                investLottery.setAmount(0L);
            case 3:
                investLottery.setPrizeType(InvestLotteryPrizeType.D);
                investLottery.setAmount(0L);
            case 4:
                investLottery.setPrizeType(InvestLotteryPrizeType.C);
                investLottery.setAmount(0L);
        }
        return investLottery;
    }

    private int findArrayIndex(int[] intScale, int number) {
        int result = 0;
        for (int i=0;i<intScale.length;i++) {
            if (number <= intScale[i]) {
                result = i;
                break;
            }
        }
        return result;
    }

    private long calculateMoney(double investMoney,InvestLottery investLottery) {
        Loan loan = investLottery.getInvest().getLoan();
        String repayTimeUnit = loan.getType().getRepayTimeUnit();
        int deadLine = loan.getDeadline();
        int repayWay = 1;
        if(repayTimeUnit.equals("month")){
            repayWay = 12;
        }else if(repayTimeUnit.equals("day")){
            repayWay = 365;
        }
        double money = ArithUtil.div(ArithUtil.mul(ArithUtil.mul(investMoney,0.01), deadLine), repayWay, 2);
        return (long) (money*100);
    }

}
