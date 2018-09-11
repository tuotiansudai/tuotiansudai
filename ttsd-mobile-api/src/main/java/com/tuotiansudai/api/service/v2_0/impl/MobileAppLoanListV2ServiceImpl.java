package com.tuotiansudai.api.service.v2_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.ExtraRateListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppLoanListV2ServiceImpl implements MobileAppLoanListV2Service {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExperienceLoanDetailService experienceLoanDetailService;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    public BaseResponseDto<LoanListResponseDataDto> generateIndexLoan(String loginName) {
        List<LoanModel> loanModels = Lists.newArrayList();
        List<ProductType> allProductTypesCondition = Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360);
        List<ProductType> noContainExperienceCondition = Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360);
        ActivityType activityType = ActivityType.NORMAL;
        if (Strings.isNullOrEmpty(loginName)
                || investMapper.findCountSuccessByLoginNameAndProductTypes(loginName, allProductTypesCondition) == 0) {
            loanModels.addAll(loanMapper.findByProductType(LoanStatus.RAISING, allProductTypesCondition, ActivityType.NEWBIE));
        }

        if (investMapper.findCountSuccessByLoginNameAndProductTypes(loginName, noContainExperienceCondition) == 0) {
            activityType = null;
        }

        List<LoanModel> notContainNewbieList = loanMapper.findByProductType(LoanStatus.RAISING, noContainExperienceCondition, activityType);
        if (CollectionUtils.isNotEmpty(notContainNewbieList)) {
            loanModels.addAll(notContainNewbieList);
        }

        if (CollectionUtils.isEmpty(loanModels)) {
            List<LoanModel> completeLoanModels = loanMapper.findByProductType(LoanStatus.COMPLETE, allProductTypesCondition, null);
            if (CollectionUtils.isNotEmpty(completeLoanModels)) {
                loanModels.add(completeLoanModels.get(0));
            }
        }

        BaseResponseDto<LoanListResponseDataDto> dto = new BaseResponseDto<>();
        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
        loanListResponseDataDto.setLoanList(convertLoanDto(loginName, loanModels));
        dto.setData(loanListResponseDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }

    private List<LoanResponseDataDto> convertLoanDto(String loginName, List<LoanModel> loanList) {
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");

        List<PledgeType> pledgeTypeList = Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE, PledgeType.NONE);
        if(AppVersionUtil.compareVersion() == AppVersionUtil.low ){
            loanList = loanList.stream().filter(n -> pledgeTypeList.contains(n.getPledgeType())).collect(Collectors.toList());
        }

        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());

            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loan.getId());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setActivityType(loan.getActivityType().name());
            loanResponseDataDto.setActivityDesc(loanDetailsModel != null ? loanDetailsModel.getActivityDesc() : "");
            loanResponseDataDto.setPledgeType(loan.getPledgeType());
            loanResponseDataDto.setDuration(String.valueOf(loan.getDuration()));
            loanResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
            loanResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));
            loanResponseDataDto.setLoanAmount(AmountConverter.convertCentToString(loan.getLoanAmount()));
            loanResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmount(loan.getId())));
            if (com.tuotiansudai.repository.model.LoanStatus.PREHEAT.equals(loan.getStatus())) {
                loanResponseDataDto.setLoanStatus(com.tuotiansudai.repository.model.LoanStatus.RAISING.name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(com.tuotiansudai.repository.model.LoanStatus.RAISING.getDescription());
            } else {
                loanResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
                loanResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
            }
            loanResponseDataDto.setFundraisingStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getFundraisingStartTime()));
            loanResponseDataDto.setFundraisingCountDown(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
            loanResponseDataDto.setMinInvestMoney(AmountConverter.convertCentToString(loan.getMinInvestAmount()));
            loanResponseDataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loan.getMaxInvestAmount()));
            loanResponseDataDto.setCardinalNumber(AmountConverter.convertCentToString(loan.getInvestIncreasingAmount()));
            loanResponseDataDto.setProductNewType(loan.getProductType() != null ? loan.getProductType().name() : "");

            loanResponseDataDto.setMinInvestMoneyCent(String.valueOf(loan.getMinInvestAmount()));
            loanResponseDataDto.setCardinalNumberCent(String.valueOf(loan.getInvestIncreasingAmount()));
            loanResponseDataDto.setMaxInvestMoneyCent(String.valueOf(loan.getMaxInvestAmount()));
            if (loan.getProductType().equals(ProductType.EXPERIENCE)) {
                ExperienceLoanDto experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(loan.getId(), loginName);
                loanResponseDataDto.setInvestedMoneyCent(String.valueOf(loan.getLoanAmount() - AmountConverter.convertStringToCent(experienceLoanDto.getInvestAmount())));
            } else {
                loanResponseDataDto.setInvestedMoneyCent(String.valueOf(investMapper.sumSuccessInvestAmount(loan.getId())));
            }
            loanResponseDataDto.setLoanMoneyCent(String.valueOf(loan.getLoanAmount()));

            loanResponseDataDto.setExtraRates(convertExtraRateList(loan.getId()));
            double investFeeRate = userMembershipService.obtainServiceFee(loginName);
            if (ProductType.EXPERIENCE == loan.getProductType()) {
                investFeeRate = this.defaultFee;
            }
            loanResponseDataDto.setInvestFeeRate(String.valueOf(investFeeRate));

            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }

    private List<ExtraRateListResponseDataDto> convertExtraRateList(long loanId) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        List<ExtraRateListResponseDataDto> extraRateListResponseDataDtos = Lists.newArrayList();
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
            ExtraRateListResponseDataDto extraRateListResponseDataDto = new ExtraRateListResponseDataDto();
            extraRateListResponseDataDto.setRate(decimalFormat.format(extraLoanRateModel.getRate() * 100));
            extraRateListResponseDataDto.setAmountLower(AmountConverter.convertCentToString(extraLoanRateModel.getMinInvestAmount()));
            extraRateListResponseDataDto.setAmountUpper(AmountConverter.convertCentToString(extraLoanRateModel.getMaxInvestAmount()));
            extraRateListResponseDataDtos.add(extraRateListResponseDataDto);
        }
        return extraRateListResponseDataDtos;
    }

}
