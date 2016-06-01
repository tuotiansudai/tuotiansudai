package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PointExchangeRequestDto;
import com.tuotiansudai.api.dto.v1_0.PointExchangeResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppPointExchangeService;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.point.service.PointExchangeService;
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
    private PointExchangeService pointExchangeService;


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
        if(pointExchangeService.exchangeableCoupon(Long.parseLong(couponId), loginName)){
            if(pointExchangeService.exchangeCoupon(Long.parseLong(couponId),loginName,CouponExchangePoint)){
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

        }
        else{
            if(userPoint < CouponExchangePoint){
                pointExchangeResponseDataDto.setPoint(userPoint);
                dto.setCode(ReturnMessage.POINT_EXCHANGE_POINT_INSUFFICIENT.getCode());
                dto.setMessage(ReturnMessage.POINT_EXCHANGE_POINT_INSUFFICIENT.getMsg());
                dto.setData(pointExchangeResponseDataDto);
            }
            else
            {
                pointExchangeResponseDataDto.setPoint(userPoint);
                dto.setCode(ReturnMessage.POINT_COUPON_NUM_INSUFFICIENT.getCode());
                dto.setMessage(ReturnMessage.POINT_COUPON_NUM_INSUFFICIENT.getMsg());
                dto.setData(pointExchangeResponseDataDto);
            }
        }
        return dto;
    }

}
