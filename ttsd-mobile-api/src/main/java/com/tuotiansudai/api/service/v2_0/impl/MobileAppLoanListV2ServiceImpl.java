package com.tuotiansudai.api.service.v2_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.LoanResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppLoanListV2ServiceImpl implements MobileAppLoanListV2Service {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    public BaseResponseDto generateIndexLoan(String loginName) {
        List<LoanModel> loanModels = Lists.newArrayList();

        if (investMapper.sumSuccessInvestCountByLoginName(loginName) == 0) {
            if (investMapper.sumSuccessExperienceInvestCountByLoginName(loginName) == 0) {
                loanModels.addAll(loanMapper.findByProductType(ProductType.EXPERIENCE));
            }
            loanModels.addAll(loanMapper.findHomeLoanByIsContainNewbie(LoanStatus.RAISING, true));
            if (CollectionUtils.isEmpty(loanModels)) {
                List<LoanModel> completeLoanModels = loanMapper.findHomeLoanByIsContainNewbie(LoanStatus.COMPLETE, true);
                if (CollectionUtils.isNotEmpty(completeLoanModels)) {
                    loanModels.add(completeLoanModels.get(0));
                }
            }
        }

        List<LoanModel> notContainNewbieList = loanMapper.findHomeLoanByIsContainNewbie(LoanStatus.RAISING, false);
        if (CollectionUtils.isNotEmpty(notContainNewbieList)) {
            loanModels.addAll(notContainNewbieList);
        }

        if (CollectionUtils.isEmpty(loanModels)) {
            List<LoanModel> completeLoanModels = loanMapper.findHomeLoanByIsContainNewbie(LoanStatus.COMPLETE, false);
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
        List<LoanResponseDataDto> loanDtoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setActivityType(loan.getActivityType().name());
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

            MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
            double investFeeRate = membershipModel == null ? defaultFee : membershipModel.getFee();
            if(loan != null && ProductType.EXPERIENCE == loan.getProductType()){
                investFeeRate = this.defaultFee;
            }
            loanResponseDataDto.setInvestFeeRate(String.valueOf(investFeeRate));
            loanDtoList.add(loanResponseDataDto);
        }
        return loanDtoList;
    }
}
