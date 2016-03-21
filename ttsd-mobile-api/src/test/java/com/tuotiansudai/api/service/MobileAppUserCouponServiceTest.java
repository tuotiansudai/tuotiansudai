package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppUserCouponServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
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

    @Mock
    private UserCouponService userCouponService;

    @Test
    public void shouldGetUsedUserCoupons() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(1);
        loanModel.setName("name");
        loanModel.setProductType(ProductType.JYF);
        InvestModel investModel = new InvestModel();
        investModel.setId(1);
        investModel.setLoanId(1);
        investModel.setAmount(1000);


        List<UserCouponView> usedUserCoupon = Lists.newArrayList();
        UserCouponView usedUserCouponView = new UserCouponView();
        usedUserCoupon.add(usedUserCouponView);
        usedUserCouponView.setUsedTime(new Date());
        usedUserCouponView.setLoanId(loanModel.getId());
        usedUserCouponView.setLoanName(loanModel.getName());
        usedUserCouponView.setLoanProductType(loanModel.getProductType());
        usedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);

        List<UserCouponView> unusedUserCoupon = Lists.newArrayList();
        UserCouponView unUsedUserCouponView = new UserCouponView();
        unusedUserCoupon.add(unUsedUserCouponView);
        unUsedUserCouponView.setId(1);
        unUsedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);
        unUsedUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unUsedUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unUsedUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unUsedUserCouponView.setCouponAmount(100);

        List<UserCouponView> expiredUserCoupon = Lists.newArrayList();
        UserCouponView expiredUserCouponView = new UserCouponView();
        expiredUserCoupon.add(expiredUserCouponView);
        expiredUserCouponView.setId(1);
        expiredUserCouponView.setCouponType(CouponType.BIRTHDAY_COUPON);
        expiredUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        expiredUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        expiredUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        expiredUserCouponView.setCouponAmount(200);


        when(userCouponService.getUnusedUserCoupons(anyString())).thenReturn(unusedUserCoupon);
        when(userCouponService.getExpiredUserCoupons(anyString())).thenReturn(expiredUserCoupon);
        when(userCouponService.findUseRecords(anyString())).thenReturn(usedUserCoupon);


        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setUsed(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(((UserCouponResponseDataDto)(responseDto.getData().getCoupons().get(0))).getLoanId(), is(String.valueOf(loanModel.getId())));
        assertThat(((UserCouponResponseDataDto)responseDto.getData().getCoupons().get(0)).getLoanName(), is(String.valueOf(loanModel.getName())));
        assertThat(((UserCouponResponseDataDto)responseDto.getData().getCoupons().get(0)).getLoanProductType(), is(loanModel.getProductType()));
    }

    @Test
    public void shouldGetUnusedUserCoupons() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(1);
        loanModel.setName("name");
        loanModel.setProductType(ProductType.JYF);
        InvestModel investModel = new InvestModel();
        investModel.setId(1);
        investModel.setLoanId(1);
        investModel.setAmount(1000);


        List<UserCouponView> usedUserCoupon = Lists.newArrayList();
        UserCouponView usedUserCouponView = new UserCouponView();
        usedUserCoupon.add(usedUserCouponView);
        usedUserCouponView.setUsedTime(new Date());
        usedUserCouponView.setLoanId(loanModel.getId());
        usedUserCouponView.setLoanName(loanModel.getName());
        usedUserCouponView.setLoanProductType(loanModel.getProductType());
        usedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);

        List<UserCouponView> unusedUserCoupon = Lists.newArrayList();
        UserCouponView unUsedUserCouponView = new UserCouponView();
        unusedUserCoupon.add(unUsedUserCouponView);
        unUsedUserCouponView.setId(1);
        unUsedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);
        unUsedUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unUsedUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unUsedUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unUsedUserCouponView.setCouponAmount(100);

        List<UserCouponView> expiredUserCoupon = Lists.newArrayList();
        UserCouponView expiredUserCouponView = new UserCouponView();
        expiredUserCoupon.add(expiredUserCouponView);
        expiredUserCouponView.setId(1);
        expiredUserCouponView.setCouponType(CouponType.BIRTHDAY_COUPON);
        expiredUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        expiredUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        expiredUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        expiredUserCouponView.setCouponAmount(200);


        when(userCouponService.getUnusedUserCoupons(anyString())).thenReturn(unusedUserCoupon);
        when(userCouponService.getExpiredUserCoupons(anyString())).thenReturn(expiredUserCoupon);
        when(userCouponService.findUseRecords(anyString())).thenReturn(usedUserCoupon);


        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setUnused(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(((UserCouponResponseDataDto)(responseDto.getData().getCoupons().get(0))).getType(), is(CouponType.NEWBIE_COUPON));
    }

    @Test
    public void shouldGetExpiredUserCoupons() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(1);
        loanModel.setName("name");
        loanModel.setProductType(ProductType.JYF);
        InvestModel investModel = new InvestModel();
        investModel.setId(1);
        investModel.setLoanId(1);
        investModel.setAmount(1000);


        List<UserCouponView> usedUserCoupon = Lists.newArrayList();
        UserCouponView usedUserCouponView = new UserCouponView();
        usedUserCoupon.add(usedUserCouponView);
        usedUserCouponView.setUsedTime(new Date());
        usedUserCouponView.setLoanId(loanModel.getId());
        usedUserCouponView.setLoanName(loanModel.getName());
        usedUserCouponView.setLoanProductType(loanModel.getProductType());
        usedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);

        List<UserCouponView> unusedUserCoupon = Lists.newArrayList();
        UserCouponView unUsedUserCouponView = new UserCouponView();
        unusedUserCoupon.add(unUsedUserCouponView);
        unUsedUserCouponView.setId(1);
        unUsedUserCouponView.setCouponType(CouponType.NEWBIE_COUPON);
        unUsedUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        unUsedUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        unUsedUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        unUsedUserCouponView.setCouponAmount(100);

        List<UserCouponView> expiredUserCoupon = Lists.newArrayList();
        UserCouponView expiredUserCouponView = new UserCouponView();
        expiredUserCoupon.add(expiredUserCouponView);
        expiredUserCouponView.setId(1);
        expiredUserCouponView.setCouponType(CouponType.BIRTHDAY_COUPON);
        expiredUserCouponView.setProductTypeList(Lists.newArrayList(ProductType.SYL, ProductType.WYX));
        expiredUserCouponView.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        expiredUserCouponView.setEndTime(new DateTime().withTimeAtStartOfDay().plusDays(1).toDate());
        expiredUserCouponView.setCouponAmount(200);


        when(userCouponService.getUnusedUserCoupons(anyString())).thenReturn(unusedUserCoupon);
        when(userCouponService.getExpiredUserCoupons(anyString())).thenReturn(expiredUserCoupon);
        when(userCouponService.findUseRecords(anyString())).thenReturn(usedUserCoupon);


        UserCouponRequestDto requestDto = new UserCouponRequestDto();
        requestDto.setBaseParam(new BaseParam());
        requestDto.setExpired(true);
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = mobileAppUserCouponService.getUserCoupons(requestDto);

        assertThat(responseDto.getData().getCoupons().size(), is(1));
        assertThat(((UserCouponResponseDataDto)(responseDto.getData().getCoupons().get(0))).getType(), is(CouponType.BIRTHDAY_COUPON));
    }
}
