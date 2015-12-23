package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.dto.UserInvestingCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestService investService;

    @Override
    public List<UserCouponDto> getUserCouponDtoByLoginName(String loginName) {
        List<UserCouponModel> modelList = userCouponMapper.findByLoginName(loginName);
        List<UserCouponDto> dtoList = Lists.transform(modelList, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCoupon) {
                CouponModel coupon = couponMapper.findById(userCoupon.getCouponId());
                return new UserCouponDto(coupon, userCoupon);
            }
        });
        return dtoList;
    }

    @Override
    public List<UserInvestingCouponDto> getValidCoupons(String loginName, final long loanId) {
        List<UserCouponDto> userCouponDtos = getUserCouponDtoByLoginName(loginName);
        List<UserCouponDto> validUserCouponDtos = ListUtils.select(userCouponDtos, new Predicate<UserCouponDto>() {
            @Override
            public boolean evaluate(UserCouponDto userCouponDto) {
                return userCouponDto.isValid();
            }
        });
        List<UserInvestingCouponDto> investingCouponDtos = Lists.transform(validUserCouponDtos, new Function<UserCouponDto, UserInvestingCouponDto>() {
            @Override
            public UserInvestingCouponDto apply(UserCouponDto userCouponDto) {
                long interest = investService.estimateInvestIncome(loanId, userCouponDto.getAmount());
                UserInvestingCouponDto userInvestingCouponDto = new UserInvestingCouponDto(userCouponDto);
                userInvestingCouponDto.setLoanId(loanId);
                userInvestingCouponDto.setInterest(interest);
                return userInvestingCouponDto;
            }
        });
        return investingCouponDtos;
    }

    @Override
    public BaseDto<BasePaginationDataDto> findUseRecords(String loginName, int index, int pageSize) {
        int count = userCouponMapper.findUseRecordsCount(loginName);
        List<CouponUseRecordView> couponUseRecordList = userCouponMapper.findUseRecords(loginName, (index - 1) * pageSize, pageSize);

        for(CouponUseRecordView curm : couponUseRecordList) {
            long expectInterest = investService.estimateInvestIncome(curm.getLoanId(),curm.getCouponAmount());
            curm.setExpectInterest(expectInterest);
            curm.setExpectInterestStr(AmountConverter.convertCentToString(expectInterest));
            curm.setCouponAmountStr(AmountConverter.convertCentToString(curm.getCouponAmount()));
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<CouponUseRecordView> dataDto = new BasePaginationDataDto<>(index, pageSize, count, couponUseRecordList);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}
