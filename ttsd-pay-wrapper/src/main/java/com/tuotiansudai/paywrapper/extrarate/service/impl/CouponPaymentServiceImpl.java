package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.extrarate.service.CouponPaymentService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponPaymentServiceImpl implements CouponPaymentService {

    private static Logger logger = Logger.getLogger(CouponPaymentServiceImpl.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private LoanMapper loanMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCouponPayment(long loanId) {

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        if (CollectionUtils.isEmpty(successInvestModels)) {
            logger.error(MessageFormat.format("(invest record is exist (loanId = {0}))", String.valueOf(loanId)));
            return;
        }

        LoanModel loanModel = loanMapper.findById(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.getPeriods();
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);

        ArrayList<CouponRepayModel> couponRepayModels = Lists.newArrayList();

        for (int index = 0; index < totalPeriods; index++) {
            int period = index + 1;
            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            Date currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration).toDate();
            for (InvestModel successInvestModel : successInvestModels) {
                List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(successInvestModel.getId());
                if (CollectionUtils.isEmpty(userCouponModels)) {
                    logger.error(MessageFormat.format("(user coupon is exist (investId = {0}))", String.valueOf(successInvestModel.getId())));
                    return;
                }
                for (UserCouponModel userCouponModel : userCouponModels) {
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    if (couponModel == null) {
                        logger.error(MessageFormat.format("(coupon is exist (couponId = {0}))", userCouponModel.getCouponId()));
                        return;
                    }
                    InterestCalculator.estimateCouponExpectedInterest(successInvestModel.getAmount(),
                            loanModel, couponModel);
                    long expectedCouponInterest = 0;
                    long expectedFee = 0;
                    couponRepayModels.add(new CouponRepayModel(successInvestModel.getLoginName(),
                            couponModel.getId(),
                            userCouponModel.getId(),
                            successInvestModel.getId(),
                            expectedCouponInterest,
                            expectedFee,
                            period,
                            currentRepayDate
                    ));
                }

            }
        }
        couponRepayMapper.create(couponRepayModels);
    }


}
