package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NewBieCouponResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppNewBieCouponService;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNewBieCouponServiceImpl implements MobileAppNewBieCouponService {

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public BaseResponseDto getNewBieCoupon(BaseParamDto baseParamDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        UserCouponDto userCouponDto = userCouponService.getUsableNewbieCoupon(loginName);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        if(userCouponDto != null){
            NewBieCouponResponseDataDto responseDataDto = new NewBieCouponResponseDataDto(userCouponDto);
            baseDto.setData(responseDataDto);
        }

        return baseDto;
    }
}
