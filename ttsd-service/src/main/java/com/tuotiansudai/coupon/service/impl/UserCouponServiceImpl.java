package com.tuotiansudai.coupon.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
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
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private RedisWrapperClient redisWrapperClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String NEWBIE_COUPON_ALERT_KEY = "web:newbiecoupon:alert";

    @Override
    public List<UserCouponDto> getUserCoupons(String loginName, List<CouponType> couponTypeList) {
        List<UserCouponModel> modelList = userCouponMapper.findByLoginName(loginName, couponTypeList);
        List<UserCouponDto> userCouponDtoList = new ArrayList<>();
        for (UserCouponModel couponModel : modelList) {
            CouponModel coupon = couponMapper.findById(couponModel.getCouponId());
            UserCouponDto dto = new UserCouponDto(coupon, couponModel);
            userCouponDtoList.add(dto);
        }
        Collections.sort(userCouponDtoList);
        return userCouponDtoList;
    }


    @Override
    public UserCouponDto getUsableNewbieCoupon(String loginName) {
        try {
            if (!redisWrapperClient.exists(NEWBIE_COUPON_ALERT_KEY)) {
                redisWrapperClient.set(NEWBIE_COUPON_ALERT_KEY, objectMapper.writeValueAsString(Sets.newHashSet()));
            }
            String redisValue = redisWrapperClient.get(NEWBIE_COUPON_ALERT_KEY);
            Set<String> loginNames = objectMapper.readValue(redisValue, new TypeReference<Set<String>>() {
            });
            if (loginNames.contains(loginName)) {
                return null;
            }
            List<UserCouponModel> userCoupons = userCouponMapper.findByLoginName(loginName, null);
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
    public List<UserCouponDto> getUsableCoupons(String loginName, final long loanId) {
        final LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return Lists.newArrayList();
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        List<UserCouponDto> dtoList = Lists.transform(userCouponModels, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCouponModel) {
                return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
            }
        });

        return Lists.newArrayList(Iterators.filter(dtoList.iterator(), new Predicate<UserCouponDto>() {
            @Override
            public boolean apply(UserCouponDto dto) {
                return dto.getProductTypeList().contains(loanModel.getProductType()) && dto.isUnused();
            }
        }));
    }

    @Override
    public BaseDto<BasePaginationDataDto> findUseRecords(List<CouponType> couponTypeList, String loginName, int index, int pageSize) {
        int count = userCouponMapper.findUseRecordsCount(couponTypeList, loginName);
        List<CouponUseRecordView> couponUseRecordList = userCouponMapper.findUseRecords(couponTypeList, loginName, (index - 1) * pageSize, pageSize);

        for (CouponUseRecordView curm : couponUseRecordList) {
            curm.setExpectedIncomeStr(AmountConverter.convertCentToString(curm.getExpectedIncome()));
            curm.setInvestAmountStr(AmountConverter.convertCentToString(curm.getInvestAmount()));
            curm.setCouponAmountStr(AmountConverter.convertCentToString(curm.getCouponAmount()));
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<CouponUseRecordView> dataDto = new BasePaginationDataDto<>(index, pageSize, count, couponUseRecordList);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

}
