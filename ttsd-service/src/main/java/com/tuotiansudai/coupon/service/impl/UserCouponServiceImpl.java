package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private UserBirthdayUtil userBirthdayUtil;

    public List<UserCouponDto> getUnusedUserCoupons(String loginName) {
        List<UserCouponModel> userModelList = userCouponMapper.findByLoginName(loginName, null);
        List<UserCouponDto> unusedUserCouponDtoList = new ArrayList<>();

        for (UserCouponModel userCouponModel : userModelList) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON) {
                UserCouponDto userCouponDto = new UserCouponDto(couponModel, userCouponModel);
                if (userCouponDto.isUnused()) {
                    unusedUserCouponDtoList.add(userCouponDto);
                }
            }
        }
        Collections.sort(unusedUserCouponDtoList);
        return unusedUserCouponDtoList;
    }

    public List<CouponUseRecordView> findUseRecords(String loginName) {
        List<CouponUseRecordView> useRecordViews = userCouponMapper.findUseRecords(loginName);
        return useRecordViews;
    }

    public List<UserCouponDto> getExpiredUserCoupons(String loginName) {
        List<UserCouponModel> userModelList = userCouponMapper.findByLoginName(loginName, null);
        List<UserCouponDto> expiredUserCouponDtoList = new ArrayList<>();

        for (UserCouponModel userCouponModel : userModelList) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON) {
                UserCouponDto userCouponDto = new UserCouponDto(couponModel, userCouponModel);
                if (userCouponDto.isExpired()) {
                    expiredUserCouponDtoList.add(userCouponDto);
                }
            }
        }
        Collections.sort(expiredUserCouponDtoList);
        return expiredUserCouponDtoList;
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
                boolean isUsable = dto.getProductTypeList().contains(loanModel.getProductType()) && dto.isUnused();
                switch (dto.getCouponType()) {
                    case BIRTHDAY_COUPON:
                        return isUsable && userBirthdayUtil.isBirthMonth(dto.getLoginName());
                    default:
                        return isUsable;
                }
            }
        }));
    }

}