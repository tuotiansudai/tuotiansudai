package com.ttsd.special.services.impl;

import com.esoft.archer.user.model.User;

import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.GrantCashPrizeJob;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.google.common.collect.Lists;
import com.ttsd.special.dao.InvestLotteryDao;
import com.ttsd.special.dto.LotteryPrizeResponseDto;
import com.ttsd.special.model.*;
import com.ttsd.special.services.InvestLotteryService;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.joda.time.DateTime;
import org.apache.commons.logging.Log;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    @Autowired
    private UmPayLoanMoneyService umPayLoanMoneyService;

    @Autowired
    private UserBillBO userBillBO;

    @Autowired
    private HibernateTemplate ht;

    @Logger
    Log log;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIntoInvestLottery(String investId) {
        Invest invest = hibernateTemplate.get(Invest.class,investId);
        Loan loan = invest.getLoan();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date createdTime = null;
        try {
            createdTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(),e);
        }
        List<InvestLottery> investLotteryList = hibernateTemplate.find("from InvestLottery t where t.user.id = ? and t.type = ? and t.createdTime = ?",
                new Object[]{invest.getUser().getId(),InvestLotteryType.NORMAL,createdTime});
        int total = 0;
        if (invest.getInvestMoney() >= 500 && invest.getInvestMoney() < 1500) {
            total = 1;
        } else if (invest.getInvestMoney() >= 1500) {
            total = 3;
        }
        int nowTotal = investLotteryList.size();
        List<InvestLottery> investLotterys = Lists.newArrayList();
        if ((LoanConstants.LoanActivityType.XS).equals(loan.getLoanActivityType())){
            InvestLottery investLottery = luckyDrawRules(invest.getInvestMoney(), this.getInvestLottery(invest, InvestLotteryType.NOVICE));
            investLotterys.add(investLottery);
        } else if ((LoanConstants.LoanActivityType.PT).equals(loan.getLoanActivityType())) {
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
                dto.setPrizeDesc("现金" + investLottery.getAmount()/100d +"元");
            }else{
                dto.setPrizeDesc(investLottery.getPrizeType().getDesc());
            }
            investLotteryDao.updateInvestLottery(investLottery);
            if(InvestLotteryPrizeType.G.equals(investLottery.getPrizeType())){

                double bonus = investLottery.getAmount() / 100d;
                if(bonus > 0){
                    log.info("add grantCashPrizeJob begin ");
                    String userId = investLottery.getUser().getId();
                    this.addGrantCashPrizeJob("" + investLottery.getId(), "" + bonus,userId);
                    log.info("add grantCashPrizeJob end ");
                }
            }
        }
        return dto;

    }

    private void addGrantCashPrizeJob(String id,String bonus,String userId) {
        Date now = new Date();
        Date threeMinutesLater = DateUtil.addMinute(now, 5);
        JobDetail jobDetail = JobBuilder
                .newJob(GrantCashPrizeJob.class)
                .withIdentity(id, ScheduleConstants.JobGroup.AUTO_GRANT_CASH_PRIZE)
                .build();
        jobDetail.getJobDataMap().put(GrantCashPrizeJob.LOTTERY_ID, id);
        jobDetail.getJobDataMap().put(GrantCashPrizeJob.USERID, userId);
        jobDetail.getJobDataMap().put(GrantCashPrizeJob.BONUS, bonus);
        SimpleTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(id, ScheduleConstants.TriggerGroup.AUTO_GRANT_CASH_PRIZE)
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(threeMinutesLater).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.debug("add make grant cash prize job,userId : " + userId + ",id :" + id);
        } catch (SchedulerException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug("id:" + id + "," + userId + ":抽奖现金奖品发放成功.");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateInvestLotteryGranted(long id, ReceiveStatus receiveStatus) {
        InvestLottery investLottery = hibernateTemplate.get(InvestLottery.class, id);
        investLottery.setReceiveStatus(receiveStatus);
        investLottery.setReceivedTime(new Date());
        hibernateTemplate.update(investLottery);

    }

    private InvestLottery getInvestLottery(Invest invest,InvestLotteryType investLotteryType ) {
        InvestLottery investLottery = new InvestLottery();
        investLottery.setInvest(invest);
        investLottery.setUser(invest.getUser());
        investLottery.setType(investLotteryType);
        investLottery.setCreatedTime(new Date());
        investLottery.setValid(false);
        investLottery.setReceiveStatus(ReceiveStatus.NOT_RECEIVED);
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
                break;
            case 1:
                investLottery.setPrizeType(InvestLotteryPrizeType.F);
                investLottery.setAmount(0L);
                break;
            case 2:
                investLottery.setPrizeType(InvestLotteryPrizeType.E);
                investLottery.setAmount(0L);
                break;
            case 3:
                investLottery.setPrizeType(InvestLotteryPrizeType.D);
                investLottery.setAmount(0L);
                break;
            case 4:
                investLottery.setPrizeType(InvestLotteryPrizeType.C);
                investLottery.setAmount(0L);
                break;
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

    @Override
    public List<InvestLottery> findInvestLotteryTops(int limit) {
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("from InvestLottery where valid = true order by awardTime desc");
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public int getRemainingTimes(String userName, InvestLotteryType type) {
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("select count(*) from InvestLottery " +
                " where valid = false" +
                " and user.username = ?" +
                " and type = ?" +
                " and createdTime >= ? and createdTime < ?");
        Calendar calendar = Calendar.getInstance();
        query.setParameter(0, userName);
        query.setParameter(1, type);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        query.setParameter(2, calendar.getTime());
        calendar.add(Calendar.HOUR, 24);
        query.setParameter(3, calendar.getTime());
        return ((Number)query.uniqueResult()).intValue();
    }
    @Transactional(rollbackFor = Exception.class)
    public boolean winningPersonIncome(String lotteryId, String money, String userId) {
        User user = ht.get(User.class,userId);
        double bonus = Double.valueOf(money);
        TrusteeshipAccount trusteeshipAccount = this.getTrusteeshipAccount(userId);
        String particUserId = trusteeshipAccount.getId();
        String orderId = lotteryId + System.currentTimeMillis();
        String particAccType = UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON;
        String transAction = UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT;
        String transferOutDetailFormat = "投资人中奖金额发放, 中奖纪录号:{0}, 投资人:{1}, 订单:{2}";
        String transferOutDetail = MessageFormat.format(transferOutDetailFormat, lotteryId, user.getId(), orderId);
        String returnMsg = null;
        try {
            returnMsg = umPayLoanMoneyService.giveMoney2ParticUserId(orderId, bonus,particAccType,transAction,particUserId,transferOutDetail,"invest_lottery");
        } catch (ReqDataException | RetDataException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
        if(returnMsg.split("\\|")[0].equals("0000")){
            insertIntoUserBill(user,bonus,transferOutDetail);
            return true;
        }else{
            return false;
        }
    }

    private void insertIntoUserBill(User user, double bonus, String detail) {
        double balance = userBillBO.getBalance(user.getId());
        double frozenMoney = userBillBO.getFrozenMoney(user.getId());
        UserBill ub = new UserBill();
        ub.setBalance(ArithUtil.add(balance, bonus));
        ub.setFrozenMoney(frozenMoney);
        ub.setTime(new Date());
        ub.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ub.setType("ti_balance");
        ub.setTypeInfo("invest_lottery");
        ub.setMoney(bonus);
        ub.setSeqNum((userBillBO.getLastestBill(user.getId())!=null?userBillBO.getLastestBill(user.getId()).getSeqNum():0) + 1);
        ub.setUser(user);
        ub.setDetail(detail);
        ht.save(ub);
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    private TrusteeshipAccount getTrusteeshipAccount(String userId) {
        TrusteeshipAccount ta = null;
        List<TrusteeshipAccount> taList = ht.find(
                "from TrusteeshipAccount t where t.user.id=?",
                new String[]{userId.trim()});
        if (null != taList && taList.size() > 0) {
            ta = taList.get(0);
        }
        return ta;
    }

}
