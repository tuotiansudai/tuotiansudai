package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserCouponListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestCouponService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
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
    private LoanDetailsMapper loanDetailsMapper;

    @Test
    public void shouldGetInvestCouponsIsSuccess() {
        UserModel userModel = getFakeUser("testInvestCoupon");
        userMapper.create(userModel);
        CouponModel couponModelInvestCoupon = fakeCouponModel(userModel, CouponType.INVEST_COUPON);
        CouponModel couponModelRedEnvelope = fakeCouponModel(userModel, CouponType.RED_ENVELOPE);
        couponMapper.create(couponModelInvestCoupon);
        couponMapper.create(couponModelRedEnvelope);
        UserCouponModel userCouponModelRedEnvelope = fakeUserCouponModel(couponModelRedEnvelope.getId(),
                userModel.getLoginName(), new DateTime().plusDays(-1).withTimeAtStartOfDay().toDate(),
                new DateTime().plusDays(5).withTimeAtStartOfDay().toDate());

        UserCouponModel userCouponModelInvestCoupon = fakeUserCouponModel(couponModelInvestCoupon.getId(),
                userModel.getLoginName(), new DateTime().plusDays(-1).withTimeAtStartOfDay().toDate(),
                new DateTime().plusDays(5).withTimeAtStartOfDay().toDate());
        userCouponMapper.create(userCouponModelInvestCoupon);
        userCouponMapper.create(userCouponModelRedEnvelope);
        LoanModel loanModel = getFakeLoan(LoanStatus.RAISING, ActivityType.NORMAL, userModel.getLoginName());
        loanMapper.create(loanModel);
        LoanDetailsModel loanDetailsModel = getFakeLoanDetail(loanModel.getId());
        loanDetailsMapper.create(loanDetailsModel);
        InvestRequestDto investRequestDto = new InvestRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        investRequestDto.setBaseParam(baseParam);
        investRequestDto.setInvestMoney("50");
        investRequestDto.setLoanId(String.valueOf(loanModel.getId()));
        investRequestDto.setUserId(userModel.getLoginName());


        BaseResponseDto<UserCouponListResponseDataDto> baseResponseDto = mobileAppInvestCouponService.getInvestCoupons(investRequestDto);

        assertEquals(2, baseResponseDto.getData().getCoupons().size());
        assertEquals(CouponType.RED_ENVELOPE, baseResponseDto.getData().getCoupons().get(0).getType());
        assertEquals(CouponType.INVEST_COUPON, baseResponseDto.getData().getCoupons().get(1).getType());
    }

    private UserCouponModel fakeUserCouponModel(long couponId, String loginName, Date startTime, Date endTime) {
        return new UserCouponModel(loginName, couponId, startTime, endTime);
    }

    private LoanModel getFakeLoan(LoanStatus loanStatus, ActivityType activityType, String agentLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
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
        fakeLoanModel.setProductType(ProductType._30);
        return fakeLoanModel;
    }

    private LoanDetailsModel getFakeLoanDetail(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setId(IdGenerator.generate());
        loanDetailsModel.setLoanId(loanId);
        loanDetailsModel.setDisableCoupon(false);
        loanDetailsModel.setActivity(false);
        loanDetailsModel.setDeclaration("declaration");
        return loanDetailsModel;
    }

}
