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
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
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

        LoanModel loanModel = loanMapper.findById(loanId);

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        if (CollectionUtils.isEmpty(successInvestModels)) {
            logger.error(MessageFormat.format("(invest record is exist (loanId = {0}), String.valueOf(loanId))", String.valueOf(loanId)));
            return;
        }
        List<CouponRepayModel> couponRepayModels = Lists.newArrayList();

        for (InvestModel successInvestModel : successInvestModels) {
            List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(successInvestModel.getId());
            if (CollectionUtils.isEmpty(userCouponModels)) {
                logger.error(MessageFormat.format("(user coupon is exist (invest id = {0})", String.valueOf(successInvestModel.getId())));
                return;
            }
            for (UserCouponModel userCouponModel : userCouponModels) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                if (couponModel == null) {
                    logger.error(MessageFormat.format("(coupon is exist (user coupon id = {0})", String.valueOf(userCouponModel.getId())));
                    return;
                }

                long expectedCouponInterest = InterestCalculator.calculateCouponInterest(loanModel, successInvestModel, userCouponModel, couponModel);

                long expectedFee = new BigDecimal(expectedCouponInterest)
                        .setScale(0, BigDecimal.ROUND_DOWN)
                        .multiply(new BigDecimal(couponModel.getRate())).longValue();

                couponRepayModels.add(new CouponRepayModel(successInvestModel.getLoginName(),
                        couponModel.getId(),
                        userCouponModel.getId(),
                        successInvestModel.getId(),
                        expectedCouponInterest,
                        expectedFee,
                        loanModel.getProductType().getPeriods(),
                        new Date()));
            }
        }

        couponRepayMapper.create(couponRepayModels);
    }


}
