package com.tuotiansudai.coupon.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Optional;
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
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    static Logger logger = Logger.getLogger(UserCouponServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String NEWBIE_COUPON_ALERT_KEY = "web:newbiecoupon:alert";

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
        try {
            String redisValue = redisWrapperClient.get(NEWBIE_COUPON_ALERT_KEY);
            if(StringUtils.isEmpty(redisValue)){
                return null;
            }
            Set<String> loginNames = objectMapper.readValue(redisValue, new TypeReference<Set<String>>() {});
            if (loginNames.contains(loginName)) {
                return null;
            }
            List<UserCouponModel> userCoupons = userCouponMapper.findByLoginName(loginName);
            Optional<UserCouponModel> found = Iterators.tryFind(userCoupons.iterator(), new Predicate<UserCouponModel>() {
                @Override
                public boolean apply(UserCouponModel userCouponModel) {
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    return couponModel.getCouponType() == CouponType.NEWBIE_COUPON && InvestStatus.SUCCESS != userCouponModel.getStatus() && new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isAfterNow();
                }
            });

            if (found.isPresent()) {
                loginNames.add(loginName);
                redisWrapperClient.set(NEWBIE_COUPON_ALERT_KEY, objectMapper.writeValueAsString(loginNames));
                return new UserCouponDto(couponMapper.findById(found.get().getCouponId()), found.get());
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public List<UserCouponDto> getUsableCoupons(String loginName, long loanId, final long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName);

        List<UserCouponDto> usableCoupons = Lists.newArrayList();
        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            if (InvestStatus.SUCCESS != userCouponModel.getStatus()
                    && new DateTime(couponModel.getEndTime()).plusDays(1).withTimeAtStartOfDay().isAfterNow()
                    && couponModel.getProductTypes().contains(loanModel.getProductType())) {
                usableCoupons.add(new UserCouponDto(couponModel, userCouponModel, amount));
            }
        }

        return usableCoupons;
    }

    @Override
    public BaseDto<BasePaginationDataDto> findUseRecords(String loginName, int index, int pageSize) {
        int count = userCouponMapper.findUseRecordsCount(loginName);
        List<CouponUseRecordView> couponUseRecordList = userCouponMapper.findUseRecords(loginName, (index - 1) * pageSize, pageSize);

        for (CouponUseRecordView curm : couponUseRecordList) {
            long expectInterest = investService.estimateInvestIncome(curm.getLoanId(), curm.getCouponAmount());
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
