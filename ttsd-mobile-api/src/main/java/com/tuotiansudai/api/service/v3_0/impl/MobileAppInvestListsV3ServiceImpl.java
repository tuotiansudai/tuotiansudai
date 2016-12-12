package com.tuotiansudai.api.service.v3_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppInvestListsV3Service;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class MobileAppInvestListsV3ServiceImpl implements MobileAppInvestListsV3Service {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto) {
        String loginName = userInvestListRequestDto.getBaseParam().getUserId();
        int pageSize = pageValidUtils.validPageSizeLimit(userInvestListRequestDto.getPageSize());
        int index = (userInvestListRequestDto.getIndex() - 1) * pageSize;

        List<InvestModel> investModels = investMapper.findInvestorInvestAndTransferPagination(loginName, userInvestListRequestDto.getStatus(), index, pageSize);

        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investModels, userInvestListRequestDto.getStatus()));
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

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investModels, LoanStatus loanStatus) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        if (CollectionUtils.isNotEmpty(investModels)) {
            for (InvestModel investModel : investModels) {
                LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(investModel, loanModel, loanDetailsModel);

                if (investModel.getTransferInvestId() != null) {
                    TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                    dto.setLoanName(transferApplicationModel != null ? transferApplicationModel.getName() : "");
                    dto.setInvestId(transferApplicationModel != null ? String.valueOf(transferApplicationModel.getInvestId()) : "");
                    dto.setTransferInvestId(transferApplicationModel != null ? String.valueOf(transferApplicationModel.getTransferInvestId()) : "");
                    dto.setTransferApplicationId(transferApplicationModel != null ? String.valueOf(transferApplicationModel.getId()) : "");
                    dto.setInvestAmount(transferApplicationModel != null ? String.valueOf(transferApplicationModel.getInvestAmount()) : "");
                    dto.setCategoryType(CategoryType.TRANSFER_LOAN);
                } else {
                    dto.setLoanName(loanModel.getName());
                    dto.setTransferInvestId("");
                    dto.setTransferApplicationId("");
                    dto.setInvestAmount(String.valueOf(investModel.getAmount()));
                    dto.setCategoryType(CategoryType.LOAN);
                }

                List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
                if(transferApplicationModels.size() > 0){
                    dto.setTransferApplicationId(String.valueOf(transferApplicationModels.get(0).getId()));
                }

                if (loanStatus.equals(LoanStatus.REPAYING) && loanModel.getProductType().equals(ProductType.EXPERIENCE)) {
                    List<UserCouponModel> userCouponModelList = userCouponMapper.findByInvestId(investModel.getId());
                    if (CollectionUtils.isNotEmpty(userCouponModelList)) {
                        dto.setInvestAmount(AmountConverter.convertCentToString(couponMapper.findById(userCouponModelList.get(0).getCouponId()).getAmount()));
                    }
                }

                InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());
                dto.setExtraRate(investExtraRateModel != null ? decimalFormat.format(investExtraRateModel.getExtraRate() * 100) : null);
                long actualInterest = 0;
                long expectedInterest = 0;

                if (investExtraRateModel != null) {
                    expectedInterest += investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                }

                if (investExtraRateModel != null && investExtraRateModel.getActualRepayDate() != null) {
                    actualInterest += investExtraRateModel.getRepayAmount();
                }

                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod());
                    actualInterest += investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest();
                    expectedInterest += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
                    if (couponRepayModel != null) {
                        actualInterest += couponRepayModel.getRepayAmount();
                        expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    }
                }
                dto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
                dto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest));
                InvestRepayModel lastInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanModel.getPeriods());
                String lastRepayDate = null;
                if (lastInvestRepayModel != null) {
                    lastRepayDate = new DateTime(loanModel.getStatus() == LoanStatus.COMPLETE ? lastInvestRepayModel.getActualRepayDate() : lastInvestRepayModel.getRepayDate()).toString("yyyy-MM-dd");
                }
                dto.setLastRepayDate(StringUtils.trimToEmpty(lastRepayDate));

                String transferStatus = "";
                if (investModel.getTransferStatus() == TransferStatus.TRANSFERABLE) {
                    transferStatus = investTransferService.isTransferable(investModel.getId()) ? investModel.getTransferStatus().name() : "";
                } else if(investModel.getTransferStatus() == TransferStatus.SUCCESS){
                    transferStatus = investModel.getTransferStatus().name();
                } else if(investModel.getTransferStatus() == TransferStatus.TRANSFERRING){
                    transferStatus = investModel.getTransferStatus().name();
                }
                dto.setTransferStatus(transferStatus);

                List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(investModel.getId());
                List<CouponType> couponTypes = Lists.newArrayList();
                for (UserCouponModel userCouponModel : userCouponModels) {
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    couponTypes.add(couponModel.getCouponType());
                }

                dto.setUserCoupons(couponTypes);
                dto.setUsedCoupon(CollectionUtils.isNotEmpty(couponTypes) && !couponTypes.contains(CouponType.RED_ENVELOPE));
                dto.setUsedRedEnvelope(couponTypes.contains(CouponType.RED_ENVELOPE));
                dto.setProductNewType(loanModel.getProductType().name());

                list.add(dto);
            }
        }
        return list;
    }

}
