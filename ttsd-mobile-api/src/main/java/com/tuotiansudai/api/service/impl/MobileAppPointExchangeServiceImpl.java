package com.tuotiansudai.api.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppPointExchangeService;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MobileAppPointExchangeServiceImpl implements MobileAppPointExchangeService {

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CouponActivationService couponActivationService;
    @Autowired
    private PointBillService pointBillService;

    @Override
    @Transactional
    public BaseResponseDto generatePointExchange(PointExchangeRequestDto pointExchangeRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String couponId = pointExchangeRequestDto.getCouponId();
        String loginName = pointExchangeRequestDto.getBaseParam().getUserId();

        PointExchangeResponseDataDto pointExchangeResponseDataDto = new PointExchangeResponseDataDto();
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long CouponExchangePoint = couponExchangeMapper.findByCouponId(Long.parseLong(couponId)).getExchangePoint();
        long userPoint = accountModel.getPoint();
        if(userPoint > CouponExchangePoint){
            couponActivationService.assignUserCoupon(pointExchangeRequestDto.getBaseParam().getUserId(),Lists.newArrayList(UserGroup.ALL_USER,
                    UserGroup.EXCHANGER),Long.parseLong(couponId));
            pointBillService.createPointBill(loginName, Long.parseLong(couponId), PointBusinessType.EXCHANGE, (-CouponExchangePoint));
            pointExchangeResponseDataDto.setPoint((userPoint - CouponExchangePoint));
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
            dto.setData(pointExchangeResponseDataDto);
        }
        else{
            pointExchangeResponseDataDto.setPoint(userPoint);
            dto.setCode(ReturnMessage.POINT_EXCHANGE_FAIL.getCode());
            dto.setMessage(ReturnMessage.POINT_EXCHANGE_FAIL.getMsg());
            dto.setData(pointExchangeResponseDataDto);
        }
        return dto;
    }

}
