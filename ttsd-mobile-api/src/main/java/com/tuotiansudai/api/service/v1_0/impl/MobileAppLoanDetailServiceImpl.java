package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanDetailService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.service.impl.ContractServiceImpl;

import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppLoanDetailServiceImpl implements MobileAppLoanDetailService {
    static Logger log = Logger.getLogger(MobileAppLoanDetailServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;
    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Value("${web.server}")
    private String domainName;

    private String title = "拓天速贷引领投资热，开启互金新概念";

    private String content = "个人经营借款理财项目，总额{0}元期限{1}{2}，年化利率{3}%，先到先抢！！！";

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private ContractService contractService;

    @Override
    public BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto) {
        BaseResponseDto<LoanDetailResponseDataDto> dto = new BaseResponseDto<LoanDetailResponseDataDto>();
        String loanId = loanDetailRequestDto.getLoanId();
        LoanModel loan = loanMapper.findById(Long.parseLong(loanId));

        if (loan == null) {
            log.info("标的详情" + ReturnMessage.LOAN_NOT_FOUND.getCode() + ":" + ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return new BaseResponseDto(ReturnMessage.LOAN_NOT_FOUND.getCode(), ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }

        LoanDetailResponseDataDto loanDetailResponseDataDto = this.convertLoanDetailFromLoan(loan, loanDetailRequestDto.getBaseParam().getUserId());
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(loanDetailResponseDataDto);
        return dto;
    }

    private LoanDetailResponseDataDto convertLoanDetailFromLoan(LoanModel loan, String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        LoanDetailResponseDataDto loanDetailResponseDataDto = new LoanDetailResponseDataDto();
        loanDetailResponseDataDto.setLoanId("" + loan.getId());
        loanDetailResponseDataDto.setLoanType(loan.getProductType() != null ? loan.getProductType().getProductLine() : "");
        loanDetailResponseDataDto.setLoanName(loan.getName());
        String repayTypeName = loan.getType().getRepayType();
        String interestPointName = loan.getType().getInterestPointName();
        loanDetailResponseDataDto.setRepayTypeCode("");
        loanDetailResponseDataDto.setRepayTypeName(repayTypeName);
        loanDetailResponseDataDto.setInterestPointName(interestPointName);
        loanDetailResponseDataDto.setPeriods(loan.getPeriods());
        loanDetailResponseDataDto.setDeadline(loan.getPeriods());
        loanDetailResponseDataDto.setRepayUnit(loan.getType().getLoanPeriodUnit().getDesc());
        loanDetailResponseDataDto.setRatePercent(decimalFormat.format((loan.getBaseRate() + loan.getActivityRate()) * 100));
        loanDetailResponseDataDto.setLoanMoney(AmountConverter.convertCentToString(loan.getLoanAmount()));
        loanDetailResponseDataDto.setTitle(title);
        loanDetailResponseDataDto.setContent(MessageFormat.format(content,loanDetailResponseDataDto.getLoanMoney(),loanDetailResponseDataDto.getDeadline(),loanDetailResponseDataDto.getRepayUnit(),loanDetailResponseDataDto.getRatePercent()));
        if(LoanStatus.PREHEAT.equals(loan.getStatus())){
            loanDetailResponseDataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
            loanDetailResponseDataDto.setLoanStatusDesc(LoanStatus.RAISING.getDescription());
        }else{
            loanDetailResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
            loanDetailResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
        }

        loanDetailResponseDataDto.setAgent(loan.getAgentLoginName());
        loanDetailResponseDataDto.setLoaner(loan.getLoanerLoginName());
        loanDetailResponseDataDto.setInvestedCount(investMapper.countSuccessInvest(loan.getId()));
        loanDetailResponseDataDto.setRemainTime(calculateRemainTime(loan.getFundraisingEndTime(), loan.getStatus()));
        if (loan.getFundraisingStartTime() != null) {
            loanDetailResponseDataDto.setInvestBeginTime(new DateTime(loan.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
            loanDetailResponseDataDto.setVerifyTime(new DateTime(loan.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if(loan.getFundraisingEndTime() != null){
            loanDetailResponseDataDto.setFundRaisingEndTime(new DateTime(loan.getFundraisingEndTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        loanDetailResponseDataDto.setInvestBeginSeconds(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
        long investedAmount;
        if (loan.getProductType() == ProductType.EXPERIENCE) {
            Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
            Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
            List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loan.getId(), beginTime, endTime);
            investedAmount = couponService.findExperienceInvestAmount(investModelList);
            loanDetailResponseDataDto.setVerifyTime(new DateTime().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss"));
        } else {
            investedAmount = investMapper.sumSuccessInvestAmount(loan.getId());
            //TODO:fake
            if (loan.getId() == 41650602422768L && loan.getStatus() == LoanStatus.REPAYING) {
                investedAmount = loan.getLoanAmount();
            }
        }
        loanDetailResponseDataDto.setInvestedMoney(AmountConverter.convertCentToString(investedAmount));
        loanDetailResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
        loanDetailResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));
        loanDetailResponseDataDto.setLoanDetail(obtainLoanDetail(loan.getId()));
        loanDetailResponseDataDto.setEvidence(getEvidenceByLoanId(loan.getId()));

        List<InvestModel> investAchievements = investMapper.findInvestAchievementsByLoanId(loan.getId());
        StringBuilder marqueeTitle = new StringBuilder();
        if (CollectionUtils.isEmpty(investAchievements) && Lists.newArrayList(LoanStatus.RAISING, LoanStatus.PREHEAT).contains(loan.getStatus())) {
            marqueeTitle.append("第一个投资者将获得“拓荒先锋”称号及0.2％加息券＋50元红包    ");
        } else {
            for (InvestModel investModel : investAchievements) {
                String investorLoginName = randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId(),Source.MOBILE);
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loan.getStatus() == LoanStatus.RAISING) {
                    marqueeTitle.append(investorLoginName).append(" 以累计投资").append(AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loan.getId(), investModel.getLoginName()))).append("元暂居标王，快来争夺吧    ");
                    marqueeTitle.append("目前项目剩余").append(AmountConverter.convertCentToString(loan.getLoanAmount() - investedAmount)).append("元，快来一锤定音获取奖励吧    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loan.getStatus() != LoanStatus.RAISING) {
                    marqueeTitle.append("恭喜").append(investorLoginName).append(" 以累计投资").append(AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loan.getId(), investModel.getLoginName()))).append("元夺得标王，奖励0.5％加息券＋100元红包    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.FIRST_INVEST)) {
                    marqueeTitle.append("恭喜").append(investorLoginName).append(" ").append(new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss")).append("占领先锋，奖励0.2％加息券＋50元红包    ");
                }
                if (investModel.getAchievements().contains(InvestAchievement.LAST_INVEST)){
                    marqueeTitle.append("恭喜").append(investorLoginName).append(" ").append(new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss")).append("一锤定音，奖励0.2％加息券＋50元红包    ");
                }
            }
            loanDetailResponseDataDto.setMarqueeTitle(marqueeTitle.toString());
        }
        //TODO:fake
        if (loan.getId() == 41650602422768L && loan.getStatus() == LoanStatus.REPAYING) {
            loanDetailResponseDataDto.setMarqueeTitle(MessageFormat.format("恭喜186**67 以累计投资{0}元夺得标王，奖励0.5％加息券＋100元红包    恭喜186**67 {0}占领先锋，奖励0.2％加息券＋50元红包    恭喜186**67 2016-07-29 15:33:45一锤定音，奖励0.2％加息券＋50元红包    ",
                    AmountConverter.convertCentToString(loan.getLoanAmount()), new DateTime(2016, 7, 28, 0, 0, 0).toString("yyyy-MM-dd HH:mm:ss")));
        }

        loanDetailResponseDataDto.setMinInvestMoney(AmountConverter.convertCentToString(loan.getMinInvestAmount()));
        loanDetailResponseDataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loan.getMaxInvestAmount()));
        loanDetailResponseDataDto.setCardinalNumber(AmountConverter.convertCentToString(loan.getInvestIncreasingAmount()));
        if(loan.getRaisingCompleteTime() != null) {
            loanDetailResponseDataDto.setRaiseCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getRaisingCompleteTime()));
        }
        loanDetailResponseDataDto.setDuration("" + loan.getDuration());
        loanDetailResponseDataDto.setRaisingPeriod(String.valueOf(Days.daysBetween(new DateTime(loan.getFundraisingStartTime()).withTimeAtStartOfDay(),
                new DateTime(loan.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1));
        loanDetailResponseDataDto.setProductNewType(loan.getProductType() != null ? loan.getProductType().name(): "");

        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loan.getId());
        if (CollectionUtils.isNotEmpty(extraLoanRateModels)) {
            loanDetailResponseDataDto.setExtraRates(fillExtraLoanRateDto(extraLoanRateModels));
        }

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        double investFeeRate = membershipModel == null ? defaultFee : membershipModel.getFee();
        if(ProductType.EXPERIENCE == loan.getProductType()){
            investFeeRate = this.defaultFee;
        }
        loanDetailResponseDataDto.setInvestFeeRate(String.valueOf(investFeeRate));
        return loanDetailResponseDataDto;

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
                    String tempUrl = domainName + "/" +url;
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
    public String obtainLoanDetail(long loanId){
        LoanModel loanModel = loanMapper.findById(loanId);
        if(loanModel != null){
            Map<String, Object> dateModel = collectLoanDetailDateModel(loanId,loanModel.getPledgeType());
            switch (loanModel.getPledgeType()){
                case HOUSE:
                    return contractService.getContract("pledgeHouse",dateModel);
                case VEHICLE:
                    return contractService.getContract("pledgeVehicle",dateModel);
                default:
                    return loanModel.getDescriptionHtml();

            }
        }
        return null;

    }
    private Map<String, Object> collectLoanDetailDateModel(long loanId,PledgeType pledgeType){
        Map<String, Object> dataModel = new HashMap<>();
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getLoanerDetailByLoanId(loanId);
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(loanId);
        if(loanerDetailsModel != null){
            if(StringUtils.isNotEmpty(loanerDetailsModel.getUserName())){

                dataModel.put("loaner",MessageFormat.format("{0}某", loanerDetailsModel.getUserName().substring(0,1)));
            }
            dataModel.put("marriage",loanerDetailsModel.getMarriage());

        }
        if(loanDetailsModel != null ){
            dataModel.put("declaration",loanDetailsModel.getDeclaration());

        }
        switch (pledgeType){
            case HOUSE:
                PledgeHouseModel pledgeHouseModel = (PledgeHouseModel)pledgeHouseMapper.getPledgeHouseDetailByLoanId(loanId);
                if(pledgeHouseModel != null){
                    dataModel.put("loanAmount",pledgeHouseModel.getLoanAmount());
                    dataModel.put("estimateAmount",pledgeHouseModel.getEstimateAmount());
                    dataModel.put("pledgeLocation",pledgeHouseModel.getPledgeLocation());
                    dataModel.put("square",pledgeHouseModel.getSquare());
                    dataModel.put("propertyCardId",pledgeHouseModel.getPropertyCardId());
                    dataModel.put("estateRegisterId",pledgeHouseModel.getEstateRegisterId());
                    dataModel.put("authenticAct",pledgeHouseModel.getAuthenticAct());

                }
                break ;
            case VEHICLE:
                PledgeVehicleModel pledgeVehicleModel = (PledgeVehicleModel)pledgeVehicleMapper.getPledgeVehicleDetailByLoanId(loanId);
                if(pledgeVehicleModel != null){
                    dataModel.put("loanAmount",pledgeVehicleModel.getLoanAmount());
                    dataModel.put("estimateAmount",pledgeVehicleModel.getEstimateAmount());
                    dataModel.put("model",pledgeVehicleModel.getModel());
                }
                break;

        }

        return dataModel;
    }

}
