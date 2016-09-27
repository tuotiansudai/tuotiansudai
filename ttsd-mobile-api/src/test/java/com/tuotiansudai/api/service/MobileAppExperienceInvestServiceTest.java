package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestExperienceResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppExperienceInvestServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppExperienceInvestServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppExperienceInvestServiceImpl mobileAppExperienceInvestService;

    @Mock
    private ExperienceInvestService experienceInvestService;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldExperienceInvestSuccess() {
        long loanId = idGenerator.generate();
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setLoanId(String.valueOf(loanId));
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("investor");
        baseParam.setPlatform("IOS");
        investRequestDto.setBaseParam(baseParam);
        investRequestDto.setInvestMoney("5888.00");

        when(mobileAppChannelService.obtainChannelBySource(any(BaseParam.class))).thenReturn("tuotiansudai");

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);

        when(experienceInvestService.invest(any(InvestDto.class))).thenReturn(baseDto);

        List<UserCouponModel> userCouponModels = Lists.newArrayList();
        UserCouponModel userCouponModel = new UserCouponModel();
        long userCouponId = idGenerator.generate();
        long couponId = idGenerator.generate();
        userCouponModel.setId(userCouponId);
        userCouponModel.setLoanId(couponId);
        Date start = new Date();
        Date end = new Date();
        userCouponModel.setStartTime(start);
        userCouponModel.setEndTime(end);
        userCouponModels.add(userCouponModel);
        when(userCouponMapper.findByLoginName(anyString(), anyList())).thenReturn(userCouponModels);

        CouponModel couponModel = new CouponModel();
        couponModel.setId(couponId);
        couponModel.setUserGroup(UserGroup.EXPERIENCE_INVEST_SUCCESS);
        couponModel.setAmount(588800);
        couponModel.setInvestLowerLimit(100000);
        couponModel.setCouponType(CouponType.NEWBIE_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._180));
        couponModel.setRate(0.14);
        when(couponMapper.findById(anyLong())).thenReturn(couponModel);

        BaseResponseDto<InvestExperienceResponseDto> baseResponseDto = mobileAppExperienceInvestService.experienceInvest(investRequestDto);

        assertThat(baseResponseDto.getData().getCoupons().get(0).getType(), is(couponModel.getCouponType().name()));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getAmount(), is(AmountConverter.convertCentToString(couponModel.getAmount())));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getEndDate(), is(new DateTime(userCouponModel.getEndTime()).toString("yyyy-MM-dd")));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getStartDate(), is(new DateTime(userCouponModel.getStartTime()).toString("yyyy-MM-dd")));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getInvestLowerLimit(), is(AmountConverter.convertCentToString(couponModel.getInvestLowerLimit())));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getName(), is(couponModel.getCouponType().getName()));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getProductNewTypes(), is(couponModel.getProductTypes()));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getRate(), is("14"));
        assertThat(baseResponseDto.getData().getCoupons().get(0).getUserCouponId(), is(String.valueOf(userCouponModel.getId())));
    }

}
