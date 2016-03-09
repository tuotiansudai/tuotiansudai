package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.UserCouponListResponseDataDto;
import com.tuotiansudai.api.dto.UserCouponRequestDto;
import com.tuotiansudai.api.service.impl.MobileAppUserCouponServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppUserCouponServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppUserCouponServiceImpl mobileAppUserCouponService;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Test
    public void shouldGetUsedUserCoupons() {
        CouponModel unusedCouponModel = new CouponModel();
        unusedCouponModel.setId(1);
        unusedCouponModel.setCouponType(CouponType.NEWBIE_COUPON);
        unusedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unusedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unusedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unusedCouponModel.setAmount(100);

        CouponModel usedCouponModel = new CouponModel();
        usedCouponModel.setId(2);
        usedCouponModel.setCouponType(CouponType.INVEST_COUPON);
        usedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        usedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        usedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        usedCouponModel.setAmount(200);

        CouponModel expiredCouponModel = new CouponModel();
        expiredCouponModel.setId(3);
        expiredCouponModel.setCouponType(CouponType.INVEST_COUPON);
        expiredCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        expiredCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().minusDays(2).toDate());
        expiredCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().minusDays(1).toDate());
        expiredCouponModel.setAmount(200);


        UserCouponModel unusedUserCouponModel = new UserCouponModel();
        unusedUserCouponModel.setId(1);
        unusedUserCouponModel.setCouponId(unusedCouponModel.getId());

        LoanModel loanModel = new LoanModel();
        loanModel.setId(1);
        loanModel.setName("name");
        loanModel.setProductType(ProductType.JYF);
        InvestModel investModel = new InvestModel();
        investModel.setId(1);
        investModel.setLoanId(1);
        investModel.setAmount(1000);
        UserCouponModel usedUserCouponModel = new UserCouponModel();
        usedUserCouponModel.setId(2);
        usedUserCouponModel.setStatus(InvestStatus.SUCCESS);
        usedUserCouponModel.setLoanId(loanModel.getId());
        usedUserCouponModel.setCouponId(usedCouponModel.getId());
        usedUserCouponModel.setUsedTime(new Date());
        usedUserCouponModel.setExpectedInterest(100);
        usedUserCouponModel.setInvestId(1L);
        usedUserCouponModel.setInvestAmount(1000L);

        UserCouponModel expiredUserCouponModel = new UserCouponModel();
        expiredUserCouponModel.setId(3);
        expiredUserCouponModel.setCouponId(expiredCouponModel.getId());



        List<UserCouponModel> fakeUserCoupons = Lists.newArrayList(unusedUserCouponModel, usedUserCouponModel, expiredUserCouponModel);
        when(userCouponMapper.findByLoginName(anyString(),anyList())).thenReturn(fakeUserCoupons);
        when(couponMapper.findById(unusedCouponModel.getId())).thenReturn(unusedCouponModel);
        when(couponMapper.findById(usedCouponModel.getId())).thenReturn(usedCouponModel);
        when(couponMapper.findById(expiredUserCouponModel.getId())).thenReturn(expiredCouponModel);
        when(loanMapper.findById(loanModel.getId())).thenReturn(loanModel);
        when(investMapper.findById(loanModel.getId())).thenReturn(investModel);

        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setUsed(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(responseDto.getData().getCoupons().get(0).getUserCouponId(), is(String.valueOf(usedUserCouponModel.getId())));
        assertThat(responseDto.getData().getCoupons().get(0).getLoanId(), is(String.valueOf(loanModel.getId())));
        assertThat(responseDto.getData().getCoupons().get(0).getLoanName(), is(String.valueOf(loanModel.getName())));
        assertThat(responseDto.getData().getCoupons().get(0).getLoanProductType(), is(loanModel.getProductType()));
    }

    @Test
    public void shouldGetUnusedUserCoupons() {
        CouponModel unusedCouponModel = new CouponModel();
        unusedCouponModel.setId(1);
        unusedCouponModel.setCouponType(CouponType.NEWBIE_COUPON);
        unusedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unusedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unusedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unusedCouponModel.setAmount(100);

        CouponModel usedCouponModel = new CouponModel();
        usedCouponModel.setId(2);
        usedCouponModel.setCouponType(CouponType.INVEST_COUPON);
        usedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        unusedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unusedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        usedCouponModel.setAmount(200);

        CouponModel expiredCouponModel = new CouponModel();
        expiredCouponModel.setId(3);
        expiredCouponModel.setCouponType(CouponType.INVEST_COUPON);
        expiredCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        expiredCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().minusDays(2).toDate());
        expiredCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().minusDays(1).toDate());
        expiredCouponModel.setAmount(200);


        UserCouponModel unusedUserCouponModel = new UserCouponModel();
        unusedUserCouponModel.setId(1);
        unusedUserCouponModel.setCouponId(unusedCouponModel.getId());


        UserCouponModel usedUserCouponModel = new UserCouponModel();
        usedUserCouponModel.setId(2);
        usedUserCouponModel.setStatus(InvestStatus.SUCCESS);
        usedUserCouponModel.setCouponId(usedCouponModel.getId());

        UserCouponModel expiredUserCouponModel = new UserCouponModel();
        expiredUserCouponModel.setId(3);
        expiredUserCouponModel.setCouponId(expiredCouponModel.getId());

        List<UserCouponModel> fakeUserCoupons = Lists.newArrayList(unusedUserCouponModel, usedUserCouponModel, expiredUserCouponModel);
        when(userCouponMapper.findByLoginName(anyString(),anyList())).thenReturn(fakeUserCoupons);
        when(couponMapper.findById(unusedCouponModel.getId())).thenReturn(unusedCouponModel);
        when(couponMapper.findById(usedCouponModel.getId())).thenReturn(usedCouponModel);
        when(couponMapper.findById(expiredUserCouponModel.getId())).thenReturn(expiredCouponModel);

        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setUnused(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(responseDto.getData().getCoupons().get(0).getUserCouponId(), is(String.valueOf(unusedUserCouponModel.getId())));
    }

    @Test
    public void shouldGetExpiredUserCoupons() {
        CouponModel unusedCouponModel = new CouponModel();
        unusedCouponModel.setId(1);
        unusedCouponModel.setCouponType(CouponType.NEWBIE_COUPON);
        unusedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unusedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unusedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unusedCouponModel.setAmount(100);

        CouponModel usedCouponModel = new CouponModel();
        usedCouponModel.setId(2);
        usedCouponModel.setCouponType(CouponType.INVEST_COUPON);
        usedCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        unusedCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unusedCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        usedCouponModel.setAmount(200);

        CouponModel expiredCouponModel = new CouponModel();
        expiredCouponModel.setId(3);
        expiredCouponModel.setCouponType(CouponType.INVEST_COUPON);
        expiredCouponModel.setProductTypes(Lists.newArrayList(ProductType.SYL, ProductType.WYX, ProductType.JYF));
        expiredCouponModel.setStartTime(new DateTime().withTimeAtStartOfDay().minusDays(2).toDate());
        expiredCouponModel.setEndTime(new DateTime().withTimeAtStartOfDay().minusDays(1).toDate());
        expiredCouponModel.setAmount(200);


        UserCouponModel unusedUserCouponModel = new UserCouponModel();
        unusedUserCouponModel.setId(1);
        unusedUserCouponModel.setCouponId(unusedCouponModel.getId());


        UserCouponModel usedUserCouponModel = new UserCouponModel();
        usedUserCouponModel.setId(2);
        usedUserCouponModel.setStatus(InvestStatus.SUCCESS);
        usedUserCouponModel.setCouponId(usedCouponModel.getId());

        UserCouponModel expiredUserCouponModel = new UserCouponModel();
        expiredUserCouponModel.setId(3);
        expiredUserCouponModel.setCouponId(expiredCouponModel.getId());

        List<UserCouponModel> fakeUserCoupons = Lists.newArrayList(unusedUserCouponModel, usedUserCouponModel, expiredUserCouponModel);
        when(userCouponMapper.findByLoginName(anyString(),anyList())).thenReturn(fakeUserCoupons);
        when(couponMapper.findById(unusedCouponModel.getId())).thenReturn(unusedCouponModel);
        when(couponMapper.findById(usedCouponModel.getId())).thenReturn(usedCouponModel);
        when(couponMapper.findById(expiredUserCouponModel.getId())).thenReturn(expiredCouponModel);

        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setExpired(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(responseDto.getData().getCoupons().get(0).getUserCouponId(), is(String.valueOf(expiredUserCouponModel.getId())));
    }
}
