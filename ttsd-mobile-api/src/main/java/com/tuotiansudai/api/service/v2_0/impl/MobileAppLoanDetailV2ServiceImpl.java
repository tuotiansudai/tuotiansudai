package com.tuotiansudai.api.service.v2_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.dto.v2_0.*;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanDetailV2Service;
import com.tuotiansudai.api.service.v3_0.impl.MobileAppLoanListV3ServiceImpl;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.LoanPeriodCalculator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppLoanDetailV2ServiceImpl implements MobileAppLoanDetailV2Service {

    private static final Logger logger = Logger.getLogger(MobileAppLoanDetailV2ServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanerEnterpriseDetailsMapper loanerEnterpriseDetailsMapper;

    @Autowired
    private PledgeEnterpriseMapper pledgeEnterpriseMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private InvestService investService;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Value("${common.static.server}")
    private String staticServer;

    private String title = "拓天速贷引领投资热，开启互金新概念";

    private String content = "个人经营借款理财项目，总额{0}元期限{1}天，年化利率{2}%，先到先抢！！！";

    @Override
    public BaseResponseDto<LoanDetailV2ResponseDataDto> findLoanDetail(LoanDetailV2RequestDto requestDto) {
        BaseResponseDto<LoanDetailV2ResponseDataDto> responseDto = new BaseResponseDto<>();
        String loanId = requestDto.getLoanId();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if (loanModel == null) {
            logger.warn("标的详情" + ReturnMessage.LOAN_NOT_FOUND.getCode() + ":" + ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return new BaseResponseDto<>(ReturnMessage.LOAN_NOT_FOUND.getCode(), ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        String loginName = requestDto.getBaseParam().getUserId();
        LoanDetailV2ResponseDataDto dataDto = convertLoanDetailFromLoan(loanModel, loginName);
        responseDto.setData(dataDto);
        return responseDto;
    }

    private LoanDetailV2ResponseDataDto convertLoanDetailFromLoan(LoanModel loanModel, String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        LoanDetailV2ResponseDataDto dataDto = new LoanDetailV2ResponseDataDto();
        dataDto.setLoanId(loanModel.getId());
        dataDto.setLoanType(loanModel.getProductType() != null ? loanModel.getProductType().getProductLine() : "");
        LoanDetailsModel loanDetailsModelActivity = loanDetailsMapper.getByLoanId(loanModel.getId());
        dataDto.setLoanName(loanModel.getName());
        dataDto.setActivityDesc(loanDetailsModelActivity != null ? loanDetailsModelActivity.getActivityDesc() : "");
        dataDto.setPledgeType(loanModel.getPledgeType());
        dataDto.setRepayTypeCode("");
        dataDto.setDuration(loanModel.getDuration());
        dataDto.setAvailableDuration(LoanPeriodCalculator.calculateDuration(new Date(), loanModel.getDeadline()));
        dataDto.setDeadline(new DateTime(loanModel.getDeadline()).toString("yyyy-MM-dd"));
        String repayTypeName = loanModel.getType().getRepayType();
        dataDto.setRepayTypeName(repayTypeName);
        dataDto.setNonTransferable(loanDetailsModelActivity != null && loanDetailsModelActivity.getNonTransferable());

        long expectedInterest = investService.estimateInvestIncome(loanModel.getId(), loginName, MobileAppLoanListV3ServiceImpl.DEFAULT_INVEST_AMOUNT);
        dataDto.setInterestPerTenThousands(String.valueOf(expectedInterest));

        String interestPointName = loanModel.getType().getInterestPointName();
        dataDto.setInterestPointName(interestPointName);

        dataDto.setPeriods(loanModel.getPeriods());

        String raisingPeriod = String.valueOf(Days.daysBetween(new DateTime(loanModel.getFundraisingStartTime()).withTimeAtStartOfDay(),
                new DateTime(loanModel.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1);
        dataDto.setRaisingPeriod(raisingPeriod);

        dataDto.setRepayUnit(loanModel.getType().getLoanPeriodUnit().getDesc());
        dataDto.setRatePercent(decimalFormat.format((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100));
        dataDto.setLoanMoney(AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataDto.setActivityType(loanModel.getActivityType());
        if (loanModel.getStatus().equals(LoanStatus.PREHEAT)) {
            dataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
            dataDto.setLoanStatusDesc(LoanStatus.RAISING.getDescription());
        } else {
            dataDto.setLoanStatus(loanModel.getStatus().name().toLowerCase());
            dataDto.setLoanStatusDesc(loanModel.getStatus().getDescription());
        }
        if (loanModel.getFundraisingStartTime() != null) {
            dataDto.setInvestBeginTime(new DateTime(loanModel.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
            dataDto.setVerifyTime(new DateTime(loanModel.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        long investedAmount;
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
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
        if (loanDetailsModel != null) {
            dataDto.setDeclaration(loanDetailsModel.getDeclaration());
            dataDto.setExtraSource(loanDetailsModel.getExtraSource() != null && loanDetailsModel.getExtraSource().size() == 1 && loanDetailsModel.getExtraSource().contains(Source.WEB) ? Source.WEB.name() : "");
        }
        dataDto.setActivityType(loanModel.getActivityType());
        dataDto.setRemainTime(calculateRemainTime(loanModel.getFundraisingEndTime(), loanModel.getStatus()));
        dataDto.setInvestBeginSeconds(CommonUtils.calculatorInvestBeginSeconds(loanModel.getFundraisingStartTime()));
        dataDto.setMinInvestMoney(AmountConverter.convertCentToString(loanModel.getMinInvestAmount()));
        dataDto.setCardinalNumber(AmountConverter.convertCentToString(loanModel.getInvestIncreasingAmount()));
        dataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loanModel.getMaxInvestAmount()));
        dataDto.setInvestedCount(investMapper.countSuccessInvest(loanModel.getId()));
        dataDto.setDuration(loanModel.getDuration());

        dataDto.setMinInvestMoneyCent(String.valueOf(loanModel.getMinInvestAmount()));
        dataDto.setCardinalNumberCent(String.valueOf(loanModel.getInvestIncreasingAmount()));
        dataDto.setMaxInvestMoneyCent(String.valueOf(loanModel.getMaxInvestAmount()));
        dataDto.setInvestedMoneyCent(String.valueOf(investedAmount));
        dataDto.setLoanMoneyCent(String.valueOf(loanModel.getLoanAmount()));
        if (loanModel.getRaisingCompleteTime() != null) {
            dataDto.setRaiseCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loanModel.getRaisingCompleteTime()));
        }
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanModel.getId());
        if (loanerDetailsModel != null) {
            LoanerDto loanerDto = new LoanerDto(loanerDetailsModel);
            loanerDto.setOverdueRate(MessageFormat.format("{0}%", new BigDecimal(loanRepayMapper.calculateOverdueRate(loanModel.getAgentLoginName()) * 100).setScale(0, BigDecimal.ROUND_DOWN).toString()));
            dataDto.setLoaner(loanerDto);
            switch (loanModel.getPledgeType()) {
                case HOUSE:
                    PledgeHouseModel pledgeHouseModel = pledgeHouseMapper.getByLoanId(loanModel.getId());
                    if (pledgeHouseModel != null) {
                        dataDto.setPledgeHouse(new PledgeHouseDto(pledgeHouseModel));
                    }
                case VEHICLE:
                    PledgeVehicleModel pledgeVehicleModel = pledgeVehicleMapper.getByLoanId(loanModel.getId());
                    if (pledgeVehicleModel != null) {
                        dataDto.setPledgeVehicle(new PledgeVehicleDto(pledgeVehicleModel));
                    }
            }
        }
        if (loanModel.getPledgeType() == PledgeType.ENTERPRISE) {
            LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel = loanerEnterpriseDetailsMapper.getByLoanId(loanModel.getId());
            if (loanerEnterpriseDetailsModel != null) {
                EnterpriseDto enterpriseDto = new EnterpriseDto(loanerEnterpriseDetailsModel);
                enterpriseDto.setShareholder(StringUtils.rightPad(StringUtils.left(enterpriseDto.getShareholder(), 1), 2, "某"));
                enterpriseDto.setJuristicPerson(StringUtils.rightPad(StringUtils.left(enterpriseDto.getJuristicPerson(), 1), 2, "某"));

                dataDto.setEnterprise(enterpriseDto);
            }
            PledgeEnterpriseModel pledgeEnterpriseModel = pledgeEnterpriseMapper.getByLoanId(loanModel.getId());
            if (pledgeEnterpriseModel != null) {
                dataDto.setPledgeEnterpriseDto(new PledgeEnterpriseDto(pledgeEnterpriseModel));
            }
        }

        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
        if (loanModel != null && ProductType.EXPERIENCE == loanModel.getProductType()) {
            investFeeRate = this.defaultFee;
        }
        dataDto.setInvestFeeRate(String.valueOf(investFeeRate));
        List<EvidenceResponseDataDto> evidence = getEvidenceByLoanId(loanModel.getId());
        dataDto.setEvidence(evidence);
        List<InvestModel> investAchievements = investMapper.findInvestAchievementsByLoanId(loanModel.getId());
        StringBuffer marqueeTitle = new StringBuffer();
        if (CollectionUtils.isEmpty(investAchievements) && Lists.newArrayList(com.tuotiansudai.repository.model.LoanStatus.RAISING, com.tuotiansudai.repository.model.LoanStatus.PREHEAT).contains(loanModel.getStatus())) {
            marqueeTitle.append("第一个投资者将获得“拓荒先锋”称号及0.2％加息券＋50元红包    ");
        } else {
            for (InvestModel investModel : investAchievements) {
                String investorLoginName = randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId());
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loanModel.getStatus() == com.tuotiansudai.repository.model.LoanStatus.RAISING) {
                    marqueeTitle.append(investorLoginName + " 以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), investModel.getLoginName(),true)) + "元暂居标王，快来争夺吧    ");
                    marqueeTitle.append("目前项目剩余" + AmountConverter.convertCentToString(loanModel.getLoanAmount() - investedAmount) + "元，快来一锤定音获取奖励吧    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loanModel.getStatus() != com.tuotiansudai.repository.model.LoanStatus.RAISING) {
                    marqueeTitle.append("恭喜" + investorLoginName + " 以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), investModel.getLoginName(),true)) + "元夺得标王，奖励0.5％加息券＋100元红包    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.FIRST_INVEST)) {
                    marqueeTitle.append("恭喜" + investorLoginName + " " + new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss") + "占领先锋，奖励0.2％加息券＋50元红包    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.LAST_INVEST)) {
                    marqueeTitle.append("恭喜" + investorLoginName + " " + new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss") + "一锤定音，奖励0.2％加息券＋50元红包    ");
                }
            }
            dataDto.setMarqueeTitle(marqueeTitle.toString());
        }

        dataDto.setTitle(title);
        dataDto.setContent(MessageFormat.format(content, dataDto.getLoanMoney().replaceAll("\\.00", ""), dataDto.getDuration(), dataDto.getRatePercent()));
        dataDto.setProductNewType(loanModel.getProductType() != null ? loanModel.getProductType().name() : "");
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanModel.getId());
        if (CollectionUtils.isNotEmpty(extraLoanRateModels)) {
            List<ExtraLoanRateDto> extraLoanRateDtos = fillExtraLoanRateDto(extraLoanRateModels);
            dataDto.setExtraRates(extraLoanRateDtos);
        }
        return dataDto;
    }

    private String calculateRemainTime(Date fundraisingEndTime, com.tuotiansudai.repository.model.LoanStatus status) {

        Long time = (fundraisingEndTime.getTime() - System
                .currentTimeMillis()) / 1000;

        if (time < 0 || !status.equals(com.tuotiansudai.repository.model.LoanStatus.RAISING)) {
            return "已到期";
        }
        long days = time / 3600 / 24;
        long hours = (time / 3600) % 24;
        long minutes = (time / 60) % 60;
        if (minutes < 1) {
            minutes = 1L;
        }

        return days + "天" + hours + "时" + minutes + "分";
    }

    private List<EvidenceResponseDataDto> getEvidenceByLoanId(long loanId) {
        EvidenceResponseDataDto evidenceResponseDataDto;
        List<LoanTitleRelationModel> loanTitleRelationModels = loanTitleRelationMapper.findLoanTitleRelationAndTitleByLoanId(loanId);
        List<String> imageUrlList;
        List<EvidenceResponseDataDto> evidenceResponseDataDtos = Lists.newArrayList();
        for (LoanTitleRelationModel loanTitleRelationModel : loanTitleRelationModels) {
            imageUrlList = Lists.newArrayList();
            evidenceResponseDataDto = new EvidenceResponseDataDto();
            String materialUrl = loanTitleRelationModel.getApplicationMaterialUrls();
            if (StringUtils.isNotEmpty(materialUrl)) {
                for (String url : materialUrl.split(",")) {
                    String tempUrl = staticServer + url;
                    imageUrlList.add(tempUrl);
                }
            }
            evidenceResponseDataDto.setTitle(loanTitleRelationModel.getTitle());
            evidenceResponseDataDto.setImageUrl(imageUrlList);
            evidenceResponseDataDtos.add(evidenceResponseDataDto);
        }

        return evidenceResponseDataDtos;
    }

    private List<ExtraLoanRateDto> fillExtraLoanRateDto(List<ExtraLoanRateModel> extraLoanRateModels) {
        return Lists.transform(extraLoanRateModels, new Function<ExtraLoanRateModel, ExtraLoanRateDto>() {
            @Override
            public ExtraLoanRateDto apply(ExtraLoanRateModel model) {
                return new ExtraLoanRateDto(model);
            }
        });
    }
}
