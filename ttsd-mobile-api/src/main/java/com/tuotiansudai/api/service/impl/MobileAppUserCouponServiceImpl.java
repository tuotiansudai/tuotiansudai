package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppUserCouponService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MobileAppUserCouponServiceImpl implements MobileAppUserCouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public BaseResponseDto<UserCouponListResponseDataDto> getUserCoupons(final UserCouponRequestDto requestDto) {
        List<UserCouponModel> userCouponModels =  userCouponMapper.findByLoginName(requestDto.getBaseParam().getUserId());

        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                boolean used = InvestStatus.SUCCESS == userCouponModel.getStatus();
                boolean expired = !used && new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isBeforeNow();
                boolean unused = !used && !expired;
                return (used && requestDto.isUsed()) || (unused && requestDto.isUnused()) || (expired && requestDto.isExpired());

            }
        });

        Iterator<UserCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, UserCouponResponseDataDto>() {
            @Override
            public UserCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                UserCouponResponseDataDto dataDto = new UserCouponResponseDataDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
                if (InvestStatus.SUCCESS == userCouponModel.getStatus()) {
                    LoanModel loanModel = loanMapper.findById(userCouponModel.getLoanId());
                    dataDto.setLoanId(String.valueOf(loanModel.getId()));
                    dataDto.setLoanName(loanModel.getName());
                    dataDto.setLoanProductType(loanModel.getProductType());
                }
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
