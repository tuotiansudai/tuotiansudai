package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanListService;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.api.util.ProductTypeConverter;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppLoanListServiceImpl implements MobileAppLoanListService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Override
    public BaseResponseDto<LoanListResponseDataDto> generateLoanList(LoanListRequestDto loanListRequestDto) {
        BaseResponseDto<LoanListResponseDataDto> dto = new BaseResponseDto<>();
        Integer index = loanListRequestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(loanListRequestDto.getPageSize());
        if (index == null || pageSize == null || index <= 0 || pageSize <= 0) {
            return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        index = (loanListRequestDto.getIndex() - 1) * pageSize;

        List<LoanModel> loanModels = loanMapper.findLoanListMobileApp(ProductTypeConverter.stringConvertTo(loanListRequestDto.getProductType()), null, loanListRequestDto.getLoanStatus(), loanListRequestDto.getRateLower(), loanListRequestDto.getRateUpper(), index, pageSize);

        List<PledgeType> pledgeTypeList = Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE, PledgeType.NONE);
        if (AppVersionUtil.compareVersion() == AppVersionUtil.low) {
            loanModels = loanModels.stream().filter(n -> pledgeTypeList.contains(n.getPledgeType())).collect(Collectors.toList());
        }

        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(loanModels)) {
            loanDtoList = convertLoanDto(loanModels, loanListRequestDto.getBaseParam().getUserId());
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());

        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
        loanListResponseDataDto.setIndex(loanListRequestDto.getIndex());
        loanListResponseDataDto.setPageSize(loanListRequestDto.getPageSize());
        loanListResponseDataDto.setTotalCount(loanMapper.findLoanListCountMobileApp(ProductTypeConverter.stringConvertTo(loanListRequestDto.getProductType()), loanListRequestDto.getLoanStatus(), loanListRequestDto.getRateLower(), loanListRequestDto.getRateUpper()));
        if (CollectionUtils.isNotEmpty(loanDtoList)) {
            loanListResponseDataDto.setLoanList(loanDtoList);
            dto.setData(loanListResponseDataDto);
        } else {
            loanListResponseDataDto.setLoanList(Lists.newArrayList());
            dto.setData(loanListResponseDataDto);
        }

        return dto;
    }

    private List<LoanResponseDataDto> convertLoanDto(List<LoanModel> loanList, String loginName) {
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");

        for (LoanModel loan : loanList) {

            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            loanResponseDataDto.setLoanType(loan.getProductType() != null ? loan.getProductType().getProductLine() : "");
            loanResponseDataDto.setLoanTypeName(loan.getProductType() != null ? loan.getProductType().getProductLineName() : "");
            LoanDetailsModel loanDetailsModelActivity = loanDetailsMapper.getByLoanId(loan.getId());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setActivityDesc(loanDetailsModelActivity != null ? loanDetailsModelActivity.getActivityDesc() : "");
            loanResponseDataDto.setPledgeType(loan.getPledgeType());
            loanResponseDataDto.setRepayTypeCode("");
            loanResponseDataDto.setRepayTypeName(loan.getType().getName());
            loanResponseDataDto.setDeadline(loan.getPeriods());
            loanResponseDataDto.setRepayUnit(loan.getType().getLoanPeriodUnit().getDesc());
            loanResponseDataDto.setRatePercent(decimalFormat.format((loan.getBaseRate() + loan.getActivityRate()) * 100));
            loanResponseDataDto.setLoanMoney(AmountConverter.convertCentToString(loan.getLoanAmount()));
            if (LoanStatus.PREHEAT.equals(loan.getStatus())) {
                loanResponseDataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(LoanStatus.RAISING.getDescription());
            } else {
                loanResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
            }
            loanResponseDataDto.setMinInvestMoney(AmountConverter.convertCentToString(loan.getMinInvestAmount()));
            loanResponseDataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loan.getMaxInvestAmount()));
            loanResponseDataDto.setCardinalNumber(AmountConverter.convertCentToString(loan.getInvestIncreasingAmount()));
            if (loan.getFundraisingStartTime() != null) {
                loanResponseDataDto.setInvestBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getFundraisingStartTime()));
            }
            loanResponseDataDto.setInvestBeginSeconds(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
            long investedAmount;
            if (loan.getProductType() == ProductType.EXPERIENCE) {
                Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
                Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
                List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loan.getId(), beginTime, endTime);
                investedAmount = couponService.findExperienceInvestAmount(investModelList);
            } else {
                investedAmount = investMapper.sumSuccessInvestAmount(loan.getId());
            }
            loanResponseDataDto.setInvestedMoney(AmountConverter.convertCentToString(investedAmount));
            loanResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
            loanResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));
            loanResponseDataDto.setDuration(String.valueOf(loan.getDuration()));
            loanResponseDataDto.setAvailableDuration(String.valueOf(LoanPeriodCalculator.calculateDuration(new Date(), loan.getDeadline())));
            loanResponseDataDto.setProductNewType(loan.getProductType() != null ? loan.getProductType().name() : "");
            loanResponseDataDto.setActivityType(loan.getActivityType() != null ? loan.getActivityType().name() : "");

            loanResponseDataDto.setMinInvestMoneyCent(String.valueOf(loan.getMinInvestAmount()));
            loanResponseDataDto.setCardinalNumberCent(String.valueOf(loan.getInvestIncreasingAmount()));
            loanResponseDataDto.setMaxInvestMoneyCent(String.valueOf(loan.getMaxInvestAmount()));
            loanResponseDataDto.setInvestedMoneyCent(String.valueOf(investedAmount));
            loanResponseDataDto.setLoanMoneyCent(String.valueOf(loan.getLoanAmount()));
            List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loan.getId());
            if (CollectionUtils.isNotEmpty(extraLoanRateModels)) {
                loanResponseDataDto.setExtraRates(fillExtraRate(extraLoanRateModels));
            }

            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loan.getId());
            if (loanDetailsModel != null) {
                loanResponseDataDto.setExtraSource(loanDetailsModel.getExtraSource() != null ? (loanDetailsModel.getExtraSource().size() == 1 && loanDetailsModel.getExtraSource().contains(Source.WEB)) ? Source.WEB.name() : null : null);
            }

            double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
            //拓天体验项目不收手续费
            if (ProductType.EXPERIENCE == loan.getProductType()) {
                investFeeRate = 0;
                loanResponseDataDto.setInterestPerTenThousands(String.valueOf(InterestCalculator.estimateExperienceExpectedInterest(1000000, loan)));
            }
            loanResponseDataDto.setInvestFeeRate(String.valueOf(investFeeRate));
            if (loanDetailsModel != null && loanDetailsModel.getEstimate() != null) {
                loanResponseDataDto.setEstimateLevel(loanDetailsModelActivity.getEstimate().getLower());
                loanResponseDataDto.setRiskEstimate(loanDetailsModelActivity.getEstimate().getType());
            }
            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }

    private List<ExtraLoanRateDto> fillExtraRate(List<ExtraLoanRateModel> extraLoanRateModels) {
        return Lists.transform(extraLoanRateModels, model -> new ExtraLoanRateDto(model));
    }

}
