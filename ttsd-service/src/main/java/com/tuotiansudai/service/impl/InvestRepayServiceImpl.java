package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.InvestorInvestPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.dto.InvestRepayDataItemDto;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LatestInvestView;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.InvestService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvestRepayServiceImpl implements InvestRepayService {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestService investService;

    @Override
    public long findByLoginNameAndTimeAndSuccessInvestRepay(String loginName,Date startTime,Date endTime) {
        return investRepayMapper.findByLoginNameAndTimeAndSuccessInvestRepay(loginName, startTime, endTime);
    }

    @Override
    public long findByLoginNameAndTimeAndNotSuccessInvestRepay(String loginName,Date startTime,Date endTime) {
        return investRepayMapper.findByLoginNameAndTimeAndNotSuccessInvestRepay(loginName, startTime, endTime);
    }

    @Override
    public List<InvestRepayDataItemDto> findByLoginNameAndTimeSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndTimeSuccessInvestRepayList(loginName, startTime, endTime, startLimit, endLimit);
        return investRepayCouponAll(investRepayModels, loginName);
    }

    @Override
    public List<InvestRepayDataItemDto> findByLoginNameAndTimeNotSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndTimeNotSuccessInvestRepayList(loginName, startTime, endTime, startLimit, endLimit);
        return investRepayCouponAll(investRepayModels, loginName);
    }

    private List<InvestRepayDataItemDto> investRepayCouponAll(List<InvestRepayModel> investRepayModels, String loginName) {
        List<InvestRepayDataItemDto> investRepayDataItemDtoList = Lists.transform(investRepayModels, new Function<InvestRepayModel, InvestRepayDataItemDto>() {
            @Override
            public InvestRepayDataItemDto apply(InvestRepayModel input) {
                return new InvestRepayDataItemDto().generateInvestRepayDataItemDto(input);
            }
        });
        for (InvestRepayDataItemDto investRepayDataItemDto : investRepayDataItemDtoList) {
            List<UserCouponModel> userCouponModels = userCouponMapper.findBirthdaySuccessByLoginNameAndInvestId(loginName, investRepayDataItemDto.getInvestId());
            investRepayDataItemDto.setBirthdayCoupon(CollectionUtils.isNotEmpty(userCouponModels));
            if (CollectionUtils.isNotEmpty(userCouponModels)) {
                investRepayDataItemDto.setBirthdayBenefit(couponMapper.findById(userCouponModels.get(0).getCouponId()).getBirthdayBenefit());
            }

        }
        return investRepayDataItemDtoList;
    }

    @Override
    public List<InvestorInvestPaginationItemDataDto> findLatestInvestByLoginName(String loginName, int startLimit, int endLimit) {
        List<InvestorInvestPaginationItemDataDto> latestInvestViews=investService.getInvestPagination(loginName,1,6,null,null,null).getRecords();
        for (InvestorInvestPaginationItemDataDto latestInvestView : latestInvestViews) {
            List<UserCouponModel> userCouponModels = userCouponMapper.findBirthdaySuccessByLoginNameAndInvestId(loginName, latestInvestView.getInvestId());
            latestInvestView.setBirthdayCoupon(CollectionUtils.isNotEmpty(userCouponModels));
            if (CollectionUtils.isNotEmpty(userCouponModels)) {
                latestInvestView.setBirthdayBenefit(couponMapper.findById(userCouponModels.get(0).getCouponId()).getBirthdayBenefit());
            }
        }
        return latestInvestViews;
    }
}
