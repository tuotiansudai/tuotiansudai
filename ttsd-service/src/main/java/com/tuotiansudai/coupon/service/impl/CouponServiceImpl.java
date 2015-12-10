package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Override
    @Transactional
    public BaseDto<PayDataDto> createCoupon(String loginName,CouponDto couponDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        CouponModel couponModel = new CouponModel(couponDto);
        long amount = couponModel.getAmount();
        if(amount <= 0){
            payDataDto.setMessage("投资体验券金额应大于0!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        long totalCount = couponModel.getTotalCount();
        if(totalCount <= 0){
            payDataDto.setMessage("发放数量应大于0!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        Date startTime = couponModel.getStartTime();
        Date endTime = couponModel.getEndTime();

        if(startTime == null){
            payDataDto.setMessage("活动起期不能为空!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if(endTime == null){
            payDataDto.setMessage("活动止期不能为空!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if(startTime.before(new Date())){
            payDataDto.setMessage("活动起期不能早于当前日期!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if(endTime.before(new Date())){
            payDataDto.setMessage("活动止期不能早于当前日期!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if(endTime.before(startTime)){
            payDataDto.setMessage("活动止期早于活动起期!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }

        couponModel.setCreateUser(loginName);
        couponMapper.create(couponModel);
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
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

    @Override
    public List<CouponModel> findCoupons(int index, int pageSize) {
        return couponMapper.findCoupons((index - 1 ) * pageSize, pageSize);
    }

    @Override
    public int findCouponsCount() {
        return couponMapper.findCouponsCount();
    }

}
