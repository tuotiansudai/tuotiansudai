package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserInvestRepayService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppUserInvestRepayServiceImpl implements MobileAppUserInvestRepayService {

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    private final static String RED_ENVELOPE_TEMPLATE = "{0}元现金红包";

    private final static String NEWBIE_COUPON_TEMPLATE = "{0}元新手体验金";

    private final static String INVEST_COUPON_TEMPLATE = "{0}元投资体验券";

    private final static String INTEREST_COUPON_TEMPLATE = "{0}%加息券";

    @Override
    public BaseResponseDto userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = new BaseResponseDto<>();
        long totalExpectedInterest = 0;
        long completeTotalActualInterest = 0;
        long unPaidTotalRepay = 0;

        try {
            InvestModel investModel = investService.findById(Long.parseLong(userInvestRepayRequestDto.getInvestId().trim()));
            LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
            //未放款时按照预计利息计算
            if(loanModel.getRecheckTime() == null){
                totalExpectedInterest = investService.estimateInvestIncome(loanModel.getId(), investModel.getLoginName(), investModel.getAmount());
            }
            UserInvestRepayResponseDataDto userInvestRepayResponseDataDto = new UserInvestRepayResponseDataDto(loanModel, investModel);
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            List<InvestRepayDataDto> investRepayList = new ArrayList<>();
            int maxPeriods = investRepayModels == null?0:investRepayModels.size();
            InvestRepayModel lastedInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), maxPeriods);
            userInvestRepayResponseDataDto.setLastRepayDate(lastedInvestRepayModel == null ? "" : sdf.format(lastedInvestRepayModel.getRepayDate()));
            for (InvestRepayModel investRepayModel : investRepayModels) {
                CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investRepayModel.getInvestId(),investRepayModel.getPeriod());
                long expectedInterest = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee();
                long actualInterest = investRepayModel.getRepayAmount();
                if(couponRepayModel != null){
                    expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    actualInterest += couponRepayModel.getRepayAmount();
                }
                InvestRepayDataDto investRepayDataDto = new InvestRepayDataDto();
                investRepayDataDto.setPeriod(investRepayModel.getPeriod());
                investRepayDataDto.setRepayDate(sdf.format(investRepayModel.getRepayDate()));
                investRepayDataDto.setActualRepayDate(investRepayModel.getActualRepayDate() == null ? "" : sdf.format(investRepayModel.getActualRepayDate()));
                investRepayDataDto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest));
                investRepayDataDto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
                investRepayDataDto.setStatus(investRepayModel.getStatus().name());
                investRepayList.add(investRepayDataDto);
                if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                    completeTotalActualInterest += actualInterest;
                }else{
                    unPaidTotalRepay += expectedInterest;
                }
                totalExpectedInterest += expectedInterest;
            }

            userInvestRepayResponseDataDto.setExpectedInterest(AmountConverter.convertCentToString(totalExpectedInterest));
            userInvestRepayResponseDataDto.setActualInterest(AmountConverter.convertCentToString(completeTotalActualInterest));
            userInvestRepayResponseDataDto.setUnPaidRepay(AmountConverter.convertCentToString(unPaidTotalRepay));
            userInvestRepayResponseDataDto.setInvestRepays(investRepayList);
            List<MembershipModel> membershipModels =  membershipMapper.findAllMembership();
            for(MembershipModel membershipModel:membershipModels){
                userInvestRepayResponseDataDto.setMembershipLevel(investModel.getInvestFeeRate() == membershipModel.getFee()?String.valueOf(membershipModel.getLevel()):"0");
            }
            List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investModel.getId());

            List<String> usedCoupons = Lists.transform(userCouponModels, new Function<UserCouponModel, String>() {
                @Override
                public String apply(UserCouponModel input) {

                    return generateUsedCouponName(couponMapper.findById(input.getCouponId()));
                }
            });
            userInvestRepayResponseDataDto.setUsedCoupons(usedCoupons);

            responseDto.setCode(ReturnMessage.SUCCESS.getCode());
            responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            responseDto.setData(userInvestRepayResponseDataDto);
        }catch(Exception e){
            responseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            responseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        return responseDto;
    }

    private String generateUsedCouponName(CouponModel couponModel){
        if(couponModel == null){
            return "";
        }
        String usedCouponName = "";
        switch (couponModel.getCouponType()){
            case RED_ENVELOPE:
                usedCouponName = MessageFormat.format(RED_ENVELOPE_TEMPLATE,AmountConverter.convertCentToString(couponModel.getAmount()));
            case NEWBIE_COUPON:
                usedCouponName = MessageFormat.format(NEWBIE_COUPON_TEMPLATE,AmountConverter.convertCentToString(couponModel.getAmount()));
            case INVEST_COUPON:
                usedCouponName = MessageFormat.format(INVEST_COUPON_TEMPLATE,AmountConverter.convertCentToString(couponModel.getAmount()));
            case INTEREST_COUPON:
                usedCouponName = MessageFormat.format(INTEREST_COUPON_TEMPLATE,couponModel.getRate() * 100);
            case BIRTHDAY_COUPON:
                usedCouponName = couponModel.getCouponType().getName();

        }

        return usedCouponName;
    }
}
