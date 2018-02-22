package com.tuotiansudai.api.service.v3_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppInvestListsV3Service;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppInvestListsV3ServiceImpl implements MobileAppInvestListsV3Service {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private CouponService couponService;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto) {
        String loginName = LoginUserInfo.getLoginName();
        int pageSize = pageValidUtils.validPageSizeLimit(userInvestListRequestDto.getPageSize());
        int index = (userInvestListRequestDto.getIndex() - 1) * pageSize;

        List<InvestModel> investModels = investMapper.findInvestorInvestAndTransferPagination(loginName, userInvestListRequestDto.getStatus(), index, pageSize);

        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(userInvestListRequestDto, investModels));
        dtoData.setIndex(userInvestListRequestDto.getIndex());
        dtoData.setPageSize(userInvestListRequestDto.getPageSize());
        long investModelCount = investMapper.countInvestorInvestAndTransferPagination(loginName, userInvestListRequestDto.getStatus());
        dtoData.setTotalCount((int) investModelCount);

        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(UserInvestListRequestDto userInvestListRequestDto, List<InvestModel> investModels) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();

        for (InvestModel investModel : investModels) {
            LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
            UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(investModel, loanModel);

            TransferApplicationModel transferApplicationModel = null;
            List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
            if (investModel.getTransferInvestId() != null) {
                // 承接方
                transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
            } else if (CollectionUtils.isNotEmpty(transferApplicationModels)) {
                // 转让方
                transferApplicationModel = transferApplicationModels.get(0);
            }
            if (transferApplicationModel == null) {
                // 无承接或转让情况
                dto.setLoanName(loanModel.getName());
                dto.setTransferApplicationId("");
                dto.setInvestAmount(AmountConverter.convertCentToString(investModel.getAmount()));
                dto.setTransferInvest(false);
            } else {
                // 有承接或转让情况
                dto.setLoanName(transferApplicationModel.getName());
                dto.setTransferApplicationId(String.valueOf(transferApplicationModel.getId()));
                dto.setInvestAmount(AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
                dto.setTransferInvest(true);
            }

            long actualInterest = 0;
            long expectedInterest = 0;

            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            for (InvestRepayModel investRepayModel : investRepayModels) {
                expectedInterest += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                actualInterest += investRepayModel.getActualInterest() - investRepayModel.getActualFee();
                CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod());
                if (couponRepayModel != null) {
                    actualInterest += couponRepayModel.getActualInterest() - couponRepayModel.getActualFee();
                    expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                }
            }

            //阶梯加息的利息计算
            InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());
            if (investExtraRateModel != null && !investExtraRateModel.isTransfer()) {
                long expectedExtraInterest = investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                expectedInterest += expectedExtraInterest;
                actualInterest += investExtraRateModel.getStatus() == RepayStatus.COMPLETE ? investExtraRateModel.getActualInterest() - investExtraRateModel.getActualFee() : 0;
            }

            //债权转让折扣计算
            if (transferApplicationModel != null) {
                long discount = transferApplicationModel.getInvestAmount() - transferApplicationModel.getTransferAmount();
                expectedInterest += discount;
                actualInterest += loanModel.getStatus() == LoanStatus.COMPLETE ? discount : 0;
            }

            dto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
            dto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest));
            if (Lists.newArrayList(LoanStatus.RAISING, LoanStatus.RECHECK).contains(loanModel.getStatus())) {
                List<Long> couponIds = userCouponMapper.findUserCouponSuccessByInvestId(investModel.getId()).stream().filter(userCouponModel -> couponMapper.findById(userCouponModel.getCouponId()).getCouponType() == CouponType.INTEREST_COUPON).map(UserCouponModel::getCouponId).collect(Collectors.toList());
                long couponExpectedInterest = couponService.estimateCouponExpectedInterest(investModel.getLoginName(), investModel.getInvestFeeRate(), loanModel.getId(), couponIds, investModel.getAmount(), investModel.getCreatedTime());
                long investIncome = investService.estimateInvestIncome(loanModel.getId(), investModel.getInvestFeeRate(), investModel.getLoginName(), investModel.getAmount(), investModel.getCreatedTime());
                dto.setExpectedInterest(AmountConverter.convertCentToString(couponExpectedInterest + investIncome));
            }

            dto.setTransferStatus(investTransferService.isTransferable(investModel.getId()) ? TransferStatus.TRANSFERABLE.name() : (investModel.getTransferStatus().equals(TransferStatus.TRANSFERABLE) ? TransferStatus.NONTRANSFERABLE.name() : investModel.getTransferStatus().name()));

            List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(investModel.getId());
            List<CouponType> couponTypes = Lists.newArrayList();
            for (UserCouponModel userCouponModel : userCouponModels) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                couponTypes.add(couponModel.getCouponType());
            }

            dto.setLastRepayDate(StringUtils.trimToEmpty(new DateTime(loanModel.getStatus() == LoanStatus.COMPLETE ? investRepayModels.get(investRepayModels.size() - 1).getActualRepayDate() : loanModel.getDeadline()).toString("yyyy-MM-dd")));
            dto.setUsedCoupon(CollectionUtils.isNotEmpty(couponTypes) && !couponTypes.contains(CouponType.RED_ENVELOPE));
            dto.setUsedRedEnvelope(couponTypes.contains(CouponType.RED_ENVELOPE));
            dto.setProductNewType(loanModel.getProductType().name());

            dto.setRepayProgress(generateRepayProgress(userInvestListRequestDto, loanModel));

            list.add(dto);
        }

        return list;
    }

    private int generateRepayProgress(UserInvestListRequestDto userInvestListRequestDto, LoanModel loanModel) {
        if (userInvestListRequestDto.getStatus() == LoanStatus.COMPLETE) {
            return 100;
        }
        if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE).contains(loanModel.getStatus())) {
            int passedDays = Days.daysBetween(new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay(), new DateTime().withTimeAtStartOfDay()).getDays() + 1;
            int totalRepayDays = Days.daysBetween(new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay(), new DateTime(loanModel.getDeadline()).withTimeAtStartOfDay()).getDays() + 1;
            int progress = passedDays * 100 / totalRepayDays;
            return progress > 100 ? 100 : (progress == 0 ? 1 : progress);
        }

        return 0;
    }
}
