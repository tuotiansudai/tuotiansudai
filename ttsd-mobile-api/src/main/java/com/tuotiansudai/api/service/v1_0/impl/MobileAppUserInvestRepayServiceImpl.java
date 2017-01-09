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
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private LoanMapper loanMapper;

    private final static String RED_ENVELOPE_TEMPLATE = "{0}元现金红包";

    private final static String NEWBIE_COUPON_TEMPLATE = "{0}元新手体验金";

    private final static String INVEST_COUPON_TEMPLATE = "{0}元投资体验券";

    private final static String INTEREST_COUPON_TEMPLATE = "{0}%加息券";

    @Override
    public BaseResponseDto<UserInvestRepayResponseDataDto> userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        BaseResponseDto<UserInvestRepayResponseDataDto> responseDto = new BaseResponseDto<>();
        long totalExpectedInterest = 0;
        long completeTotalActualInterest = 0;
        long unPaidTotalRepay = 0;

        try {
            InvestModel investModel = investService.findById(Long.parseLong(userInvestRepayRequestDto.getInvestId().trim()));
            LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
            //未放款时按照预计利息计算
            if (loanModel.getRecheckTime() == null) {
                totalExpectedInterest = investService.estimateInvestIncome(loanModel.getId(), investModel.getLoginName(), investModel.getAmount());
            }
            UserInvestRepayResponseDataDto userInvestRepayResponseDataDto = new UserInvestRepayResponseDataDto(loanModel, investModel);
            if(investModel.getTransferInvestId() != null){
                TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                userInvestRepayResponseDataDto.setLoanName(transferApplicationModel != null ? transferApplicationModel.getName() : loanModel.getName());
            }
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            List<InvestRepayDataDto> investRepayList = new ArrayList<>();
            int maxPeriods = investRepayModels == null ? 0 : investRepayModels.size();
            InvestRepayModel lastedInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), maxPeriods);
            userInvestRepayResponseDataDto.setLastRepayDate(lastedInvestRepayModel == null ? "" : sdf.format(lastedInvestRepayModel.getRepayDate()));
            List<TransferApplicationModel> transferApplicationModels;
            for (InvestRepayModel investRepayModel : investRepayModels) {
                Date repayDate = investRepayModel.getActualRepayDate();
                if (investRepayModel.isTransferred()) {
                    transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
                    if (CollectionUtils.isNotEmpty(transferApplicationModels) && transferApplicationModels.get(0).getPeriod() != investRepayModel.getPeriod()) {
                        continue;
                    }
                    repayDate = transferApplicationModels.get(0).getTransferTime();
                }
                CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod());
                long expectedInterest = investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee();
                long actualInterest = investRepayModel.getRepayAmount();
                if (couponRepayModel != null) {
                    expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    actualInterest += couponRepayModel.getRepayAmount();
                }

                int periods = loanMapper.findById(investModel.getLoanId()).getPeriods();
                long corpus = 0;
                if (periods == investRepayModel.getPeriod()) {
                    corpus = investRepayModel.getCorpus();
                    InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investRepayModel.getInvestId());
                    if (investExtraRateModel != null && !investExtraRateModel.isTransfer()) {
                        expectedInterest += investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                        actualInterest += investExtraRateModel.getRepayAmount();
                    }
                }

                InvestRepayDataDto investRepayDataDto = new InvestRepayDataDto();
                investRepayDataDto.setIsTransferred(investRepayModel.isTransferred());
                investRepayDataDto.setPeriod(investRepayModel.getPeriod());
                investRepayDataDto.setRepayDate(sdf.format(investRepayModel.getRepayDate()));
                investRepayDataDto.setActualRepayDate(repayDate == null ? "" : sdf.format(repayDate));
                investRepayDataDto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest + corpus));
                investRepayDataDto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
                investRepayDataDto.setStatus(investRepayModel.getStatus().name());
                investRepayList.add(investRepayDataDto);
                if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                    completeTotalActualInterest += actualInterest;
                } else {
                    unPaidTotalRepay += expectedInterest + investRepayModel.getCorpus();
                }
                totalExpectedInterest += expectedInterest;
            }

            userInvestRepayResponseDataDto.setExpectedInterest(AmountConverter.convertCentToString(totalExpectedInterest));
            userInvestRepayResponseDataDto.setActualInterest(AmountConverter.convertCentToString(completeTotalActualInterest));
            userInvestRepayResponseDataDto.setUnPaidRepay(AmountConverter.convertCentToString(unPaidTotalRepay));
            userInvestRepayResponseDataDto.setInvestRepays(investRepayList);
            MembershipModel membershipModel = userMembershipEvaluator.evaluateSpecifiedDate(investModel.getLoginName(), investModel.getInvestTime());
            userInvestRepayResponseDataDto.setMembershipLevel(String.valueOf(membershipModel.getLevel()));
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
        } catch (Exception e) {
            responseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            responseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            e.printStackTrace();
        }
        return responseDto;
    }

    private String generateUsedCouponName(CouponModel couponModel) {
        if (couponModel == null) {
            return "";
        }
        String usedCouponName = "";
        switch (couponModel.getCouponType()) {
            case RED_ENVELOPE:
                usedCouponName = MessageFormat.format(RED_ENVELOPE_TEMPLATE, AmountConverter.convertCentToString(couponModel.getAmount()));
                return usedCouponName;
            case NEWBIE_COUPON:
                usedCouponName = MessageFormat.format(NEWBIE_COUPON_TEMPLATE, AmountConverter.convertCentToString(couponModel.getAmount()));
                return usedCouponName;
            case INVEST_COUPON:
                usedCouponName = MessageFormat.format(INVEST_COUPON_TEMPLATE, AmountConverter.convertCentToString(couponModel.getAmount()));
                return usedCouponName;
            case INTEREST_COUPON:
                usedCouponName = MessageFormat.format(INTEREST_COUPON_TEMPLATE, couponModel.getRate() * 100);
                return usedCouponName;
            case BIRTHDAY_COUPON:
                usedCouponName = couponModel.getCouponType().getName();

        }

        return usedCouponName;
    }
}
