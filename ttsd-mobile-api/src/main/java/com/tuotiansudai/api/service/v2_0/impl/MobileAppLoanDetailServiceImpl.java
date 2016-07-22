package com.tuotiansudai.api.service.v2_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoanStatus;
import com.tuotiansudai.api.dto.v2_0.LoanDetailRequestDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanDetailService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanerDetailsMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.PledgeType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppLoanDetailServiceImpl implements MobileAppLoanDetailService {

    private static final Logger logger = Logger.getLogger(MobileAppLoanDetailServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Override
    public BaseResponseDto<LoanDetailResponseDataDto> findLoanDetail(LoanDetailRequestDto requestDto) {
        BaseResponseDto<LoanDetailResponseDataDto> responseDto = new BaseResponseDto<>();
        String loanId = requestDto.getLoanId();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if (loanModel == null) {
            logger.error("标的详情" + ReturnMessage.LOAN_NOT_FOUND.getCode() + ":" + ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return new BaseResponseDto(ReturnMessage.LOAN_NOT_FOUND.getCode(), ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        String loginName = requestDto.getBaseParam().getUserId();
        LoanDetailResponseDataDto dataDto = convertLoanDetailFromLoan(loanModel, loginName);
        responseDto.setData(dataDto);
        return responseDto;
    }

    private LoanDetailResponseDataDto convertLoanDetailFromLoan(LoanModel loanModel, String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        LoanDetailResponseDataDto dataDto = new LoanDetailResponseDataDto();
        dataDto.setLoanId(loanModel.getId());
        dataDto.setLoanType();
        dataDto.setLoanName(loanModel.getName());
        dataDto.setRepayTypeCode("");

        String repayTypeName = loanModel.getType().getRepayType();
        dataDto.setRepayTypeName(repayTypeName);

        String interestPointName = loanModel.getType().getInterestPointName();
        dataDto.setInterestPointName(interestPointName);

        dataDto.setPeriods(loanModel.getPeriods());

        String raisingPeriod = String.valueOf(Days.daysBetween(new DateTime(loanModel.getFundraisingStartTime()).withTimeAtStartOfDay(),
                new DateTime(loanModel.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1);
        dataDto.setRaisingPeriod(raisingPeriod);

        dataDto.setRepayUnit(loanModel.getType().getLoanPeriodUnit().getDesc());
        dataDto.setRatePercent(decimalFormat.format((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100));
        dataDto.setLoanMoney(AmountConverter.convertCentToString(loanModel.getLoanAmount()));

        if (loanModel.getStatus().equals(LoanStatus.PREHEAT)) {
            dataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
            dataDto.setLoanStatusDesc(LoanStatus.RAISING.getMessage());
        } else {
            dataDto.setLoanStatus(loanModel.getStatus().name().toLowerCase());
            dataDto.setLoanStatusDesc(loanModel.getStatus().getDescription());
        }
        long investedAmount = 0;
        if (loanModel.getProductType() == ProductType.EXPERIENCE) {
            Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
            Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
            List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loanModel.getId(), beginTime, endTime);
            investedAmount = couponService.findExperienceInvestAmount(investModelList);
            dataDto.setVerifyTime(new DateTime().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss"));
        } else {
            investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
        }
        dataDto.setInvestedMoney(AmountConverter.convertCentToString(investedAmount));
        dataDto.setBaseRatePercent(decimalFormat.format(loanModel.getBaseRate() * 100));
        dataDto.setActivityRatePercent(decimalFormat.format(loanModel.getActivityRate() * 100));
        dataDto.setDeclaration();

        return dataDto;
    }

    private Map<String,Object> collectLoanDetailDateModel(long loanId,PledgeType pledgeType){
        
    }
}
