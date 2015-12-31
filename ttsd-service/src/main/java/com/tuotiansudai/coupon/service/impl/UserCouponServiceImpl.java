package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String NEWBIE_COUPON_ALERT_KEY = "web:newbiecoupon:alert:{0}";

    @Override
    public List<UserCouponDto> getUserCoupons(String loginName) {
        List<UserCouponModel> modelList = userCouponMapper.findByLoginName(loginName);
        return Lists.transform(modelList, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCoupon) {
                CouponModel coupon = couponMapper.findById(userCoupon.getCouponId());
                return new UserCouponDto(coupon, userCoupon);
            }
        });
    }

    @Override
    public UserCouponDto getUsableNewbieCoupon(String loginName) {
        List<UserCouponModel> userCoupons = userCouponMapper.findByLoginName(loginName);

        if (!redisWrapperClient.exists(MessageFormat.format(NEWBIE_COUPON_ALERT_KEY, 111))) {
            redisWrapperClient.set(MessageFormat.format(NEWBIE_COUPON_ALERT_KEY, 11111), null);


            return new UserCouponDto();
        }
        return null;
    }

    @Override
    public List<UserCouponDto> getUsableCoupons(String loginName, long loanId, final long amount) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName);

        final DateTime now = new DateTime();

        Iterator<UserCouponModel> iterator = userCouponModels.iterator();
        while (iterator.hasNext()) {
            UserCouponModel model = iterator.next();
            if (true) {
                iterator.remove();
            }
        }

        return Lists.transform(userCouponModels, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                return new UserCouponDto(couponModel, userCouponModel, amount);
            }
        });
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
