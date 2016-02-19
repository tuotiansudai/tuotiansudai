package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppInvestCouponService;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MobileAppInvestCouponServiceImpl implements MobileAppInvestCouponService {
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private CouponMapper couponMapper;

    @Override
    public BaseResponseDto getInvestCoupons(InvestRequestDto dto) {
        String loanId = dto.getLoanId();
        String investMoney = dto.getInvestMoney();
        if(StringUtils.isEmpty(loanId) || StringUtils.isEmpty(investMoney)){
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }


        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        long investMoneyLong = AmountConverter.convertStringToCent(dto.getInvestMoney());

        List<UserCouponModel> userCouponModels =  userCouponMapper.findInvestCouponByLoginName(dto.getBaseParam().getUserId(), loanModel.getProductType(), investMoneyLong);

        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                boolean used = InvestStatus.SUCCESS == userCouponModel.getStatus();
                boolean expired = !used && new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isBeforeNow();
                boolean unused = !used && !expired;
                return unused;

            }
        });
        Iterator<UserCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, UserCouponResponseDataDto>() {
            @Override
            public UserCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                UserCouponResponseDataDto dataDto = new UserCouponResponseDataDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
                return dataDto;
            }
        });
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(new UserCouponListResponseDataDto(Lists.newArrayList(items)));
        return responseDto;
    }
}
