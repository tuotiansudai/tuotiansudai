package com.tuotiansudai.util;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InterestCalculatorTest {

    @Test
    public void estimateDayRepayCouponRepayExpectedInterestTest(){
        Date recheckTime = DateTime.parse("2018-10-10 10:10:10", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        CouponModel couponModel = createCouponModel();
        InvestModel investModel = createInvest();
        LoanModel loanModel = createLoanByLoanType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, recheckTime);

        List<Integer> daysOfPerPeriod = LoanPeriodCalculator.calculateDaysOfPerPeriod(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        DateTime currentRepayDate = lastRepayDate.plusDays(daysOfPerPeriod.get(0));

        // 一期  360天
        long amount = InterestCalculator.estimateCouponRepayExpectedInterest(investModel, loanModel, couponModel, currentRepayDate, lastRepayDate);

        assertThat(amount, is(19726L));
    }

    @Test
    public void estimateMonthRepayCouponRepayExpectedInterestTest(){
        Date recheckTime = DateTime.parse("2018-10-10 10:10:10", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        CouponModel couponModel = createCouponModel();
        InvestModel investModel = createInvest();
        LoanModel loanModel = createLoanByLoanType(LoanType.INVEST_INTEREST_MONTHLY_REPAY, recheckTime);

        List<Integer> daysOfPerPeriod = LoanPeriodCalculator.calculateDaysOfPerPeriod(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        DateTime currentRepayDate = lastRepayDate.plusDays(daysOfPerPeriod.get(0));

        // 共12期, 第一期 28天
        long amount = InterestCalculator.estimateCouponRepayExpectedInterest(investModel, loanModel, couponModel, currentRepayDate, lastRepayDate);

        assertThat(amount, is(1534L));
    }

    private InvestModel createInvest() {
        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        model.setTradingTime(new Date());
        return model;
    }

    private LoanModel createLoanByLoanType(LoanType loanType, Date recheckTime) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setBasicRate("16.00");
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000000");
        loanDto.setType(loanType);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setProductType(ProductType._180);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setCreatedTime(recheckTime);
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setRecheckTime(recheckTime);
        loanModel.setDeadline(DateTime.now().plusDays(357).toDate());
        return loanModel;
    }


    private CouponModel createCouponModel(){
        CouponModel couponModel = new CouponModel();
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setRate(0.02D);
        return couponModel;
    }
}
