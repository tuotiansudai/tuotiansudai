package com.ttsd.special.services.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.service.InvestService;
import com.ttsd.special.services.ActivityRewardService;
import com.ttsd.special.services.ConferenceSaleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConferenceSaleServiceImpl implements ConferenceSaleService {

    private static final Date ACTITY_BEGIN_TIME;
    private static final Date ACTITY_END_TIME;
    private static final Set<String> ACTITY_REFERRER_LIST;

    private static final int INVEST_THRESHOLD;
    private static final int REWARD_BIND_CARD;
    private static final int REWARD_INVEST;


    static {
        ResourceBundle rb = ResourceBundle.getBundle("confierence_sale");
        String actityBeginTime = rb.getString("time.begin");
        String actityEndTime = rb.getString("time.end");
        String referrers = rb.getString("referrers");

        String investThreshold = rb.getString("invest.threshold");
        String rewardBindCard = rb.getString("reward.bindcard");
        String rewardInvest = rb.getString("reward.invest");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = new Date();
        Date endTime = new Date();

        try {
            beginTime = simpleDateFormat.parse(actityBeginTime);
            endTime = simpleDateFormat.parse(actityEndTime);
        } catch (ParseException e) {
        }

        String[] referrerArray = referrers.split("\\s*,\\s*");
        ACTITY_BEGIN_TIME = beginTime;
        ACTITY_END_TIME = endTime;
        ACTITY_REFERRER_LIST = new HashSet<>(Arrays.asList(referrerArray));
        INVEST_THRESHOLD = Integer.parseInt(investThreshold);
        REWARD_BIND_CARD = Integer.parseInt(rewardBindCard);
        REWARD_INVEST = Integer.parseInt(rewardInvest);
    }

    @Autowired
    private InvestService investService;

    @Autowired
    private ActivityRewardService activityRewardService;

    @Logger
    Log log;

    private boolean isInActity(User user) {
        String userReferrer = user.getReferrer();
        if (StringUtils.isEmpty(userReferrer)) {
            return false;
        }
        Date currentDate = new Date();
        if (currentDate.before(ACTITY_BEGIN_TIME) || currentDate.after(ACTITY_END_TIME)) {
            return false;
        }
        return ACTITY_REFERRER_LIST.contains(userReferrer);
    }

    private boolean hasInvestRewardRecord(String userName) {
        long investCount = investService.getUserInvestCount(userName, INVEST_THRESHOLD);
        return investCount > 1;
    }

    @Override
    public void processIfInActivityForBindCard(String orderId, User user) {
        try {
            if (isInActity(user)) {
                log.debug(MessageFormat.format("会销活动发送绑卡奖励，orderid: {0}, userId: {1}", orderId, user.getId()));
                activityRewardService.payActivityReward(orderId, user.getId(), REWARD_BIND_CARD, "会销活动绑卡奖励");
            }
        } catch (Exception exp) {
            log.error(MessageFormat.format("会销活动发送绑卡奖励失败，orderid: {0}, userId: {1}", orderId, user.getId()), exp);
        }
    }

    @Override
    public void processIfInActivityForInvest(String orderId, User user) {
        try {
            if (isInActity(user) && !hasInvestRewardRecord(user.getId())) {
                log.debug(MessageFormat.format("会销活动发送投资奖励，orderid: {0}, userId: {1}", orderId, user.getId()));
                activityRewardService.payActivityReward(orderId, user.getId(), REWARD_INVEST, "会销活动投资奖励");
            }
        } catch (Exception exp) {
            log.error(MessageFormat.format("会销活动发送投资奖励失败，orderid: {0}, userId: {1}", orderId, user.getId()), exp);
        }
    }
}
