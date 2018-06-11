package com.tuotiansudai.paywrapper.loanout.impl;


import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.loanout.AchievementCouponService;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserGroup;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementCouponServiceImpl implements AchievementCouponService {

    static Logger logger = Logger.getLogger(AchievementCouponServiceImpl.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public boolean assignInvestAchievementUserCoupon(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        boolean result = true;
        if (!createUserCouponModel(loanModel.getFirstInvestAchievementId(), UserGroup.FIRST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign FIRST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getMaxAmountAchievementId(), UserGroup.MAX_AMOUNT_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign MAX_AMOUNT_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getLastInvestAchievementId(), UserGroup.LAST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign LAST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        return result;
    }

    private boolean createUserCouponModel(Long investId, final UserGroup userGroup, long loanId) {
        if (investId == null || investId == 0) {
            logger.error(MessageFormat.format("loan id : {0} nothing {1}", String.valueOf(loanId), userGroup.name()));
            return false;
        }

        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();

        couponModelList.stream().filter(couponModel -> couponModel.getUserGroup().equals(userGroup)
                && DateTime.now().toDate().before(couponModel.getEndTime())
                && DateTime.now().toDate().after(couponModel.getStartTime()))
                .collect(Collectors.toList())
                .forEach(couponModel -> mqWrapperClient.sendMessage(MessageQueue.Coupon_Assigning,
                        investMapper.findById(investId).getLoginName() + ":" + loanId + ":" + couponModel.getId()));
        return true;
    }
}
