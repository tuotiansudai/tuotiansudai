package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserCouponListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestCouponService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class MobileAppInvestCouponServiceTest extends ServiceTestBase {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private MobileAppInvestCouponService mobileAppInvestCouponService;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGetInvestCouponsIsSuccess(){
        UserModel userModel = getFakeUser("testInvestCoupon");
        userMapper.create(userModel);
        CouponModel couponModelBirthday = fakeCouponModel(userModel, CouponType.BIRTHDAY_COUPON);
        CouponModel couponModelInvestCoupon = fakeCouponModel(userModel,CouponType.INVEST_COUPON);
        couponMapper.create(couponModelBirthday);
        couponMapper.create(couponModelInvestCoupon);
        UserCouponModel userCouponModelBirthday = fakeUserCouponModel(couponModelBirthday.getId(),
                userModel.getLoginName(),new DateTime().plusDays(-1).withTimeAtStartOfDay().toDate(),
                new DateTime().plusDays(5).withTimeAtStartOfDay().toDate());

        UserCouponModel userCouponModelInvestCoupon = fakeUserCouponModel(couponModelInvestCoupon.getId(),
                userModel.getLoginName(),new DateTime().plusDays(-1).withTimeAtStartOfDay().toDate(),
                new DateTime().plusDays(5).withTimeAtStartOfDay().toDate());
        userCouponMapper.create(userCouponModelBirthday);
        userCouponMapper.create(userCouponModelInvestCoupon);
        LoanModel loanModel = getFakeLoan(LoanStatus.RAISING,ActivityType.NORMAL,userModel.getLoginName());
        loanMapper.create(loanModel);
        InvestRequestDto investRequestDto = new InvestRequestDto();
        BaseParam baseParam = new BaseParam();
        investRequestDto.setBaseParam(baseParam);
        investRequestDto.setInvestMoney("50");
        investRequestDto.setLoanId(String.valueOf(loanModel.getId()));
        investRequestDto.setUserId(userModel.getLoginName());


        BaseResponseDto<UserCouponListResponseDataDto> baseResponseDto = mobileAppInvestCouponService.getInvestCoupons(investRequestDto);

        assertEquals(2,baseResponseDto.getData().getCoupons().size());




    }
    private UserCouponModel fakeUserCouponModel(long couponId,String loginName,Date startTime,Date endTime) {
        return new UserCouponModel(loginName, couponId, startTime, endTime);
    }

    private LoanModel getFakeLoan(LoanStatus loanStatus, ActivityType activityType,String agentLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName("loanerLoginName");
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setPledgeType(PledgeType.VEHICLE);
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }


}
