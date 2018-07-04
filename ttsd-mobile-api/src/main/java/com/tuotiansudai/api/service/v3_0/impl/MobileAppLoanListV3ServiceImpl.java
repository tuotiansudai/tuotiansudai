package com.tuotiansudai.api.service.v3_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.ExtraRateListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppLoanListV3ServiceImpl implements MobileAppLoanListV3Service {

    static Logger logger = Logger.getLogger(MobileAppLoanListV3ServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExperienceLoanDetailService experienceLoanDetailService;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private InvestService investService;

    public static long DEFAULT_INVEST_AMOUNT = 1000000;

    @Override
    public BaseResponseDto<LoanListResponseDataDto> generateIndexLoan(String loginName) {
        List<ProductType> allProductTypesCondition = Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360);
        LoanModel loanModel = null;

        List<PledgeType> pledgeTypeList = Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE, PledgeType.NONE);
        //目前同一时间可投标不超过100个
        List<LoanModel> raisingLoanModels = loanMapper.findByProductType(LoanStatus.RAISING, allProductTypesCondition, null);
        //有可投标的,版本号小于4.3过滤掉经营性借款
        if (AppVersionUtil.compareVersion() == AppVersionUtil.low) {
            raisingLoanModels = raisingLoanModels.stream().filter(n -> pledgeTypeList.contains(n.getPledgeType())).collect(Collectors.toList());
        }
        if (raisingLoanModels.size() != 0) {
            Collections.sort(raisingLoanModels, new Comparator<LoanModel>() {
                @Override
                public int compare(LoanModel o1, LoanModel o2) {
                    if (o1.getActivityType().equals(ActivityType.NEWBIE) && !o2.getActivityType().equals(ActivityType.NEWBIE)) {
                        return -1;
                    } else if (!o1.getActivityType().equals(ActivityType.NEWBIE) && o2.getActivityType().equals(ActivityType.NEWBIE)) {
                        return 1;
                    } else {
                        if (o1.getProductType().getDuration() > o2.getProductType().getDuration()) {
                            return 1;
                        } else if (o1.getProductType().getDuration() < o2.getProductType().getDuration()) {
                            return -1;
                        } else {
                            if (o1.getVerifyTime().after(o2.getVerifyTime())) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            });

            if (Strings.isNullOrEmpty(loginName) || 0 == investMapper.findCountSuccessByLoginNameAndProductTypes(loginName, allProductTypesCondition)) {
                loanModel = raisingLoanModels.get(0);
            } else {
                //登录 && 投资过其它标
                Collections.reverse(raisingLoanModels);
                loanModel = raisingLoanModels.get(0);
                if (raisingLoanModels.size() > 1) {
                    for (int i = 1; i < raisingLoanModels.size(); ++i) {
                        if (loanModel.getProductType() == raisingLoanModels.get(i).getProductType()) {
                            loanModel = raisingLoanModels.get(i);
                        } else {
                            break;
                        }
                    }
                }
            }
        } else {
            //没有可投标的
            List<LoanModel> soldLoanModels = loanMapper.findByStatus(LoanStatus.COMPLETE);
            soldLoanModels.addAll(loanMapper.findByStatus(LoanStatus.REPAYING));
            //版本号小于4.3过滤掉经营性借款
            if (AppVersionUtil.compareVersion() == AppVersionUtil.low) {
                soldLoanModels = soldLoanModels.stream().filter(n -> pledgeTypeList.contains(n.getPledgeType())).collect(Collectors.toList());
            }
            if (soldLoanModels.size() > 0) {
                loanModel = soldLoanModels.get(0);
                for (LoanModel curLoanModel : soldLoanModels) {
                    if (curLoanModel.getRaisingCompleteTime() == null) {
                        continue;
                    }
                    if (loanModel.getRaisingCompleteTime().before(curLoanModel.getRaisingCompleteTime())) {
                        loanModel = curLoanModel;
                    }
                }
            } else {
                logger.warn("[MobileAppLoanListV3ServiceImpl][generateIndexLoan]没有售完的标");
            }
        }

        BaseResponseDto<LoanListResponseDataDto> dto = new BaseResponseDto<>();
        LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
        loanListResponseDataDto.setLoanList(convertLoanDto(loginName, Lists.newArrayList(loanModel)));
        dto.setData(loanListResponseDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());

        return dto;
    }

    private List<LoanResponseDataDto> convertLoanDto(String loginName, List<LoanModel> loanList) {
        List<LoanResponseDataDto> loanDtoList = Lists.newArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        if(CollectionUtils.isEmpty(loanList)){
            return loanDtoList;
        }

        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        for (LoanModel loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId("" + loan.getId());
            LoanDetailsModel loanDetailsModelActivity = loanDetailsMapper.getByLoanId(loan.getId());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setActivityType(loan.getActivityType().name());

            loanResponseDataDto.setActivityDesc(loanDetailsModelActivity != null ? loanDetailsModelActivity.getActivityDesc() : "");
            loanResponseDataDto.setPledgeType(loan.getPledgeType());
            loanResponseDataDto.setDuration(String.valueOf(loan.getDuration()));
            loanResponseDataDto.setAvailableDuration(String.valueOf(LoanPeriodCalculator.calculateDuration(new Date(), loan.getDeadline())));
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
            loanResponseDataDto.setInvestFeeRate(String.valueOf(ProductType.EXPERIENCE == loan.getProductType() ? this.defaultFee : investFeeRate));

            if (ProductType.EXPERIENCE != loan.getProductType()) {
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loan.getId());
                loanResponseDataDto.setExtraSource((loanDetailsModel != null && loanDetailsModel.getExtraSource() != null) ? ((loanDetailsModel.getExtraSource().size() == 1 && loanDetailsModel.getExtraSource().contains(Source.WEB)) ? Source.WEB.name() : "") : "");
            }

            long expectedInterest = investService.estimateInvestIncome(loan.getId(),
                    ProductType.EXPERIENCE == loan.getProductType() ? this.defaultFee : investFeeRate,
                    loginName, DEFAULT_INVEST_AMOUNT, new Date());

            loanResponseDataDto.setInterestPerTenThousands(String.valueOf(expectedInterest));
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
