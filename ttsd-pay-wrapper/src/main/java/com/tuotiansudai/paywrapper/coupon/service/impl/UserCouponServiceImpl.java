package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.paywrapper.coupon.service.UserCouponService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class UserCouponServiceImpl implements UserCouponService{

    static Logger logger = Logger.getLogger(UserCouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    @Transactional
    public void afterReturningInvest(InvestDto investDto, long investId) {
        if(investDto.getUserCouponId() == null){
            return;
        }
        long userCouponId = Long.parseLong(investDto.getUserCouponId());
        UserCouponDto userCouponDto = convertUserCouponDto(userCouponId);
        if (userCouponDto == null){
            logger.debug(MessageFormat.format("userCouponId:{0} , is not exist", userCouponId));
            return;
        }
        if(userCouponDto.isExpired()){
            logger.debug(MessageFormat.format("userCouponId:{0} , is expired",userCouponId));

        }
        if(userCouponDto.isUsed()){
            logger.debug(MessageFormat.format("userCouponId:{0} , is used",userCouponId));
        }
        if (userCouponDto.isUsable()){
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            userCouponModel.setLoanId(Long.parseLong(investDto.getLoanId()));
            userCouponModel.setUsedTime(new Date());
            userCouponModel.setInvestId(investId);
            userCouponMapper.update(userCouponModel);
        }else {
            logger.debug(MessageFormat.format("userCouponId:{0} , is invalid",userCouponId));
        }
    }

    private UserCouponDto convertUserCouponDto(long userCouponId){
        UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
        if(userCouponModel != null){
            long couponId = userCouponModel.getCouponId();
            CouponModel couponModel = couponMapper.findById(couponId);
            UserCouponDto userCouponDto = new UserCouponDto(couponModel,userCouponModel);

            return userCouponDto;
        }

        return null;
    }

    @Override
    @Transactional
    public void recordUsedCount(long id){
        CouponModel couponModel = couponMapper.lockByCoupon(id);
        long usedCount = couponModel.getUsedCount();
        couponModel.setUsedCount(usedCount + 1);
        couponMapper.updateCoupon(couponModel);
    }

}
