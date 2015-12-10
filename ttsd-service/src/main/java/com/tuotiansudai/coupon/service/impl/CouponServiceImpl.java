package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.service.CouponService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class CouponServiceImpl implements CouponService {
    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Override
    public void afterUserRegistered(String loginName) {
        logger.info(MessageFormat.format("after user registered , loginName : {0}.", loginName));
    }

    @Override
    public void afterInvest(String loginName, long loanId) {
        logger.info(MessageFormat.format("after user invest, loginName : {0}, loanId : {1}.", loginName, loanId));
    }

    @Override
    public void afterRepay(long loanId, boolean isAdvanced) {
        logger.info(MessageFormat.format("after loan repay, loanId : {0}.", loanId));
        // do create job
    }

}
