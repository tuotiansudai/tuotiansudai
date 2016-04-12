package com.ttsd.special.services.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.redis.RedisClient;
import com.ttsd.special.services.ActivityRewardService;
import com.ttsd.special.services.ConferenceSaleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConferenceSaleServiceImpl implements ConferenceSaleService {

    private static final Date ACTIVITY_BEGIN_TIME;
    private static final Date ACTIVITY_END_TIME;
    private static final Set<String> ACTIVITY_REFERRER_LIST;

    private static final int INVEST_THRESHOLD;
    private static final int REWARD_BIND_CARD;
    private static final int REWARD_INVEST;

    private static final Date JOB_EXPIRE_TIME;

    private static final String REWARD_ORDERID_FORMAT = "HX201512{0}";

    private static final String REWARD_RECORD_KEY = "HX201512";

    static {
        ResourceBundle rb = ResourceBundle.getBundle("conference_sale");
        String actityBeginTime = rb.getString("time.begin");
        String actityEndTime = rb.getString("time.end");
        String jobExpireTime = rb.getString("job.expire.time");
        String referrers = rb.getString("referrers");

        String investThreshold = rb.getString("invest.threshold");
        String rewardBindCard = rb.getString("reward.bindcard");
        String rewardInvest = rb.getString("reward.invest");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = new Date();
        Date endTime = new Date();
        Date expireTime = null;

        try {
            beginTime = simpleDateFormat.parse(actityBeginTime.trim());
            endTime = simpleDateFormat.parse(actityEndTime.trim());
            expireTime = simpleDateFormat.parse(jobExpireTime.trim());
        } catch (ParseException e) {
        }

        String[] referrerArray = referrers.trim().split("\\s*,\\s*");
        ACTIVITY_BEGIN_TIME = beginTime;
        ACTIVITY_END_TIME = endTime;
        JOB_EXPIRE_TIME = expireTime;
        ACTIVITY_REFERRER_LIST = new HashSet<>(Arrays.asList(referrerArray));
        INVEST_THRESHOLD = Integer.parseInt(investThreshold.trim());
        REWARD_BIND_CARD = Integer.parseInt(rewardBindCard.trim());
        REWARD_INVEST = Integer.parseInt(rewardInvest.trim());
    }

    @Autowired
    private ActivityRewardService activityRewardService;

    @Autowired
    HibernateTemplate ht;

    @Autowired
    RedisClient redisClient;

    @Logger
    Log log;

    @Transactional
    @Override
    public void processIfInActivityForBindCard(String cardNo, String userId) {
        if(REWARD_BIND_CARD <= 0){return;}
        try {
            User user = ht.get(User.class, userId);
            if (isInActivity(user) && (!hasBindCardRewardRecord(user.getId()))) {
                String logMessage = MessageFormat.format("会销活动绑卡奖励，卡号: {0}, userId: {1}", cardNo, user.getId());
                log.debug(logMessage);
                String orderId = generateOrderId(cardNo);
                activityRewardService.payActivityReward(orderId, user.getId(), REWARD_BIND_CARD, logMessage);
                markUserBindCardReward(user.getId());
            }
        } catch (Exception exp) {
            log.error(MessageFormat.format("会销活动发送绑卡奖励失败，卡号: {0}, userId: {1}", cardNo, userId), exp);
        }
    }

    @Transactional
    @Override
    public void processIfInActivityForInvest(Invest invest) {
        if(REWARD_INVEST <= 0){return;}
        try {
            User user = invest.getUser();
            if (isInActivity(user) && (!hasInvestRewardRecord(user.getId())) && hasMatchRewardCondition(invest)) {
                String logMessage = MessageFormat.format("会销活动投资奖励，investId: {0}", invest.getId());
                log.debug(logMessage);
                String orderId = generateOrderId(invest.getId());
                activityRewardService.payActivityReward(orderId, user.getId(), REWARD_INVEST, logMessage);
                markUserInvestReward(user.getId());
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            log.error(MessageFormat.format("会销活动发送投资奖励失败，investId: {0}", invest.getId()), exp);
        }
    }

    @Transactional
    @Override
    public void reissueMissedReward(Date deadlineDate) {
        if (JOB_EXPIRE_TIME != null && new Date().before(JOB_EXPIRE_TIME)) {
            log.info("reissue missed reward for conference sale activity");
            reissueMissedBindcardReward(deadlineDate);
            reissueMissedInvestReward(deadlineDate);
            log.info("reissue missed reward for conference sale activity complete");
        }
    }

    private void reissueMissedBindcardReward(Date deadlineDate) {
        String hql = "from BankCard bankCard where bankCard.time >= ? and bankCard.time <= ? and bankCard.status=? order by bankCard.time asc";
        List<BankCard> bankCards = ht.find(hql, ACTIVITY_BEGIN_TIME, deadlineDate, "passed");
        for (BankCard bankCard : bankCards) {
            processIfInActivityForBindCard(bankCard.getCardNo(), bankCard.getUser().getId());
        }
    }

    private void reissueMissedInvestReward(Date deadlineDate) {
        String hql = "from Invest invest where invest.time >= ? and invest.time <= ? and invest.money >= ? and invest.status not in (?,?,?,?) order by invest.time asc";
        List<Invest> invests = ht.find(hql, ACTIVITY_BEGIN_TIME, deadlineDate, new Double(INVEST_THRESHOLD),
                InvestConstants.InvestStatus.UNFINISHED,
                InvestConstants.InvestStatus.TEST,
                InvestConstants.InvestStatus.WAIT_AFFIRM,
                InvestConstants.InvestStatus.CANCEL);
        for (Invest invest : invests) {
            processIfInActivityForInvest(invest);
        }
    }

    private boolean isInActivity(User user) {
        if (user == null) {
            return false;
        }
        String userReferrer = user.getReferrer();
        if (StringUtils.isEmpty(userReferrer)) {
            return false;
        }
        Date currentDate = new Date();
        if (currentDate.before(ACTIVITY_BEGIN_TIME) || currentDate.after(ACTIVITY_END_TIME)) {
            return false;
        }
        if (user.getRegisterTime().before(ACTIVITY_BEGIN_TIME) || user.getRegisterTime().after(ACTIVITY_END_TIME)) {
            return false;
        }
        return ACTIVITY_REFERRER_LIST.contains(userReferrer);
    }

    private boolean hasMatchRewardCondition(Invest invest) {
        return invest.getInvestMoney() >= INVEST_THRESHOLD;
    }

    private String generateOrderId(String baseId) {
        return MessageFormat.format(REWARD_ORDERID_FORMAT, baseId);
    }

    private void markUserBindCardReward(String userId) {
        String hkey = MessageFormat.format("{0}:bindcard", userId);
        redisClient.hset(REWARD_RECORD_KEY, hkey, "1");
    }

    private boolean hasBindCardRewardRecord(String userId) {
        String hkey = MessageFormat.format("{0}:bindcard", userId);
        String markValue = redisClient.hget(REWARD_RECORD_KEY, hkey);
        return "1".equalsIgnoreCase(markValue);
    }

    private void markUserInvestReward(String userId) {
        String hkey = MessageFormat.format("{0}:invest", userId);
        redisClient.hset(REWARD_RECORD_KEY, hkey, "1");
    }

    private boolean hasInvestRewardRecord(String userId) {
        String hkey = MessageFormat.format("{0}:invest", userId);
        String markValue = redisClient.hget(REWARD_RECORD_KEY, hkey);
        return "1".equalsIgnoreCase(markValue);
    }
}
