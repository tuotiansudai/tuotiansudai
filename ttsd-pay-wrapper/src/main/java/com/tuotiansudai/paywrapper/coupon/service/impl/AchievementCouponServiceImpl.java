package com.tuotiansudai.paywrapper.coupon.service.impl;


import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.paywrapper.coupon.service.AchievementCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementCouponServiceImpl implements AchievementCouponService{

    static Logger logger = Logger.getLogger(AchievementCouponServiceImpl.class);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public boolean assignInvestAchievementUserCoupon(long loanId){
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

        boolean result = true;
        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();

        List<CouponModel> collect = couponModelList.stream().filter(couponModel -> couponModel.getUserGroup().equals(userGroup)
                && DateTime.now().toDate().before(couponModel.getEndTime())
                && DateTime.now().toDate().after(couponModel.getStartTime())).collect(Collectors.toList());

        for (CouponModel couponModel : collect) {
            if (!couponAssignmentService.assignInvestAchievementUserCoupon(loanId, investMapper.findById(investId).getLoginName(), couponModel.getId())) {
                result = false;
            }
        }
        return result;
    }
}
