package com.tuotiansudai.api.service.v3_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.EvidenceResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ExtraLoanRateDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.DisclosureDto;
import com.tuotiansudai.api.dto.v3_0.ItemDto;
import com.tuotiansudai.api.dto.v3_0.LoanDetailV3RequestDto;
import com.tuotiansudai.api.dto.v3_0.LoanDetailV3ResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanDetailV3Service;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
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
public class MobileAppLoanDetailV3ServiceImpl implements MobileAppLoanDetailV3Service {

    private static final Logger logger = Logger.getLogger(MobileAppLoanDetailV3ServiceImpl.class);

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
    private LoanerEnterpriseInfoMapper loanerEnterpriseInfoMapper;

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
    private String commonStaticServer;

    private String title = "拓天速贷引领投资热，开启互金新概念";

    private String content = "个人经营借款理财项目，总额{0}元期限{1}天，年化利率{2}%，先到先抢！！！";

    @Override
    public BaseResponseDto<LoanDetailV3ResponseDataDto> findLoanDetail(LoanDetailV3RequestDto requestDto) {
        BaseResponseDto<LoanDetailV3ResponseDataDto> responseDto = new BaseResponseDto<>();
        String loanId = requestDto.getLoanId();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if (loanModel == null) {
            logger.warn("标的详情" + ReturnMessage.LOAN_NOT_FOUND.getCode() + ":" + ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return new BaseResponseDto<>(ReturnMessage.LOAN_NOT_FOUND.getCode(), ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        String loginName = requestDto.getBaseParam().getUserId();
        LoanDetailV3ResponseDataDto dataDto = convertLoanDetailFromLoan(loanModel, loginName);
        responseDto.setData(dataDto);
        return responseDto;
    }

    private LoanDetailV3ResponseDataDto convertLoanDetailFromLoan(LoanModel loanModel, String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        LoanDetailV3ResponseDataDto dataDto = new LoanDetailV3ResponseDataDto();
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

        double investFeeRate = ProductType.EXPERIENCE == loanModel.getProductType() ? this.defaultFee : membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        long expectedInterest = investService.estimateInvestIncome(loanModel.getId(), investFeeRate, loginName, MobileAppLoanListV3ServiceImpl.DEFAULT_INVEST_AMOUNT, new Date());
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

        List<DisclosureDto> disclosureDtoList = Lists.newArrayList();

        if (loanerDetailsModel != null) {
            String overdueRate = MessageFormat.format("{0}%", new BigDecimal(loanRepayMapper.calculateOverdueRate(loanModel.getAgentLoginName()) * 100).setScale(0, BigDecimal.ROUND_DOWN).toString());
            DisclosureDto loanerDisclosureDto = convertLoanerInfoFromLoan(loanerDetailsModel, overdueRate);
            disclosureDtoList.add(loanerDisclosureDto);

            if (loanModel.getPledgeType() == PledgeType.HOUSE) {
                List<PledgeHouseModel> pledgeHouseModelList = pledgeHouseMapper.getByLoanId(loanModel.getId());
                int seq = 1;
                for (PledgeHouseModel pledgeHouseModel : pledgeHouseModelList) {
                    DisclosureDto pledgeDisclosureDto = convertPledgeInfoFromLoan(loanModel, pledgeHouseModel, null, pledgeHouseModelList.size() > 1 ? String.valueOf(seq) : "");
                    disclosureDtoList.add(pledgeDisclosureDto);
                    seq++;
                }
            }

            if (loanModel.getPledgeType() == PledgeType.VEHICLE) {
                List<PledgeVehicleModel> pledgeVehicleModelList = pledgeVehicleMapper.getByLoanId(loanModel.getId());
                int seq = 1;
                for (PledgeVehicleModel pledgeVehicleModel : pledgeVehicleModelList) {
                    DisclosureDto pledgeDisclosureDto = convertPledgeInfoFromLoan(loanModel, null, pledgeVehicleModel, pledgeVehicleModelList.size() > 1 ? String.valueOf(seq) : "");
                    disclosureDtoList.add(pledgeDisclosureDto);
                    seq++;
                }
            }

        }
        if (loanModel.getPledgeType() == PledgeType.ENTERPRISE_CREDIT || loanModel.getPledgeType() == PledgeType.ENTERPRISE_PLEDGE) {
            LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel = loanerEnterpriseDetailsMapper.getByLoanId(loanModel.getId());
            if (loanerEnterpriseDetailsModel != null) {
                DisclosureDto loanerEnterpriseInfoPurposeDisclosureDto = convertLoanerEnterpriseDetailsPurposeFromLoan(loanerEnterpriseDetailsModel);
                disclosureDtoList.add(loanerEnterpriseInfoPurposeDisclosureDto);

                DisclosureDto loanerEnterpriseDetailsDisclosureDto = convertLoanerEnterpriseDetailsFromLoan(loanerEnterpriseDetailsModel);
                disclosureDtoList.add(loanerEnterpriseDetailsDisclosureDto);
            }
        }
        if (loanModel.getPledgeType() == PledgeType.ENTERPRISE_PLEDGE) {
            List<PledgeEnterpriseModel> pledgeEnterpriseModelList = pledgeEnterpriseMapper.getByLoanId(loanModel.getId());
            if (pledgeEnterpriseModelList.size() > 0) {
                int seq = 1;
                for (PledgeEnterpriseModel pledgeEnterpriseModel : pledgeEnterpriseModelList) {
                    DisclosureDto loanerPledgeEnterpriseDisclosureDto = convertLoanerPledgeEnterpriseFromLoan(pledgeEnterpriseModel, pledgeEnterpriseModelList.size() > 1 ? String.valueOf(seq) : "");
                    disclosureDtoList.add(loanerPledgeEnterpriseDisclosureDto);
                    seq++;
                }
            }
        }

        if (loanModel.getPledgeType() == PledgeType.ENTERPRISE_BILL) {
            LoanerEnterpriseInfoModel loanerEnterpriseInfoModel = loanerEnterpriseInfoMapper.getByLoanId(loanModel.getId());
            if (loanerEnterpriseInfoModel != null) {
                DisclosureDto loanerEnterpriseInfoDisclosureDto = convertLoanerEnterpriseInfoFromLoan(loanerEnterpriseInfoModel);
                disclosureDtoList.add(loanerEnterpriseInfoDisclosureDto);
            }
        }

        if (loanModel.getPledgeType() == PledgeType.ENTERPRISE_FACTORING) {
            LoanerEnterpriseInfoModel loanerEnterpriseInfoModel = loanerEnterpriseInfoMapper.getByLoanId(loanModel.getId());
            if (loanerEnterpriseInfoModel != null) {
                DisclosureDto loanerEnterpriseInfoDisclosureDto = convertLoanerEnterpriseInfoFromLoan(loanerEnterpriseInfoModel);
                disclosureDtoList.add(loanerEnterpriseInfoDisclosureDto);

                DisclosureDto loanerEnterpriseFactoringInfoDisclosureDto = convertLoanerEnterpriseFactoringInfoFromLoan(loanerEnterpriseInfoModel);
                disclosureDtoList.add(loanerEnterpriseFactoringInfoDisclosureDto);
            }
        }

        dataDto.setDisclosures(disclosureDtoList);
        dataDto.setInvestFeeRate(String.valueOf(investFeeRate));
        List<EvidenceResponseDataDto> evidence = getEvidenceByLoanId(loanModel.getId());
        dataDto.setEvidence(evidence);
        List<InvestModel> investAchievements = investMapper.findInvestAchievementsByLoanId(loanModel.getId());
        StringBuffer marqueeTitle = new StringBuffer();
        if (CollectionUtils.isEmpty(investAchievements) && Lists.newArrayList(LoanStatus.RAISING, LoanStatus.PREHEAT).contains(loanModel.getStatus())) {
            marqueeTitle.append("第一个投资者将获得“拓荒先锋”称号及0.2％加息券＋50元红包    ");
        } else {
            for (InvestModel investModel : investAchievements) {
                String investorLoginName = randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId());
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loanModel.getStatus() == LoanStatus.RAISING) {
                    marqueeTitle.append(investorLoginName + " 以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), investModel.getLoginName(), true)) + "元暂居标王，快来争夺吧    ");
                    marqueeTitle.append("目前项目剩余" + AmountConverter.convertCentToString(loanModel.getLoanAmount() - investedAmount) + "元，快来一锤定音获取奖励吧    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loanModel.getStatus() != LoanStatus.RAISING) {
                    marqueeTitle.append("恭喜" + investorLoginName + " 以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), investModel.getLoginName(), true)) + "元夺得标王，奖励0.5％加息券＋100元红包    ");
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

    private String calculateRemainTime(Date fundraisingEndTime, LoanStatus status) {

        Long time = (fundraisingEndTime.getTime() - System
                .currentTimeMillis()) / 1000;

        if (time < 0 || !status.equals(LoanStatus.RAISING)) {
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
                    String tempUrl = commonStaticServer + url;
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


    private DisclosureDto convertLoanerInfoFromLoan(LoanerDetailsModel model, String overdueRate) {
        DisclosureDto loanerDisclosureDto = new DisclosureDto();
        loanerDisclosureDto.setTitle("借款人基本信息");
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto genderItemDto = new ItemDto();
        genderItemDto.setLabel("性别");
        genderItemDto.setValue(model.getGender().getDescription());
        itemDtoList.add(genderItemDto);

        ItemDto ageItemDto = new ItemDto();
        ageItemDto.setLabel("年龄");
        ageItemDto.setValue(String.valueOf(model.getAge()));
        itemDtoList.add(ageItemDto);

        ItemDto marriageItemDto = new ItemDto();
        marriageItemDto.setLabel("婚姻状况");
        marriageItemDto.setValue(model.getMarriage().getDescription());
        itemDtoList.add(marriageItemDto);

        ItemDto employmentStatusItemDto = new ItemDto();
        employmentStatusItemDto.setLabel("从业情况");
        employmentStatusItemDto.setValue(model.getEmploymentStatus());
        itemDtoList.add(employmentStatusItemDto);

        ItemDto incomeItemDto = new ItemDto();
        incomeItemDto.setLabel("年收入");
        incomeItemDto.setValue(model.getIncome());
        itemDtoList.add(incomeItemDto);

        ItemDto purposeItemDto = new ItemDto();
        purposeItemDto.setLabel("借款用途");
        purposeItemDto.setValue(model.getPurpose());
        itemDtoList.add(purposeItemDto);

        ItemDto overdueRateItemDto = new ItemDto();
        overdueRateItemDto.setLabel("逾期率");
        overdueRateItemDto.setValue(overdueRate);
        itemDtoList.add(overdueRateItemDto);

        loanerDisclosureDto.setItems(itemDtoList);
        return loanerDisclosureDto;
    }

    private DisclosureDto convertPledgeInfoFromLoan(LoanModel loanModel, PledgeHouseModel pledgeHouseModel, PledgeVehicleModel pledgeVehicleModel, String seq) {
        DisclosureDto pledgeDisclosureDto = new DisclosureDto();
        pledgeDisclosureDto.setTitle("抵押档案" + seq);
        List<ItemDto> itemDtoList = Lists.newArrayList();

        switch (loanModel.getPledgeType()) {
            case HOUSE:
                if (pledgeHouseModel != null) {
                    ItemDto pledgeLocationItemDto = new ItemDto();
                    pledgeLocationItemDto.setLabel("抵押房屋所在地");
                    pledgeLocationItemDto.setValue(pledgeHouseModel.getPledgeLocation());
                    itemDtoList.add(pledgeLocationItemDto);

                    ItemDto squareItemDto = new ItemDto();
                    squareItemDto.setLabel("房屋面积");
                    squareItemDto.setValue(pledgeHouseModel.getSquare());
                    itemDtoList.add(squareItemDto);

                    ItemDto estimateAmountItemDto = new ItemDto();
                    estimateAmountItemDto.setLabel("房产估值");
                    estimateAmountItemDto.setValue(pledgeHouseModel.getEstimateAmount());
                    itemDtoList.add(estimateAmountItemDto);

                    ItemDto loanAmountItemDto = new ItemDto();
                    loanAmountItemDto.setLabel("借款公证金额");
                    loanAmountItemDto.setValue(pledgeHouseModel.getLoanAmount());
                    itemDtoList.add(loanAmountItemDto);
                }
            case VEHICLE:
                if (pledgeVehicleModel != null) {
                    ItemDto brandItemDto = new ItemDto();
                    brandItemDto.setLabel("抵押车辆品牌");
                    brandItemDto.setValue(pledgeVehicleModel.getBrand());
                    itemDtoList.add(brandItemDto);

                    ItemDto modelItemDto = new ItemDto();
                    modelItemDto.setLabel("车辆型号");
                    modelItemDto.setValue(pledgeVehicleModel.getModel());
                    itemDtoList.add(modelItemDto);

                    ItemDto estimateAmountItemDto = new ItemDto();
                    estimateAmountItemDto.setLabel("车辆估值");
                    estimateAmountItemDto.setValue(pledgeVehicleModel.getEstimateAmount());
                    itemDtoList.add(estimateAmountItemDto);

                    ItemDto loanAmountItemDto = new ItemDto();
                    loanAmountItemDto.setLabel("借款公证金额");
                    loanAmountItemDto.setValue(pledgeVehicleModel.getLoanAmount());
                    itemDtoList.add(loanAmountItemDto);
                }
        }

        pledgeDisclosureDto.setItems(itemDtoList);
        return pledgeDisclosureDto;
    }

    private DisclosureDto convertLoanerEnterpriseDetailsFromLoan(LoanerEnterpriseDetailsModel model) {
        DisclosureDto LoanerEnterpriseDetailsDisclosureDto = new DisclosureDto();
        LoanerEnterpriseDetailsDisclosureDto.setTitle("借款人基本信息");
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto juristicPersonItemDto = new ItemDto();
        juristicPersonItemDto.setLabel("借款人");
        juristicPersonItemDto.setValue(model.getJuristicPerson());
        itemDtoList.add(juristicPersonItemDto);

        ItemDto addressItemDto = new ItemDto();
        addressItemDto.setLabel("公司所在地");
        addressItemDto.setValue(model.getAddress());
        itemDtoList.add(addressItemDto);

        LoanerEnterpriseDetailsDisclosureDto.setItems(itemDtoList);
        return LoanerEnterpriseDetailsDisclosureDto;
    }

    private DisclosureDto convertLoanerEnterpriseDetailsPurposeFromLoan(LoanerEnterpriseDetailsModel model) {
        DisclosureDto LoanerEnterpriseDetailsPurposeDisclosureDto = new DisclosureDto();
        LoanerEnterpriseDetailsPurposeDisclosureDto.setTitle("借款用途描述");
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto purposeItemDto = new ItemDto();
        purposeItemDto.setLabel("借款用途");
        purposeItemDto.setValue(model.getPurpose());
        itemDtoList.add(purposeItemDto);

        LoanerEnterpriseDetailsPurposeDisclosureDto.setItems(itemDtoList);
        return LoanerEnterpriseDetailsPurposeDisclosureDto;
    }

    private DisclosureDto convertLoanerPledgeEnterpriseFromLoan(PledgeEnterpriseModel model, String seq) {
        DisclosureDto LoanerPledgeEnterprisesDisclosureDto = new DisclosureDto();
        LoanerPledgeEnterprisesDisclosureDto.setTitle("抵押物信息" + seq);
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto guaranteeItemDto = new ItemDto();
        guaranteeItemDto.setLabel("担保方式");
        guaranteeItemDto.setValue(model.getGuarantee());
        itemDtoList.add(guaranteeItemDto);

        ItemDto estimateAmountItemDto = new ItemDto();
        estimateAmountItemDto.setLabel("抵押物估值");
        estimateAmountItemDto.setValue(model.getEstimateAmount());
        itemDtoList.add(estimateAmountItemDto);

        ItemDto pledgeLocationItemDto = new ItemDto();
        pledgeLocationItemDto.setLabel("抵押物所在地");
        pledgeLocationItemDto.setValue(model.getPledgeLocation());
        itemDtoList.add(pledgeLocationItemDto);

        LoanerPledgeEnterprisesDisclosureDto.setItems(itemDtoList);
        return LoanerPledgeEnterprisesDisclosureDto;
    }

    private DisclosureDto convertLoanerEnterpriseInfoFromLoan(LoanerEnterpriseInfoModel model) {
        DisclosureDto LoanerEnterpriseInfoDisclosureDto = new DisclosureDto();
        LoanerEnterpriseInfoDisclosureDto.setTitle("借款企业基本信息");
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto companyNameItemDto = new ItemDto();
        companyNameItemDto.setLabel("企业名称");
        companyNameItemDto.setValue(model.getCompanyName());
        itemDtoList.add(companyNameItemDto);

        ItemDto addressItemDto = new ItemDto();
        addressItemDto.setLabel("经营地址");
        addressItemDto.setValue(model.getAddress());
        itemDtoList.add(addressItemDto);

        ItemDto purposeItemDto = new ItemDto();
        purposeItemDto.setLabel("借款用途");
        purposeItemDto.setValue(model.getPurpose());
        itemDtoList.add(purposeItemDto);

        LoanerEnterpriseInfoDisclosureDto.setItems(itemDtoList);
        return LoanerEnterpriseInfoDisclosureDto;
    }

    private DisclosureDto convertLoanerEnterpriseFactoringInfoFromLoan(LoanerEnterpriseInfoModel model) {
        DisclosureDto LoanerEnterpriseInfoDisclosureDto = new DisclosureDto();
        LoanerEnterpriseInfoDisclosureDto.setTitle("保理公司基本信息");
        List<ItemDto> itemDtoList = Lists.newArrayList();

        ItemDto companyNameItemDto = new ItemDto();
        companyNameItemDto.setLabel("公司名称");
        companyNameItemDto.setValue(model.getFactoringCompanyName());
        itemDtoList.add(companyNameItemDto);

        ItemDto addressItemDto = new ItemDto();
        addressItemDto.setLabel("公司简介");
        addressItemDto.setValue(model.getFactoringCompanyDesc());
        itemDtoList.add(addressItemDto);

        LoanerEnterpriseInfoDisclosureDto.setItems(itemDtoList);
        return LoanerEnterpriseInfoDisclosureDto;
    }

}
