package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppUserCouponService;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public BaseResponseDto<UserCouponListResponseDataDto> getUserCoupons(UserCouponRequestDto requestDto) {
        List<UserCouponDto> userCouponDtos = null;
        List<CouponUseRecordView> useRecords = null;
        List<UserCouponResponseDataDto> coupons = null;

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();

        if(requestDto.isUnused()){
            userCouponDtos = userCouponService.getUnusedUserCoupons(requestDto.getBaseParam().getUserId());
        }
        if(requestDto.isExpired()){
            userCouponDtos = userCouponService.getExpiredUserCoupons(requestDto.getBaseParam().getUserId());
        }

        if(requestDto.isUsed()){
            useRecords = userCouponService.findUseRecords(requestDto.getBaseParam().getUserId());
        }

        if(CollectionUtils.isNotEmpty(userCouponDtos)){
            coupons = Lists.transform(userCouponDtos, new Function<UserCouponDto, UserCouponResponseDataDto>() {
                @Override
                public UserCouponResponseDataDto apply(UserCouponDto userCouponDto) {
                    UserCouponModel userCouponModel = userCouponMapper.findById(userCouponDto.getId());
                    UserCouponResponseDataDto dataDto = new UserCouponResponseDataDto(couponMapper.findById(userCouponDto.getCouponId()), userCouponModel);
                    if (InvestStatus.SUCCESS == userCouponModel.getStatus()) {
                        LoanModel loanModel = loanMapper.findById(userCouponModel.getLoanId());
                        dataDto.setLoanId(String.valueOf(loanModel.getId()));
                        dataDto.setLoanName(loanModel.getName());
                        dataDto.setLoanProductType(loanModel.getProductType());
                        InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
                        if (investModel != null) {
                            dataDto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
                        }

                    }
                    return dataDto;
                }
            });

        }

        if(CollectionUtils.isNotEmpty(useRecords)){
            coupons = Lists.transform(useRecords, new Function<CouponUseRecordView, UserCouponResponseDataDto>() {
                @Override
                public UserCouponResponseDataDto apply(CouponUseRecordView couponUseRecordView) {
                    return new UserCouponResponseDataDto(couponUseRecordView);
                }
            });
        }
        if(CollectionUtils.isNotEmpty(coupons)){
            responseDto.setData(new UserCouponListResponseDataDto(coupons));
        }else{
            responseDto.setData(new UserCouponListResponseDataDto());
        }
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return responseDto;
    }
}
