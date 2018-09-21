package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferApplicationService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationDetailDto;
import com.tuotiansudai.dto.TransferApplicationDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.impl.InvestServiceImpl;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateLeftDays;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppTransferApplicationServiceImpl implements MobileAppTransferApplicationService {

    static Logger logger = Logger.getLogger(MobileAppTransferApplicationServiceImpl.class);
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanDetailsMapper loanDetailsMapper;
    @Autowired
    private InvestTransferService investTransferService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private TransferRuleMapper transferRuleMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private CouponRepayMapper couponRepayMapper;
    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;
    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;
    @Value(value = "${pay.interest.fee}")
    private double defaultFee;
    @Value("${mobile.server}")
    private String mobileServer;
    @Autowired
    private InvestService investService;
    @Value("${risk.estimate.limit.key}")
    private String riskEstimateLimitKey;
    @Autowired
    private RiskEstimateMapper riskEstimateMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<TransferApplicationResponseDataDto> generateTransferApplication(TransferApplicationRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        String loginName = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if (index == null || index <= 0) {
            index = 1;
        }

        List<TransferApplicationRecordView> transferApplicationRecordDtos = transferApplicationMapper.findTransferApplicationPaginationByLoginName(loginName,
                transferStatusList,
                (index - 1) * pageSize,
                pageSize);
        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(loginName, transferStatusList));
        if (CollectionUtils.isNotEmpty(transferApplicationRecordDtos)) {
            List<TransferApplicationRecordResponseDataDto> transferApplication = Lists.transform(transferApplicationRecordDtos, transferApplicationRecordDto -> {
                TransferApplicationRecordResponseDataDto transferApplicationRecordResponseDataDto = new TransferApplicationRecordResponseDataDto(transferApplicationRecordDto);
                LoanModel loanModel = loanMapper.findById(transferApplicationRecordDto.getLoanId());
                InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordDto.getTransferInvestId(), loanModel.getPeriods());
                Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
                transferApplicationRecordResponseDataDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
                return transferApplicationRecordResponseDataDto;
            });
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);

        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto transferApply(TransferApplyRequestDto requestDto) {
        TransferApplicationDto transferApplicationDto = requestDto.convertToTransferApplicationDto();
        InvestModel investModel = investMapper.findById(transferApplicationDto.getTransferInvestId());
        BigDecimal investAmountBig = new BigDecimal(investModel.getAmount());
        BigDecimal discountBig = new BigDecimal(transferRuleMapper.find().getDiscount());
        long transferAmount = investModel.getAmount();
        long discountLower = investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationDto.getTransferInvestId());

        if(CollectionUtils.isEmpty(investRepayModels)){
            return new BaseResponseDto(ReturnMessage.REPAY_IS_GENERATION_IN.getCode(), ReturnMessage.REPAY_IS_GENERATION_IN.getMsg());
        }

        if (transferAmount > investModel.getAmount() || transferAmount < discountLower) {
            return new BaseResponseDto(ReturnMessage.TRANSFER_AMOUNT_OUT_OF_RANGE.getCode(), ReturnMessage.TRANSFER_AMOUNT_OUT_OF_RANGE.getMsg());
        }

        if(loanMapper.findById(investModel.getLoanId()).getStatus() == LoanStatus.OVERDUE){
            return new BaseResponseDto(ReturnMessage.TRANSFER_IS_OVERDUE.getCode(), ReturnMessage.TRANSFER_IS_OVERDUE.getMsg());
        }

        if(!investTransferService.validTransferIsDayLimit(investModel.getLoanId())){
            return new BaseResponseDto(ReturnMessage.TRANSFER_IMPEND_REPAYING.getCode(), ReturnMessage.TRANSFER_IMPEND_REPAYING.getMsg());
        }

        if (!investTransferService.validTransferIsCanceled(investModel.getId())) {
            return new BaseResponseDto(ReturnMessage.TRANSFER_ALREADY_CANCELED_TODAY.getCode(), ReturnMessage.TRANSFER_ALREADY_CANCELED_TODAY.getMsg());
        }

        if (!investTransferService.investTransferApply(transferApplicationDto)) {
            return new BaseResponseDto(ReturnMessage.TRANSFER_APPLY_IS_FAIL.getCode(), ReturnMessage.TRANSFER_APPLY_IS_FAIL.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    @Override
    public BaseResponseDto<TransferApplyQueryResponseDataDto> transferApplyQuery(TransferApplyQueryRequestDto requestDto) {
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        String investId = requestDto.getInvestId();

        if (!investTransferService.isTransferable(Long.parseLong(investId))) {
            return new BaseResponseDto<>(ReturnMessage.TRANSFER_IS_NOT_EXIST.getCode(), ReturnMessage.TRANSFER_IS_NOT_EXIST.getMsg());
        }
        InvestModel investModel = investMapper.findById(Long.parseLong(investId));
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        TransferApplyQueryResponseDataDto transferApplyQueryResponseDataDto = new TransferApplyQueryResponseDataDto();

        transferApplyQueryResponseDataDto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
        transferApplyQueryResponseDataDto.setDeadLine(new DateTime(investTransferService.getDeadlineFromNow()).toString("yyyy/MM/dd HH:mm:ss"));

        BigDecimal investAmountBig = new BigDecimal(investModel.getAmount());
        BigDecimal discountBig = new BigDecimal(transferRuleModel.getDiscount());

        long discountLower = investAmountBig.subtract(discountBig.multiply(investAmountBig)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        transferApplyQueryResponseDataDto.setDiscountLower(AmountConverter.convertCentToString(discountLower));
        transferApplyQueryResponseDataDto.setDiscountUpper(transferApplyQueryResponseDataDto.getInvestAmount());
        transferApplyQueryResponseDataDto.setTransferFee(AmountConverter.convertCentToString(TransferRuleUtil.getTransferFee(loanModel.getType(), loanModel.getRecheckTime(), investModel.getAmount(), investModel.getCreatedTime(), transferRuleModel)));

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(transferApplyQueryResponseDataDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<TransferApplicationResponseDataDto> generateTransfereeApplication(PaginationRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        String loginName = requestDto.getBaseParam().getUserId();
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto<>();
        if (index == null || index <= 0) {
            index = 1;
        }

        List<TransferApplicationRecordView> transferApplicationRecordDtos = transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(loginName,
                (index - 1) * pageSize,
                pageSize);
        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(loginName));
        if (CollectionUtils.isNotEmpty(transferApplicationRecordDtos)) {
            List<TransferApplicationRecordResponseDataDto> transferApplication = transferApplicationRecordDtos.stream().map(transferApplicationRecordDto -> {
                TransferApplicationRecordResponseDataDto transferApplicationRecordResponseDataDto = new TransferApplicationRecordResponseDataDto(transferApplicationRecordDto);
                LoanModel loanModel = loanMapper.findById(transferApplicationRecordDto.getLoanId());
                InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordDto.getInvestId(), loanModel.getPeriods());
                Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
                transferApplicationRecordResponseDataDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
                return transferApplicationRecordResponseDataDto;
            }).collect(Collectors.toList());
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);

        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto transferApplicationCancel(TransferCancelRequestDto transferCancelRequestDto) {
        if (investTransferService.cancelTransferApplicationManually(transferCancelRequestDto.getTransferApplicationId())) {
            return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        } else {
            return new BaseResponseDto(ReturnMessage.TRANSFER_CANCEL_IS_FAIL.getCode(), ReturnMessage.TRANSFER_CANCEL_IS_FAIL.getMsg());
        }
    }

    @Override
    public BaseResponseDto<TransferPurchaseResponseDataDto> transferPurchase(TransferPurchaseRequestDto requestDto) {
        BaseResponseDto<TransferPurchaseResponseDataDto> dto = new BaseResponseDto<>();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(requestDto.getTransferApplicationId()));

        TransferPurchaseResponseDataDto transferPurchaseResponseDataDto = new TransferPurchaseResponseDataDto();
        transferPurchaseResponseDataDto.setBalance(AmountConverter.convertCentToString((accountMapper.findByLoginName(requestDto.getBaseParam().getUserId()).getBalance())));
        transferPurchaseResponseDataDto.setTransferAmount(AmountConverter.convertCentToString((transferApplicationModel.getTransferAmount())));
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? transferApplicationModel.getInvestId() : transferApplicationModel.getTransferInvestId());
        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(requestDto.getBaseParam().getUserId());
        transferPurchaseResponseDataDto.setExpectedInterestAmount(AmountConverter.convertCentToString(InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels, investFeeRate)));
        //
        LoanDetailsModel loanDetailsModel=loanDetailsMapper.getByLoanId(transferApplicationModel.getLoanId());
        RiskEstimateModel userEstimate=riskEstimateMapper.findByLoginName(requestDto.getBaseParam().getUserId());

        transferPurchaseResponseDataDto.setAvailableEstimateMoney(investService.availableInvestMoney(requestDto.getBaseParam().getUserId()));
        if(loanDetailsModel!=null && loanDetailsModel.getEstimate()!=null){
            transferPurchaseResponseDataDto.setEstimateLevel(loanDetailsModel.getEstimate().getLower());
            transferPurchaseResponseDataDto.setRiskEstimate(loanDetailsModel.getEstimate().getType());
        }
        if(userEstimate !=null && userEstimate.getEstimate()!=null){
            transferPurchaseResponseDataDto.setUserRiskEstimate(userEstimate.getEstimate().getType());
            transferPurchaseResponseDataDto.setUserRstimateLevel(userEstimate.getEstimate().getLower());
            transferPurchaseResponseDataDto.setUserEstimateLimit(AmountConverter.convertStringToCent(redisWrapperClient.hget(riskEstimateLimitKey,userEstimate.getEstimate().name())));
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferPurchaseResponseDataDto);
        return dto;
    }

    public BaseResponseDto transferApplicationList(TransferApplicationListRequestDto requestDto) {
        BaseResponseDto<TransferApplicationResponseDataDto> dto = new BaseResponseDto();
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        String rateLower = requestDto.getRateLower();
        String rateUpper = requestDto.getRateUpper();
        List<TransferStatus> transferStatusList = requestDto.getTransferStatus();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (Strings.isNullOrEmpty(rateLower)) {
            rateLower = "0";
        }
        if (Strings.isNullOrEmpty(rateUpper)) {
            rateUpper = "0";
        }

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationRecordDto = transferService.findAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper), index, pageSize);

        TransferApplicationResponseDataDto transferApplicationResponseDataDto = new TransferApplicationResponseDataDto();
        transferApplicationResponseDataDto.setIndex(index);
        transferApplicationResponseDataDto.setPageSize(pageSize);
        transferApplicationResponseDataDto.setTotalCount(transferService.findCountAllTransferApplicationPaginationList(transferStatusList, Double.parseDouble(rateLower), Double.parseDouble(rateUpper)));

        if (CollectionUtils.isNotEmpty(transferApplicationRecordDto.getRecords())) {
            List<TransferApplicationRecordResponseDataDto> transferApplication = transferApplicationRecordDto.getRecords().stream().map(TransferApplicationRecordResponseDataDto::new).collect(Collectors.toList());
            transferApplicationResponseDataDto.setTransferApplication(transferApplication);
        }

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationResponseDataDto);
        return dto;
    }

    public BaseResponseDto<TransferApplicationDetailResponseDataDto> transferApplicationById(TransferApplicationDetailRequestDto requestDto) {
        BaseResponseDto<TransferApplicationDetailResponseDataDto> dto = new BaseResponseDto<>();
        long transferApplicationId = requestDto.getTransferApplicationId();

        String loginName = requestDto.getBaseParam().getUserId();
        TransferApplicationDetailDto transferApplicationDetailDto = transferService.getTransferApplicationDetailDto(transferApplicationId, loginName, 3);
        if (transferApplicationDetailDto == null){
            dto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            dto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return dto;
        }

        TransferApplicationDetailResponseDataDto transferApplicationDetailResponseDataDto = new TransferApplicationDetailResponseDataDto(transferApplicationDetailDto);
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationDetailDto.getTransferInvestId(), loanModel.getPeriods());
        Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
        transferApplicationDetailResponseDataDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
        if (loanDetailsModel != null && loanDetailsModel.getEstimate() != null) {
            transferApplicationDetailResponseDataDto.setEstimate(loanDetailsModel.getEstimate().getType());
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferApplicationDetailResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto<UserInvestRepayResponseDataDto> userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto) {
        Preconditions.checkNotNull(userInvestRepayRequestDto.getInvestId());
        Preconditions.checkNotNull(userInvestRepayRequestDto.getBaseParam().getUserId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        final long transferInvestId = Long.parseLong(userInvestRepayRequestDto.getInvestId().trim());
        //return TransferLoan Details
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(transferInvestId);
        InvestModel investModel = investMapper.findById(transferInvestId);
        if (null == investModel) {
            return new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), ReturnMessage.ERROR.getMsg());
        }
        if (null == transferApplicationModel) {
            return new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), ReturnMessage.ERROR.getMsg());
        }
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (null == loanModel) {
            return new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), ReturnMessage.ERROR.getMsg());
        }

        UserInvestRepayResponseDataDto userInvestRepayResponseDataDto = new UserInvestRepayResponseDataDto(loanModel, transferApplicationModel);

        long totalExpectedInterest = 0;
        long totalActualInterest = 0;
        long corpus = 0;
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(userInvestRepayRequestDto.getBaseParam().getUserId(),
                transferInvestId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            totalExpectedInterest += investRepayModel.getExpectedInterest();
            totalActualInterest += investRepayModel.getRepayAmount();
            corpus += investRepayModel.getCorpus();
            CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod());
            InvestRepayDataDto investRepayDataDto = new InvestRepayDataDto(investRepayModel, couponRepayModel);
            userInvestRepayResponseDataDto.getInvestRepays().add(investRepayDataDto);
            if (investRepayModel.getPeriod() == loanModel.getPeriods()) {
                userInvestRepayResponseDataDto.setLastRepayDate(simpleDateFormat.format(investRepayModel.getRepayDate()));
            }
        }

        userInvestRepayResponseDataDto.setExpectedInterest(AmountConverter.convertCentToString(totalExpectedInterest));
        userInvestRepayResponseDataDto.setActualInterest(AmountConverter.convertCentToString(totalActualInterest));
        userInvestRepayResponseDataDto.setUnPaidRepay(AmountConverter.convertCentToString(totalExpectedInterest + corpus - totalActualInterest));

        MembershipModel membershipModel = userMembershipEvaluator.evaluateSpecifiedDate(userInvestRepayRequestDto.getBaseParam().getUserId(), transferApplicationModel.getTransferTime());
        userInvestRepayResponseDataDto.setMembershipLevel(String.valueOf(membershipModel.getLevel()));
        userInvestRepayResponseDataDto.setServiceFeeDesc(ServiceFeeReduce.getDescriptionByRate(investModel.getInvestFeeRate()));
        if (!Strings.isNullOrEmpty(investModel.getContractNo())) {
            String contractUrl =  investModel.getContractNo().equals("OLD") ?
                    MessageFormat.format("{0}/v1.0/contract/investor/loanId/{1}/investId/{2}", this.mobileServer, String.valueOf(investModel.getLoanId()), String.valueOf(investModel.getId()))
                    : MessageFormat.format("{0}/v1.0/contract/invest/contractNo/{1}", this.mobileServer, investModel.getContractNo());
            userInvestRepayResponseDataDto.setContractLocation(contractUrl);
        }
        BaseResponseDto<UserInvestRepayResponseDataDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(userInvestRepayResponseDataDto);

        return baseResponseDto;
    }
}
