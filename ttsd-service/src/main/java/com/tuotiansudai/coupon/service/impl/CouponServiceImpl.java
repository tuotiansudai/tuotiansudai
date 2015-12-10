package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.exception.CreateCouponException;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;


@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;
    @Override
    @Transactional
    public void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException {
        CouponModel couponModel = new CouponModel(couponDto);
        long amount = couponModel.getAmount();
        if(amount <= 0){
            throw new CreateCouponException("投资体验券金额应大于0!");
        }
        long totalCount = couponModel.getTotalCount();
        if(totalCount <= 0){
            throw new CreateCouponException("发放数量应大于0!");
        }
        Date startTime = couponModel.getStartTime();
        Date endTime = couponModel.getEndTime();

        if(startTime == null){
            throw new CreateCouponException("活动起期不能为空!");
        }
        if(endTime == null){
            throw new CreateCouponException("活动止期不能为空!");
        }
        if(startTime.before(new Date())){
            throw new CreateCouponException("活动起期不能早于当前日期!");
        }
        if(endTime.before(new Date())){
            throw new CreateCouponException("活动止期不能早于当前日期!");
        }
        if(endTime.before(startTime)){
            throw new CreateCouponException("活动止期早于活动起期!");
        }
        couponModel.setCreateUser(loginName);
        couponMapper.create(couponModel);
    }

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

    @Override
    public void activeCoupon(String loginName, long couponId) {

    }
}
